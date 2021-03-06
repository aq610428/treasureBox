package com.treasure.million.adapter;

import android.content.Context;
import com.treasure.million.R;
import com.treasure.million.bean.ImageInfo;
import com.treasure.million.glide.GlideUtils;

import java.util.List;


/**
 * @author: zt
 * @date: 2020/9/15
 * @name:IndexListAdapter
 */
public class WareAdapter1 extends AutoRVAdapter {
    List<ImageInfo> list;

    public WareAdapter1(Context context, List<ImageInfo> list) {
        super(context, list);
        this.list = list;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_list_index1;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        GlideUtils.CreateImageRound(list.get(position).getPhotoFile(),vh.getImageView(R.id.iv_logo),5);
    }
}
