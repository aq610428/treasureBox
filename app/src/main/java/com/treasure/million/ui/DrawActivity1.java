package com.treasure.million.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.treasure.million.R;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.bean.UsdtInfo;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.util.Constants;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import com.treasure.million.weight.DialogUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import org.json.JSONObject;
import java.util.List;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/7/21
 * @name:充币
 */
public class DrawActivity1 extends BaseActivity implements NetWorkListener {
    private TextView title_text_tv, title_left_btn, text_copy, text_dig, text_service, text_user;
    private UsdtInfo usdtBean;
    private TextView text_trc, text_erc;
    private EditText text_address, et_num, et_password;
    private ImageView iv_code;
    private String coinTypeId = "USDT";

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_draw1);
    }


    @Override
    protected void initView() {
        text_erc = getView(R.id.text_erc);
        text_trc = getView(R.id.text_trc);
        iv_code = getView(R.id.iv_code);
        et_password = getView(R.id.et_password);
        text_user = getView(R.id.text_user);
        text_address = getView(R.id.text_address);
        et_num = getView(R.id.et_num);
        text_service = getView(R.id.text_service1);
        text_dig = getView(R.id.text_dig);
        text_copy = getView(R.id.text_copy);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        text_copy.setOnClickListener(this);
        title_text_tv.setText("提币");
        text_dig.setOnClickListener(this);
        iv_code.setOnClickListener(this);
        text_erc.setOnClickListener(this);
        text_trc.setOnClickListener(this);
        usdtBean = (UsdtInfo) getIntent().getSerializableExtra("usdBean");
        if (usdtBean != null) {
            text_user.setText("可用" + usdtBean.getUserable());
            et_num.setHint("最小提币数" + usdtBean.getMinLimit() + "");
            text_service.setText(usdtBean.getFee() + "");
            if ("1".equals(usdtBean.getIsLock())) {
                DialogUtils.showPassword2(this);
            } else {
                DialogUtils.showBoxMsg(this);
            }
        }
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
            case R.id.text_copy:
                save();
                break;
            case R.id.iv_code:
                checkPermission();
                break;
            case R.id.text_trc:
                coinTypeId="TRCUSDT";
                text_trc.setTextColor(Color.parseColor("#3F63F4"));
                text_trc.setBackgroundResource(R.drawable.erc_shape);
                text_erc.setTextColor(Color.parseColor("#333333"));
                text_erc.setBackgroundResource(R.drawable.trc_shape);
                break;
            case R.id.text_erc:
                coinTypeId="USDT";
                text_erc.setTextColor(Color.parseColor("#3F63F4"));
                text_erc.setBackgroundResource(R.drawable.erc_shape);
                text_trc.setTextColor(Color.parseColor("#333333"));
                text_trc.setBackgroundResource(R.drawable.trc_shape);
                break;
        }
    }

    private void checkPermission() {
        PermissionX.init(this).permissions(Manifest.permission.CAMERA).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                if (allGranted) {
                    Intent intent = new Intent(DrawActivity1.this, CaptureActivity.class);
                    startActivityForResult(intent, 101);
                } else {
                    showSettingDialog();
                }
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            String result = data.getExtras().getString(CodeUtils.RESULT_STRING);
            if (!Utility.isEmpty(result)) {
                text_address.setText(result + "");
            }
        }
    }


    public void save() {
        String address = text_address.getText().toString();
        String balance = et_num.getText().toString();
        String password = et_password.getText().toString();
        String fee = text_service.getText().toString().trim();

        if (Utility.isEmpty(address)) {
            ToastUtil.showToast("提币地址不能为空");
            return;
        }
        if (Utility.isEmpty(balance)) {
            ToastUtil.showToast("提币数量不能为空");
            return;
        }
        double num = Double.parseDouble(balance);
        double monery = num + usdtBean.getFee();
        if (num < usdtBean.getMinLimit()) {
            ToastUtil.showToast("提币数量不能小于" + usdtBean.getMinLimit());
            return;
        }

        if (usdtBean.getUserable() < monery) {
            ToastUtil.showToast("余额不足");
            return;
        }
        if (Utility.isEmpty(password)) {
            ToastUtil.showToast("支付密码不能为空");
            return;
        }
        String sign = "address=" + address + "&balance=" + balance+"&coinTypeId"+coinTypeId + "&fee=" + fee + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1
                + "&paypassword=" + Md5Util.encode(password)
                + Constants.SECREKEY1;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("address", address);
        params.put("balance", balance);
        params.put("coinTypeId", coinTypeId);
        params.put("fee", fee);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("paypassword", Md5Util.encode(password));
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.post(Api.REDE_DRAW_RECORD, params, Api.REDE_DRAW_RECORD_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.REDE_DRAW_RECORD_ID:
                        et_num.setText("");
                        text_address.setText("");
                        ToastUtil.showToast(commonality.getErrorDesc() + "");
                        finish();
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


    @Override
    public void onFail() {
        stopProgressDialog();
    }

    @Override
    public void onError(Exception e) {
        stopProgressDialog();
    }
}
