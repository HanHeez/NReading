package com.gtv.hanhee.novelreading.Ui.Fragment;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gtv.hanhee.novelreading.Base.BaseFragment;
import com.gtv.hanhee.novelreading.Common.OnRvItemClickListener;
import com.gtv.hanhee.novelreading.Component.AppComponent;
import com.gtv.hanhee.novelreading.Model.FindBean;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Ui.Adapter.FindAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FindFragment extends BaseFragment implements OnRvItemClickListener<FindBean> {

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
        mList.add(new FindBean("Xếp hạng", R.drawable.home_find_rank));
        mList.add(new FindBean("Chủ đề", R.drawable.home_find_topic));
        mList.add(new FindBean("Phân loại", R.drawable.home_find_category));
    }

    @Override
    public void configViews() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(mContext, R.color.common_divider_narrow), 1, 0, 0);//color & height & paddingLeft & paddingRight
        itemDecoration.setDrawLastItem(true);//sometimes you don't want draw the divider for the last item,default is true.
        itemDecoration.setDrawHeaderFooter(false);//whether draw divider for header and footer,default is false.
        mRecyclerView.addItemDecoration(itemDecoration);

        mAdapter = new FindAdapter(mContext, mList, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void attachView() {

    }

    @Override
    public void onItemClick(View view, int position, FindBean data) {
        switch (position) {
//            case 0:
//                TopRankActivity.startActivity(activity);
//                break;
//            case 1:
//                SubjectBookListActivity.startActivity(activity);
//                break;
//            case 2:
//                startActivity(new Intent(activity, TopCategoryListActivity.class));
//                break;
//            default:
//                break;
        }
    }

}
