package com.gtv.hanhee.novelreading.Ui.Activity;

import android.content.Context;
import android.content.Intent;

import com.gtv.hanhee.novelreading.Base.BaseActivity;
import com.gtv.hanhee.novelreading.Component.AppComponent;
import com.gtv.hanhee.novelreading.R;

public class ScanLocalBookActivity extends BaseActivity {


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ScanLocalBookActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_local_book;
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
