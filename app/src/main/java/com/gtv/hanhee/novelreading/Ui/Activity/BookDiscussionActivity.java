package com.gtv.hanhee.novelreading.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gtv.hanhee.novelreading.Base.BaseCommuniteActivity;
import com.gtv.hanhee.novelreading.Component.AppComponent;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Ui.CustomView.SelectionLayout;
import com.gtv.hanhee.novelreading.Ui.Fragment.BookDiscussionFragment;

import java.util.List;

import butterknife.BindView;

public class BookDiscussionActivity extends BaseCommuniteActivity {

    private static final String INTENT_DIS = "isDis";

    public static void startActivity(Context context, boolean isDiscussion) {
        context.startActivity(new Intent(context, BookDiscussionActivity.class)
                .putExtra(INTENT_DIS, isDiscussion));
    }

    private boolean mIsDiscussion;

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_discussion;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mIsDiscussion = getIntent().getBooleanExtra(INTENT_DIS, false);
        if (mIsDiscussion) {
            mCommonToolbar.setTitle("综合讨论区");
        } else {
            mCommonToolbar.setTitle("原创区");
        }
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {
        super.initDatas();
    }

    @Override
    protected List<List<String>> getTabList() {
        return list1;
    }

    @Override
    public void configViews() {
        BookDiscussionFragment fragment = BookDiscussionFragment.newInstance(mIsDiscussion ? "ramble" : "original");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentCO, fragment).commit();
    }
}
