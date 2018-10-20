package com.gtv.hanhee.novelreading.Module;

import com.google.gson.Gson;
import com.gtv.hanhee.novelreading.Api.ReaderApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class ReaderApiModule {

    @Provides
    protected ReaderApi provideBookService(OkHttpClient okHttpClient) {
        return ReaderApi.getInstance(okHttpClient);
    }

    public static class MyLog implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(String message) {

        }
    }
}
