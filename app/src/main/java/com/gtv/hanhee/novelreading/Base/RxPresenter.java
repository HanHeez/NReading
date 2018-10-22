package com.gtv.hanhee.novelreading.Base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RxPresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {

    protected T mView;
    protected static CompositeDisposable compositeDisposable;

    protected void unSubscribe() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    protected void addSubscrebe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    private static CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
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
