package com.treasure.million.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.treasure.million.R;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.util.SaveUtils;

/**
 * @author: zt
 * @date: 2020/9/8
 * @name:PassworadActivity
 */
public class SalesActivity extends BaseActivity {
    private TextView title_text_tv, title_left_btn, text_transaction, text_change, text_reset;
    private View view2,view1;

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sales);
    }

    @Override
    protected void initView() {
        view1= getView(R.id.view1);
        view2= getView(R.id.view2);
        text_transaction = getView(R.id.text_transaction);
        text_change = getView(R.id.text_change);
        text_reset = getView(R.id.text_reset);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        text_reset.setOnClickListener(this);
        text_transaction.setOnClickListener(this);
        text_change.setOnClickListener(this);
        title_text_tv.setText("交易密码");
        String isPay = SaveUtils.getSaveInfo().getIsPayPassword();
        if ("1".equals(isPay)) {
            text_transaction.setVisibility(View.GONE);
        }else{
            text_change.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            text_reset.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_btn:
                finish();
                break;
            case R.id.text_transaction:
                startActivity(new Intent(this, TransactionActivity.class));
                break;
            case R.id.text_change:
                startActivity(new Intent(this, ChangeActivity.class));
                break;
            case R.id.text_reset:
                startActivity(new Intent(this, ResetActivity.class));
                break;
        }
    }


    @Override
    protected void initData() {

    }


}
