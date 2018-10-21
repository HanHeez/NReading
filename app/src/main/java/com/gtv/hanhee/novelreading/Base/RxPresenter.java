package com.gtv.hanhee.novelreading.Base;

import com.gtv.hanhee.novelreading.Ui.Contract.BaseContract;

import org.reactivestreams.Subscription;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RxPresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {

    protected T mView;
    protected CompositeDisposable compositeDisposable;

    protected void unSubscribe() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    protected void addSubscrebe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }
}
