package com.gtv.hanhee.novelreading.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gtv.hanhee.novelreading.Base.BaseActivity;
import com.gtv.hanhee.novelreading.Base.Constant;
import com.gtv.hanhee.novelreading.Common.OnRvItemClickListener;
import com.gtv.hanhee.novelreading.Component.AppComponent;
import com.gtv.hanhee.novelreading.Component.DaggerBookComponent;

import com.gtv.hanhee.novelreading.Model.BookDetail;
import com.gtv.hanhee.novelreading.Model.HotReview;
import com.gtv.hanhee.novelreading.Model.RecommendBookList;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Ui.Adapter.HotReviewAdapter;
import com.gtv.hanhee.novelreading.Ui.Adapter.RecommendBookListAdapter;
import com.gtv.hanhee.novelreading.Ui.Contract.BookDetailContract;
import com.gtv.hanhee.novelreading.Ui.CustomView.DrawableCenterButton;
import com.gtv.hanhee.novelreading.Ui.CustomView.TagColor;
import com.gtv.hanhee.novelreading.Ui.CustomView.TagGroup;
import com.gtv.hanhee.novelreading.Ui.Presenter.BookDetailPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class BookDetailActivity extends BaseActivity implements BookDetailContract.View, OnRvItemClickListener<Object> {

    @BindView(R.id.common_toolbar)
    Toolbar mCommonToolbar;
    @BindView(R.id.ivBookCover)
    ImageView mIvBookCover;
    @BindView(R.id.tvBookTitle)
    TextView mTvBookTitle;
    @BindView(R.id.tvAuthor)
    TextView mTvAuthor;
    @BindView(R.id.tvCatgory)
    TextView mTvCatgory;
    @BindView(R.id.tvWordCount)
    TextView mTvWordCount;
    @BindView(R.id.tvLatelyUpdate)
    TextView mTvLatelyUpdate;
    @BindView(R.id.tvLatelyFollower)
    TextView mTvLatelyFollower;
    @BindView(R.id.tvRetentionRatio)
    TextView mTvRetentionRatio;
    @BindView(R.id.tvSerializeWordCount)
    TextView mTvSerializeWordCount;
    @BindView(R.id.tag_group)
    TagGroup mTagGroup;
    @BindView(R.id.tvlongIntro)
    TextView mTvlongIntro;
    @BindView(R.id.tvMoreReview)
    TextView mTvMoreReview;
    @BindView(R.id.rvHotReview)
    RecyclerView mRvHotReview;
    @BindView(R.id.btnRead)
    DrawableCenterButton mBtnRead;
    @BindView(R.id.tvCommunity)
    TextView mTvCommunity;
    @BindView(R.id.tvPostCount)
    TextView mTvPostCount;
    @BindView(R.id.tvRecommendBookList)
    TextView mTvRecommendBookList;

    @BindView(R.id.rvRecommendBoookList)
    RecyclerView mRvRecommendBoookList;

    @Inject
    BookDetailPresenter mPresenter;

    private List<String> tags = new ArrayList<>();
    private List<String> tagsTrans = new ArrayList<>();

    private List<String> tagList = new ArrayList<>();
    private int times = 0;

    private HotReviewAdapter mHotReviewAdapter;
    private List<HotReview.Reviews> mHotReviewList = new ArrayList<>();
    private RecommendBookListAdapter mRecommendBookListAdapter;
    private List<RecommendBookList.RecommendBook> mRecommendBookList = new ArrayList<>();
    private String bookId;

    public static String INTENT_BOOK_ID = "bookId";

    public static void startActivity(Context context, String bookId) {
        context.startActivity(new Intent(context, BookDetailActivity.class)
                .putExtra(INTENT_BOOK_ID, bookId));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerBookComponent.builder()
                .appComponent(appComponent)
                //.mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle(R.string.book_detail);
    }

    @Override
    public void initDatas() {
        bookId = getIntent().getStringExtra("bookId");
    }

    @Override
    public void configViews() {


        mRvHotReview.setHasFixedSize(true);
        mRvHotReview.setLayoutManager(new LinearLayoutManager(this));
        mHotReviewAdapter = new HotReviewAdapter(mContext, mHotReviewList, this);
        mRvHotReview.setAdapter(mHotReviewAdapter);

        mRvRecommendBoookList.setHasFixedSize(true);
        mRvRecommendBoookList.setLayoutManager(new LinearLayoutManager(this));
        mRecommendBookListAdapter = new RecommendBookListAdapter(mContext, mRecommendBookList,
                this);
        mRvRecommendBoookList.setAdapter(mRecommendBookListAdapter);

        mTagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                startActivity(new Intent(BookDetailActivity.this, BooksByTagActivity.class)
                        .putExtra("tag", tag));
            }
        });

        mPresenter.attachView(this);
        mPresenter.getBookDetail(bookId);
        mPresenter.getHotReview(bookId);
        mPresenter.getRecommendBookList(bookId, "3");
    }

    @Override
    public void showBookDetail(BookDetail data) {
        Glide.with(mContext).load(Constant.IMG_BASE_URL + data.cover).into(mIvBookCover);
        mTvBookTitle.setText(data.titleTrans);
        mTvAuthor.setText(String.format(getString(R.string.book_detail_author), data.authorTrans));
        mTvCatgory.setText(String.format(getString(R.string.book_detail_category), data.catTrans));
        mTvWordCount.setText(String.valueOf(data.wordCount));
        mTvLatelyFollower.setText(String.valueOf(data.latelyFollower));
        mTvRetentionRatio.setText(String.valueOf(data.retentionRatio) + "%");
        mTvSerializeWordCount.setText(String.valueOf(data.serializeWordCount));

        tagList.clear();
        tagList.addAll(data.tags);
        times = 0;
        showHotWord();

        mTvlongIntro.setText(data.longIntroTrans);
        mTvCommunity.setText(String.format(getString(R.string.book_detail_community), data.titleTrans));
        mTvPostCount.setText(String.format(getString(R.string.book_detail_post_count), data
                .postCount));
    }


    @Override
    public void showBookDetailTags(List<BookDetail.combineTags> combineTags, int tagsSize) {
        if (combineTags.size() == tagsSize) {
            for (int i = 0; i < tagsSize; i++) {
                this.tagsTrans.add(combineTags.get(i).getTagTrans());
                this.tags.add(combineTags.get(i).getTag());
            }
            mTagGroup.setTags(this.tagsTrans);
        }

    }

    @Override
    public void showBookDetailCategories(List<BookDetail.combineCategories> combineCategories, int categoriesSize) {

    }

    private void showHotWord() {
        int start, end;
        if (times < tagList.size() && times + 8 <= tagList.size()) {
            start = times;
            end = times + 8;
        } else if (times < tagList.size() - 1 && times + 8 > tagList.size()) {
            start = times;
            end = tagList.size() - 1;
        } else {
            start = 0;
            end = tagList.size() > 8 ? 8 : tagList.size();
        }
        times = end;
        if (end - start > 0) {
            List<String> batch = tagList.subList(start, end);
            List<TagColor> colors = TagColor.getRandomColors(batch.size());
            mTagGroup.setTags(colors, (String[]) batch.toArray(new String[batch.size()]));
        }
    }

    @Override
    public void showHotReview(List<HotReview.Reviews> list) {
        mHotReviewList.clear();
        mHotReviewList.addAll(list);
        mHotReviewAdapter.notifyDataSetChanged();
    }


    @Override
    public void showRecommendBookList(List<RecommendBookList.RecommendBook> list) {
        if (!list.isEmpty()) {
            mTvRecommendBookList.setVisibility(View.VISIBLE);
            mRecommendBookList.clear();
            mRecommendBookList.addAll(list);
            mRecommendBookListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position, Object data) {
        if (data instanceof HotReview.Reviews) {

        } else if (data instanceof RecommendBookList.RecommendBook) {

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnRead)
    public void onClickRead() {
        startActivity(new Intent(this, ReadActivity.class)
                .putExtra("bookId", bookId));
    }
}

