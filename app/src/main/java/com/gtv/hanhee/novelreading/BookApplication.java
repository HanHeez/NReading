package com.gtv.hanhee.novelreading;

import android.app.Application;

import com.gtv.hanhee.novelreading.Module.AppModule;
import com.gtv.hanhee.novelreading.Module.BookApiModule;

public class BookApplication extends Application {
    private static BookApplication sInstance;
    private AppComponent appComponent;

    public static BookApplication getsInstance() {
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
