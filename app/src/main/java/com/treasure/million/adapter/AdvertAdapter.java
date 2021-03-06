package com.treasure.million.adapter;

import android.content.Context;
import android.text.Html;
import com.treasure.million.R;
import com.treasure.million.bean.Massage;
import com.treasure.million.util.Utility;

import java.util.List;


/**
 * @author: zt
 * @date: 2020/7/9
 * @name:MinIngAdapter
 */
public class AdvertAdapter extends AutoRVAdapter {
    private List<Massage> list;

    public AdvertAdapter(Context context, List<Massage> list) {
        super(context, list);
        this.list = list;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_advert;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Massage massage = list.get(position);
        vh.getTextView(R.id.text_name).setText(massage.getTitle());
        String createTime = massage.getStringCreateTime().toString();
        if (!Utility.isEmpty(createTime) && createTime.length() > 8) {
            String end = createTime.substring(0, createTime.length() - 8);
            String start = createTime.substring(createTime.length() -8, createTime.length());
            vh.getTextView(R.id.text_date).setText(end + "  " + start);
        }
        vh.getTextView(R.id.text_verify).setText(Html.fromHtml(massage.getContent()));
    }

    public void setData(List<Massage> data) {
        this.list = data;
    }
}
