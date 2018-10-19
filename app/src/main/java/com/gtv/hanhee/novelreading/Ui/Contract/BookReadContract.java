package com.gtv.hanhee.novelreading.Ui.Contract;

import com.gtv.hanhee.novelreading.Model.BookMixAToc;
import com.gtv.hanhee.novelreading.Model.ChapterRead;

import java.util.List;

public interface BookReadContract {

    interface View {

        void showChapterRead(ChapterRead.Chapter data, int chapter);

        void showBookToc(List<BookMixAToc.mixToc.Chapters> list);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getBookToc(String bookId, String view);
        void getChapterRead(String url, int chapter);
    }

}
