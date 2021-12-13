package com.treasure.million.adapter;

import android.content.Context;

import com.treasure.million.R;
import com.treasure.million.bean.OreInfo;

import java.util.List;

/**
 * @author: zt
 * @date: 2020/9/27
 * @name:AddressAdapter
 */
public class DefiListAdapter1 extends AutoRVAdapter {
    public List<OreInfo> list;
    public Context context;

    public DefiListAdapter1(Context context, List<OreInfo> list) {
        super(context, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_defi_list1;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        OreInfo beans = list.get(position);


    }

    public void setData(List<OreInfo> beanList) {
        this.list = beanList;
    }
}
