package com.treasure.million.adapter;

import android.content.Context;
import android.view.View;

import com.treasure.million.R;
import com.treasure.million.bean.BoxInfo;
import com.treasure.million.util.BigDecimalUtils;
import com.treasure.million.util.TypefaceUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @author: zt
 * @date: 2020/7/9
 * @name:MinIngAdapter
 */
public class IncomeAdapter extends AutoRVAdapter {
    private List<BoxInfo> list;
    private Context context;

    public IncomeAdapter(Context context, List<BoxInfo> list) {
        super(context, list);
        this.list = list;
        this.context = context;
    }


    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_income;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        BoxInfo boxInfo = list.get(position);
        vh.getTextView(R.id.text_date).setText(boxInfo.getSumdate());
        vh.getTextView(R.id.text_eth).setText(BigDecimalUtils.getBigDecimal(boxInfo.getBox()+ ""));
        BigDecimal decimal = BigDecimalUtils.mul(new BigDecimal(boxInfo.getBoxPrice()), new BigDecimal(boxInfo.getBox()));
        BigDecimal decimal1 = BigDecimalUtils.round(decimal, 4);
        vh.getTextView(R.id.text_price).setText(BigDecimalUtils.getBigDecimal(decimal1.toPlainString()));
        TypefaceUtil.setTextType(context, "DINOT-Bold.ttf", vh.getTextView(R.id.text_price));
    }

    public void setData(List<BoxInfo> data) {
        this.list = data;
    }
}
