package com.gtv.hanhee.novelreading.Ui.Presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.gtv.hanhee.novelreading.Api.BookApi;
import com.gtv.hanhee.novelreading.Api.VietPhraseApi;
import com.gtv.hanhee.novelreading.Base.RxPresenter;
import com.gtv.hanhee.novelreading.Model.AutoComplete;
import com.gtv.hanhee.novelreading.Model.HotWord;
import com.gtv.hanhee.novelreading.Model.SearchDetail;
import com.gtv.hanhee.novelreading.Ui.Contract.SearchContract;
import com.gtv.hanhee.novelreading.Utils.LogUtils;
import com.gtv.hanhee.novelreading.Utils.RxUtil;
import com.gtv.hanhee.novelreading.Utils.StringUtils;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter extends RxPresenter<SearchContract.View> implements SearchContract.Presenter<SearchContract.View> {

    private BookApi bookApi;

    @Inject
    public SearchPresenter(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    public void getHotWordList() {
        String key = StringUtils.creatAcacheKey("hot-word-list");
        Observable<HotWord> fromNetWork = bookApi.getHotWord()
                .compose(RxUtil.<HotWord>rxCacheListHelper(key));

        //依次检查disk、network
        Observable.concat(RxUtil.rxCreateDiskObservable(key, HotWord.class), fromNetWork)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotWord>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HotWord hotWord) {
                        List<String> list = hotWord.hotWords;
                        if (list != null && !list.isEmpty() && mView != null) {
                            mView.showHotWordList(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("onError: " + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getAutoCompleteList(String query) {
        bookApi.getAutoComplete(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AutoComplete>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AutoComplete autoComplete) {
                        LogUtils.e("getAutoCompleteList" + autoComplete.keywords);
                        List<String> list = autoComplete.keywords;
                        if (list != null && !list.isEmpty() && mView != null) {
                            mView.showAutoCompleteList(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getSearchResultList(String query) {
        bookApi.getSearchResult(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SearchDetail bean) {
                        List<SearchDetail.SearchBooks> list = bean.books;
                        if (list != null && !list.isEmpty() && mView != null) {
                            mView.showSearchResultList(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
