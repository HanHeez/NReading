package com.gtv.hanhee.novelreading.Ui.Contract;

import com.gtv.hanhee.novelreading.Base.BaseContract;
import com.gtv.hanhee.novelreading.Model.SearchDetail;

import java.util.List;

public interface SearchContract {

    interface View extends BaseContract.BaseView {
        void showHotWordList(List<String> list);

        void showAutoCompleteList(List<String> list);

        void showSearchResultList(List<SearchDetail.SearchBooks> list);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getHotWordList();

        void getAutoCompleteList(String query);

        void getSearchResultList(String query);
    }

}
