package com.gtv.hanhee.novelreading.Ui.Contract;

import com.gtv.hanhee.novelreading.Base.BaseContract;
import com.gtv.hanhee.novelreading.Model.BookListDetail;
import com.gtv.hanhee.novelreading.Model.BookListTags;

public interface SubjectBookListDetailContract {

    interface View extends BaseContract.BaseView {
        void showBookListDetail(BookListDetail data);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getBookListDetail(String bookListId);
    }
}

