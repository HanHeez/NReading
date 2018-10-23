package com.gtv.hanhee.novelreading.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gtv.hanhee.novelreading.Base.BaseRVActivity;
import com.gtv.hanhee.novelreading.Base.Constant;
import com.gtv.hanhee.novelreading.Component.AppComponent;
import com.gtv.hanhee.novelreading.Component.DaggerFindComponent;
import com.gtv.hanhee.novelreading.Manager.CacheManager;
import com.gtv.hanhee.novelreading.Model.BookListDetail;
import com.gtv.hanhee.novelreading.Model.BookLists;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Ui.Adapter.SubjectBookListDetailBooksAdapter;
import com.gtv.hanhee.novelreading.Ui.Contract.SubjectBookListDetailContract;
import com.gtv.hanhee.novelreading.Ui.Presenter.SubjectBookListDetailPresenter;
import com.gtv.hanhee.novelreading.Utils.GlideCircleTransform;
import com.gtv.hanhee.novelreading.Utils.GlideUtils;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubjectBookListDetailActivity extends BaseRVActivity<BookListDetail.BookListBean.BooksBean> implements SubjectBookListDetailContract.View {

    private HeaderViewHolder headerViewHolder;


    static class HeaderViewHolder {
        @BindView(R.id.tvBookListTitle)
        TextView tvBookListTitle;
        @BindView(R.id.tvBookListDesc)
        TextView tvBookListDesc;
        @BindView(R.id.ivAuthorAvatar)
        ImageView ivAuthorAvatar;
        @BindView(R.id.tvBookListAuthor)
        TextView tvBookListAuthor;
        @BindView(R.id.btnShare)
        TextView btnShare;

        public HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private List<BookListDetail.BookListBean.BooksBean> mAllBooks = new ArrayList<>();

    private int start = 0;
    private int limit = 20;

    @Inject
    SubjectBookListDetailPresenter mPresenter;

    public static final String INTENT_BEAN = "bookListsBean";

    private BookLists.BookListsBean bookListsBean;

    public static void startActivity(Context context, BookLists.BookListsBean bookListsBean) {
        context.startActivity(new Intent(context, SubjectBookListDetailActivity.class)
                .putExtra(INTENT_BEAN, bookListsBean));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_subject_book_list_detail;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerFindComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle(R.string.subject_book_list_detail);
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {
        bookListsBean = (BookLists.BookListsBean) getIntent().getSerializableExtra(INTENT_BEAN);
    }

    @Override
    public void configViews() {
        initAdapter(SubjectBookListDetailBooksAdapter.class, false, true);
        mRecyclerView.removeItemDecoration(itemDecoration);
        mAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_view_book_list_detail, parent, false);
                return headerView;
            }

            @Override
            public void onBindView(View headerView) {
                headerViewHolder = new HeaderViewHolder(headerView);
            }
        });

        mPresenter.attachView(this);
        mPresenter.getBookListDetail(bookListsBean._id);
    }

    @Override
    public void showBookListDetail(BookListDetail data) {
        headerViewHolder.tvBookListTitle.setText(data.getBookList().getTitle());
        headerViewHolder.tvBookListDesc.setText(data.getBookList().getDesc());
        headerViewHolder.tvBookListAuthor.setText(data.getBookList().getAuthor().getNickname());

        GlideUtils.loadCircleGlide(mContext, Constant.IMG_BASE_URL + data.getBookList().getAuthor().getAvatar(), headerViewHolder.ivAuthorAvatar);
//        Glide.with(mContext)
//                .load(Constant.IMG_BASE_URL + data.getBookList().getAuthor().getAvatar())
//                .transform(new GlideCircleTransform(mContext))
//                .into(headerViewHolder.ivAuthorAvatar);

        List<BookListDetail.BookListBean.BooksBean> list = data.getBookList().getBooks();
        mAllBooks.clear();
        mAllBooks.addAll(list);
        mAdapter.clear();
        loadNextPage();
    }

    private void loadNextPage() {
        if (start < mAllBooks.size()) {
            if (mAllBooks.size() - start > limit) {
                mAdapter.addAll(mAllBooks.subList(start, start + limit));
            } else {
                mAdapter.addAll(mAllBooks.subList(start, mAllBooks.size()));
            }
            start += limit;
        } else {
            mAdapter.addAll(new ArrayList<BookListDetail.BookListBean.BooksBean>());
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void onItemClick(int position) {
        BookDetailActivity.startActivity(this, mAdapter.getItem(position).getBook().get_id());
    }

    @Override
    public void onRefresh() {
        mPresenter.getBookListDetail(bookListsBean._id);
    }

    @Override
    public void onLoadMore() {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadNextPage();
            }
        }, 500);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_subject_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_collect) {
            CacheManager.getInstance().addCollection(bookListsBean);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
