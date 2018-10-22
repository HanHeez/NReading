package com.gtv.hanhee.novelreading.Utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.gtv.hanhee.novelreading.ReaderApplication;

import java.lang.reflect.Field;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxUtil {

    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {    // đơn giản chuỗi compose
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Observable rxCreateDiskObservable(final String key, final Class<T> clazz) {
        return Observable.create((ObservableOnSubscribe<String>) emitter -> {
            LogUtils.d("get data from disk: key==" + key);
            String json = ACache.get(ReaderApplication.getsInstance()).getAsString(key);
            LogUtils.d("get data from disk finish , json==" + json);
            if (!TextUtils.isEmpty(json)) {
                emitter.onNext(json);
            }
            emitter.onComplete();
        })
                .map(s -> new Gson().fromJson(s, clazz))
                .subscribeOn(Schedulers.io());
    }

    public static <T> ObservableTransformer<T, T> rxCacheListHelper(final String key) {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())//Chỉ định chuỗi thực thi doOnNext làm chủ đề mới
                .doOnNext(data -> {
                    Schedulers.io().createWorker().schedule(() -> {
                        LogUtils.d("get data from network finish ,start cache...");
                        //Nhận list bằng cách Reflection và sau đó quyết định có nên lưu vào bộ nhớ cache hay không.
                        if (data == null)
                            return;
                        Class clazz = data.getClass();
                        Field[] fields = clazz.getFields();
                        for (Field field : fields) {
                            String className = field.getType().getSimpleName();
                            // Nhận giá trị thuộc tính
                            if (className.equalsIgnoreCase("List")) {
                                try {
                                    List list = (List) field.get(data);
                                    LogUtils.d("list==" + list);
                                    if (list != null && !list.isEmpty()) {
                                        ACache.get(ReaderApplication.getsInstance())
                                                .put(key, new Gson().toJson(data, clazz));
                                        LogUtils.d("cache finish");
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> rxCacheBeanHelper(final String key) {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())//Chỉ định chuỗi thực thi doOnNext làm chủ đề mới
                .doOnNext(data -> {
                    Schedulers.io().createWorker().schedule(() -> {
                        LogUtils.d("get data from network finish ,start cache...");
                        ACache.get(ReaderApplication.getsInstance())
                                .put(key, new Gson().toJson(data, data.getClass()));
                        LogUtils.d("cache finish");
                    });

                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}
