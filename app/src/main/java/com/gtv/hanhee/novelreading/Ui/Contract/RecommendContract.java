package com.gtv.hanhee.novelreading.Ui.Contract;

import com.gtv.hanhee.novelreading.Model.Recommend;

import java.util.List;

public interface RecommendContract {
    interface View {
        void showRecommendList(List<Recommend.RecommendBooks> list);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {

        void getRecommendList();
    }
}
