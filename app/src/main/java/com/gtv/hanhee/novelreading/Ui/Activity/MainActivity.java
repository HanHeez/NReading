package com.gtv.hanhee.novelreading.Ui.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gtv.hanhee.novelreading.Base.BaseActivity;
import com.gtv.hanhee.novelreading.Component.AppComponent;
import com.gtv.hanhee.novelreading.Component.DaggerMainActivityComponent;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Ui.Adapter.RecommendTabLayoutAdapter;
import com.gtv.hanhee.novelreading.Ui.Contract.MainContract;
import com.gtv.hanhee.novelreading.Ui.CustomView.TabEntity;
import com.gtv.hanhee.novelreading.Ui.Fragment.RecommendFragment;
import com.gtv.hanhee.novelreading.Ui.Presenter.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

//import com.gtv.hanhee.novelreading.Component.DaggerMainActivityComponent;

public class MainActivity extends BaseActivity implements MainContract.View {

    @BindView(R.id.common_toolbar)
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
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainActivityComponent.builder()
                .appComponent(appComponent)
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
        // set vị trí Viewpager khi click vào tabLayout
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ab_search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}