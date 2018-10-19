package com.gtv.hanhee.novelreading.Ui.Contract;

import com.gtv.hanhee.novelreading.Model.BooksByTag;

import java.util.List;

public interface BooksByTagContract {

    interface View {
        void showBooksByTag(List<BooksByTag.TagBook> list, boolean isRefresh);

        void onLoadComplete(boolean isSuccess, String msg);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {

        void getBooksByTag(String tags, String start, String limit);
    }

}
