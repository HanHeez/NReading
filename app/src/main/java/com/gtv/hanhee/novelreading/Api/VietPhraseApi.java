package com.gtv.hanhee.novelreading.Api;

import com.google.gson.Gson;
import com.gtv.hanhee.novelreading.Base.Constant;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class VietPhraseApi {

    public static VietPhraseApi instance;

    private VietPhraseService service;

    public VietPhraseApi(OkHttpClient okHttpClient, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_VIETPHRASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(VietPhraseService.class);
    }

    public static VietPhraseApi getInstance(OkHttpClient okHttpClient, Gson gson) {
        if (instance == null)
            instance = new VietPhraseApi(okHttpClient, gson);
        return instance;
    }

    public Observable<String> getTranslateText(String contentText) {
        return service.translateText(contentText);
    }

}
