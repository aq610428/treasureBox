package com.treasure.million.adapter;

import android.content.Context;
import android.text.Html;

import com.treasure.million.R;
import com.treasure.million.bean.Massage;
import com.treasure.million.bean.MinVo;
import com.treasure.million.bean.MoneryBean;
import com.treasure.million.util.BigDecimalUtils;
import com.treasure.million.util.Utility;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author: zt
 * @date: 2020/7/9
 * @name:MinIngAdapter
 */
public class AdvertListAdapter extends AutoRVAdapter {
    private List<MinVo> list;

    public AdvertListAdapter(Context context, List<MinVo> list) {
        super(context, list);
        this.list = list;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_advert_list;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        MinVo bean = list.get(position);
        if (!Utility.isEmpty(bean.getStringCreateTime())){
            vh.getTextView(R.id.text_date).setText(bean.getStringCreateTime().substring(0,10));
        }

        vh.getTextView(R.id.text_type).setText(bean.getTitle());
        vh.getTextView(R.id.text_num).setText(BigDecimalUtils.round(new BigDecimal(bean.getBalance()),4).toPlainString());
    }

    public void setData(List<MinVo> data) {
        this.list = data;
    }
}
