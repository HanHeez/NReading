package com.gtv.hanhee.novelreading.Ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.gtv.hanhee.novelreading.R;

public abstract class BaseViewHolder<M> extends RecyclerView.ViewHolder {

    protected BaseViewHolder<M> holder;

    private int mLayoutId;
    protected Context mContext;
    private View mConvertView;
    private SparseArray<View> mViews = new SparseArray<>();

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        holder = this; //instance
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

    public BaseViewHolder setText(int viewId, String value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public BaseViewHolder setImageResource(int viewId, int imgResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imgResId);
        return this;
    }

    public BaseViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
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

    public BaseViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public BaseViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}
