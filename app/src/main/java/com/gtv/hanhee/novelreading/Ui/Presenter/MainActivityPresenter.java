package com.gtv.hanhee.novelreading.Ui.Presenter;

import android.content.Context;
import android.widget.Toast;

import com.gtv.hanhee.novelreading.Api.BookApi;
import com.gtv.hanhee.novelreading.Model.PlayerList;
import com.gtv.hanhee.novelreading.Ui.Contract.MainContract;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivityPresenter implements MainContract.Presenter {

    private Context context;
    private BookApi bookApi;

    @Inject
    public MainActivityPresenter(Context context, BookApi bookApi){
        this.context = context;
        this.bookApi = bookApi;
    }

    public void getPlayerList(){
        bookApi.getPlayerList("387699584").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PlayerList>() {
                    @Override
                    public void onNext(PlayerList playerList) {

                    }

                    @Override
                    public void onCompleted() {
                        Toast.makeText(context, "khởi tạo hoàn tất", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e){

                    }
                });
    }
}
