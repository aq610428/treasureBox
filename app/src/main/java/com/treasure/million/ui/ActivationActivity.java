package com.treasure.million.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.UsdBean;
import com.treasure.million.bean.UserInfo;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.TypefaceUtil;
import com.treasure.million.util.Utility;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.R;
import org.json.JSONObject;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/7/21
 * @name:ActivationActivity
 */
public class ActivationActivity extends BaseActivity implements NetWorkListener {
    private TextView title_text_tv, title_left_btn, text_key, text_usdt_charge;
    private TextView text_usd, text_name, text_about, text_title, text_extension;
    private LinearLayout ll_usdt, ll_extension;
    private int type = 1;
    private UsdBean usdtBean;
    private int limit = 10;
    private int page = 1;


    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_activation);
    }

    @Override
    protected void initView() {
        text_usdt_charge = getView(R.id.text_usdt_charge);
        text_title = getView(R.id.text_title);
        text_extension = getView(R.id.text_extension);
        text_about = getView(R.id.text_about);
        text_name = getView(R.id.text_name);
        ll_usdt = getView(R.id.ll_usdt);
        ll_extension = getView(R.id.ll_extension);

        text_usd = getView(R.id.text_usd);
        text_key = getView(R.id.text_key);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        text_key.setOnClickListener(this);
        ll_usdt.setOnClickListener(this);
        ll_extension.setOnClickListener(this);
        text_usdt_charge.setOnClickListener(this);

        title_text_tv.setText("????????????");
        TypefaceUtil tfUtil = new TypefaceUtil(this, "OpenSans-Light.ttf");
        tfUtil.setTypeface(text_usd, false);
        query();
        load();
    }


    public void query() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.PAY_REMOVE_ACTIVE, params, Api.PAY_REMOVE_ACTIVE_ID, this);
    }


    /*****??????*****/
    public void activation() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_ACTIVE_BOX, params, Api.GET_ACTIVE_BOX_ID, this);
    }


    /*****????????????*****/
    public void activation1() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.PAY_REMOVE_CHANCE, params, Api.PAY_REMOVE_CHANCE_ID, this);
    }


    /******??????????????????*****/
    public void queryUser() {
        showProgressDialog(this, false);
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID);
        params.put("apptype", Constants.TYPE);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_MEID_USER, params, Api.GET_MEID_USER_ID, this);
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
        okHttpModel.get(Api.MINING_BAL_BOX, params, Api.GETRECORD_BOX_ID, this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_btn:
                finish();
                break;
            case R.id.text_usdt_charge:
                if (usdtBean != null && usdtBean.getUsdt() != null) {
                    Intent intent = new Intent(this, RechargeActivity.class);
                    intent.putExtra("usdtBean", usdtBean);
                    intent.putExtra("coinTypeId", usdtBean.getUsdt().getCoinTypeId());
                    startActivity(intent);
                }
                break;

            case R.id.ll_extension:
                ll_usdt.setBackgroundResource(R.drawable.usdt_bg_shape);
                ll_extension.setBackground(getResources().getDrawable(R.mipmap.icon_bg_bc));

                text_name.setTextColor(Color.parseColor("#3F63F4"));
                text_usd.setTextColor(Color.parseColor("#8BA2FF"));
                text_about.setTextColor(Color.parseColor("#8BA2FF"));

                text_title.setTextColor(Color.parseColor("#ffffff"));
                text_extension.setTextColor(Color.parseColor("#A5B7FF"));
                type = 2;
                break;
            case R.id.ll_usdt:
                type = 1;
                text_name.setTextColor(Color.parseColor("#ffffff"));
                text_usd.setTextColor(Color.parseColor("#A5B7FF"));
                text_about.setTextColor(Color.parseColor("#A5B7FF"));

                text_title.setTextColor(Color.parseColor("#3F63F4"));
                text_extension.setTextColor(Color.parseColor("#8BA2FF"));

                ll_usdt.setBackground(getResources().getDrawable(R.mipmap.icon_bg_bc));
                ll_extension.setBackgroundResource(R.drawable.usdt_bg_shape);
                break;
            case R.id.text_key:
                if (type == 1) {
                    activation();
                } else {
                    activation1();
                }
                break;
        }
    }


    @Override
    protected void initData() {
    }

    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.PAY_REMOVE_CHANCE_ID:
                    case Api.GET_ACTIVE_BOX_ID:
                        ToastUtil.showToast(commonality.getErrorDesc());
                        queryUser();
                        break;
                    case Api.PAY_REMOVE_ACTIVE_ID:
                        updateView(object);
                        break;
                    case Api.GET_MEID_USER_ID:
                        UserInfo info = JsonParse.getUserInfo(object);
                        if (info != null) {
                            SaveUtils.saveInfo(info);
                        }
                        finish();
                        break;
                    case Api.GETRECORD_BOX_ID:
                        usdtBean = JsonParse.getJSONObjectUsdtBean(object);
                        break;

                }
            } else {
                ToastUtil.showToast(commonality.getErrorDesc());
            }
        }
        stopProgressDialog();
    }

    double useableActiveNum, miningAmount, useableBalance;

    private void updateView(JSONObject jsonObject) {
        JSONObject jsonObject1 = jsonObject.optJSONObject("result");
        useableBalance = jsonObject1.optDouble("useableBalance");
        useableActiveNum = jsonObject1.optDouble("useableActiveNum");
        miningAmount = jsonObject1.optDouble("miningAmount");
        text_usd.setText("??????USDT:" + useableBalance + "");
        text_about.setText("????????????????????????USDT?????????" + miningAmount + " \n????????????????????????????????????");
        text_extension.setText("?????????????????????" + useableActiveNum);
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
