package com.gtv.hanhee.novelreading.Api;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface VietPhraseService {

    @FormUrlEncoded
    @POST("TranslateVietPhraseS")
    Observable<String> translateText(@Field("chineseContent") String contentText);

}
