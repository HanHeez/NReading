package com.gtv.hanhee.novelreading.Ui.Contract;

import com.gtv.hanhee.novelreading.Base.BaseContract;
import com.gtv.hanhee.novelreading.Model.DiscussionList;

import java.util.List;

public interface BookDiscussionContract {

    interface View extends BaseContract.BaseView {
        void showBookDisscussionList(List<DiscussionList.PostsBean> list, boolean isRefresh);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getBookDisscussionList(String block, String sort, String distillate, int start, int limit);
    }

}
