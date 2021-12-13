package com.treasure.million.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.treasure.million.R;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.util.Constants;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import org.json.JSONObject;
import java.util.Map;

/**
 * @ClassName: RedeemActivity
 * @Description: 赎回
 * @Author: zt
 * @Date: 2021/10/28 16:13
 */
public class RedeemActivity extends BaseActivity implements NetWorkListener {
    private TextView title_text_tv, title_left_btn;
    private String id;
    private TextView text_machine, text_eth, text_balance, text_day, text_produce, text_quota;
    private TextView btn_next;

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_redeem);
    }

    @Override
    protected void initView() {
        btn_next = getView(R.id.btn_next);
        text_machine = getView(R.id.text_machine);
        text_eth = getView(R.id.text_eth);
        text_balance = getView(R.id.text_balance);
        text_day = getView(R.id.text_day);
        text_produce = getView(R.id.text_produce);
        text_quota = getView(R.id.text_quota);

        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        title_text_tv.setText("赎回");
    }

    @Override
    protected void initData() {
        id = getIntent().getStringExtra("id");
        query();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_btn:
                finish();
                break;
            case R.id.btn_next:

                break;
        }
    }

    private void query() {
        String sign = "id=" + id + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("memberid", SaveUtils.getSaveInfo().getId() + "");
        params.put("id", id + "");
        params.put("partnerid", Constants.PARTNERID1);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.post(Api.REDE_LIST_DET, params, Api.REDE_LIST_DETY_ID, this);
    }




    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.REDE_LIST_DETY_ID:
                        updateView(object);
                        break;
                    case Api.REDE_LIST_FINANCE1_ID:
                        ToastUtil.showToast(commonality.getErrorDesc());
                        finish();
                        break;
                }
            }else{
                ToastUtil.showToast(commonality.getErrorDesc());
            }
        }else{
            ToastUtil.showToast(commonality.getErrorDesc());
        }
        stopProgressDialog();
    }

    private void updateView(JSONObject object) {
        JSONObject jsonObject = object.optJSONObject("result");
        if (jsonObject != null) {
            String totalProfit = jsonObject.optString("totalProfit");
            String balance = jsonObject.optString("balance");
            String days = jsonObject.optString("days");
            String power = jsonObject.optString("power");
            String name = getIntent().getStringExtra("name");
            text_eth.setText(power + "算力");
            text_balance.setText("ETH");
            text_quota.setText(balance + "ETH");
            text_day.setText(days + "天");
            text_machine.setText(power + "算力");
            text_produce.setText(totalProfit + "ET");
            text_machine.setText(name);
        }
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
