package com.gtv.hanhee.novelreading.Base;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Utils.NetworkUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import butterknife.BindView;

public abstract class BaseRVActivity<T> extends BaseActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnItemClickListener {

    @BindView(R.id.recyclerview)
    protected EasyRecyclerView mRecyclerView;

    protected RecyclerArrayAdapter<T> mAdapter;

    protected int start = 0;
    protected int limit = 20;

    protected void initAdapter(boolean refreshable, boolean loadmoreable) {
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

        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(this, R.color.common_divider_narrow), 1, 0, 0);//color & height & paddingLeft & paddingRight
            itemDecoration.setDrawLastItem(true);//sometimes you don't want draw the divider for the last item,default is true.
            itemDecoration.setDrawHeaderFooter(false);//whether draw divider for header and footer,default is false.
            mRecyclerView.addItemDecoration(itemDecoration);
            mRecyclerView.setAdapterWithProgress(mAdapter);
        }
    }

    protected void initAdapter(Class<? extends RecyclerArrayAdapter<T>> clazz, boolean refreshable, boolean loadmoreable) {
        mAdapter = (RecyclerArrayAdapter) createInstance(clazz);
        initAdapter(refreshable, loadmoreable);
    }

    public Object createInstance(Class<?> cls) {
        Object obj;
        try {
            //cl là constructor của Class <?> truyền vào, kiểu dữ liệu là Context.class,
            // ở đây là : public Class<?>(Context context) { this.context = context }
            Constructor c1 = cls.getDeclaredConstructor(Context.class);
            c1.setAccessible(true);
            // trả về new Class<?>(mContext) , theo như initAdapter thì nếu Class<?> truyền vào là
            //  SearchAdapter extends RecyclerArrayAdapter<SearchDetail.SearchBooks> thì obj sẽ là:
            // obj = new SearchAdapter<SearchDetail.SearchBooks>(mContext);
            // định nghĩa cho context = mContext, mAdapter = new ArrayList<SearchDetail.SearchBooks>
            obj = c1.newInstance(mContext);
        } catch (Exception e) {
            obj = null;
        }
        return obj;
    }

    @Override
    public void onLoadMore() {
        if (!NetworkUtils.isConnected(getApplicationContext())) {
            mAdapter.pauseMore();
            return;
        }
    }

    @Override
    public void onRefresh() {
        start = 0;
        if (!NetworkUtils.isConnected(getApplicationContext())) {
            mAdapter.pauseMore();
            return;
        }
    }

    protected void loaddingError(){
        mAdapter.clear();
        mAdapter.pauseMore();
        mRecyclerView.setRefreshing(false);
    }
}
