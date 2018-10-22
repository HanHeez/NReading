package com.gtv.hanhee.novelreading.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gtv.hanhee.novelreading.Base.BaseActivity;
import com.gtv.hanhee.novelreading.Component.AppComponent;
import com.gtv.hanhee.novelreading.R;

public class BookDiscussionDetailActivity extends BaseActivity {

    private static final String INTENT_ID = "id";

    public static void startActivity(Context context, String id) {
        context.startActivity(new Intent(context, BookDiscussionDetailActivity.class)
                .putExtra(INTENT_ID, id));
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_community_book_discussion_detail;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {

    }
}
