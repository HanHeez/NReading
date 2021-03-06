package com.gtv.hanhee.novelreading.Base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.gtv.hanhee.novelreading.R;


abstract public class BaseViewHolder<M> extends com.jude.easyrecyclerview.adapter.BaseViewHolder<M> {

    protected BaseViewHolder<M> holder;

    private int mLayoutId;
    protected Context mContext;
    private View mConvertView;

    private SparseArray<View> mViews = new SparseArray<>();

    public BaseViewHolder(View itemView) {
        super(itemView);
        holder = this;
        mConvertView = itemView;
        mContext = mConvertView.getContext();
    }

    public BaseViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
        holder = this;
        mConvertView = itemView;
        mLayoutId = res;
        mContext = mConvertView.getContext();
    }

    public void setData(M item) {

    }

    protected <T extends View> T $(@IdRes int id) {
        return (T) itemView.findViewById(id);
    }

    protected Context getContext() {
        return mContext == null ? (mContext = itemView.getContext()) : mContext;
    }

    public <V extends View> V getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (V) view;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    /**
     * 获取item布局
     *
     * @return
     */
    public View getItemView() {
        return mConvertView;
    }

    public BaseViewHolder setOnItemViewClickListener(View.OnClickListener listener) {
        mConvertView.setOnClickListener(listener);
        return this;
    }

    public BaseViewHolder setText(int viewId, String value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public BaseViewHolder setTextColor(int viewId, int color) {
        TextView view = getView(viewId);
        view.setTextColor(color);
        return this;
    }

    public BaseViewHolder setTextColorRes(int viewId, int colorRes) {
        TextView view = getView(viewId);
        view.setTextColor(ContextCompat.getColor(mContext, colorRes));
        return this;
    }

    public BaseViewHolder setImageResource(int viewId, int imgResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imgResId);
        return this;
    }

    public BaseViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public BaseViewHolder setBackgroundColorRes(int viewId, int colorRes) {
        View view = getView(viewId);
        view.setBackgroundResource(colorRes);
        return this;
    }

    public BaseViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public BaseViewHolder setImageDrawableRes(int viewId, int drawableRes) {
        Drawable drawable = ContextCompat.getDrawable(mContext, drawableRes);
        return setImageDrawable(viewId, drawable);
    }

    public BaseViewHolder setImageUrl(int viewId, String imgUrl) {
        ImageView view = getView(viewId);
        Glide.with(mContext).load(imgUrl).into(view);
        return this;
    }

    public BaseViewHolder setImageUrl(int viewId, String imgUrl, int placeHolderRes) {
        ImageView view = getView(viewId);
        Glide.with(mContext).load(imgUrl).into(view);
        return this;
    }

    public BaseViewHolder setCircleImageUrl(int viewId, String imgUrl, int placeHolderRes) {
        ImageView view = getView(viewId);
        Glide.with(mContext)
                .load(imgUrl)
                .apply(RequestOptions.circleCropTransform())
                .transition(new DrawableTransitionOptions().crossFade(300))
                .into(view);
        return this;
    }

    public BaseViewHolder setRoundImageUrl(int viewId, String imgUrl, int placeHolderRes) {
        ImageView view = getView(viewId);
        int radius = (int) getContext().getResources().getDimension(R.dimen.book_image_radius);
        Glide.with(mContext)
                .load(imgUrl)
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(radius)))
                .transition(new DrawableTransitionOptions().crossFade(300))
                .into(view);
        return this;
    }

    public BaseViewHolder setImageBitmap(int viewId, Bitmap imgBitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(imgBitmap);
        return this;
    }

    public BaseViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public BaseViewHolder setVisible(int viewId, int visible) {
        View view = getView(viewId);
        view.setVisibility(visible);
        return this;
    }

    public BaseViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public BaseViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public BaseViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    public BaseViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public BaseViewHolder setTypeface(int viewId, Typeface typeface) {
        TextView view = getView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    public BaseViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public BaseViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

}