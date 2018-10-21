package com.gtv.hanhee.novelreading.Module;

import com.gtv.hanhee.novelreading.Api.BookApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class BookApiModule {

    @Provides
    protected BookApi provideBookService(OkHttpClient okHttpClient) {
        return BookApi.getInstance(okHttpClient);
    }

    public static class MyLog implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(String message) {

        }
    }
}
