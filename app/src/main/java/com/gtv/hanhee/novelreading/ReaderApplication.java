package com.gtv.hanhee.novelreading;

import android.app.Application;

import com.gtv.hanhee.novelreading.Component.AppComponent;

import com.gtv.hanhee.novelreading.Component.DaggerAppComponent;
import com.gtv.hanhee.novelreading.Module.AppModule;
import com.gtv.hanhee.novelreading.Module.ReaderApiModule;
import com.gtv.hanhee.novelreading.Utils.AppUtils;

public class ReaderApplication extends Application {
    private static ReaderApplication sInstance;
    private AppComponent appComponent;

    public static ReaderApplication getsInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.sInstance = this;
        initComponent();
        AppUtils.init(this);
    }

    private void initComponent() {
        appComponent = DaggerAppComponent.builder()
                .readerApiModule(new ReaderApiModule())
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
