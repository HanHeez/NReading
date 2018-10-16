package com.gtv.hanhee.novelreading;

import android.app.Application;

import com.gtv.hanhee.novelreading.Module.AppModule;
import com.gtv.hanhee.novelreading.Module.BookApiModule;

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
    }

    private void initComponent() {
        appComponent = DaggerAppComponent.builder()
                .bookApiModule(new BookApiModule())
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
