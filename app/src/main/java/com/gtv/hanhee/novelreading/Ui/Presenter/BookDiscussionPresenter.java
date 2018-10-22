package com.gtv.hanhee.novelreading.Ui.Presenter;

import com.gtv.hanhee.novelreading.Api.BookApi;
import com.gtv.hanhee.novelreading.Base.RxPresenter;
import com.gtv.hanhee.novelreading.Model.DiscussionList;
import com.gtv.hanhee.novelreading.Ui.Contract.BookDiscussionContract;
import com.gtv.hanhee.novelreading.Utils.LogUtils;
import com.gtv.hanhee.novelreading.Utils.RxUtil;
import com.gtv.hanhee.novelreading.Utils.StringUtils;

import org.reactivestreams.Subscription;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class BookDiscussionPresenter extends RxPresenter<BookDiscussionContract.View> implements BookDiscussionContract.Presenter {

    private BookApi bookApi;

    @Inject
    public BookDiscussionPresenter(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    @Override
    public void getBookDisscussionList(String block, String sort, String distillate, final int start, int limit) {
        String key = StringUtils.creatAcacheKey("book-discussion-list", block, "all", sort, "all", start + "", limit + "", distillate);

        Observable<DiscussionList> fromNetWork = bookApi.getBookDisscussionList(block, "all", sort, "all", start + "", limit + "", distillate)
                .compose(RxUtil.<DiscussionList>rxCacheListHelper(key));

        //Kiểm tra lần lượt disk, network
        Observable.concat(RxUtil.rxCreateDiskObservable(key, DiscussionList.class), fromNetWork)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DiscussionList>() {

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("getBookDisscussionList:" + e.toString());
                        mView.showError();
                    }

                    @Override
                    public void onComplete() {
                        mView.complete();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DiscussionList list) {
                        boolean isRefresh = start == 0 ? true : false;
                        mView.showBookDisscussionList(list.posts, isRefresh);
                    }
                });
    }

}
