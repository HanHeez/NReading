package com.gtv.hanhee.novelreading.Ui.Presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.gtv.hanhee.novelreading.Api.BookApi;
import com.gtv.hanhee.novelreading.Api.VietPhraseApi;
import com.gtv.hanhee.novelreading.Base.RxPresenter;
import com.gtv.hanhee.novelreading.Manager.SettingManager;
import com.gtv.hanhee.novelreading.Model.Recommend;
import com.gtv.hanhee.novelreading.Ui.Contract.RecommendContract;
import com.gtv.hanhee.novelreading.Utils.LogUtils;
import com.gtv.hanhee.novelreading.Utils.RxUtil;
import com.gtv.hanhee.novelreading.Utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class RecommendPresenter implements RecommendContract.Presenter<RecommendContract.View> {

    List<Recommend.RecommendBooks> list = new ArrayList<>();
    private Context context;
    private BookApi bookApi;
    private VietPhraseApi vietPhraseApi;
    private RecommendContract.View view;

    @Inject
    public RecommendPresenter(Context context, BookApi bookApi, VietPhraseApi vietPhraseApi) {
        this.context = context;
        this.bookApi = bookApi;
        this.vietPhraseApi = vietPhraseApi;
    }

    @Override
    public void attachView(RecommendContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }


    @SuppressLint("CheckResult")
    @Override
    public void getRecommendList() {
        bookApi.getRecommend("male").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(this::ResultList, this::ErrorList);

    }

    private void ErrorList(Throwable throwable) {
    }

    private void ResultList(Recommend recommend) {
        list = recommend.books;
        if (list != null && !list.isEmpty() && view != null) {
            translateRecommend();
        }
    }


    @SuppressLint("CheckResult")
    private void translateRecommend() {

        for (int i = 0; i < list.size(); i++) {
            int finalI = i;
            vietPhraseApi.getTranslateText(list.get(i).getTitle())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(s -> ResultTitle(s, finalI), this::Error);

            vietPhraseApi.getTranslateText(list.get(i).getAuthor())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(s -> ResultAuthor(s, finalI), this::Error);

            vietPhraseApi.getTranslateText(list.get(i).getShortIntro())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(s -> ResultShortIntro(s, finalI), this::Error);

            vietPhraseApi.getTranslateText(list.get(i).getLastChapter())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(s -> ResultLastChapter(s, finalI), this::Error);
        }
    }

    private void ResultLastChapter(String s, int finalI) {
        list.get(finalI).setLastChapter(s);
        view.showRecommendList(list);
    }

    private void ResultShortIntro(String s, int finalI) {
        list.get(finalI).setShortIntro(s);
        view.showRecommendList(list);
    }

    private void ResultAuthor(String s, int finalI) {
        list.get(finalI).setAuthor(s);
        view.showRecommendList(list);
    }

    private void ResultTitle(String s, int finalI) {
        list.get(finalI).setTitle(s);
        view.showRecommendList(list);
    }


    private void Error(Throwable throwable) {

    }
}

//public class RecommendPresenter extends RxPresenter<RecommendContract.View>
//        implements RecommendContract.Presenter<RecommendContract.View> {
//
//    List<Recommend.RecommendBooks> list = new ArrayList<>();
//    private Context context;
//    private BookApi bookApi;
//    private VietPhraseApi vietPhraseApi;
//    private RecommendContract.View view;
//
//    @Inject
//    public RecommendPresenter(Context context, BookApi bookApi, VietPhraseApi vietPhraseApi) {
//        this.context = context;
//        this.bookApi = bookApi;
//        this.vietPhraseApi = vietPhraseApi;
//    }
//
//
//    @SuppressLint("CheckResult")
//    @Override
//    public void getRecommendList() {
////        bookApi.getRecommend("male").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
////                .subscribe(this::ResultList, this::ErrorList);
//
//        String key = StringUtils.creatAcacheKey("recommend-list", SettingManager.getInstance().getUserChooseSex());
//        Log.d("kiemtra",SettingManager.getInstance().getUserChooseSex());
//        Observable<Recommend> fromNetWork = bookApi.getRecommend(SettingManager.getInstance().getUserChooseSex())
//                .compose(RxUtil.<Recommend>rxCacheListHelper(key));
//
//        //依次检查disk、network
//        Observable.concat(RxUtil.rxCreateDiskObservable(key, Recommend.class), fromNetWork)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Recommend>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Recommend recommend) {
//                        Log.d("kiemtra", String.valueOf(recommend.books.size()));
//                        list = recommend.books;
//                        if (list != null && !list.isEmpty() && view != null) {
//                            translateRecommend();
//                        }
////                        if (recommend != null) {
////                            List<Recommend.RecommendBooks> list = recommend.books;
////                            if (list != null && !list.isEmpty() && mView != null) {
////                                mView.showRecommendList(list);
////                            }
////                        }
//                    }
//
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtils.e("getRecommendList", e.toString());
//                        mView.showError();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        mView.complete();
//
//                    }
//                });
//    }
//
////    private void ErrorList(Throwable throwable) {
////
////    }
////
////    private void ResultList(Recommend recommend) {
////        list = recommend.books;
////        if (list != null && !list.isEmpty() && view != null) {
////            translateRecommend();
////        }
////    }
//
//
//    @SuppressLint("CheckResult")
//    private void translateRecommend() {
//
//        for (int i = 0; i < list.size(); i++) {
//            int finalI = i;
//            vietPhraseApi.getTranslateText(list.get(i).getTitle())
//                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                    .subscribe(s -> ResultTitle(s, finalI), this::Error);
//
//            vietPhraseApi.getTranslateText(list.get(i).getAuthor())
//                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                    .subscribe(s -> ResultAuthor(s, finalI), this::Error);
//
//            vietPhraseApi.getTranslateText(list.get(i).getShortIntro())
//                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                    .subscribe(s -> ResultShortIntro(s, finalI), this::Error);
//
//            vietPhraseApi.getTranslateText(list.get(i).getLastChapter())
//                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                    .subscribe(s -> ResultLastChapter(s, finalI), this::Error);
//        }
//
//
//    }
//
//    private void ResultLastChapter(String s, int finalI) {
//        list.get(finalI).setLastChapter(s);
//        view.showRecommendList(list);
//    }
//
//    private void ResultShortIntro(String s, int finalI) {
//        list.get(finalI).setShortIntro(s);
//        view.showRecommendList(list);
//    }
//
//    private void ResultAuthor(String s, int finalI) {
//        list.get(finalI).setAuthor(s);
//        view.showRecommendList(list);
//    }
//
//    private void ResultTitle(String s, int finalI) {
//        list.get(finalI).setTitle(s);
//        view.showRecommendList(list);
//    }
//
//
//    private void Error(Throwable throwable) {
//
//    }
//}
//
