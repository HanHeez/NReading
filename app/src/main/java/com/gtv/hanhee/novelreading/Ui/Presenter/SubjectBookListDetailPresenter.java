package com.gtv.hanhee.novelreading.Ui.Presenter;

import com.gtv.hanhee.novelreading.Api.BookApi;
import com.gtv.hanhee.novelreading.Base.RxPresenter;
import com.gtv.hanhee.novelreading.Model.BookListDetail;
import com.gtv.hanhee.novelreading.Ui.Contract.SubjectBookListDetailContract;
import com.gtv.hanhee.novelreading.Utils.LogUtils;

import org.reactivestreams.Subscription;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SubjectBookListDetailPresenter extends RxPresenter<SubjectBookListDetailContract.View> implements SubjectBookListDetailContract.Presenter<SubjectBookListDetailContract.View> {

    private BookApi bookApi;

    @Inject
    public SubjectBookListDetailPresenter(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    @Override
    public void getBookListDetail(String bookListId) {
        bookApi.getBookListDetail(bookListId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookListDetail>() {

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("getBookListDetail:" + e.toString());
                        mView.complete();
                    }

                    @Override
                    public void onComplete() {
                        mView.complete();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BookListDetail data) {
                        mView.showBookListDetail(data);
                    }
                });

    }

}
