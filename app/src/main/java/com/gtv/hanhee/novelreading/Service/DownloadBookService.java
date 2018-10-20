package com.gtv.hanhee.novelreading.Service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.gtv.hanhee.novelreading.Api.ReaderApi;
import com.gtv.hanhee.novelreading.Api.Support.HeaderInterceptor;
import com.gtv.hanhee.novelreading.Model.BookToc;
import com.gtv.hanhee.novelreading.Model.ChapterRead;
import com.gtv.hanhee.novelreading.Model.DownloadComplete;
import com.gtv.hanhee.novelreading.Model.DownloadProgress;
import com.gtv.hanhee.novelreading.Model.DownloadQueue;
import com.gtv.hanhee.novelreading.Module.ReaderApiModule;
import com.gtv.hanhee.novelreading.Utils.BookPageFactory;
import com.gtv.hanhee.novelreading.Utils.LogUtils;
import com.gtv.hanhee.novelreading.Utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class DownloadBookService extends Service {

    public static List<DownloadQueue> downloadQueues = new ArrayList<>();

    public ReaderApi readerApi;

    public boolean isBusy = false; // 当前是否有下载任务在进行
    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new ReaderApiModule.MyLog());
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true) // 失败重发
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(logging);
        readerApi = ReaderApi.getInstance(builder.build());
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
        EventBus.getDefault().unregister(this);
    }

    public static void post(DownloadQueue downloadQueue) {
        EventBus.getDefault().post(downloadQueue);
    }

    public static void post(DownloadProgress progress){
        EventBus.getDefault().post(progress);
    }

    private void post(DownloadComplete complete) {
        EventBus.getDefault().post(complete);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public synchronized void addToDownloadQueue(DownloadQueue downloadQueue) {
        if(!TextUtils.isEmpty(downloadQueue.bookId)) {
            LogUtils.e("addToDownloadQueue:" + downloadQueue.bookId);
            boolean exists = false;
            // 判断当前书籍缓存任务是否存在
            for (int i = 0; i < downloadQueues.size(); i++) {
                if (downloadQueues.get(i).bookId.equals(downloadQueue.bookId)) {
                    LogUtils.e("addToDownloadQueue:exists");
                    exists = true;
                    break;
                }
            }
            if (exists) {
                ToastUtils.showSingleToast("当前缓存任务已存在");
                return;
            }

            // 添加到下载队列
            downloadQueues.add(downloadQueue);
            // 当前队列里有其他任务，才提示
            if(downloadQueues.size() >1)
                ToastUtils.showSingleToast("成功加入缓存队列");
        }
        // 从队列顺序取出第一条下载
        if(downloadQueues.size() >0 && !isBusy){
            isBusy = true;
            downloadBook(downloadQueues.get(0));
        }
    }

    public synchronized void downloadBook(final DownloadQueue downloadQueue) {
        AsyncTask<Integer, Integer, Integer> downloadTask = new AsyncTask<Integer, Integer, Integer>() {

            List<BookToc.mixToc.Chapters> list = downloadQueue.list;
            String bookId = downloadQueue.bookId;
            int start = downloadQueue.start; // 起始章节
            int end = downloadQueue.end; // 结束章节
            BookPageFactory factory = new BookPageFactory(bookId, 0);

            @Override
            protected Integer doInBackground(Integer... params) {
                int failureCount = 0;
                for (int i = start; i <=end && i <= list.size(); i++) {
                    if (!downloadQueue.isFinish && !downloadQueue.isCancel) {
                        if (factory.getBookFile(i).length() < 50) { // 认为章节文件不存在,则下载
                            BookToc.mixToc.Chapters chapters = list.get(i - 1);
                            String url = chapters.link;
                            int ret = download(factory, url, bookId, i);
                            if (ret != 1) {
                                failureCount++;
                            }
                        } else {
                            post(new DownloadProgress(bookId, i));
                        }
                    }
                }
                return failureCount;
            }


            @Override
            protected void onPostExecute(Integer failureCount) {
                super.onPostExecute(failureCount);
                downloadQueue.isFinish = true;
                // 下载完成，从队列里移除
                downloadQueues.remove(downloadQueue);
                // 通知
                post(new DownloadComplete(bookId));
                // 释放 空闲状态
                isBusy = false;
                // post一个空事件，通知继续执行下一个任务
                post(new DownloadQueue());
                LogUtils.i(bookId + "缓存完成，失败" + failureCount + "章");
            }
        };
        downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private int download(final BookPageFactory factory, String url, final String bookId, final int chapter) {

        final int[] result = {-1};

        readerApi.getChapterRead(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChapterRead>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ChapterRead data) {
                        if (data.chapter != null) {
                            //factory.append(data.chapter, chapter);
                            post(new DownloadProgress(bookId, chapter));
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

                    }
                });

        while (result[0] == -1) {
            try {
                Thread.sleep(350);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result[0];
    }
}
