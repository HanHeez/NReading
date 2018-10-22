package com.gtv.hanhee.novelreading.Ui.Contract;

import com.gtv.hanhee.novelreading.Base.BaseContract;
import com.gtv.hanhee.novelreading.Model.BookMixAToc;
import com.gtv.hanhee.novelreading.Model.Recommend;

import java.util.List;

public interface RecommendContract {
    interface View extends BaseContract.BaseView {
        void showRecommendList(List<Recommend.RecommendBooks> list);

        void showBookToc(String bookId, List<BookMixAToc.mixToc.Chapters> list);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {

        void getRecommendList();
    }
}
