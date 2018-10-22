package com.gtv.hanhee.novelreading.Ui.Presenter;

import android.util.Log;

import com.gtv.hanhee.novelreading.Api.BookApi;
import com.gtv.hanhee.novelreading.Base.RxPresenter;
import com.gtv.hanhee.novelreading.Manager.CollectionsManager;
import com.gtv.hanhee.novelreading.Model.BookMixAToc;
import com.gtv.hanhee.novelreading.Model.Login;
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

//    @Override
//    public void login(String uid, String token, String platform) {
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
//    }

    @Override
    public void login(String uid, String token, String platform) {

    }

    @Override
    public void syncBookShelf() {
        List<Recommend.RecommendBooks> list = CollectionsManager.getInstance().getCollectionList();

        List<Observable<BookMixAToc.mixToc>> observables = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            //Lấy chương của từng phần từ trong RecommendBooks
            for (Recommend.RecommendBooks bean : list) {
                if (!bean.isFromSD) {
                    Observable<BookMixAToc.mixToc> fromNetWork = bookApi.getBookMixAToc(bean._id, "chapters")
                            .map(bookMixAToc -> bookMixAToc.mixToc);
                    observables.add(fromNetWork);
                }
            }
        } else {
            ToastUtils.showSingleToast("Kệ sách trống...");
            mView.syncBookShelfCompleted();
            return;
        }
        isLastSyncUpdateed = false;

        Observable.mergeDelayError(observables)
                .subscribeOn(Schedulers.io())
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
                        mView.showError();
                        mView.complete();
                    }

                    @Override
                    public void onComplete() {
                        mView.complete();
                    }
                });

    }
}
