package com.gtv.hanhee.novelreading.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.gtv.hanhee.novelreading.Api.BookApi;
import com.gtv.hanhee.novelreading.Manager.CacheManager;
import com.gtv.hanhee.novelreading.Model.BookMixAToc;
import com.gtv.hanhee.novelreading.Model.ChapterRead;
import com.gtv.hanhee.novelreading.Model.DownloadProgress;
import com.gtv.hanhee.novelreading.Model.DownloadQueue;
import com.gtv.hanhee.novelreading.Model.Support.DownloadMessage;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.ReaderApplication;
import com.gtv.hanhee.novelreading.Utils.AppUtils;
import com.gtv.hanhee.novelreading.Utils.LogUtils;
import com.gtv.hanhee.novelreading.Utils.NetworkUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.logging.HttpLoggingInterceptor;

public class DownloadBookService extends Service {

    public static List<DownloadQueue> downloadQueues = new ArrayList<>();
    public static boolean canceled = false;
    public BookApi bookApi;
    public boolean isBusy = false; // Có thread tải xuống đang diễn ra không
    protected CompositeDisposable mCompositeDisposable;

    public static void post(DownloadQueue downloadQueue) {
        EventBus.getDefault().post(downloadQueue);
    }

    public static void cancel() {
        canceled = true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.i("http : " + message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        bookApi = ReaderApplication.getsInstance().getAppComponent().getBookApi();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe();
        EventBus.getDefault().unregister(this);
    }

    public void post(DownloadProgress progress) {
        EventBus.getDefault().post(progress);
    }

    private void post(DownloadMessage message) {
        EventBus.getDefault().post(message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public synchronized void addToDownloadQueue(DownloadQueue queue) {
        if (!TextUtils.isEmpty(queue.bookId)) {
            boolean exists = false;
            // Xác định xem tác vụ bộ nhớ cache sách hiện tại có tồn tại hay không
            for (int i = 0; i < downloadQueues.size(); i++) {
                if (downloadQueues.get(i).bookId.equals(queue.bookId)) {
                    LogUtils.e("addToDownloadQueue:exists");
                    exists = true;
                    break;
                }
            }
            if (exists) {
                post(new DownloadMessage(queue.bookId, "Tác vụ bộ nhớ cache hiện tại đã tồn tại", false));
                return;
            }

            // được thêm vào hàng đợi tải xuống
            downloadQueues.add(queue);
            LogUtils.e("addToDownloadQueue:" + queue.bookId);
            post(new DownloadMessage(queue.bookId, "Thành công đăng nhập vào bộ nhớ cache", false));
        }
        // Lấy bản tải xuống đầu tiên từ thứ tự hàng đợi
        if (downloadQueues.size() > 0 && !isBusy) {
            isBusy = true;
            downloadBook(downloadQueues.get(0));
        }
    }

    public synchronized void downloadBook(final DownloadQueue downloadQueue) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Integer, Integer> downloadTask = new AsyncTask<Integer, Integer, Integer>() {

            List<BookMixAToc.mixToc.Chapters> list = downloadQueue.list;
            String bookId = downloadQueue.bookId;
            int start = downloadQueue.start; // bắt đầu download
            int end = downloadQueue.end; // End

            @Override
            protected Integer doInBackground(Integer... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                int failureCount = 0;
                for (int i = start; i <= end && i <= list.size(); i++) {
                    if (canceled) {
                        break;
                    }
                    // lỗi mạng, hủy tải về
                    if (!NetworkUtils.isAvailable(AppUtils.getAppContext())) {
                        downloadQueue.isCancel = true;
                        post(new DownloadMessage(bookId, getString(R.string.book_read_download_error), true));
                        failureCount = -1;
                        break;
                    }
                    if (!downloadQueue.isFinish && !downloadQueue.isCancel) {
                        // file chương ko tồn tại, tải về sau, nếu không thì bỏ qua
                        if (CacheManager.getInstance().getChapterFile(bookId, i) == null) {
                            BookMixAToc.mixToc.Chapters chapters = list.get(i - 1);
                            String url = chapters.link;
                            int ret = download(url, bookId, chapters.title, i, list.size());
                            if (ret != 1) {
                                failureCount++;
                            }
                        } else {
                            post(new DownloadProgress(bookId, String.format(
                                    getString(R.string.book_read_alreday_download), list.get(i - 1).title, i, list.size()),
                                    true));
                        }
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                return failureCount;
            }


            @Override
            protected void onPostExecute(Integer failureCount) {
                super.onPostExecute(failureCount);
                downloadQueue.isFinish = true;
                if (failureCount > -1) {
                    // 完成通知
                    post(new DownloadMessage(bookId,
                            String.format(getString(R.string.book_read_download_complete), failureCount), true));
                }
                // tải về xong, loại bỏ nó khỏi hàng đợi
                downloadQueues.remove(downloadQueue);
                // giải phóng trạng thái tải
                isBusy = false;
                if (!canceled) {
                    //  post new EventBus, thông báo để tiếp tục công việc tiếp theo
                    post(new DownloadQueue());
                } else {
                    downloadQueues.clear();
                }
                canceled = false;
                LogUtils.i(bookId + "Đã hoàn thành bộ nhớ cache, không thành công" + failureCount + "chương");
            }
        };
        downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private int download(String url, final String bookId, final String title, final int chapter, final int chapterSize) {

        final int[] result = {-1};

        Disposable disposable = bookApi.getChapterRead(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ChapterRead>() {

                    @Override
                    public void onNext(ChapterRead data) {
                        if (data.chapter != null) {
                            post(new DownloadProgress(bookId, String.format(
                                    getString(R.string.book_read_download_progress), title, chapter, chapterSize),
                                    true));
                            CacheManager.getInstance().saveChapterFile(bookId, chapter, data.chapter);
                            result[0] = 1;
                        } else {
                            result[0] = 0;
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        result[0] = 0;
                    }

                    @Override
                    public void onComplete() {
                        result[0] = 1;
                    }
                });


        addSubscrebe(disposable);

        while (result[0] == -1) {
            try {
                Thread.sleep(350);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result[0];
    }

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    protected void addSubscrebe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }
}
