package com.gtv.hanhee.novelreading.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gtv.hanhee.novelreading.Base.Constant;
import com.gtv.hanhee.novelreading.Model.AutoComplete;
import com.gtv.hanhee.novelreading.Model.BookDetail;
import com.gtv.hanhee.novelreading.Model.BookMixAToc;
import com.gtv.hanhee.novelreading.Model.BookSource;
import com.gtv.hanhee.novelreading.Model.BooksByTag;
import com.gtv.hanhee.novelreading.Model.ChapterRead;
import com.gtv.hanhee.novelreading.Model.HotReview;
import com.gtv.hanhee.novelreading.Model.HotWord;
import com.gtv.hanhee.novelreading.Model.Login;
import com.gtv.hanhee.novelreading.Model.Recommend;
import com.gtv.hanhee.novelreading.Model.RecommendBookList;
import com.gtv.hanhee.novelreading.Model.SearchDetail;
import com.gtv.hanhee.novelreading.Model.User.LoginReq;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class BookApi {

    public static BookApi instance;

    private BookApiService service;

    public BookApi(OkHttpClient okHttpClient) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(BookApiService.class);
    }

    public static BookApi getInstance(OkHttpClient okHttpClient) {
        if (instance == null)
            instance = new BookApi(okHttpClient);
        return instance;
    }

    public Observable<Recommend> getRecommend(String gender) {
        return service.getRecomend(gender);
    }

    public Observable<HotWord> getHotWord() {
        return service.getHotWord();
    }

    public Observable<AutoComplete> getAutoComplete(String query) {
        return service.autoComplete(query);
    }

    public Observable<SearchDetail> getSearchResult(String query) {
        return service.searchBooks(query);
    }

    public Observable<BookDetail> getBookDetail(String bookId) {
        return service.getBookDetail(bookId);
    }

    public Observable<HotReview> getHotReview(String book) {
        return service.getHotReview(book);
    }


    public Observable<RecommendBookList> getRecommendBookList(String bookId, String limit) {
        return service.getRecommendBookList(bookId, limit);
    }

    public Observable<BooksByTag> getBooksByTag(String tags, String start, String limit) {
        return service.getBooksByTag(tags, start, limit);
    }

    public Observable<BookMixAToc> getBookMixAToc(String bookId, String view) {
        return service.getBookMixAToc(bookId, view);
    }

    public synchronized Observable<ChapterRead> getChapterRead(String url) {
        return service.getChapterRead(url);
    }

    public synchronized Observable<List<BookSource>> getBookSource(String view, String book) {
        return service.getABookSource(view, book);
    }

    public Observable<Login> login(String platform_uid, String platform_token, String platform_code) {
        LoginReq loginReq = new LoginReq();
        loginReq.platform_code = platform_code;
        loginReq.platform_token = platform_token;
        loginReq.platform_uid = platform_uid;
        return service.login(loginReq);
    }

}
