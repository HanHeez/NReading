package com.gtv.hanhee.novelreading.Ui.Activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.gtv.hanhee.novelreading.Base.BaseActivity;
import com.gtv.hanhee.novelreading.Common.OnRvItemClickListener;
import com.gtv.hanhee.novelreading.Component.AppComponent;
import com.gtv.hanhee.novelreading.Component.DaggerBooksByTagActivityComponent;
import com.gtv.hanhee.novelreading.Model.BooksByTag;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Ui.Adapter.BooksByTagAdapter;
import com.gtv.hanhee.novelreading.Ui.Contract.BooksByTagContract;
import com.gtv.hanhee.novelreading.Ui.Presenter.BooksByTagPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class BooksByTagActivity extends BaseActivity implements BooksByTagContract.View,
        OnRvItemClickListener<BooksByTag.TagBook> {

    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Inject
    BooksByTagPresenter mPresenter;
    private LinearLayoutManager linearLayoutManager;
    private BooksByTagAdapter mAdapter;
    private List<BooksByTag.TagBook> mList = new ArrayList<>();

    private String tag;
    private int current = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_books_by_tag;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerBooksByTagActivityComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {
        mToolbar.setTitle(getIntent().getStringExtra("tag"));
        mToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {
        tag = getIntent().getStringExtra("tag");
    }

    @Override
    public void configViews() {


        refreshLayout.setOnRefreshListener(new RefreshListener());

        mRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new BooksByTagAdapter(mContext, mList, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RefreshListener());

        mPresenter.attachView(this);
        mPresenter.getBooksByTag(tag, current + "", (current + 10) + "");
    }


    @Override
    public void showBooksByTag(List<BooksByTag.TagBook> list, boolean isRefresh) {
        if (isRefresh)
            mList.clear();
        mList.addAll(list);
        current = mList.size();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadComplete(boolean isSuccess, String msg) {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position, BooksByTag.TagBook data) {
        startActivity(new Intent(BooksByTagActivity.this, BookDetailActivity.class)
                .putExtra("bookId", data._id));
    }

    private class RefreshListener extends RecyclerView.OnScrollListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            current = 0;
            mPresenter.getBooksByTag(tag, current + "", "10");
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            if (lastVisibleItemPosition + 1 == mAdapter.getItemCount()) { // Trượt đến cuối cùng thì tải thêm

                boolean isRefreshing = refreshLayout.isRefreshing();
                if (isRefreshing) {
                    mAdapter.notifyItemRemoved(mAdapter.getItemCount());
                    return;
                }
                mPresenter.getBooksByTag(tag, current + "", "10");
            }
        }
    }

}
