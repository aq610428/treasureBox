package com.treasure.million.adapter;

import android.content.Context;
import com.treasure.million.R;
import com.treasure.million.bean.GoodBean;

import java.util.List;

/**
 * @author: zt
 * @date: 2020/9/27
 * @name:AddressAdapter
 */
public class GoodAdapter extends AutoRVAdapter {
    List<GoodBean.GoodsSpecListBean> list;

    public GoodAdapter(Context context, List<GoodBean.GoodsSpecListBean> list) {
        super(context, list);
        this.list=list;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_goog;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        GoodBean.GoodsSpecListBean goodsSpecListBean=list.get(position);
        vh.getTextView(R.id.text_key).setText(goodsSpecListBean.getKey());
        vh.getTextView(R.id.text_value).setText(goodsSpecListBean.getValue());
    }
}
