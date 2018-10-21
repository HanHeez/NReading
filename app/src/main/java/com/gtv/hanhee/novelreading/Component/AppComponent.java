package com.gtv.hanhee.novelreading.Component;

import android.content.Context;

import com.gtv.hanhee.novelreading.Api.BookApi;
import com.gtv.hanhee.novelreading.Api.VietPhraseApi;
import com.gtv.hanhee.novelreading.Module.AppModule;
import com.gtv.hanhee.novelreading.Module.BookApiModule;
import com.gtv.hanhee.novelreading.Module.VietPhraseApiModule;

import dagger.Component;

@Component(modules = {AppModule.class, BookApiModule.class, VietPhraseApiModule.class})
public interface AppComponent {

    Context getContext();

    BookApi getBookApi();

    VietPhraseApi getVietPhraseApi();

}