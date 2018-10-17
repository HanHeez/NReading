package com.gtv.hanhee.novelreading.Ui.Presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.gtv.hanhee.novelreading.Api.ReaderApi;
import com.gtv.hanhee.novelreading.Api.VietPhraseApi;
import com.gtv.hanhee.novelreading.Model.AutoComplete;
import com.gtv.hanhee.novelreading.Model.HotWord;
import com.gtv.hanhee.novelreading.Model.SearchDetail;
import com.gtv.hanhee.novelreading.Ui.Contract.SearchContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter implements SearchContract.Presenter<SearchContract.View> {

    List<String> hotWords = new ArrayList<>();
    List<SearchDetail.SearchBooks> listSearchDetail = new ArrayList<>();
    List<HotWord.combineHotWord> combineHotWords= new ArrayList<>();
    private static final String TAG = "SearchPresenter";
    private Context context;
    private ReaderApi readerApi;
    private VietPhraseApi vietPhraseApi;
    private SearchContract.View view;

    @Inject
    public SearchPresenter(Context context, ReaderApi readerApi, VietPhraseApi vietPhraseApi) {
        this.context = context;
        this.readerApi = readerApi;
        this.vietPhraseApi = vietPhraseApi;
    }

    @Override
    public void attachView(SearchContract.View view) {
        this.view = view;
    }

    @SuppressLint("CheckResult")
    public void getHotWordList() {

        readerApi.getHotWord().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultHotWord,this::Error);;
    }

    private void resultHotWord(HotWord hotWord) {
        hotWords = hotWord.hotWords;
        if (hotWords != null && !hotWords.isEmpty() && view != null) {
            translateHotWords();
        }
    }

    @SuppressLint("CheckResult")
    private void translateHotWords() {
        for (int i = 0; i < hotWords.size(); i++) {
            int finalI = i;
            vietPhraseApi.getTranslateText(hotWords.get(i))
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(transText -> resultTransHotWord(transText, hotWords.get(finalI)), this::Error);
        }
    }

    private void resultTransHotWord(String transText, String beforeText ) {
        HotWord.combineHotWord combineHotWord = new HotWord.combineHotWord(transText, beforeText);
        combineHotWords.add(combineHotWord);
        view.showHotWordList(combineHotWords,hotWords.size());
    }


    @SuppressLint("CheckResult")
    @Override
    public void getAutoCompleteList(String query) {
        readerApi.getAutoComplete(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::ResultAutoCompleteList,this::Error);
    }

    private void ResultAutoCompleteList(AutoComplete autoComplete) {
        List<String> list = autoComplete.keywords;
        if (list != null && !list.isEmpty() && view != null) {
            view.showAutoCompleteList(list);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void getSearchResultList(String query) {
        readerApi.getSearchResult(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultSearchResultList,this::Error);
    }

    private void resultSearchResultList(SearchDetail searchDetail) {
        listSearchDetail = searchDetail.books;
        if (listSearchDetail != null && !listSearchDetail.isEmpty() && view != null) {
            translateSearchResultList();

        }
    }

    @SuppressLint("CheckResult")
    private void translateSearchResultList() {
        for (int i = 0; i < listSearchDetail.size(); i++) {
            int finalI = i;
            vietPhraseApi.getTranslateText(listSearchDetail.get(i).getTitle())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(transText -> resultTransTitleSearchList(transText, finalI), this::Error);

            vietPhraseApi.getTranslateText(listSearchDetail.get(i).getAuthor())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(transText -> resultTransAuthorSearchList(transText, finalI), this::Error);

            vietPhraseApi.getTranslateText(listSearchDetail.get(i).getCat())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(transText -> resultTransCategorySearchList(transText, finalI), this::Error);

            vietPhraseApi.getTranslateText(listSearchDetail.get(i).getTitle())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(transText -> resultTransTitleSearchList(transText, finalI), this::Error);


            vietPhraseApi.getTranslateText(listSearchDetail.get(i).getShortIntro())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(transText -> resultTransShortIntroSearchList(transText, finalI), this::Error);


            vietPhraseApi.getTranslateText(listSearchDetail.get(i).getLastChapter())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(transText -> resultTransLastChapterSearchList(transText, finalI), this::Error);
        }
    }

    private void resultTransLastChapterSearchList(String transText, int finalI) {
        listSearchDetail.get(finalI).setLastChapterTrans(transText);
        view.showSearchResultList(listSearchDetail);
    }


    private void resultTransShortIntroSearchList(String transText, int finalI) {
        listSearchDetail.get(finalI).setLastChapterTrans(transText);
        view.showSearchResultList(listSearchDetail);

    }

    private void resultTransCategorySearchList(String transText, int finalI) {
        listSearchDetail.get(finalI).setCatTrans(transText);
        view.showSearchResultList(listSearchDetail);
    }

    private void resultTransAuthorSearchList(String transText, int finalI) {
        listSearchDetail.get(finalI).setAuthorTrans(transText);
        view.showSearchResultList(listSearchDetail);
    }

    private void resultTransTitleSearchList(String transText, int finalI) {
        listSearchDetail.get(finalI).setTitleTrans(transText);
        view.showSearchResultList(listSearchDetail);

    }

    private void Error(Throwable throwable) {
    }
}
