package com.gtv.hanhee.novelreading.Ui.Contract;

import com.gtv.hanhee.novelreading.Model.BookSource;
import com.gtv.hanhee.novelreading.Model.BookToc;
import com.gtv.hanhee.novelreading.Model.ChapterRead;

import java.util.List;

public interface BookReadContract {

    interface View {

        void showChapterRead(ChapterRead.Chapter data, int chapter);

        void showBookToc(List<BookToc.mixToc.Chapters> list);

        void showBookSource(List<BookSource> list);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getBookToc(String bookId, String view);
        void getChapterRead(String url, int chapter);

        void getBookSource(String view, String book);
    }

}
