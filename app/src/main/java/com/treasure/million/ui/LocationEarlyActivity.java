package com.treasure.million.ui;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.treasure.million.R;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.base.BaseApplication;
import com.treasure.million.bean.EarlyInfo;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.SystemTools;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;


/**
 * @author: zt
 * @date: 2020/6/6
 * @name:行车轨迹
 */
public class LocationEarlyActivity extends BaseActivity implements AMap.InfoWindowAdapter {
    private TextView title_text_tv, title_left_btn, text_mileage;
    private MapView mapView;
    private EarlyInfo travrt;
    private AMap aMap;
    private LinearLayout ll_location,ll_opl;


    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_location_map);
        mapView = getView(R.id.mMapView);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        BaseApplication.activityTaskManager.putActivity("LocationTravelActivity", this);
    }


    @Override
    protected void initView() {
        ll_opl= getView(R.id.ll_opl);
        ll_location= getView(R.id.ll_location);
        text_mileage = getView(R.id.text_mileage);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_text_tv.setText("预警位置");
        aMap = mapView.getMap();
        aMap.setInfoWindowAdapter(this);
        text_mileage.setOnClickListener(this);
        ll_location.setVisibility(View.GONE);
        ll_opl.setVisibility(View.GONE);
    }


    @Override
    protected void initData() {
        travrt = (EarlyInfo) getIntent().getSerializableExtra("info");
        if (travrt != null&&!Utility.isEmpty(travrt.getLat())) {
            LatLng latLng = SystemTools.getLatLng(Double.parseDouble(travrt.getLat()), Double.parseDouble(travrt.getLng()));
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.im_device_loc)));
            markerOption.position(latLng);
            Marker marker = aMap.addMarker(markerOption);
            marker.showInfoWindow();
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(
                R.layout.custom_info_contents, null);
        render(marker, infoWindow);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View infoContent = getLayoutInflater().inflate(
                R.layout.custom_info_contents, null);
        render(marker, infoContent);
        return infoContent;
    }


    public void render(Marker marker, View view) {
        TextView text_name = view.findViewById(R.id.text_name);
        TextView text_date = view.findViewById(R.id.text_date);
        TextView text_adress = view.findViewById(R.id.text_adress);
        if (travrt != null) {
            text_name.setText("车牌号码：" + travrt.getCarcard());
            String ss = travrt.getStringgpstime();
            if (!Utility.isEmpty(ss)) {
                String start = ss.substring(0, ss.length() - 8);
                String end = ss.substring(ss.length() - 8, ss.length());
                text_date.setText("定位时间：" + start + " " + end);
            }
            text_adress.setText("车辆位置：" + travrt.getAddress());
            marker.showInfoWindow();
        }
    }

}
