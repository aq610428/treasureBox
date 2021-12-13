package com.treasure.million.adapter;

import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.treasure.million.R;
import com.treasure.million.bean.MoneryBean;
import com.treasure.million.bean.OreInfo;
import com.treasure.million.ui.fragment.MoneyFragment;
import com.treasure.million.util.BigDecimalUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import com.treasure.million.weight.FilterUtil;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author: zt
 * @date: 2020/7/9
 * @name:MinIngAdapter
 */
public class MoneryAdapter extends AutoRVAdapter {
    private List<MoneryBean> list;
    public MoneyFragment fragment;

    public MoneryAdapter(MoneyFragment fragment, List<MoneryBean> list) {
        super(fragment.getContext(), list);
        this.list = list;
        this.fragment = fragment;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_monery;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        MoneryBean massage = list.get(position);
        vh.getTextView(R.id.text_name).setText(massage.getName());
        vh.getTextView(R.id.text_cycle).setText(massage.getDays() + "天");
        vh.getTextView(R.id.text_day).setText(massage.getTotalNum() + "算力");
        vh.getTextView(R.id.text_price).setText("1算力/"+massage.getPrice() + "USDT");
        EditText editText = vh.getEditText(R.id.edit_powe);
        editText.setHint(massage.getMinNum() + "-" + massage.getMaxNum() + "算力");
        InputFilter[] filters = null;
        if (massage.getName().contains("高配")) {
            filters = new InputFilter[]{
                    FilterUtil.getInputFilterProhibitEmoji(),
                    new InputFilter.LengthFilter(3)
            };
        } else {
            filters = new InputFilter[]{
                    FilterUtil.getInputFilterProhibitEmoji(),
                    new InputFilter.LengthFilter(2)
            };
        }
        editText.setFilters(filters);

        String num = BigDecimalUtils.sub(new BigDecimal(massage.getTotalNum()), new BigDecimal(massage.getBuyNum())).toPlainString();
        vh.getTextView(R.id.text_surplus).setText(num + "算力");

        vh.getTextView(R.id.text_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = editText.getText().toString();
                if (Utility.isEmpty(num)) {
                    ToastUtil.showToast("算力不能为空");
                    return;
                }
                int count = Integer.parseInt(num);
                if (count >= massage.getMinNum() && massage.getMaxNum() >= count) {
                    fragment.showPay(massage.getId(), num);
                } else {
                    ToastUtil.showToast("购买算力不能超出限制范围");
                }

            }
        });
    }

    public void setData(List<MoneryBean> data) {
        this.list = data;
    }
}
