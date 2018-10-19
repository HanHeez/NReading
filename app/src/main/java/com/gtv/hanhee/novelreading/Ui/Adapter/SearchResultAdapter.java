package com.gtv.hanhee.novelreading.Ui.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gtv.hanhee.novelreading.Base.Constant;
import com.gtv.hanhee.novelreading.Common.OnRvItemClickListener;
import com.gtv.hanhee.novelreading.Model.SearchDetail;
import com.gtv.hanhee.novelreading.R;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

public class SearchResultAdapter extends EasyRVAdapter<SearchDetail.SearchBooks> {
    private OnRvItemClickListener itemClickListener;

    public SearchResultAdapter(Context context, List<SearchDetail.SearchBooks> list, OnRvItemClickListener listener) {
        super(context, list, R.layout.item_search_result_list);
        this.itemClickListener = listener;
    }

    @SuppressLint("StringFormatMatches")
    @Override
    protected void onBindData(EasyRVHolder holder, int position, SearchDetail.SearchBooks item) {
        ImageView ivCover = holder.getView(R.id.ivBookCover);
        Glide.with(mContext).load(Constant.IMG_BASE_URL + item.cover).into(ivCover);

        holder.setText(R.id.tvBookTitle, item.titleTrans)
                .setText(R.id.tvLatelyFollower, String.format(mContext.getString(R.string
                        .search_result_lately_follower), item.latelyFollower))
                .setText(R.id.tvRetentionRatio, (TextUtils.isEmpty(item.retentionRatio) ? String
                        .format(mContext.getString(R.string.search_result_retention_ratio),
                                "0") :
                        String.format(mContext.getString(R.string.search_result_retention_ratio),
                                item.retentionRatio)))
                .setText(R.id.tvAuthor, String.format(mContext.getString(R.string
                        .search_result_author), item.authorTrans));
        holder.setOnItemViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(holder.getItemView(), position, item);
            }
        });
    }
}

