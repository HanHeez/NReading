package com.gtv.hanhee.novelreading.Base;

import android.app.Activity;
import android.os.Bundle;

import com.gtv.hanhee.novelreading.AppComponent;
import com.gtv.hanhee.novelreading.BookApplication;

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent(BookApplication.getsInstance().getAppComponent());
    }

    protected abstract void setupActivityComponent(AppComponent appComponent);
}
