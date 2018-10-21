package com.gtv.hanhee.novelreading.Ui.Presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.gtv.hanhee.novelreading.Api.BookApi;
import com.gtv.hanhee.novelreading.Api.VietPhraseApi;
import com.gtv.hanhee.novelreading.Model.BookDetail;
import com.gtv.hanhee.novelreading.Model.HotReview;
import com.gtv.hanhee.novelreading.Model.RecommendBookList;
import com.gtv.hanhee.novelreading.Ui.Contract.BookDetailContract;
import com.gtv.hanhee.novelreading.Utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BookDetailPresenter implements BookDetailContract.Presenter<BookDetailContract.View> {

    private static final String TAG = "BookDetailPresenter";
    private Context context;
    private BookApi bookApi;
    private VietPhraseApi vietPhraseApi;
    private BookDetail bookDetail;
    ;
    private List<BookDetail.combineTags> combineTags = new ArrayList<>();
    ;
    private List<BookDetail.combineCategories> combineCategories = new ArrayList<>();
    private BookDetailContract.View view;

    @Inject
    public BookDetailPresenter(Context context, BookApi bookApi, VietPhraseApi vietPhraseApi) {
        this.context = context;
        this.bookApi = bookApi;
        this.vietPhraseApi = vietPhraseApi;
    }

    @Override
    public void attachView(BookDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }

    @SuppressLint("CheckResult")
    public void getBookDetail(String bookId) {
        bookApi.getBookDetail(bookId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::ResultBookDetail, this::Error);
    }

    private void Error(Throwable throwable) {

    }

    @SuppressLint("CheckResult")
    private void ResultBookDetail(BookDetail bookDetail) {
        this.bookDetail = bookDetail;
        if (bookDetail != null && view != null) {
            vietPhraseApi.getTranslateText(bookDetail.getAuthor()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resultTransAuthor, this::Error);
            vietPhraseApi.getTranslateText(bookDetail.getTitle()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resultTransTitle, this::Error);
            vietPhraseApi.getTranslateText(bookDetail.getCat()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resultTransCat, this::Error);

            vietPhraseApi.getTranslateText(bookDetail.getLastChapter()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resultTransLastChapter, this::Error);

            vietPhraseApi.getTranslateText(bookDetail.getLongIntro()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resultTransLongIntro, this::Error);

            vietPhraseApi.getTranslateText(bookDetail.getMajorCate()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resultTransMajorCate, this::Error);

            vietPhraseApi.getTranslateText(bookDetail.getMinorCate()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resultTransMinorCate, this::Error);

            int tagsSize = bookDetail.getTags().size();
            for (int i = 0; i < tagsSize; i++) {
                int finalI = i;
                vietPhraseApi.getTranslateText(bookDetail.getTags().get(i)).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(transText -> resultTransTags(transText, bookDetail.getTags().get(finalI), tagsSize), this::Error);
            }
        }
    }

    private void resultTransCat(String s) {
        bookDetail.setCatTrans(s);
        view.showBookDetail(bookDetail);
    }

    private void resultTransTags(String transText, String beforeText, int tagsSize) {
        BookDetail.combineTags combineTag = new BookDetail.combineTags(beforeText, transText);
        combineTags.add(combineTag);
        view.showBookDetailTags(combineTags, tagsSize);
    }

//    private void resultTransCategories(String transText, String beforeText, int categoriesSize) {
//        BookDetail.combineCategories combineCategory = new BookDetail.combineCategories(beforeText, transText);
//        combineCategories.add(combineCategory);
//        view.showBookDetailCategories(combineCategories, categoriesSize);
//    }


    private void resultTransMinorCate(String s) {
        bookDetail.setMinorCateTrans(s);
        view.showBookDetail(bookDetail);
    }

    private void resultTransMajorCate(String s) {
        bookDetail.setMajorCateTrans(s);
        view.showBookDetail(bookDetail);
    }

    private void resultTransLongIntro(String s) {
        bookDetail.setLongIntroTrans(s);
        view.showBookDetail(bookDetail);
    }

    private void resultTransLastChapter(String s) {
        bookDetail.setLastChapterTrans(s);
        view.showBookDetail(bookDetail);
    }

    private void resultTransTitle(String s) {
        bookDetail.setTitleTrans(s);
        view.showBookDetail(bookDetail);
    }

    private void resultTransAuthor(String s) {
        bookDetail.setAuthorTrans(s);
        view.showBookDetail(bookDetail);
    }

    @Override
    public void getHotReview(String book) {
        bookApi.getHotReview(book).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotReview>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HotReview hotReview) {
                        List<HotReview.Reviews> list = hotReview.reviews;
                        if (list != null && !list.isEmpty() && view != null) {
                            view.showHotReview(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getRecommendBookList(String bookId, String limit) {
        bookApi.getRecommendBookList(bookId, limit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RecommendBookList>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RecommendBookList data) {
                        LogUtils.i(data.booklists);
                        List<RecommendBookList.RecommendBook> list = data.booklists;
                        if (list != null && !list.isEmpty() && view != null) {
                            view.showRecommendBookList(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("+++" + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

