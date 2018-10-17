package com.gtv.hanhee.novelreading.Ui.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gtv.hanhee.novelreading.Base.Constant;
import com.gtv.hanhee.novelreading.Model.SearchDetail;
import com.gtv.hanhee.novelreading.R;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

public class SearchResultAdapter extends EasyRVAdapter<SearchDetail.SearchBooks> {

    public SearchResultAdapter(Context context, List<SearchDetail.SearchBooks> list) {
        super(context, list, R.layout.item_search_result_list);
    }

    @Override
    protected void onBindData(EasyRVHolder holder, int position, SearchDetail.SearchBooks item) {
        ImageView ivCover = holder.getView(R.id.ivBookCover);
        Glide.with(mContext).load(Constant.IMG_BASE_URL + item.cover).into(ivCover);

        holder.setText(R.id.tvBookTitle, item.titleTrans)
                .setText(R.id.tvLatelyFollower, item.latelyFollower + " người theo dõi | ")
                .setText(R.id.tvRetentionRatio, (TextUtils.isEmpty(item.retentionRatio) ? "0" : item.retentionRatio) + "% người đọc | ")
                .setText(R.id.tvAuthor, "Tác giả: " + item.authorTrans);
    }
}

