package com.treasure.million.adapter;

import android.content.Context;
import android.view.View;

import com.treasure.million.R;
import com.treasure.million.bean.OreInfo;
import com.treasure.million.glide.GlideUtils;
import com.treasure.million.ui.fragment.OreFragment;
import com.treasure.million.util.BigDecimalUtils;
import com.treasure.million.util.SystemTools;
import com.treasure.million.util.Utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zt
 * @date: 2020/5/26
 * @name:清单
 */
public class CarAdapter extends AutoRVAdapter {
    private List<OreInfo> inventories = new ArrayList<>();
    private OreFragment fragment;

    public CarAdapter(OreFragment fragment, List<OreInfo> inventories) {
        super(fragment.getActivity(), inventories);
        this.fragment = fragment;
        this.inventories = inventories;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_keep_car;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        OreInfo inventory = inventories.get(position);
        vh.getTextView(R.id.text_name).setText(inventory.getSymbol() + "");

        if (!Utility.isEmpty(inventory.getPrice_usd())) {
            BigDecimal price = BigDecimalUtils.round(new BigDecimal(inventory.getPrice_usd()) , 4);
            vh.getTextView(R.id.text_price).setText("$:" + price.toPlainString());
        }
        if (!Utility.isEmpty(inventory.getPercent_change_24h())){
            if (Double.parseDouble(inventory.getPercent_change_24h())<0) {
                vh.getTextView(R.id.text_Increase).setBackgroundResource(R.drawable.shape_login);
                vh.getTextView(R.id.text_Increase).setText("-"+Math.abs(Double.parseDouble(inventory.getPercent_change_24h())) + "%");
            } else {
                vh.getTextView(R.id.text_Increase).setText(Math.abs(Double.parseDouble(inventory.getPercent_change_24h())) + "%");
                vh.getTextView(R.id.text_Increase).setBackgroundResource(R.drawable.shape_login_red);
            }
        }

        GlideUtils.setImageUrl(inventory.getLogo_png(), vh.getImageView(R.id.iv_logo));
        vh.getShadowLayout(R.id.ll_sd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=inventory.getUrl();
                SystemTools.openBrowser(fragment.getActivity(),url);
            }
        });

    }


}
