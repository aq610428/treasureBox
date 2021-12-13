package com.treasure.million.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.base.BaseApplication;
import com.treasure.million.bean.InsureInfo;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.R;

/**
 * @author: zt
 * @date: 2020/8/11
 * @name:TripDeilActivity
 */
public class InsureDeilActivity extends BaseActivity {
    private TextView title_text_tv, title_left_btn;
    private InsureInfo travrt;
    private TextView text_address, text_high, text_device, text_ignition, text_extinguish, text_reporting,
            text_travel, text_begin_lat;

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_insuredeil);
        BaseApplication.activityTaskManager.putActivity("InsureDeilActivity", this);
    }

    @Override
    protected void initView() {
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_text_tv.setText("保险数据区块详情");
        text_address = getView(R.id.text_address);
        text_high = getView(R.id.text_high);
        text_device = getView(R.id.text_device);
        text_ignition = getView(R.id.text_ignition);
        text_extinguish = getView(R.id.text_extinguish);
        text_reporting = getView(R.id.text_reporting);
        text_travel = getView(R.id.text_travel);
        text_begin_lat = getView(R.id.text_begin_lat);
    }

    @Override
    protected void initData() {
        travrt = (InsureInfo) getIntent().getSerializableExtra("travrt");
        if (travrt != null) {
            text_address.setText(travrt.getHashcode() + "");
            text_high.setText(travrt.getBlockid()+"");
            text_device.setText(travrt.getIsuranceName()+"");
            text_ignition.setText(travrt.getIsuranceNo()+"");
            text_extinguish.setText(travrt.getIsuranceCompany()+"");
            text_reporting.setText(travrt.getIsuranceStarttime());
            text_travel.setText(travrt.getIsuranceEndtime());
            text_begin_lat.setText(travrt.getIsuranceDetail()+"");
        }

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
