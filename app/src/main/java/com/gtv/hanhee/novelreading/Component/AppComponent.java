package com.gtv.hanhee.novelreading.Component;

import android.content.Context;

import com.gtv.hanhee.novelreading.Api.ReaderApi;
import com.gtv.hanhee.novelreading.Api.VietPhraseApi;
import com.gtv.hanhee.novelreading.Module.AppModule;
import com.gtv.hanhee.novelreading.Module.ReaderApiModule;
import com.gtv.hanhee.novelreading.Module.VietPhraseApiModule;

import dagger.Component;

@Component(modules = {AppModule.class, ReaderApiModule.class, VietPhraseApiModule.class})
public interface AppComponent {

    Context getContext();

    ReaderApi getReaderApi();

    VietPhraseApi getVietPhraseApi();

}