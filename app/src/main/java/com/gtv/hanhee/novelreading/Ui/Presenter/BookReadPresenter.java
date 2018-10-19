package com.gtv.hanhee.novelreading.Ui.Presenter;

import android.content.Context;
import android.util.Log;

import com.gtv.hanhee.novelreading.Api.ReaderApi;
import com.gtv.hanhee.novelreading.Api.VietPhraseApi;
import com.gtv.hanhee.novelreading.Model.BookMixAToc;
import com.gtv.hanhee.novelreading.Model.ChapterRead;
import com.gtv.hanhee.novelreading.Ui.Contract.BookReadContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BookReadPresenter implements BookReadContract.Presenter<BookReadContract.View> {
    private static final String TAG = "BookReadPresenter";
    VietPhraseApi vietPhraseApi;
    private Context context;
    private ReaderApi readerApi;
    private BookReadContract.View view;

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
                .subscribe(new Observer<BookMixAToc>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BookMixAToc data) {
                        List<BookMixAToc.mixToc.Chapters> list = data.mixToc.chapters;
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


}