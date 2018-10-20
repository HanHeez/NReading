package com.gtv.hanhee.novelreading.Ui.Presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.gtv.hanhee.novelreading.Api.ReaderApi;
import com.gtv.hanhee.novelreading.Api.VietPhraseApi;
import com.gtv.hanhee.novelreading.Model.BookSource;
import com.gtv.hanhee.novelreading.Model.BookToc;
import com.gtv.hanhee.novelreading.Model.ChapterRead;
import com.gtv.hanhee.novelreading.Ui.Contract.BookReadContract;
import com.gtv.hanhee.novelreading.Utils.BookPageFactory;
import com.gtv.hanhee.novelreading.Utils.LogUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BookReadPresenter implements BookReadContract.Presenter<BookReadContract.View> {

    VietPhraseApi vietPhraseApi;
    private Context context;
    private ReaderApi readerApi;
    private BookReadContract.View view;
    private AsyncTask<Integer, Integer, Integer> downloadTask;

    private static final String TAG = "BookReadPresenter";

    public boolean interrupted = true;

    @Inject
    public BookReadPresenter(Context context, ReaderApi readerApi, VietPhraseApi vietPhraseApi) {
        this.context = context;
        this.readerApi = readerApi;
        this.vietPhraseApi = vietPhraseApi;

    }

    @Override
    public void attachView(BookReadContract.View view) {
        this.view = view;
    }

    @Override
    public void getBookToc(String bookId, String viewChapters) {
        readerApi.getBookToc(bookId, viewChapters).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookToc>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BookToc data) {
                        List<BookToc.mixToc.Chapters> list = data.mixToc.chapters;
                        if (list != null && !list.isEmpty() && view != null) {
                            view.showBookToc(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getChapterRead(String url, final int chapter) {
        readerApi.getChapterRead(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChapterRead>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ChapterRead data) {
                        if (data.chapter != null && view != null) {
                            view.showChapterRead(data.chapter, chapter);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getBookSource(String viewSummary, String book) {
        readerApi.getBookSource(viewSummary, book).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookSource>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<BookSource> data) {
                        if (data != null && view != null) {
                            view.showBookSource(data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("StaticFieldLeak")
    public synchronized void downloadBook(final String bookId, final List<BookToc.mixToc.Chapters> list, final int start, final int end) {
        interrupted = false;
        downloadTask = new AsyncTask<Integer, Integer, Integer>() {

            int failureCount = 0;
            BookPageFactory factory = new BookPageFactory(bookId, 0);

            @Override
            protected Integer doInBackground(Integer... params) {
                for (int i = start; i < end && i <= list.size(); i++) {
                    if(!interrupted) {
                        if (factory.getBookFile(i).length() < 50) { // 认为章节文件不存在,则下载
                            BookToc.mixToc.Chapters chapters = list.get(i - 1);
                            String url = chapters.link;
                            int ret = download(url, i);
                            if (ret != 1) {
                                failureCount++;
                            }
                        } else {
                            //view.showDownloadProgress(null, i);
                        }
                    }
                }
                return null;
            }


            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                interrupted = true;
                //if(view!=null)
                //view.downloadComplete();
                LogUtils.i("缓存完成，失败" + failureCount + "章");
            }
        };
        downloadTask.execute();
    }

    private int download(String url, final int chapter) {

        final int[] result = {-1};

        readerApi.getChapterRead(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChapterRead>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ChapterRead data) {
                        if (data.chapter != null && view != null) {
                            //view.showDownloadProgress(data.chapter, chapter);
                            result[0] = 1;
                        } else {
                            result[0] = 0;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        result[0] = 0;
                        Log.e(TAG, "onError: " + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        while (result[0] == -1) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result[0];
    }

    public void cancelDownload(){
        interrupted = true;
    }

}