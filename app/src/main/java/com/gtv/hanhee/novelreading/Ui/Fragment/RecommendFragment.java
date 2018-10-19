package com.gtv.hanhee.novelreading.Ui.Fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.gtv.hanhee.novelreading.Base.BaseFragment;
import com.gtv.hanhee.novelreading.Common.OnRvItemClickListener;
import com.gtv.hanhee.novelreading.Component.AppComponent;

import com.gtv.hanhee.novelreading.Component.DaggerRecommendFragmentComponent;
import com.gtv.hanhee.novelreading.Model.Recommend;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Ui.Activity.BookReadActivity;
import com.gtv.hanhee.novelreading.Ui.Adapter.RecommendAdapter;
import com.gtv.hanhee.novelreading.Ui.Contract.RecommendContract;
import com.gtv.hanhee.novelreading.Ui.Presenter.RecommendPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class RecommendFragment extends BaseFragment implements RecommendContract.View, OnRvItemClickListener<Recommend.RecommendBooks> {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    RecommendPresenter mPresenter;

    private RecommendAdapter mAdapter;
    private List<Recommend.RecommendBooks> mList = new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {


            }
        });

        mAdapter = new RecommendAdapter(mContext, mList, this);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.attachView(this);
        mPresenter.getRecommendList();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerRecommendFragmentComponent.builder()
                .appComponent(appComponent)
                //.recommendFragmentModule(new RecommendFragmentModule(this))
                .build()
                .inject(this);
    }


    @Override
    public void showRecommendList(List<Recommend.RecommendBooks> list) {
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(View view, int position, Recommend.RecommendBooks data) {

        startActivity(new Intent(activity, BookReadActivity.class)
                .putExtra("bookId", data._id));

    }
}