package com.gtv.hanhee.novelreading.Module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gtv.hanhee.novelreading.Api.Support.HeaderInterceptor;
import com.gtv.hanhee.novelreading.Utils.LogUtils;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    public OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new MyLog());
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true) //
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(logging);
        return builder.build();
    }

    @Provides
    public Gson provideGson() {
        Gson gson = new GsonBuilder().setLenient().create();
        return gson;
    }


    @Provides
    public Context provideContext() {
        return context;
    }

    static class MyLog implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            LogUtils.i("oklog: " + message);

        }
    }
}
