package com.treasure.million.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.treasure.million.R;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.bean.Oil;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.util.BigDecimalUtils;
import com.treasure.million.util.Constants;
import com.treasure.million.util.DateUtils;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import com.treasure.million.weight.CustomXAxisRenderer;
import com.treasure.million.weight.PreferenceUtils;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


/**
 * @author: zt
 * @date: 2020/7/14
 * @name:OilActivity
 */
public class ChatOilActivity extends BaseActivity implements NetWorkListener {
    private TextView text_date, text_develop, text_tel, text_cost, text_mean, text_week;
    private TextView title_text_tv, title_left_btn;
    private BarChart mBarChart;
    private String starttime;
    private String endtime;
    private Oil oil;
    private EditText text_price;


    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_oil);
    }

    @Override
    protected void initView() {
        text_price = getView(R.id.text_price);
        text_week = getView(R.id.text_week);
        mBarChart = getView(R.id.bar_chart);
        text_date = getView(R.id.text_date);
        text_develop = getView(R.id.text_develop);
        text_tel = getView(R.id.text_tel);
        text_cost = getView(R.id.text_cost);
        text_mean = getView(R.id.text_mean);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        text_week.setOnClickListener(this);
        title_text_tv.setText("油耗分析");
        endtime = DateUtils.DateToStr1(DateUtils.getAddDay(new Date(), -1));
        starttime = DateUtils.DateToStr1(DateUtils.getAddDay(new Date(), -7));
        String oil = PreferenceUtils.getPrefString(this, Constants.OIL, "");
        if (!Utility.isEmpty(oil)) {
            text_price.setText(oil + "元");
        }
        text_date.setText(starttime + "至" + endtime);
        text_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String price = text_price.getText().toString();
                if (!Utility.isEmpty(price)) {
                    price = price.replaceAll("元", "");
                    PreferenceUtils.setPrefString(ChatOilActivity.this, Constants.OIL, price);
                    if (oil != null) {
                        updateView();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        qury();
    }


    @Override
    protected void initData() {

    }


    private void qury() {
        String sign = "endtime=" + endtime + "&imeicode=" + SaveUtils.getCar().getImeicode() + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID + "&starttime=" + starttime + Constants.SECREKEY;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("endtime", endtime + "");
        params.put("imeicode", SaveUtils.getCar().getImeicode() + "");
        params.put("memberid", SaveUtils.getSaveInfo().getId() + "");
        params.put("partnerid", Constants.PARTNERID);
        params.put("starttime", starttime + "");
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_OIL_DEVICE, params, Api.GET_OIL_DEVICE_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.GET_OIL_DEVICE_ID:
                        oil = JsonParse.getOilJson(object);
                        if (oil != null) {
                            updateView();
                        }
                        break;
                }
            } else {
                ToastUtil.showToast(commonality.getErrorDesc());
            }
        }
        stopProgressDialog();
    }

    private void updateView() {
        text_develop.setText(oil.getMileage() + "公里");
        text_mean.setText(oil.getAvgoil());
        String price = PreferenceUtils.getPrefString(ChatOilActivity.this, Constants.OIL, "").replaceAll("元", "");
        if (!Utility.isEmpty(price)) {
            if ("0".equals(oil.getOils() + "")) {
                text_tel.setText("0元");
                text_cost.setText("0元/公里");
            } else {
                if (!Utility.isEmpty(oil.getOils())){
                    String spend = BigDecimalUtils.mul(new BigDecimal(oil.getOils()), new BigDecimal(price.replaceAll("元", "'"))).toPlainString();
                    text_tel.setText(spend + "元");
                    BigDecimal spen = BigDecimalUtils.div(new BigDecimal(oil.getOils()), new BigDecimal(oil.getMileage()+""), 2);
                    text_cost.setText(BigDecimalUtils.mul(spen, new BigDecimal(price)).toPlainString() + "元/公里");
                }
            }
        }

        ArrayList<BarEntry> yValues = new ArrayList<>();
        ArrayList<BarEntry> yValues2 = new ArrayList<>();
        yValues.add(new BarEntry(0, Float.valueOf(oil.getIdling())));
        yValues2.add(new BarEntry(0, (float) oil.getPkidling()));

        yValues.add(new BarEntry(1, Float.valueOf(oil.getAcce())));

        yValues2.add(new BarEntry(1, (float) oil.getPkacce()));

        yValues.add(new BarEntry(2, Float.valueOf(oil.getDece())));

        yValues2.add(new BarEntry(2, (float) oil.getPkdece()));

        yValues.add(new BarEntry(3, Float.valueOf(oil.getSpeedavg())));

        yValues2.add(new BarEntry(3, oil.getSpeedavg()));

        // y 轴数据集
        BarDataSet barDataSet = new BarDataSet(yValues, "我的水平");
        barDataSet.setBarShadowColor(R.color.blue03);
        // y2 轴数据集
        BarDataSet barDataSet2 = new BarDataSet(yValues2, "同城车的水平");
        barDataSet2.setColor(R.color.blue03);

        BarData mBarData = new BarData(barDataSet, barDataSet2);
        //设置动画效果
        mBarChart.animateY(1000, Easing.Linear);
        mBarChart.animateX(1000, Easing.Linear);
        //不显示柱状图顶部值
        barDataSet.setDrawValues(true);
        barDataSet2.setDrawValues(true);

        float groupSpace = 0.3f;
        float barSpace = 0.05f;
        float barWidth = 0.3f;

        // 设置 柱子宽度
        mBarChart.setData(mBarData);
        mBarData.setBarWidth(barWidth);
        mBarChart.groupBars(0f, groupSpace, barSpace);
        Description des = new Description();
        des.setText("");
        mBarChart.setDescription(des);
        mBarChart.setDrawGridBackground(true);


        // 获取 x 轴
        XAxis xAxis = mBarChart.getXAxis();
        // 设置 x 轴显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 取消 垂直 网格线
        xAxis.setDrawGridLines(true);
        xAxis.setLabelCount(4, false);

        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(4f);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        ValueFormatter valueFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0.0) {
                    return "怠速" + "\n" + "(%)";
                }
                if (value == 1.0) {
                    return "急踩油门次" + "\n" + "(十公里)";
                }
                if (value == 2.0) {
                    return "急踩刹车次" + "\n" + "(十公里)";
                }
                if (value == 3.0) {
                    return "平均车速" + "\n" + "(小时)";
                }
                return super.getFormattedValue(value);
            }
        };
        xAxis.setValueFormatter(valueFormatter);


        mBarChart.setExtraBottomOffset(2 * 9f);
        xAxis.setTextSize(10);

        YAxis rLefteft = mBarChart.getAxisRight();
        rLefteft.setEnabled(false);
        rLefteft.setDrawGridLines(true);

        YAxis lLefteft = mBarChart.getAxisLeft();
        lLefteft.setLabelCount(4, false);
        lLefteft.setAxisMinimum(0f);
        lLefteft.setGranularity(30f);

        mBarChart.setXAxisRenderer(new CustomXAxisRenderer(mBarChart.getViewPortHandler(), mBarChart.getXAxis(),
                mBarChart.getTransformer(YAxis.AxisDependency.LEFT)));

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.text_week:
                initTime();
                break;
            case R.id.title_left_btn:
                finish();
                break;
        }
    }


    protected void initTime() {
        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout_live, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        TextView text_day = view.findViewById(R.id.text_day);
        TextView text_week_ = view.findViewById(R.id.text_week);
        text_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endtime = DateUtils.DateToStr1(DateUtils.getAddDay(new Date(), -1));
                starttime = DateUtils.DateToStr1(DateUtils.getAddDay(new Date(), -1));
                text_date.setText(starttime);
                text_week.setText(text_day.getText().toString());
                qury();
                dialog.dismiss();
            }
        });
        text_week_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endtime = DateUtils.DateToStr1(DateUtils.getAddDay(new Date(), -1));
                starttime = DateUtils.DateToStr1(DateUtils.getAddDay(new Date(), -7));
                text_date.setText(starttime + "至" + starttime);
                text_week.setText(text_week_.getText().toString());
                qury();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onFail() {
        stopProgressDialog();
    }

    @Override
    public void onError(Exception e) {
        stopProgressDialog();
    }


}
