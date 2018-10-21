package com.gtv.hanhee.novelreading.Manager;

import com.gtv.hanhee.novelreading.Model.Support.RefreshCollectionIconEvent;
import com.gtv.hanhee.novelreading.Model.Support.RefreshCollectionListEvent;
import com.gtv.hanhee.novelreading.Model.Support.SubEvent;

import org.greenrobot.eventbus.EventBus;

public class EventManager {
    // Chưa thiết kế
    public static void refreshCollectionList() {
        EventBus.getDefault().post(new RefreshCollectionListEvent());
    }

    public static void refreshCollectionIcon() {
        EventBus.getDefault().post(new RefreshCollectionIconEvent());
    }

    public static void refreshSubCategory(String minor, String type) {
        EventBus.getDefault().post(new SubEvent(minor, type));
    }

}
