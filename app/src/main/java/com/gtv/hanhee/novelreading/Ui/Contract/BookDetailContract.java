package com.gtv.hanhee.novelreading.Ui.Contract;

import com.gtv.hanhee.novelreading.Model.BookDetail;
import com.gtv.hanhee.novelreading.Model.HotReview;

import java.util.List;

public interface BookDetailContract {

    interface View {
        void showBookDetail(BookDetail data);
        void showHotReview(List<HotReview.Reviews> list);       

        void showBookDetailTags(List<BookDetail.combineTags> combineTags, int tagsSize);

        void showBookDetailCategories(List<BookDetail.combineCategories> combineCategories, int categoriesSize);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getBookDetail(String bookId);
        void getHotReview(String book);
    }

}
