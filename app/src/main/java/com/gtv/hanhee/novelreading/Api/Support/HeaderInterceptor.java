package com.gtv.hanhee.novelreading.Api.Support;

import com.gtv.hanhee.novelreading.Utils.AppUtils;
import com.gtv.hanhee.novelreading.Utils.DeviceUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Retrofit2 Cookie, được sử dụng và cài đặt để lưu Cookie
 *
 */
public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        if (original.url().toString().contains("book/")) {
            Request request = original.newBuilder()
                    .addHeader("User-Agent", "ZhuiShuShenQi/3.68.2[preload=false;locale=zh_CN;clientidbase=android-nvidia]") // Không thể chuyển sang UTF 8
                    .addHeader("X-User-Agent", "ZhuiShuShenQi/3.68.2[preload=false;locale=zh_CN;clientidbase=android-nvidia]")
                    .addHeader("X-Device-Id", DeviceUtils.getIMEI(AppUtils.getAppContext()))
                    .addHeader("Host", "api.zhuishushenqi.com")
                    .addHeader("Connection", "Keep-Alive")
                    //.addHeader("Accept-Encoding", "gzip")
                    .build();
            return chain.proceed(request);
        }
        return chain.proceed(original);
    }
}
