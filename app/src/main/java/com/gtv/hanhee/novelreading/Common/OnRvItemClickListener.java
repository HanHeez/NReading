package com.gtv.hanhee.novelreading.Common;

import android.view.View;

public interface OnRvItemClickListener<T> {
    void onItemClick(View view, int position, T data);
}
