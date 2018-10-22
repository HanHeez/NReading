package com.gtv.hanhee.novelreading.Ui.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.gtv.hanhee.novelreading.Base.BaseActivity;
import com.gtv.hanhee.novelreading.Base.Constant;
import com.gtv.hanhee.novelreading.Component.AppComponent;

import com.gtv.hanhee.novelreading.Component.DaggerMainComponent;
import com.gtv.hanhee.novelreading.Manager.EventManager;
import com.gtv.hanhee.novelreading.Manager.SettingManager;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Service.DownloadBookService;
import com.gtv.hanhee.novelreading.Ui.Adapter.RecommendTabLayoutAdapter;
import com.gtv.hanhee.novelreading.Ui.Contract.MainContract;
import com.gtv.hanhee.novelreading.Ui.CustomView.GenderPopupWindow;
import com.gtv.hanhee.novelreading.Ui.CustomView.LoginPopupWindow;
import com.gtv.hanhee.novelreading.Ui.Fragment.CommunityFragment;
import com.gtv.hanhee.novelreading.Ui.Fragment.FindFragment;
import com.gtv.hanhee.novelreading.Ui.Fragment.RecommendFragment;
import com.gtv.hanhee.novelreading.Ui.Presenter.MainActivityPresenter;
import com.gtv.hanhee.novelreading.Utils.SharedPreferencesUtil;
import com.gtv.hanhee.novelreading.Utils.ToastUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainContract.View, LoginPopupWindow.LoginTypeListener {

    // Thời gian nhấn phím back ứng dụng
    private static final int BACK_PRESSED_INTERVAL = 2000;
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    String[] mDatas = new String[]{};
    RecommendTabLayoutAdapter recommendTabLayoutAdapter;
    @Inject
    MainActivityPresenter mPresenter;
    private List<Fragment> mTabContents;

    // Thời gian thoát
    private long currentBackPressedTime = 0;
    private LoginPopupWindow popupWindow;
    private GenderPopupWindow genderPopupWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {
        setTitle("");
    }

    public void pullSyncBookShelf() {
        mPresenter.syncBookShelf();
    }

    @Override
    public void initDatas() {
        startService(new Intent(this, DownloadBookService.class));
        mTabContents = new ArrayList<>();
        mDatas = getResources().getStringArray(R.array.home_tabs);

        mTabContents = new ArrayList<>();
        mTabContents.add(new RecommendFragment());
        mTabContents.add(new CommunityFragment());
        mTabContents.add(new FindFragment());

        recommendTabLayoutAdapter = new RecommendTabLayoutAdapter(getSupportFragmentManager(), mTabContents, mDatas);
        mViewPager.setAdapter(recommendTabLayoutAdapter);
        mViewPager.setOffscreenPageLimit(3);

        recommendTabLayoutAdapter.notifyDataSetChanged();

        tabLayout.setViewPager(mViewPager);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }


    public void configViews() {
        mPresenter.attachView(this);

        tabLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!SettingManager.getInstance().isUserChooseSex()
                        && (genderPopupWindow == null || !genderPopupWindow.isShowing())) {
                    showChooseSexPopupWindow();
                } else {
                    showDialog();
                    mPresenter.syncBookShelf();
                }
            }
        }, 500);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                tabLayout.setCurrentTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public void showChooseSexPopupWindow() {
        if (genderPopupWindow == null) {
            genderPopupWindow = new GenderPopupWindow(MainActivity.this);
        }
        if (!SettingManager.getInstance().isUserChooseSex()
                && !genderPopupWindow.isShowing()) {
            genderPopupWindow.showAtLocation(mCommonToolbar, Gravity.CENTER, 0, 0);
        }
    }

    public void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
            case R.id.action_login:
                if (popupWindow == null) {
                    popupWindow = new LoginPopupWindow(this);
                    popupWindow.setLoginTypeListener(this);
                }
                popupWindow.showAtLocation(mCommonToolbar, Gravity.CENTER, 0, 0);
                break;
            case R.id.action_my_message:
                if (popupWindow == null) {
                    popupWindow = new LoginPopupWindow(this);
                    popupWindow.setLoginTypeListener(this);
                }
                popupWindow.showAtLocation(mCommonToolbar, Gravity.CENTER, 0, 0);
                break;
            case R.id.action_sync_bookshelf:
                showDialog();
                mPresenter.syncBookShelf();
               /* if (popupWindow == null) {
                    popupWindow = new LoginPopupWindow(this);
                    popupWindow.setLoginTypeListener(this);
                }
                popupWindow.showAtLocation(mCommonToolbar, Gravity.CENTER, 0, 0);*/
                break;
            case R.id.action_scan_local_book:
                ScanLocalBookActivity.startActivity(this);
                break;
            case R.id.action_wifi_book:
                WifiBookActivity.startActivity(this);
                break;
            case R.id.action_feedback:
                FeedbackActivity.startActivity(this);
                break;
            case R.id.action_night_mode:
                if (SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false)) {
                    SharedPreferencesUtil.getInstance().putBoolean(Constant.ISNIGHT, false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    SharedPreferencesUtil.getInstance().putBoolean(Constant.ISNIGHT, true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                recreate();
                break;
            case R.id.action_settings:
                SettingActivity.startActivity(this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
                currentBackPressedTime = System.currentTimeMillis();
                ToastUtils.showToast(getString(R.string.exit_tips));
                return true;
            } else {
                finish(); // Thoát
            }
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    /**
     * Hiển thị hình ảnh trong thư mục menu；
     *
     * @param view
     * @param menu
     * @return
     */

    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    public void loginSuccess() {
        ToastUtils.showSingleToast("Đăng nhập thành công");
    }

    @Override
    public void syncBookShelfCompleted() {
        dismissDialog();
        EventManager.refreshCollectionList();
    }

    @Override
    public void onLogin(ImageView view, String type) {
        if (type.equals("Login")) {

        }
        //4f45e920ff5d1a0e29d997986cd97181
    }

    @Override
    public void showError() {
        ToastUtils.showSingleToast("Lỗi đồng bộ hóa");
        dismissDialog();
    }

    @Override
    public void complete() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadBookService.cancel();
        stopService(new Intent(this, DownloadBookService.class));
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}