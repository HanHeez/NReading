package com.gtv.hanhee.novelreading.Ui.Fragment;

import android.os.Bundle;

import com.gtv.hanhee.novelreading.Base.BaseRVFragment;
import com.gtv.hanhee.novelreading.Base.Constant;
import com.gtv.hanhee.novelreading.Component.AppComponent;
import com.gtv.hanhee.novelreading.Component.DaggerCommunityComponent;
import com.gtv.hanhee.novelreading.Model.DiscussionList;
import com.gtv.hanhee.novelreading.Model.Support.SelectionEvent;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Ui.Activity.BookDiscussionDetailActivity;
import com.gtv.hanhee.novelreading.Ui.Adapter.BookDiscussionAdapter;
import com.gtv.hanhee.novelreading.Ui.Contract.BookDiscussionContract;
import com.gtv.hanhee.novelreading.Ui.Presenter.BookDiscussionPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class BookDiscussionFragment extends BaseRVFragment<BookDiscussionPresenter, DiscussionList.PostsBean> implements BookDiscussionContract.View {

    private static final String BUNDLE_BLOCK = "block";

    public static BookDiscussionFragment newInstance(String block) {
        BookDiscussionFragment fragment = new BookDiscussionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_BLOCK, block);
        fragment.setArguments(bundle);
        return fragment;
    }

    private String block = "ramble";
    private String sort = Constant.SortType.DEFAULT;
    private String distillate = Constant.Distillate.ALL;

    @Override
    public int getLayoutResId() {
        return R.layout.common_easy_recyclerview;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerCommunityComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initDatas() {
        block = getArguments().getString(BUNDLE_BLOCK);
        EventBus.getDefault().register(this);
    }

    @Override
    public void configViews() {
        initAdapter(BookDiscussionAdapter.class, true, true);
        onRefresh();
    }

    @Override
    public void showBookDisscussionList(List<DiscussionList.PostsBean> list, boolean isRefresh) {
        if (isRefresh) {
            mAdapter.clear();
            start = 0;
        }
        mAdapter.addAll(list);
        start = start + list.size();
    }

    @Override
    public void showError() {
        loaddingError();
    }

    @Override
    public void complete() {
        mRecyclerView.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initCategoryList(SelectionEvent event) {
        mRecyclerView.setRefreshing(true);
        sort = event.sort;
        distillate = event.distillate;
        onRefresh();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.getBookDisscussionList(block, sort, distillate, 0, limit);
    }

    @Override
    public void onLoadMore() {
        mPresenter.getBookDisscussionList(block, sort, distillate, start, limit);
    }

    @Override
    public void onItemClick(int position) {
        DiscussionList.PostsBean data = mAdapter.getItem(position);
        BookDiscussionDetailActivity.startActivity(activity, data._id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
