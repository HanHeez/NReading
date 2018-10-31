package com.gtv.hanhee.novelreading.Ui.Fragment;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gtv.hanhee.novelreading.Base.BaseRVFragment;
import com.gtv.hanhee.novelreading.Component.AppComponent;
import com.gtv.hanhee.novelreading.Component.DaggerMainComponent;
import com.gtv.hanhee.novelreading.Manager.CollectionsManager;
import com.gtv.hanhee.novelreading.Model.BookMixAToc;
import com.gtv.hanhee.novelreading.Model.DownloadProgress;
import com.gtv.hanhee.novelreading.Model.DownloadQueue;
import com.gtv.hanhee.novelreading.Model.Recommend;
import com.gtv.hanhee.novelreading.Model.Support.DownloadMessage;
import com.gtv.hanhee.novelreading.Model.Support.RefreshCollectionListEvent;
import com.gtv.hanhee.novelreading.Model.Support.UserSexChooseFinishedEvent;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Service.DownloadBookService;
import com.gtv.hanhee.novelreading.Ui.Activity.BookDetailActivity;
import com.gtv.hanhee.novelreading.Ui.Activity.ReadActivity;
import com.gtv.hanhee.novelreading.Ui.Activity.MainActivity;
import com.gtv.hanhee.novelreading.Ui.Adapter.RecommendAdapter;
import com.gtv.hanhee.novelreading.Ui.Contract.RecommendContract;
import com.gtv.hanhee.novelreading.Ui.Presenter.RecommendPresenter;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RecommendFragment extends BaseRVFragment<RecommendPresenter, Recommend.RecommendBooks> implements RecommendContract.View, RecyclerArrayAdapter.OnItemLongClickListener {

    @BindView(R.id.llBatchManagement)
    LinearLayout llBatchManagement;
    @BindView(R.id.tvSelectAll)
    TextView tvSelectAll;
    @BindView(R.id.tvDelete)
    TextView tvDelete;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private boolean isSelectAll = false;

    private List<BookMixAToc.mixToc.Chapters> chaptersList = new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void initDatas() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void configViews() {
        initAdapter(RecommendAdapter.class, true, false);
        mAdapter.setOnItemLongClickListener(this);
        mAdapter.addFooter(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View headerView = LayoutInflater.from(activity).inflate(R.layout.foot_view_shelf, parent, false);
                return headerView;
            }

            @Override
            public void onBindView(View headerView) {
                headerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) activity).setCurrentItem(2);
                    }
                });
            }
        });
        mRecyclerView.getEmptyView().findViewById(R.id.btnToAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) activity).setCurrentItem(2);
            }
        });

        refreshLayout.setRefreshHeader(new WaterDropHeader(mContext));
//设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new BallPulseFooter(mContext).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(300/*,false*/);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(300/*,false*/);
            }
        });


        onRefresh();

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void showRecommendList(List<Recommend.RecommendBooks> list) {
        mAdapter.clear();
        mAdapter.addAll(list);
        //推荐列表默认加入收藏
        for (Recommend.RecommendBooks bean : list) {
            //TODO Ở đây có thể được tối ưu hóa: thêm vào bộ sưu tập theo budle ->
            // cần phải đánh giá liệu nó đã được thu thập trước khi tham gia
            CollectionsManager.getInstance().add(bean);
        }
    }

    @Override
    public void showBookToc(String bookId, List<BookMixAToc.mixToc.Chapters> list) {
        chaptersList.clear();
        chaptersList.addAll(list);
        DownloadBookService.post(new DownloadQueue(bookId, list, 1, list.size()));
        dismissDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downloadMessage(final DownloadMessage msg) {
        setTipViewText(msg.message);
        if (msg.isComplete) {
            hideTipView(2200);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showDownProgress(DownloadProgress progress) {
        setTipViewText(progress.message);
    }

    @Override
    public void onItemClick(int position) {
        if (isVisible(llBatchManagement)) //批量管理时，屏蔽点击事件
            return;
        ReadActivity.startActivity(activity, mAdapter.getItem(position), mAdapter.getItem(position).isFromSD);
    }

    @Override
    public boolean onItemLongClick(int position) {
        //批量管理时，屏蔽长按事件
        if (isVisible(llBatchManagement)) return false;
        showLongClickDialog(position);
        return false;
    }

    /**
     * 显示长按对话框
     *
     * @param position
     */
    private void showLongClickDialog(final int position) {
        final boolean isTop = CollectionsManager.getInstance().isTop(mAdapter.getItem(position)._id);
        String[] items;
        DialogInterface.OnClickListener listener;
        if (mAdapter.getItem(position).isFromSD) {
            items = getResources().getStringArray(R.array.recommend_item_long_click_choice_local);
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            //置顶、取消置顶
                            CollectionsManager.getInstance().top(mAdapter.getItem(position)._id, !isTop);
                            break;
                        case 1:
                            //删除
                            List<Recommend.RecommendBooks> removeList = new ArrayList<>();
                            removeList.add(mAdapter.getItem(position));
                            showDeleteCacheDialog(removeList);
                            break;
                        case 2:
                            //批量管理
                            showBatchManagementLayout();
                            break;
                        default:
                            break;
                    }
                    dialog.dismiss();
                }
            };
        } else {
            items = getResources().getStringArray(R.array.recommend_item_long_click_choice);
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            //置顶、取消置顶
                            CollectionsManager.getInstance().top(mAdapter.getItem(position)._id, !isTop);
                            break;
                        case 1:
                            //书籍详情
                            BookDetailActivity.startActivity(activity,
                                    mAdapter.getItem(position)._id);
                            break;
                        case 2:
                            //移入养肥区
                            showTipViewAndDelayClose("正在拼命开发中...");
                            break;
                        case 3:
                            //缓存全本
                            if (mAdapter.getItem(position).isFromSD) {
                                showTipViewAndDelayClose("本地文件不支持该选项哦");
                            } else {
                                showDialog();
//                                mPresenter.getTocList(mAdapter.getItem(position)._id);
                            }
                            break;
                        case 4:
                            //删除
                            List<Recommend.RecommendBooks> removeList = new ArrayList<>();
                            removeList.add(mAdapter.getItem(position));
                            showDeleteCacheDialog(removeList);
                            break;
                        case 5:
                            //批量管理
                            showBatchManagementLayout();
                            break;
                        default:
                            break;
                    }
                    dialog.dismiss();
                }
            };
        }
        if (isTop) items[0] = getString(R.string.cancle_top);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.Custom_Dialog);
        builder.setView(R.layout.foot_view_shelf);
        builder.setTitle(mAdapter.getItem(position).title).setItems(items, listener).setNegativeButton(null, null);
        AlertDialog alertDialog = builder.create();

        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getAttributes().windowAnimations = R.style.SlideAnimation;
        alertDialog.show();

//        new AlertDialog.Builder(activity)
//                .setTitle(mAdapter.getItem(position).title)
//                .setItems(items, listener)
//                .setNegativeButton(null, null)
//                .create().show();
    }

    /**
     * 显示删除本地缓存对话框
     *
     * @param removeList
     */
    private void showDeleteCacheDialog(final List<Recommend.RecommendBooks> removeList) {
        final boolean selected[] = {true};
        new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.remove_selected_book))
                .setMultiChoiceItems(new String[]{activity.getString(R.string.delete_local_cache)}, selected,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                selected[0] = isChecked;
                            }
                        })
                .setPositiveButton(activity.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new AsyncTask<String, String, String>() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                showDialog();
                            }

                            @Override
                            protected String doInBackground(String... params) {
                                CollectionsManager.getInstance().removeSome(removeList, selected[0]);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                showTipViewAndDelayClose("Xóa truyện thành công");
                                for (Recommend.RecommendBooks bean : removeList) {
                                    mAdapter.remove(bean);
                                }
                                if (isVisible(llBatchManagement)) {
                                    //批量管理完成后，隐藏批量管理布局并刷新页面
                                    goneBatchManagementAndRefreshUI();
                                }
                                hideDialog();
                            }
                        }.execute();

                    }
                })
                .setNegativeButton(activity.getString(R.string.cancel), null)
                .create().show();
    }

    /**
     * 隐藏批量管理布局并刷新页面
     */
    public void goneBatchManagementAndRefreshUI() {
        if (mAdapter == null) return;
        gone(llBatchManagement);
        for (Recommend.RecommendBooks bean :
                mAdapter.getAllData()) {
            bean.showCheckBox = false;
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 显示批量管理布局
     */
    private void showBatchManagementLayout() {
        visible(llBatchManagement);
        for (Recommend.RecommendBooks bean : mAdapter.getAllData()) {
            bean.showCheckBox = true;
        }
        mAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.tvSelectAll)
    public void selectAll() {
        isSelectAll = !isSelectAll;
        tvSelectAll.setText(isSelectAll ? activity.getString(R.string.cancel_selected_all) : activity.getString(R.string.selected_all));
        for (Recommend.RecommendBooks bean : mAdapter.getAllData()) {
            bean.isSeleted = isSelectAll;
        }
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tvDelete)
    public void delete() {
        List<Recommend.RecommendBooks> removeList = new ArrayList<>();
        for (Recommend.RecommendBooks bean : mAdapter.getAllData()) {
            if (bean.isSeleted) removeList.add(bean);
        }
        if (removeList.isEmpty()) {
            showTipViewAndDelayClose(activity.getString(R.string.has_not_selected_delete_book));
        } else {
            showDeleteCacheDialog(removeList);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        StackTraceElement stack[] = (new Throwable()).getStackTrace();


        boolean hasRefBookShelfInCallStack = false;
        boolean isMRefresh = false;
        for (int i = 0; i < stack.length; i++) {
            StackTraceElement ste = stack[i];
            if (ste.getMethodName().equals("pullSyncBookShelf")) {
                hasRefBookShelfInCallStack = true;
            }
            if (ste.getMethodName().equals("onAnimationEnd") && ste.getFileName().equals("SwipeRefreshLayout.java")) {
                isMRefresh = true;
            }
        }

        if (!hasRefBookShelfInCallStack && isMRefresh) {
            ((MainActivity) activity).pullSyncBookShelf();
            return;
        }


        gone(llBatchManagement);
        List<Recommend.RecommendBooks> data = CollectionsManager.getInstance().getCollectionListBySort();
        mAdapter.clear();
        mAdapter.addAll(data);
        //不加下面这句代码会导致，添加本地书籍的时候，部分书籍添加后直接崩溃
        //报错：Scrapped or attached views may not be recycled. isScrap:false isAttached:true
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshCollectionList(RefreshCollectionListEvent event) {
        mRecyclerView.setRefreshing(true);
        onRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UserSexChooseFinished(UserSexChooseFinishedEvent event) {

        //首次进入APP，选择性别后，获取推荐列表
        mPresenter.getRecommendList();
    }

    @Override
    public void showError() {
        loaddingError();
        dismissDialog();
    }

    @Override
    public void complete() {
        mRecyclerView.setRefreshing(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!getUserVisibleHint()) {
            goneBatchManagementAndRefreshUI();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //这样监听返回键有个缺点就是没有拦截Activity的返回监听，如果有更优方案可以改掉
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (isVisible(llBatchManagement)) {
                        goneBatchManagementAndRefreshUI();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private boolean isForeground() {
        ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (MainActivity.class.getName().contains(cpn.getClassName())) {
                return true;
            }
        }

        return false;
    }


}
