package com.gtv.hanhee.novelreading.Base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gtv.hanhee.novelreading.AppComponent;
import com.gtv.hanhee.novelreading.BookApplication;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        setupActivityComponent(BookApplication.getsInstance().getAppComponent());
        initToolBar();
        initDatas();
        configViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    protected abstract int getLayoutId();

    protected abstract void setupActivityComponent(AppComponent appComponent);

    public abstract void initToolBar();

    public abstract void initDatas();

    public abstract void configViews();
}
