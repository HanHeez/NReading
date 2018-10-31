package com.gtv.hanhee.novelreading.Ui.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gtv.hanhee.novelreading.Base.BaseFragment;
import com.gtv.hanhee.novelreading.Common.OnRvItemClickListener;
import com.gtv.hanhee.novelreading.Component.AppComponent;
import com.gtv.hanhee.novelreading.Model.FindBean;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Ui.Activity.BookDiscussionActivity;
import com.gtv.hanhee.novelreading.Ui.Activity.BookHelpActivity;
import com.gtv.hanhee.novelreading.Ui.Activity.BookReviewActivity;
import com.gtv.hanhee.novelreading.Ui.Activity.GirlBookDiscussionActivity;
import com.gtv.hanhee.novelreading.Ui.Adapter.FindAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityFragment extends BaseFragment implements OnRvItemClickListener<FindBean> {

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
        mList.add(new FindBean("Thảo luận chung", R.drawable.discuss_section));
        mList.add(new FindBean("Review truyện", R.drawable.comment_section));
        mList.add(new FindBean("Khu vực hỗ trợ", R.drawable.helper_section));
        mList.add(new FindBean("Khu vực dành cho phái nữ", R.drawable.girl_section));
        mList.add(new FindBean("Khu vực chung",R.drawable.yuanchuang));
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
            case 0:
                BookDiscussionActivity.startActivity(activity,true);
                getActivity().overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                break;
//            case 1:
//                BookReviewActivity.startActivity(activity);
//                break;
//            case 2:
//                BookHelpActivity.startActivity(activity);
//                break;
//            case 3:
//                GirlBookDiscussionActivity.startActivity(activity);
//                break;
            case 4:
                BookDiscussionActivity.startActivity(activity,false);
                break;
            default:
                break;
        }
    }

}
