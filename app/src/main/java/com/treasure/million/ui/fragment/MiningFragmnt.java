package com.treasure.million.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.treasure.million.R;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.ui.ActivationActivity;
import com.treasure.million.ui.IncomeActivity;
import com.treasure.million.util.Constants;
import com.treasure.million.util.DateUtils;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.Utility;
import com.treasure.million.weight.DialogUtils;

import org.json.JSONObject;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/7/21
 * @name:挖矿
 */
public class MiningFragmnt extends BaseActivity implements NetWorkListener, View.OnClickListener, OnRefreshListener {
    private TextView text_right, text_cny, text_travel, text_work, text_dig, text_bind;
    private SwipeToLoadLayout swipeToLoadLayout;
    private TextView title_text_tv, title_left_btn;


    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_mining);
    }

    public void initView() {
        text_bind = getView(R.id.text_bind);
        swipeToLoadLayout = getView(R.id.swipeToLoadLayout);
        text_cny = getView(R.id.text_cny);
        text_right = getView(R.id.text_right);
        text_cny = getView(R.id.text_cny);
        text_travel = getView(R.id.text_travel);
        text_work = getView(R.id.text_work);
        text_dig = getView(R.id.text_dig);
        text_right.setOnClickListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        text_bind.setOnClickListener(this);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_text_tv.setText("挖矿");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
        query();
    }


    public void query() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_UNMODEL_BOX, params, Api.GET_UNMODEL_BOX_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.GET_UNMODEL_BOX_ID:
                        updateJson(object);
                        break;

                }
            }
        }
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
    }

    private void updateJson(JSONObject object) {
        JSONObject jsonObject = object.optJSONObject("result");
        if (jsonObject != null) {
            text_travel.setText(jsonObject.optString("yestodayBox") + " BOX");
            text_work.setText(jsonObject.optString("activeCount") + " 个");
            text_dig.setText(jsonObject.optString("miningBox") + " BOX");
            text_cny.setText(jsonObject.optString("totalBox") + "");
            String stat = jsonObject.optString("state");
            String miningDate = jsonObject.optString("miningDate");
            if ("1".equals(stat + "") || "3".equals(stat)) {
                text_bind.setVisibility(View.GONE);
            } else if ("2".equals(stat + "") && !Utility.isEmpty(miningDate)) {
                String str[] = miningDate.split("T");
                if (str.length == 2) {
                    String date = str[0] + " " + str[1].substring(0, 8);
                    float day = DateUtils.getDayLong(date);
                    if (day <= 7) {
                        DialogUtils.showPay(this);
                    }
                }
            }
        }

    }

    private void updateView() {
        if (SaveUtils.getCar() == null || Utility.isEmpty(SaveUtils.getCar().getId())) {
            DialogUtils.showBind(1, this);
        } else {
            if (!"2".equals(SaveUtils.getSaveInfo().getIsMining() + "")) {
                DialogUtils.showBind(2, this);
            }
        }
    }

    @Override
    public void onFail() {
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onError(Exception e) {
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_right:
                startActivity(new Intent(this, IncomeActivity.class));
                break;
            case R.id.text_bind:
                startActivity(new Intent(this, ActivationActivity.class));
                break;
            case R.id.title_left_btn:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        query();
    }
}
