package com.gtv.hanhee.novelreading.Ui.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gtv.hanhee.novelreading.Base.Constant;
import com.gtv.hanhee.novelreading.Common.OnRvItemClickListener;
import com.gtv.hanhee.novelreading.Model.SearchDetail;
import com.gtv.hanhee.novelreading.R;
import com.gtv.hanhee.novelreading.Ui.Activity.SearchActivity;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

public class SearchResultAdapter extends EasyRVAdapter<SearchDetail.SearchBooks> {
    private ItemClickListener itemClickListener;

    public SearchResultAdapter(Context context, List<SearchDetail.SearchBooks> list, ItemClickListener itemClickListener) {
        super(context, list, R.layout.item_search_result_list);
        this.itemClickListener = itemClickListener;
    }

    @Override
    protected void onBindData(EasyRVHolder holder, int position, SearchDetail.SearchBooks item) {
        ImageView ivCover = holder.getView(R.id.ivBookCover);
        Glide.with(mContext).load(Constant.IMG_BASE_URL + item.cover).into(ivCover);

        holder.setText(R.id.tvBookTitle, item.titleTrans)
                .setText(R.id.tvLatelyFollower, item.latelyFollower + " người theo dõi | ")
                .setText(R.id.tvRetentionRatio, (TextUtils.isEmpty(item.retentionRatio) ? "0" : item.retentionRatio) + "% người đọc | ")
                .setText(R.id.tvAuthor, "Tác giả:" + item.authorTrans);
        holder.setOnItemViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(item);
            }
        });
    }

    public interface ItemClickListener {
        void onItemClick(SearchDetail.SearchBooks item);
    }


}

