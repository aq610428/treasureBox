package com.treasure.million.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.treasure.million.R;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.base.BaseApplication;


/**
 * @author: zt
 * @date: 2020/6/4
 * @name:AgreementActivity
 */
public class AgreementActivity extends BaseActivity {
    private TextView title_text_tv, title_left_btn,text_doc;

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_agreement);
        BaseApplication.activityTaskManager.putActivity("AgreementActivity",this);
    }

    @Override
    protected void initView() {
        text_doc = getView(R.id.text_doc);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_text_tv.setText("用户协议");
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_btn:
                finish();
                break;
        }
    }
}
