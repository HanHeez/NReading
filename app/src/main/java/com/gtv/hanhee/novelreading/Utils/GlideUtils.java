package com.gtv.hanhee.novelreading.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

public class GlideUtils {

    public static void loadRoundedGlide(Context mContext, String imgUrl, int radius, ImageView view) {
        Glide.with(mContext)
                .load(imgUrl)
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(radius)))
                .transition(new DrawableTransitionOptions().crossFade(300))
                .into(view);
    }
    public static void loadCircleGlide(Context mContext, String imgUrl, ImageView view) {
        Glide.with(mContext)
                .load(imgUrl)
                .apply(RequestOptions.circleCropTransform())
                .transition(new DrawableTransitionOptions().crossFade(300))
                .into(view);
    }

    public static void loadRoundedGlide(Context mContext, String s, ImageView mIvBookCover) {
    }
}
