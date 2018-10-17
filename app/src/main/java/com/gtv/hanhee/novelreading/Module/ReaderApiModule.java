package com.gtv.hanhee.novelreading.Module;

import com.google.gson.Gson;
import com.gtv.hanhee.novelreading.Api.ReaderApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class ReaderApiModule {

    @Provides
    protected ReaderApi provideBookService(OkHttpClient okHttpClient, Gson gson) {
        return ReaderApi.getInstance(okHttpClient, gson);
    }
}
