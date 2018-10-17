package com.gtv.hanhee.novelreading.Api;

import com.google.gson.Gson;
import com.gtv.hanhee.novelreading.Model.AutoComplete;
import com.gtv.hanhee.novelreading.Model.BookDetail;
import com.gtv.hanhee.novelreading.Model.HotReview;
import com.gtv.hanhee.novelreading.Model.HotWord;
import com.gtv.hanhee.novelreading.Model.Recommend;
import com.gtv.hanhee.novelreading.Model.SearchDetail;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ReaderApi {

    public static ReaderApi instance;

    private ReaderApiService service;

    public ReaderApi(OkHttpClient okHttpClient, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlApi.readerUrlApi)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(ReaderApiService.class);
    }

    public static ReaderApi getInstance(OkHttpClient okHttpClient, Gson gson) {
        if (instance == null)
            instance = new ReaderApi(okHttpClient, gson);
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
    public Observable<BookDetail> getBookDetail(String bookId){
        return service.getBookDetail(bookId);
    }
    public Observable<HotReview> getHotReview(String book){
        return service.getHotReview(book);
    }
}
