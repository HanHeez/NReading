package com.gtv.hanhee.novelreading.Ui.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gtv.hanhee.novelreading.Base.BaseActivity;
import com.gtv.hanhee.novelreading.Base.Constant;
import com.gtv.hanhee.novelreading.Common.OnRvItemClickListener;
import com.gtv.hanhee.novelreading.Component.AppComponent;
import com.gtv.hanhee.novelreading.Component.DaggerBookDetailActivityComponent;
import com.gtv.hanhee.novelreading.Model.BookDetail;
import com.gtv.hanhee.novelreading.Model.HotReview;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Ui.Adapter.HotReviewAdapter;
import com.gtv.hanhee.novelreading.Ui.Contract.BookDetailContract;
import com.gtv.hanhee.novelreading.Ui.Presenter.BookDetailPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.gujun.android.taggroup.TagGroup;

public class BookDetailActivity extends BaseActivity implements BookDetailContract.View,OnRvItemClickListener<HotReview.Reviews> {

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

    @Inject
    BookDetailPresenter mPresenter;

    private HotReviewAdapter mAdapter;
    private List<HotReview.Reviews> mList = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private List<String> tagsTrans = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerBookDetailActivityComponent.builder()
                .appComponent(appComponent)
                //.mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle("书籍详情");
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {
        setSupportActionBar(mCommonToolbar);

        mRvHotReview.setHasFixedSize(true);
        mRvHotReview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HotReviewAdapter(mContext, mList, this);
        mRvHotReview.setAdapter(mAdapter);

        mPresenter.attachView(this);
        mPresenter.getBookDetail(getIntent().getStringExtra("bookId"));
        mPresenter.getHotReview(getIntent().getStringExtra("bookId"));
    }

    @Override
    public void showBookDetail(BookDetail data) {
        Glide.with(mContext).load(Constant.IMG_BASE_URL + data.cover).into(mIvBookCover);
        mTvBookTitle.setText(data.titleTrans);
        mTvAuthor.setText(data.authorTrans+ " | ");
        mTvCatgory.setText(data.catTrans + " | ");
        mTvWordCount.setText(String.valueOf(data.wordCount)+" từ");
        mTvLatelyFollower.setText(String.valueOf(data.latelyFollower));
        mTvRetentionRatio.setText(String.valueOf(data.retentionRatio)+"%");
        mTvSerializeWordCount.setText(String.valueOf(data.serializeWordCount));

        mTvlongIntro.setText(data.longIntroTrans);
    }

    @Override
    public void showHotReview(List<HotReview.Reviews> list) {
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showBookDetailTags(List<BookDetail.combineTags> combineTags, int tagsSize) {
        if (combineTags.size() == tagsSize) {
            for (int i = 0; i< tagsSize; i++) {
                this.tagsTrans.add(combineTags.get(i).getTagTrans());
                this.tags.add(combineTags.get(i).getTag());
            }
            mTagGroup.setTags(this.tagsTrans);
        }

    }

    @Override
    public void showBookDetailCategories(List<BookDetail.combineCategories> combineCategories, int categoriesSize) {

    }

    @Override
    public void onItemClick(View view, int position, HotReview.Reviews data) {

    }
}

