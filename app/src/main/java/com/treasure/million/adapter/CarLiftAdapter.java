package com.treasure.million.adapter;

import android.content.Context;
import com.treasure.million.R;
import com.treasure.million.bean.LeftVo;
import com.treasure.million.glide.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zt
 * @date: 2020/5/26
 * @name:清单
 */
public class CarLiftAdapter extends AutoRVAdapter {
    private List<LeftVo.ItemsBean> inventories = new ArrayList<>();
    private Context context;

    public CarLiftAdapter(Context context, List<LeftVo.ItemsBean> inventories) {
        super(context, inventories);
        this.context = context;
        this.inventories = inventories;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_lefe_car;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        LeftVo.ItemsBean leftVo = inventories.get(position);
        vh.getTextView(R.id.text_name).setText(leftVo.getName());
        GlideUtils.setImageUrl(leftVo.getTagLogo(),vh.getImageView(R.id.iv_logo));
    }
}
