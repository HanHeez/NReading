package com.gtv.hanhee.novelreading.Base;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.gtv.hanhee.novelreading.R;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.lang.reflect.Constructor;

import javax.inject.Inject;

import butterknife.BindView;

public abstract class BaseRVFragment<T1 extends BaseContract.BasePresenter, T2> extends BaseFragment implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnItemClickListener {

    @Inject
    protected T1 mPresenter;

    protected TextView tipView;

    @BindView(R.id.recyclerview)
    protected EasyRecyclerView mRecyclerView;
    protected RecyclerArrayAdapter<T2> mAdapter;

    protected int start = 0;
    protected int limit = 20;

    @Override
    public void attachView() {
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    protected void initAdapter(boolean refreshable, boolean loadmoreable) {
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getSupportActivity()));
            DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(activity, R.color.common_divider_narrow), 1, 0, 0);//color & height & paddingLeft & paddingRight
            itemDecoration.setDrawLastItem(true);//sometimes you don't want draw the divider for the last item,default is true.
            itemDecoration.setDrawHeaderFooter(false);//whether draw divider for header and footer,default is false.
            mRecyclerView.addItemDecoration(itemDecoration);
            mRecyclerView.setAdapterWithProgress(mAdapter);
        }

        if (mAdapter != null) {
            mAdapter.setOnItemClickListener(this);
            mAdapter.setError(R.layout.common_error_view, new RecyclerArrayAdapter.OnErrorListener() {
                @Override
                public void onErrorShow() {
                    mAdapter.resumeMore();
                }

                @Override
                public void onErrorClick() {
                    mAdapter.resumeMore();
                }
            });
            if (loadmoreable) {
                mAdapter.setMore(R.layout.common_more_view, this);
                mAdapter.setNoMore(R.layout.common_nomore_view);
            }
            if (refreshable && mRecyclerView != null) {
                mRecyclerView.setRefreshListener(this);
            }
        }
    }

    protected void initAdapter(Class<? extends RecyclerArrayAdapter<T2>> clazz, boolean refreshable, boolean loadmoreable) {
        mAdapter = (RecyclerArrayAdapter<T2>) createInstance(clazz);
        initAdapter(refreshable, loadmoreable);
    }

    public Object createInstance(Class<?> cls) {
        Object obj;
        try {
            Constructor c1 = cls.getDeclaredConstructor(Context.class);
            c1.setAccessible(true);
            obj = c1.newInstance(mContext);
        } catch (Exception e) {
            obj = null;
        }
        return obj;
    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public void onRefresh() {
        mRecyclerView.setRefreshing(true);
    }

    protected void loaddingError() {
        if (mAdapter.getCount() < 1) {
            // Lưu ý rằng bộ nhớ cache không được tải, sau đó hiển thị errorview Nếu có bộ nhớ cache,
            // lỗi sẽ không được hiển thị ngay cả khi làm mới không thành công.
            mAdapter.clear();
        }
        mAdapter.pauseMore();
        mRecyclerView.setRefreshing(false);
        showTipViewAndDelayClose("Mạng chưa kết nối");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    public void showTipViewAndDelayClose(String tip) {
        tipView.setText(tip);
        Animation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        tipView.startAnimation(mShowAction);
        tipView.setVisibility(View.VISIBLE);

        tipView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        -1.0f);
                mHiddenAction.setDuration(500);
                tipView.startAnimation(mHiddenAction);
                tipView.setVisibility(View.GONE);
            }
        }, 2200);
    }

    public void showTipView(String tip) {
        tipView.setText(tip);
        Animation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        tipView.startAnimation(mShowAction);
        tipView.setVisibility(View.VISIBLE);
    }

    public void hideTipView(long delayMillis) {
        tipView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        -1.0f);
                mHiddenAction.setDuration(500);
                tipView.startAnimation(mHiddenAction);
                tipView.setVisibility(View.GONE);
            }
        }, delayMillis);
    }

    public void setTipViewText(String tip) {
        if (!isTipViewVisible())
            showTipView(tip);
        else
            tipView.setText(tip);
    }

    public boolean isTipViewVisible() {
        return tipView.getVisibility() == View.VISIBLE;
    }

}
