package com.treasure.million.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.treasure.million.R;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.AssetsBean;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.bean.UsdBean;
import com.treasure.million.config.Api;
import com.treasure.million.config.Config;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import org.json.JSONObject;
import java.util.Map;

/**
 * @ClassName: SwitchActivity
 * @Description: java类作用描述
 * @Author: zt
 * @Date: 2021/6/11 15:07
 */
public class SwitchActivity extends BaseActivity implements NetWorkListener {
    private TextView title_text_tv, title_left_btn, text_usdt, text_eth, text_copy, text_balance;
    private ImageView iv_switch;
    private EditText et_num, et_password;
    private UsdBean usdBean;
    private String coinTypeId;
    private int limit = 10;
    private int page = 1;

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_switch);
    }

    @Override
    protected void initView() {
        text_balance = getView(R.id.text_balance);
        text_usdt = getView(R.id.text_usdt);
        text_eth = getView(R.id.text_eth);
        text_copy = getView(R.id.text_copy);
        iv_switch = getView(R.id.iv_switch);
        et_num = getView(R.id.et_num);
        et_password = getView(R.id.et_password);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        iv_switch.setOnClickListener(this);
        text_copy.setOnClickListener(this);
        title_text_tv.setText("兑换");
    }

    @Override
    protected void initData() {
        coinTypeId = getIntent().getStringExtra("coinTypeId");
        load();
    }


    public void query() {
        String pass = et_password.getText().toString();
        String num = et_num.getText().toString();
        if (Utility.isEmpty(pass)) {
            ToastUtil.showToast("交易密码不能为空");
            return;
        }
        if (Utility.isEmpty(num)) {
            ToastUtil.showToast("兑换金额不能为空");
            return;
        }

        double userable;
        if ("USDT".equals(coinTypeId)) {
            userable = usdBean.getUsdt().getUserable();
        } else {
            userable = usdBean.getEth().getUserable();
        }

        double count = Double.parseDouble(num);
        if (count < 0) {
            ToastUtil.showToast("兑换金额不合法");
            return;
        }

        if (count > userable) {
            ToastUtil.showToast("余额不足");
            return;
        }
        String sign = "balance=" + num + "&coinTypeId=" + coinTypeId + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1
                + "&paypassword=" +  Md5Util.encode(pass)
                + Constants.SECREKEY1;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("balance", num);
        params.put("coinTypeId", coinTypeId + "");
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("paypassword", Md5Util.encode(pass));
        params.put("apptype", Constants.TYPE);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.post(Api.REDE_MEMBER_USDT, params, Api.REDE_MEMBER_USDT_ID, this);
    }


    public void load() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("limit", limit + "");
        params.put("page", page + "");
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.MINING_BAL_BOX, params, Api.PAY_REMOVE_CHANCE_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.REDE_MEMBER_USDT_ID:
                        ToastUtil.showToast(commonality.getErrorDesc());
                        load();
                        break;
                    case Api.PAY_REMOVE_CHANCE_ID:
                        usdBean = JsonParse.getJSONObjectUsdtBean(object);
                        if (usdBean != null) {
                            updateView();
                        }
                        break;
                }
            } else {
                ToastUtil.showToast(commonality.getErrorDesc());
            }
        } else {
            ToastUtil.showToast(commonality.getErrorDesc());
        }
        stopProgressDialog();

    }

    private void updateView() {
        if (!Utility.isEmpty(coinTypeId)) {
            if ("USDT".equals(coinTypeId)) {
                text_balance.setText("最多可转" + usdBean.getUsdt().getUserable() + usdBean.getUsdt().getCoinTypeName().toLowerCase() + "");
            } else {
                text_balance.setText("最多可转" + usdBean.getEth().getUserable() + usdBean.getEth().getCoinTypeName().toLowerCase());
            }
        }
    }


    private int isSwitch = 1;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_btn:
                finish();
                break;
            case R.id.iv_switch:
                if (isSwitch == 1) {
                    text_balance.setText("最多可转" + usdBean.getEth().getUserable() + usdBean.getEth().getCoinTypeName().toLowerCase());
                    isSwitch = 2;
                    coinTypeId = usdBean.getEth().getCoinTypeId();
                    text_usdt.setText("ETH");
                    text_eth.setText("USDT");
                } else {
                    text_balance.setText("最多可转" + usdBean.getUsdt().getUserable() + usdBean.getUsdt().getCoinTypeName().toLowerCase() + "");
                    isSwitch = 1;
                    coinTypeId = usdBean.getUsdt().getCoinTypeId();
                    text_usdt.setText("USDT");
                    text_eth.setText("ETH");
                }
                break;
            case R.id.text_copy:
                query();
                break;
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
