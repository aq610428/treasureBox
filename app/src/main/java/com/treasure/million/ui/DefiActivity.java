package com.treasure.million.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.TabBean;
import com.treasure.million.bean.UsdBean;
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
import com.treasure.million.weight.ClearEditText;

import org.json.JSONObject;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/10/22
 * @name:DefiActivity
 */
public class DefiActivity extends BaseActivity implements NetWorkListener {
    private TextView title_text_tv, title_left_btn, text_ensure, title_right_btn, text_name, text_box;
    private ClearEditText et_num;
    private TabBean tabBean;
    private TextView text_user, text_deif, text_pay;
    private String type;
    private UsdBean usdtBean;


    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_defi);
    }

    @Override
    protected void initView() {
        text_box = getView(R.id.text_box);
        text_name = getView(R.id.text_name);
        text_pay = getView(R.id.text_pay);
        text_deif = getView(R.id.text_deif);
        text_user = getView(R.id.text_user);
        title_right_btn = getView(R.id.title_right_btn);
        et_num = getView(R.id.et_num);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        text_ensure = getView(R.id.text_ensure);
        title_left_btn.setOnClickListener(this);
        text_ensure.setOnClickListener(this);
        title_text_tv.setText("??????");
        title_right_btn.setText("????????????");
        title_right_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_btn:
                finish();
                break;
            case R.id.title_right_btn:
                startActivity(new Intent(this, TabDefiActivity.class));
                break;
            case R.id.text_ensure:
                query();
                break;
        }
    }

    @Override
    protected void initData() {
        String mouth = getIntent().getStringExtra("mouth");
        String lv = getIntent().getStringExtra("lv");
        type = getIntent().getStringExtra("type");
        tabBean = (TabBean) getIntent().getSerializableExtra("tabBean");
        if (tabBean != null) {
            text_user.setText(tabBean.getManage_amount() + "");
            text_deif.setText(tabBean.getManage_num() + "");
            text_pay.setText(tabBean.getProfit_amount() + "");
            String str = "????????????BOXDefi????????????" + mouth + "??????????????????????????????????????????" + mouth +
                    "\n?????????????????????" + lv + "\nBOX DeFi????????????500BOX?????????100BOX?????????????????????????????????10???BOX?????????" +
                    "\n???????????????????????????????????????????????????????????????????????????\n?????????????????????????????????????????? \n??????????????????????????????????????????????????????????????????????????????+???????????????\n???????????????????????????????????????????????????????????????????????????????????????";
            text_name.setText(str);
        }
        load();
    }


    public void query() {
        String amount = et_num.getText().toString();
        if (Utility.isEmpty(amount)) {
            ToastUtil.showToast("????????????????????????");
            return;
        }

        BigDecimal decimal=new BigDecimal(amount);

        if (decimal.doubleValue()%100!=0) {
            ToastUtil.showToast("?????????????????????100BOX????????????");
            return;
        }

        if (decimal.doubleValue() < 500) {
            ToastUtil.showToast("????????????????????????500");
            return;
        }

        String sign = "amount=" + amount + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + "&type=" + type + Constants.SECREKEY1;
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("amount", amount + "");
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("type", type + "");
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.post(Api.LIST_MEMBER_BOX, params, Api.LIST_MEMBER_BOX_ID, this);
    }


    public void load() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("limit", "10");
        params.put("page", "1");
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.MINING_BAL_BOX, params, Api.GETRECORD_BOX_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.LIST_MEMBER_BOX_ID:
                        ToastUtil.showToast(commonality.getErrorDesc());
                        startActivity(new Intent(this, TabActivity.class));
                        finish();
                        break;
                    case Api.GETRECORD_BOX_ID:
                        usdtBean = JsonParse.getJSONObjectUsdtBean(object);
                        if (usdtBean != null) {
                            updateView(usdtBean);
                        }
                        break;
                }
            } else {
                ToastUtil.showToast(commonality.getErrorDesc());
            }
        }
        stopProgressDialog();
    }

    private void updateView(UsdBean usdtBean) {
        text_box.setText("???????????????" + usdtBean.getBox().getUserable() + "BOX");
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
