package com.treasure.million.adapter;

import android.content.Context;
import android.view.View;

import com.treasure.million.R;
import com.treasure.million.bean.ActiveBean;
import com.treasure.million.bean.MinVo;
import com.treasure.million.bean.RedVo;
import com.treasure.million.ui.RedListActivity;
import com.treasure.million.util.BigDecimalUtils;
import com.treasure.million.util.Utility;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author: zt
 * @date: 2020/9/27
 * @name:AddressAdapter
 */
public class RedAdapter extends AutoRVAdapter {
    List<RedVo> list;
    RedListActivity activity;

    public RedAdapter(RedListActivity activity, List<RedVo> list) {
        super(activity, list);
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_red;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        RedVo sBean = list.get(position);
        vh.getTextView(R.id.text_machine).setText(sBean.getName());
        vh.getTextView(R.id.text_eth).setText(sBean.getBuyNum() + "算力");
        BigDecimal decimal = BigDecimalUtils.mul(new BigDecimal(100), new BigDecimal(sBean.getMuls()));
        vh.getTextView(R.id.text_balance).setText(decimal.toPlainString() + "USDT");
        vh.getTextView(R.id.text_day).setText(sBean.getDays() + "天");
        vh.getTextView(R.id.text_produce).setText( BigDecimalUtils.round(new BigDecimal(sBean.getProfit()),5).toPlainString()+ "ETH");
        vh.getTextView(R.id.text_price).setText("1算力/"+sBean.getPrice() + "USDT");
        if ("1".equals(sBean.getState() + "")) {
            vh.getTextView(R.id.text_start).setText("理财中");
            vh.getTextView(R.id.text_redeem).setVisibility(View.VISIBLE);
        } else if ("2".equals(sBean.getState())) {
            vh.getTextView(R.id.text_start).setText("已结束");
            vh.getTextView(R.id.text_redeem).setVisibility(View.GONE);
        } else if ("3".equals(sBean.getState())) {
            vh.getTextView(R.id.text_start).setText("已违约");
            vh.getTextView(R.id.text_redeem).setVisibility(View.GONE);
        }

        if (sBean.getIsredeem()==0&&"1".equals(sBean.getState() + "")) {
            vh.getTextView(R.id.text_redeem).setVisibility(View.VISIBLE);
        } else {
            vh.getTextView(R.id.text_redeem).setVisibility(View.GONE);
        }


        vh.getTextView(R.id.text_redeem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showPay1(sBean.getId());
            }
        });

    }

    public void setData(List<RedVo> data) {
        this.list = data;
    }

}
