package com.gtv.hanhee.novelreading.Ui.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gtv.hanhee.novelreading.AppComponent;
import com.gtv.hanhee.novelreading.Base.BaseActivity;
import com.gtv.hanhee.novelreading.Fragment.RecommendFragment;
import com.gtv.hanhee.novelreading.Module.MainActivityModule;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Ui.Adapter.RecommendTabLayoutAdapter;
import com.gtv.hanhee.novelreading.Ui.Component.DaggerMainActivityComponent;
import com.gtv.hanhee.novelreading.Ui.Contract.MainContract;
import com.gtv.hanhee.novelreading.Ui.CustomView.TabEntity;
import com.gtv.hanhee.novelreading.Ui.Presenter.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    CommonTabLayout tabLayout;

    String[] mDatas = new String[]{"Danh sách", "Cộng đồng", "Phát hiện"};
    List<Fragment> mTabContents;
    RecommendTabLayoutAdapter recommendTabLayoutAdapter;


    ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    @Inject
    MainActivityPresenter mPresenter;
    private int[] mIconUnselectIds;
    private int[] mIconSelectIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mPresenter.getRecommend();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainActivityComponent.builder()
                .appComponent(appComponent)
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {
        setTitle("");
    }

    @Override
    public void initDatas() {

        mIconUnselectIds = new int[mDatas.length];
        mIconSelectIds = new int[mDatas.length];
        mTabContents = new ArrayList<>();
        recommendTabLayoutAdapter = new RecommendTabLayoutAdapter(getSupportFragmentManager(), mTabContents);
        mViewPager.setAdapter(recommendTabLayoutAdapter);

        for (int i = 0; i < mDatas.length; i++) {
            mIconSelectIds[i] = R.drawable.heart_love;
            mIconUnselectIds[i] = R.drawable.heart_love;
            mTabEntities.add(new TabEntity(mDatas[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        for (int i = 0; i < mDatas.length; i++) {
//              RecommendFragment fragment = RecommendFragment.newInstance(data);
            RecommendFragment fragment = new RecommendFragment();
            mTabContents.add(fragment);
            recommendTabLayoutAdapter.notifyDataSetChanged();
        }
    }


    public void configViews() {
        setSupportActionBar(mToolbar);
        tabLayout.setTabData(mTabEntities);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ab_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}