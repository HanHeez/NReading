package com.gtv.hanhee.novelreading.Ui.Presenter;

import com.gtv.hanhee.novelreading.Api.BookApi;
import com.gtv.hanhee.novelreading.Base.RxPresenter;
import com.gtv.hanhee.novelreading.Manager.CollectionsManager;
import com.gtv.hanhee.novelreading.Model.BookMixAToc;
import com.gtv.hanhee.novelreading.Model.Recommend;
import com.gtv.hanhee.novelreading.Ui.Contract.MainContract;
import com.gtv.hanhee.novelreading.Utils.LogUtils;
import com.gtv.hanhee.novelreading.Utils.ToastUtils;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter<MainContract.View> {

    private BookApi bookApi;
    public static boolean isLastSyncUpdateed = false;

    @Inject
    public MainActivityPresenter(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    @Override
    public void login(String uid, String token, String platform) {
//        Subscription rxSubscription = bookApi.login(uid, token, platform).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Login>() {
//                    @Override
//                    public void onNext(Login data) {
//                        if (data != null && mView != null && data.ok) {
//                            mView.loginSuccess();
//                            LogUtils.e(data.user.toString());
//                        }
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtils.e("login" + e.toString());
//                    }
//                });
//        addSubscrebe(rxSubscription);
    }

    @Override
    public void syncBookShelf() {
        List<Recommend.RecommendBooks> list = CollectionsManager.getInstance().getCollectionList();
        List<Observable<BookMixAToc.mixToc>> observables = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (Recommend.RecommendBooks bean : list) {
                if (!bean.isFromSD) {
                    Observable<BookMixAToc.mixToc> fromNetWork = bookApi.getBookMixAToc(bean._id, "chapters")
                            .map(new Function<BookMixAToc, BookMixAToc.mixToc>() {
                                @Override
                                public BookMixAToc.mixToc apply(BookMixAToc bookMixAToc) throws Exception {
                                    return bookMixAToc.mixToc;
                                }
                            })
//                    .compose(RxUtil.<BookMixAToc.mixToc>rxCacheListHelper(
//                            StringUtils.creatAcacheKey("book-toc", bean._id, "chapters")))
                            ;
                    observables.add(fromNetWork);
                }
            }
        } else {
            ToastUtils.showSingleToast("书架空空如也...");
            mView.syncBookShelfCompleted();
            return;
        }
        isLastSyncUpdateed = false;
        Observable<BookMixAToc.mixToc> rxObservable = Observable.mergeDelayError(observables);

                rxObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookMixAToc.mixToc>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BookMixAToc.mixToc data) {
                        String lastChapter = data.chapters.get(data.chapters.size() - 1).title;
                        CollectionsManager.getInstance().setLastChapterAndLatelyUpdate(data.book, lastChapter, data.chaptersUpdated);
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("onError: " + e);
//                        mView.showError();
                    }

                    @Override
                    public void onComplete() {
                        mView.syncBookShelfCompleted();
                        if (isLastSyncUpdateed) {
                            ToastUtils.showSingleToast("小説已更新");
                        } else {
                            ToastUtils.showSingleToast("你追的小説沒有更新");
                        }

                    }
                });


//        addSubscrebe();
    }
}
