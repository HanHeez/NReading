package com.gtv.hanhee.novelreading.Base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.WindowManager;

import com.gtv.hanhee.novelreading.Component.AppComponent;
import com.gtv.hanhee.novelreading.ReaderApplication;
import com.gtv.hanhee.novelreading.Ui.CustomView.CustomDialog;
import com.gtv.hanhee.novelreading.Utils.SharedPreferencesUtil;
import com.gtv.hanhee.novelreading.Utils.StatusBarCompat;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected int statusBarColor = 0;
    Unbinder unbinder;
    private CustomDialog dialog;
    private boolean mNowMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        StatusBarCompat.compat(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //Thanh trạng thái trong suốt
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mContext = this;
        unbinder = ButterKnife.bind(this);
        setupActivityComponent(ReaderApplication.getsInstance().getAppComponent());
        initToolBar();
        initDatas();
        configViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false) != mNowMode) {
            if (SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public abstract int getLayoutId();

    protected abstract void setupActivityComponent(AppComponent appComponent);

    public abstract void initToolBar();

    public abstract void initDatas();

    public abstract void configViews();

    // dialog
    public CustomDialog getDialog() {
        if (dialog == null) {
            dialog = CustomDialog.instance(this);
            dialog.setCancelable(true);
        }
        return dialog;
    }

    public void hideDialog() {
        if (dialog != null)
            dialog.hide();
    }

    public void showDialog() {
        getDialog().show();
    }
}
