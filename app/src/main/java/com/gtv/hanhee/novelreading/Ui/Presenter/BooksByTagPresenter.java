package com.gtv.hanhee.novelreading.Ui.Presenter;

import android.content.Context;

import com.gtv.hanhee.novelreading.Api.BookApi;
import com.gtv.hanhee.novelreading.Api.VietPhraseApi;
import com.gtv.hanhee.novelreading.Model.BooksByTag;
import com.gtv.hanhee.novelreading.Ui.Contract.BooksByTagContract;
import com.gtv.hanhee.novelreading.Utils.LogUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BooksByTagPresenter implements BooksByTagContract.Presenter<BooksByTagContract.View> {

    private Context context;
    private BookApi bookApi;
    private VietPhraseApi vietPhraseApi;

    private BooksByTagContract.View view;

    private boolean isLoading = false;

    @Inject
    public BooksByTagPresenter(Context context, BookApi bookApi, VietPhraseApi vietPhraseApi) {
        this.context = context;
        this.bookApi = bookApi;
        this.vietPhraseApi = vietPhraseApi;
    }

    @Override
    public void attachView(BooksByTagContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void getBooksByTag(String tags, final String start, String limit) {
        if (!isLoading) {
            isLoading = true;
            bookApi.getBooksByTag(tags, start, limit).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BooksByTag>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BooksByTag data) {
                            if (data != null) {
                                List<BooksByTag.TagBook> list = data.books;
                                if (list != null && !list.isEmpty() && view != null) {
                                    boolean isRefresh = start.equals("0") ? true : false;
                                    view.showBooksByTag(list, isRefresh);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.e(e.toString());
                            isLoading = false;
                            view.onLoadComplete(false, e.toString());
                        }

                        @Override
                        public void onComplete() {
                            LogUtils.i("complete");
                            isLoading = false;
                            view.onLoadComplete(true, "");
                        }
                    });
        }
    }
}
