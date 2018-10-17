package com.gtv.hanhee.novelreading.Module;

import com.google.gson.Gson;
import com.gtv.hanhee.novelreading.Api.VietPhraseApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class VietPhraseApiModule {


    @Provides
    protected VietPhraseApi providerVietPhraseService(OkHttpClient okHttpClient, Gson gson) {
        return VietPhraseApi.getInstance(okHttpClient, gson);
    }
}
