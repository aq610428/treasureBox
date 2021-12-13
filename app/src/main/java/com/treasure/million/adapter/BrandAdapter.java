package com.treasure.million.adapter;

import android.content.Context;
import com.treasure.million.R;
import com.treasure.million.bean.Brand;
import com.treasure.million.glide.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zt
 * @date: 2020/5/26
 * @name:清单
 */
public class BrandAdapter extends AutoRVAdapter {
    private List<Brand> inventories = new ArrayList<>();
    private Context mContext;

    public BrandAdapter(Context context, List<Brand> inventories) {
        super(context,inventories);
        this.mContext = context;
        this.inventories = inventories;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setData(List<Brand> infos) {
        this.inventories = inventories;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_brand_ifg;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Brand inventory = inventories.get(position);
        vh.getTextView(R.id.text_brand).setText(inventory.getModelName()+ "");
        GlideUtils.setImageUrl(inventory.getModelImg(), vh.getImageView(R.id.iv_logo));
    }


}
