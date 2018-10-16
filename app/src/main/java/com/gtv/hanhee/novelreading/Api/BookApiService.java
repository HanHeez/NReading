package com.gtv.hanhee.novelreading.Api;

import com.gtv.hanhee.novelreading.Model.PlayerList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface BookApiService {

    @GET("/api/playlist/detail")
    Observable<PlayerList> getPlayerList(@Query("id") String id);
}
