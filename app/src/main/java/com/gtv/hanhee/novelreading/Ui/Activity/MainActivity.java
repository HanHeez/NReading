package com.gtv.hanhee.novelreading.Ui.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gtv.hanhee.novelreading.AppComponent;
import com.gtv.hanhee.novelreading.Base.BaseActivity;
import com.gtv.hanhee.novelreading.Module.MainActivityModule;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Ui.Component.DaggerMainActivityComponent;
import com.gtv.hanhee.novelreading.Ui.Component.MainActivityComponent;
import com.gtv.hanhee.novelreading.Ui.Contract.MainContract;
import com.gtv.hanhee.novelreading.Ui.Presenter.MainActivityPresenter;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainContract.View  {

    @Inject
    MainActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter.getPlayerList();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainActivityComponent.builder()
                .appComponent(appComponent)
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
    }
}


