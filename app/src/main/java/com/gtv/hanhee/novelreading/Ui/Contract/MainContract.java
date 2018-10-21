package com.gtv.hanhee.novelreading.Ui.Contract;

public interface MainContract {

    interface View extends BaseContract.BaseView {
        void loginSuccess();

        void syncBookShelfCompleted();
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {

        void login(String uid, String token, String platform);

        void syncBookShelf();
    }

}
