package com.treasure.million.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.treasure.million.R;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.TripVo;
import com.treasure.million.util.BigDecimalUtils;
import com.treasure.million.util.SystemTools;

import java.math.BigDecimal;

/**
 * @author: zt
 * @date: 2020/8/11
 * @name:TripDeilActivity
 */
public class TripDeilActivity extends BaseActivity {
    private TextView title_text_tv, title_left_btn;
    private TripVo travrt;
    private TextView text_address, text_high, text_device, text_ignition, text_extinguish, text_reporting,
            text_travel, text_begin_lat, text_end_lat, text_km_lat, text_oil_lat, text_max, text_average, text_battery, text_accelerate, text_down, text_turn;

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tripdeil);
    }

    @Override
    protected void initView() {
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_text_tv.setText("行程数据区块详情");
        text_address = getView(R.id.text_address);
        text_high = getView(R.id.text_high);
        text_device = getView(R.id.text_device);
        text_ignition = getView(R.id.text_ignition);
        text_extinguish = getView(R.id.text_extinguish);
        text_reporting = getView(R.id.text_reporting);
        text_travel = getView(R.id.text_travel);
        text_begin_lat = getView(R.id.text_begin_lat);
        text_end_lat = getView(R.id.text_end_lat);
        text_km_lat = getView(R.id.text_km_lat);
        text_oil_lat = getView(R.id.text_oil_lat);
        text_max = getView(R.id.text_max);
        text_average = getView(R.id.text_average);
        text_battery = getView(R.id.text_battery);
        text_accelerate = getView(R.id.text_accelerate);
        text_down = getView(R.id.text_down);
        text_turn = getView(R.id.text_turn);

    }

    @Override
    protected void initData() {
        travrt = (TripVo) getIntent().getSerializableExtra("travrt");
        if (travrt != null) {
            text_address.setText(travrt.getHashcode() + "");
            text_high.setText(travrt.getBlockid()+"");
            text_device.setText(travrt.getPartnername());
            text_ignition.setText(travrt.getStartTime());
            text_extinguish.setText(travrt.getEndTime());
            text_reporting.setText(travrt.getUploadTime());
            text_travel.setText(SystemTools.mathMinute(Integer.valueOf(travrt.getTripTime())));
            text_begin_lat.setText(travrt.getStartLat()+","+travrt.getStartLng());
            text_end_lat.setText(travrt.getEndLat()+","+travrt.getEndLng());
            String one = SystemTools.mathKmOne(Integer.valueOf(travrt.getMile())) +"KM";//"KM\n里程"
            String two = BigDecimalUtils.div(new BigDecimal(travrt.getOil()+""),new BigDecimal(1000),2).toPlainString() +"L"  ;//+ "L\n油耗"
            text_km_lat.setText(one+"");
            text_oil_lat.setText(two);
            text_max.setText(travrt.getSpeedMax()+"KM/H");
            text_average.setText(travrt.getSpeedAvg()+"KM/H");

            text_battery.setText(travrt.getVoltage()+"V");
            text_accelerate.setText(travrt.getAcceCount()+"次");
            text_down.setText(travrt.getDeceCount()+"次");
            text_turn.setText(travrt.getSharpturnCount()+"次");

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
