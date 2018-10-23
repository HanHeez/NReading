package com.gtv.hanhee.novelreading.Ui.Presenter;

import android.util.Log;

import com.gtv.hanhee.novelreading.Api.BookApi;
import com.gtv.hanhee.novelreading.Base.RxPresenter;
import com.gtv.hanhee.novelreading.Model.BookDetail;
import com.gtv.hanhee.novelreading.Model.HotReview;
import com.gtv.hanhee.novelreading.Model.RecommendBookList;
import com.gtv.hanhee.novelreading.Ui.Contract.BookDetailContract;
import com.gtv.hanhee.novelreading.Utils.LogUtils;

import org.reactivestreams.Subscription;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BookDetailPresenter extends RxPresenter<BookDetailContract.View> implements BookDetailContract.Presenter<BookDetailContract.View> {

    private BookApi bookApi;

    private static final String TAG = "BookDetailPresenter";

    @Inject
    public BookDetailPresenter(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    public void getBookDetail(String bookId) {
        bookApi.getBookDetail(bookId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BookDetail data) {
                        if (data != null && mView != null) {
                            mView.showBookDetail(data);
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
    public void getHotReview(String book) {
        bookApi.getHotReview(book).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotReview>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HotReview data) {
                        List<HotReview.Reviews> list = data.reviews;
                        if (list != null && !list.isEmpty() && mView != null) {
                            mView.showHotReview(list);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void getRecommendBookList(String bookId, String limit) {
         bookApi.getRecommendBookList(bookId, limit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RecommendBookList>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RecommendBookList data) {
                        LogUtils.i(data.booklists);
                        List<RecommendBookList.RecommendBook> list = data.booklists;
                        if (list != null && !list.isEmpty() && mView != null) {
                            mView.showRecommendBookList(list);
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("+++" + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}
