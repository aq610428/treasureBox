package com.treasure.million.adapter;

import android.content.Context;
import com.treasure.million.R;
import com.treasure.million.bean.TabBean;
import com.treasure.million.util.BigDecimalUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: zt
 * @date: 2020/9/27
 * @name:AddressAdapter
 */
public class DefiAdapter extends AutoRVAdapter {
    List<TabBean.ItemsBean> list;
    Context context;

    public DefiAdapter(Context context, List<TabBean.ItemsBean> list) {
        super(context, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_defi;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        TabBean.ItemsBean beans = list.get(position);
        vh.getTextView(R.id.text_mouth).setText(beans.getZq());
        vh.getTextView(R.id.text_profit).setText(beans.getLv());
        BigDecimal decimal = BigDecimalUtils.div(new BigDecimal(beans.getMax()), new BigDecimal(10000));
        vh.getTextView(R.id.text_title).setText(beans.getMin() + "-" + decimal.intValue() + "万 100BOX的整数倍递增");
    }

    public void setData(List<TabBean.ItemsBean> beanList) {
        this.list = beanList;
    }
}
