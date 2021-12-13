package com.treasure.million.adapter;

import android.content.Context;

import com.treasure.million.R;
import com.treasure.million.bean.AssetsBean;
import com.treasure.million.util.BigDecimalUtils;
import com.treasure.million.util.Utility;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author: zt
 * @date: 2020/7/9
 * @name:MinIngAdapter
 */
public class AsetsAdapter extends AutoRVAdapter {
    private List<AssetsBean> list;

    public AsetsAdapter(Context context, List<AssetsBean> list) {
        super(context, list);
        this.list = list;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_asets;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        AssetsBean assetsBean = list.get(position);
        vh.getTextView(R.id.text_num).setText(assetsBean.getTitle());

        vh.getTextView(R.id.text_user).setText(BigDecimalUtils.round(new BigDecimal(assetsBean.getBalance()), 4).toPlainString() + "");
        vh.getTextView(R.id.text_usd).setText(assetsBean.getStringCreateTime() + "");
        switch (assetsBean.getStatus()) {
            case 1:
                vh.getTextView(R.id.text_congeal).setText("待审核");
                break;
            case 2:
                vh.getTextView(R.id.text_congeal).setText("交易中");
                break;
            case 3:
                vh.getTextView(R.id.text_congeal).setText("交易成功");
                break;
            case 4:
                vh.getTextView(R.id.text_congeal).setText("交易失败");
                break;
        }

    }

    public void setData(List<AssetsBean> data) {
        this.list = data;
    }
}
