package com.gtv.hanhee.novelreading.Ui.Presenter;

import android.content.Context;

import com.gtv.hanhee.novelreading.Api.ReaderApi;
import com.gtv.hanhee.novelreading.Api.VietPhraseApi;
import com.gtv.hanhee.novelreading.Ui.Contract.MainContract;

import javax.inject.Inject;

public class MainActivityPresenter implements MainContract.Presenter<MainContract.View> {

    private Context context;
    private ReaderApi readerApi;
    private VietPhraseApi vietPhraseApi;

    private MainContract.View view;

    @Inject
    public MainActivityPresenter(Context context, ReaderApi readerApi, VietPhraseApi vietPhraseApi) {
        this.context = context;
        this.readerApi = readerApi;
        this.vietPhraseApi = vietPhraseApi;
    }


    @Override
    public void attachView(MainContract.View view) {
        this.view = view;
    }

}
