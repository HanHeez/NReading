package com.gtv.hanhee.novelreading.Ui.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gtv.hanhee.novelreading.Base.Constant;
import com.gtv.hanhee.novelreading.Common.OnRvItemClickListener;
import com.gtv.hanhee.novelreading.Model.Recommend;
import com.gtv.hanhee.novelreading.R;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

public class RecommendAdapter extends EasyRVAdapter<Recommend.RecommendBooks> {
    private OnRvItemClickListener itemClickListener;

    public RecommendAdapter(Context context, List<Recommend.RecommendBooks> list, OnRvItemClickListener
            listener) {
        super(context, list, R.layout.item_recommend_list);
        this.itemClickListener = listener;
    }

    @Override
    protected void onBindData(final EasyRVHolder holder, final int position, final Recommend.RecommendBooks item) {
        ImageView ivRecommendCover = holder.getView(R.id.ivRecommendCover);
        Glide.with(mContext).load(Constant.IMG_BASE_URL + item.cover).into(ivRecommendCover);

        holder.setText(R.id.tvRecommendTitle, item.title)
                .setText(R.id.tvRecommendShort, item.lastChapter);

        holder.setOnItemViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(holder.getItemView(), position, item);
            }
        });
    }
}
