package com.gtv.hanhee.novelreading.Ui.Activity;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gtv.hanhee.novelreading.Base.BaseActivity;
import com.gtv.hanhee.novelreading.Component.AppComponent;
import com.gtv.hanhee.novelreading.Component.DaggerSearchActivityComponent;
import com.gtv.hanhee.novelreading.Model.HotWord;
import com.gtv.hanhee.novelreading.Model.SearchDetail;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Ui.Adapter.AutoCompleteAdapter;
import com.gtv.hanhee.novelreading.Ui.Adapter.SearchResultAdapter;
import com.gtv.hanhee.novelreading.Ui.Contract.SearchContract;
import com.gtv.hanhee.novelreading.Ui.Presenter.SearchPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.gujun.android.taggroup.TagGroup;

public class SearchActivity extends BaseActivity implements SearchContract.View {

    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tvChangeWords)
    TextView mTvChangeWords;
    @BindView(R.id.tag_group)
    TagGroup mTagGroup;

    @Inject
    SearchPresenter mPresenter;
    @BindView(R.id.rootLayout)
    LinearLayout mRootLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.layoutHotWord)
    RelativeLayout mLayoutHotWord;

    private SearchResultAdapter mAdapter;
    private List<SearchDetail.SearchBooks> mList = new ArrayList<>();
    private AutoCompleteAdapter mAutoAdapter;
    private List<String> mAutoList = new ArrayList<>();
    private List<String> hotWords = new ArrayList<>();
    private List<String> translateHotWords = new ArrayList<>();

    private String key;
    private SearchView searchView;

    private ListPopupWindow mListPopupWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerSearchActivityComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {
        setSupportActionBar(mToolbar);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchResultAdapter(mContext, mList);
        mRecyclerView.setAdapter(mAdapter);

        mAutoAdapter = new AutoCompleteAdapter(this, mAutoList);
        mListPopupWindow = new ListPopupWindow(this);
        mListPopupWindow.setAdapter(mAutoAdapter);
        mListPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mListPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mListPopupWindow.setAnchorView(mToolbar);
        mListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListPopupWindow.dismiss();
                TextView tv = view.findViewById(R.id.tvAutoCompleteItem);
                String str = tv.getText().toString();
                searchView.setQuery(str, true);
            }
        });

        mTagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                    searchView.onActionViewExpanded();

                    searchView.setQuery(hotWords.get(translateHotWords.indexOf(tag)), true);
            }
        });

        mPresenter.attachView(this);
        mPresenter.getHotWordList();
    }


    @Override
    public void showHotWordList(List<HotWord.combineHotWord> combineHotWords, int size) {

        if (combineHotWords.size()== size) {
            for (int i = 0; i< size; i++) {
                this.translateHotWords.add(combineHotWords.get(i).getTransHotWord());
                this.hotWords.add(combineHotWords.get(i).getHotWord());
            }
            mTagGroup.setTags(this.translateHotWords);

            Log.d("kiemtra", translateHotWords.size()+ "");
        }
    }

    @Override
    public void showAutoCompleteList(List<String> list) {
        mAutoList.clear();
        mAutoList.addAll(list);

        if (!mListPopupWindow.isShowing()) {
            mListPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            mListPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            mListPopupWindow.show();
        }
        mAutoAdapter.notifyDataSetChanged();

    }

    @Override
    public void showSearchResultList(List<SearchDetail.SearchBooks> list) {
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
        initSearchResult();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        //setting searchView trên menu
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //submit text thì bắt đầu tìm kiếm
            @Override
            public boolean onQueryTextSubmit(String query) {
                key = query;
                mPresenter.getSearchResultList(query);
                return false;
            }
            // khi thay đổi text nhập liệu trên thanh menu
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    // text là trống thì tắt popup window gợi ý đi, show TagGroup
                    if (mListPopupWindow.isShowing())
                        mListPopupWindow.dismiss();
                    initTagGroup();
                } else {
                    //ngược lại có text thì lấy danh sách gợi ý đổ vào recycle view
                    mPresenter.getAutoCompleteList(newText);
                }
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(menuItem,
                new MenuItemCompat.OnActionExpandListener() {
            // lắng nghe sự kiện đóng mở menu
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        initTagGroup();
                        return true;
                    }
                });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
//            initSearchResult();
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initSearchResult() {
        mTagGroup.setVisibility(View.GONE);
        mLayoutHotWord.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        if (mListPopupWindow.isShowing())
            mListPopupWindow.dismiss();
    }

    private void initTagGroup() {
        mTagGroup.setVisibility(View.VISIBLE);
        mLayoutHotWord.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        if (mListPopupWindow.isShowing())
            mListPopupWindow.dismiss();
    }

}
