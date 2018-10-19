package com.gtv.hanhee.novelreading.Ui.Adapter;

import android.content.Context;

import com.gtv.hanhee.novelreading.Model.BookMixAToc;
import com.gtv.hanhee.novelreading.R;
import com.yuyh.easyadapter.abslistview.EasyLVAdapter;
import com.yuyh.easyadapter.abslistview.EasyLVHolder;

import java.util.List;

public class TocListAdapter extends EasyLVAdapter<BookMixAToc.mixToc.Chapters> {

    public TocListAdapter(Context context, List<BookMixAToc.mixToc.Chapters> list) {
        super(context, list, R.layout.item_book_read_toc_list);
    }

    @Override
    public void convert(EasyLVHolder holder, int position, BookMixAToc.mixToc.Chapters chapters) {
        holder.setText(R.id.tvTocItem, chapters.title);
    }

}
