package com.gtv.hanhee.novelreading.Ui.Contract;

public interface BaseContract {

    interface BasePresenter<T> {

        void attachView(T view);

        void detachView();
    }

    interface BaseView {

        void showError();

        void complete();

    }
}
