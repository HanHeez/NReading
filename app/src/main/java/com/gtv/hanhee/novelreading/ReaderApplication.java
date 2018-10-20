package com.gtv.hanhee.novelreading;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.gtv.hanhee.novelreading.Base.Constant;
import com.gtv.hanhee.novelreading.Component.AppComponent;

import com.gtv.hanhee.novelreading.Component.DaggerAppComponent;
import com.gtv.hanhee.novelreading.Module.AppModule;
import com.gtv.hanhee.novelreading.Module.ReaderApiModule;
import com.gtv.hanhee.novelreading.Utils.AppUtils;
import com.gtv.hanhee.novelreading.Utils.FileUtils;
import com.gtv.hanhee.novelreading.Utils.LogUtils;
import com.gtv.hanhee.novelreading.Utils.SharedPreferencesUtil;
import com.sinovoice.hcicloudsdk.api.HciCloudSys;
import com.sinovoice.hcicloudsdk.common.HciErrorCode;
import com.sinovoice.hcicloudsdk.common.InitParam;

public class ReaderApplication extends Application {

    private static ReaderApplication sInstance;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.sInstance = this;
        initCompoent();
        AppUtils.init(this);
        initPrefs();
        initNightMode();
        initHciCloud();
    }

    public static ReaderApplication getsInstance() {
        return sInstance;
    }

    private void initCompoent() {
        appComponent = DaggerAppComponent.builder()
                .readerApiModule(new ReaderApiModule())
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    /**
     * 初始化SharedPreference
     */
    protected void initPrefs() {
        SharedPreferencesUtil.init(getApplicationContext(), getPackageName() + "_preference", Context.MODE_MULTI_PROCESS);
    }

    protected void initNightMode() {
        boolean isNight = SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false);
        LogUtils.d("isNight=" + isNight);
        if (isNight) {
            //使用夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            //不使用夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    protected void initHciCloud() {
        InitParam initparam = new InitParam();
        String authDirPath = getFilesDir().getAbsolutePath();
        initparam.addParam(InitParam.AuthParam.PARAM_KEY_AUTH_PATH, authDirPath);
        initparam.addParam(InitParam.AuthParam.PARAM_KEY_AUTO_CLOUD_AUTH, "no");
        initparam.addParam(InitParam.AuthParam.PARAM_KEY_CLOUD_URL, "test.api.hcicloud.com:8888");
        initparam.addParam(InitParam.AuthParam.PARAM_KEY_DEVELOPER_KEY, "0a5e69f8fb1c019b2d87a17acf200889");
        initparam.addParam(InitParam.AuthParam.PARAM_KEY_APP_KEY, "0d5d5466");
        String logDirPath = FileUtils.createRootPath(this)+"/hcicloud";
        FileUtils.createDir(logDirPath);
        initparam.addParam(InitParam.LogParam.PARAM_KEY_LOG_FILE_PATH, logDirPath);
        initparam.addParam(InitParam.LogParam.PARAM_KEY_LOG_FILE_COUNT, "5");
        initparam.addParam(InitParam.LogParam.PARAM_KEY_LOG_FILE_SIZE, "1024");
        initparam.addParam(InitParam.LogParam.PARAM_KEY_LOG_LEVEL, "5");
        int errCode = HciCloudSys.hciInit(initparam.getStringConfig(), this);
        if (errCode != HciErrorCode.HCI_ERR_NONE) {
            LogUtils.e("HciCloud初始化失败"+errCode);
            return;
        }
        LogUtils.e("HciCloud初始化成功");
    }
}
