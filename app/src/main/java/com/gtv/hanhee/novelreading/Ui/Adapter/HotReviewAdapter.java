package com.gtv.hanhee.novelreading.Ui.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gtv.hanhee.novelreading.Base.Constant;
import com.gtv.hanhee.novelreading.Common.OnRvItemClickListener;
import com.gtv.hanhee.novelreading.Model.HotReview;
import com.gtv.hanhee.novelreading.R;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class HotReviewAdapter extends EasyRVAdapter<HotReview.Reviews> {
    private OnRvItemClickListener itemClickListener;

    public HotReviewAdapter(Context context, List<HotReview.Reviews> list, OnRvItemClickListener listener) {
        super(context, list, R.layout.item_book_detai_hot_review_list);
        this.itemClickListener = listener;
    }

    @Override
    protected void onBindData(final EasyRVHolder holder, final int position, final HotReview.Reviews item) {
        ImageView ivCover = holder.getView(R.id.ivAvatar);
        Glide.with(mContext).load(Constant.IMG_BASE_URL + item.author.avatar)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(128, 0, RoundedCornersTransformation.CornerType.BOTTOM)))
                .into(ivCover);

        holder.setText(R.id.tvNickName, item.author.nickname)
                .setText(R.id.tvLv, " lv."+item.author.lv)
                .setText(R.id.tvTitle, item.title)
                .setText(R.id.tvContent, String.valueOf(item.content))
                .setText(R.id.tvHelpfulYes, String.valueOf(item.helpful.yes));
        holder.setOnItemViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(holder.getItemView(), position, item);
            }
        });
    }

}
