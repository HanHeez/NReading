package com.gtv.hanhee.novelreading.Ui.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gtv.hanhee.novelreading.Base.BaseFragment;
import com.gtv.hanhee.novelreading.Common.OnRvItemClickListener;
import com.gtv.hanhee.novelreading.Component.AppComponent;
import com.gtv.hanhee.novelreading.Model.FindBean;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Ui.Adapter.FindAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FindFragment extends BaseFragment implements
        OnRvItemClickListener<FindBean> {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private FindAdapter mAdapter;
    private List<FindBean> mList = new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_find;
    }

    @Override
    public void initDatas() {
        mList.clear();
        mList.add(new FindBean("排行榜",R.drawable.home_find_rank));
        mList.add(new FindBean("主题书单",R.drawable.home_find_topic));
        mList.add(new FindBean("分类",R.drawable.home_find_category));
        mList.add(new FindBean("有声小说",R.drawable.home_find_listen));
    }

    @Override
    public void configViews() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new FindAdapter(mContext, mList,this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void onItemClick(View view, int position, FindBean data) {
        switch (position){
            case 0:
                // startActivity(new Intent(activity, BookReadActivity.class));
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
    }

}
