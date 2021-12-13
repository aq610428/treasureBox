package com.treasure.million.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.Typeitems;
import com.treasure.million.bean.Usdinfo;
import com.treasure.million.bean.UsdBean;
import com.treasure.million.util.BigDecimalUtils;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.LogUtils;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.R;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.treasure.million.weight.DialogUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/7/21
 * @name:充币
 */
public class DrawActivity extends BaseActivity implements NetWorkListener {
    private TextView title_text_tv, title_left_btn, text_copy, text_dig, text_service, text_num_box, text_service_box, text_user;
    private UsdBean usdtBean;
    private String coinTypeId = "2";
    private EditText text_address, et_num, et_password;
    private List<Typeitems> typeitems = new ArrayList<>();
    private List<Usdinfo> usdinfos = new ArrayList<>();
    private ImageView iv_code;
    final Handler mHandler = new Handler();
    private TextView text_trc, text_erc;
    private int stats = 1;


    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_draw);
        DialogUtils.showBoxMsg(this);
    }


    @Override
    protected void initView() {
        text_erc = getView(R.id.text_erc);
        text_trc = getView(R.id.text_trc);
        iv_code = getView(R.id.iv_code);
        et_password = getView(R.id.et_password);
        text_user = getView(R.id.text_user);
        text_num_box = getView(R.id.text_num_box);
        text_service_box = getView(R.id.text_service_box);
        text_address = getView(R.id.text_address);
        et_num = getView(R.id.et_num);
        text_service = getView(R.id.text_service);
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
        coinTypeId = getIntent().getStringExtra("coinTypeId");
        usdtBean = (UsdBean) getIntent().getSerializableExtra("usdtBean");
        if (usdtBean != null) {
            if (coinTypeId.equals(usdtBean.getUsdt().getCoinTypeId())) {
                text_user.setText("可用" + usdtBean.getUsdt().getUserable());
                text_dig.setText(usdtBean.getUsdt().getCoinTypeName());
                text_num_box.setText(usdtBean.getUsdt().getCoinTypeName());
            } else if (coinTypeId.equals(usdtBean.getEth().getCoinTypeId())) {
                text_user.setText("可用" + usdtBean.getEth().getUserable());
                text_dig.setText(usdtBean.getEth().getCoinTypeName());
                text_num_box.setText(usdtBean.getEth().getCoinTypeName());
            }
        }
    }

    @Override
    protected void initData() {
        mHandler.removeCallbacks(runnable);
        mHandler.postDelayed(runnable, 100);
    }


    /*******1分钟定位一次*****/
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            query();
            mHandler.postDelayed(this, 60 * 1000);
        }
    };


    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(runnable);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_btn:
                finish();
                break;
            case R.id.text_dig:
                if (stats == 1) {
                    showUsdt();
                }
                break;
            case R.id.text_copy:
                save();
                break;
            case R.id.iv_code:
                checkPermission();
                break;
            case R.id.text_trc:
                coinTypeId = "TRCUSDT";
                stats = 0;
                text_dig.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                text_trc.setTextColor(Color.parseColor("#3F63F4"));
                text_trc.setBackgroundResource(R.drawable.erc_shape);

                text_erc.setTextColor(Color.parseColor("#333333"));
                text_erc.setBackgroundResource(R.drawable.trc_shape);

                text_user.setText("可用" + usdtBean.getUsdt().getUserable());
                text_dig.setText(usdtBean.getUsdt().getCoinTypeName());
                text_num_box.setText(usdtBean.getUsdt().getCoinTypeName());
                update();
                break;
            case R.id.text_erc:
                stats = 1;
                coinTypeId = "USDT";
                text_dig.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_right_arrow, 0);
                text_erc.setTextColor(Color.parseColor("#3F63F4"));
                text_erc.setBackgroundResource(R.drawable.erc_shape);

                text_trc.setTextColor(Color.parseColor("#333333"));
                text_trc.setBackgroundResource(R.drawable.trc_shape);
                text_user.setText("可用" + usdtBean.getUsdt().getUserable());
                text_dig.setText(usdtBean.getUsdt().getCoinTypeName());
                text_num_box.setText(usdtBean.getUsdt().getCoinTypeName());
                update();
                break;
        }
    }




    private void checkPermission() {
        PermissionX.init(this).permissions(Manifest.permission.CAMERA).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                if (allGranted) {
                    Intent intent = new Intent(DrawActivity.this, CaptureActivity.class);
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
        if (num < minBalance) {
            ToastUtil.showToast("提币数量不能小于" + minBalance);
            return;
        }
        if (num > maxBalance) {
            ToastUtil.showToast("提币数量不能大于" + maxBalance);
            return;
        }
        if (num > userable) {
            ToastUtil.showToast("余额不足");
            return;
        }
        if ("USDT".equals(coinTypeId)||"TRCUSDT".equals(coinTypeId)) {
            double count = num % 100;
            if (count != 0) {
                ToastUtil.showToast("提币数量必须是100的倍数");
                return;
            }
        }
        if ("ETH".equals(coinTypeId)) {
            double count = num % 0.1;
            if (count != 0) {
                ToastUtil.showToast("提币数量必须是0.1的倍数");
                return;
            }
        }

        if (Utility.isEmpty(password)) {
            ToastUtil.showToast("支付密码不能为空");
            return;
        }
        String sign = "address=" + address + "&balance=" + balance + "&coinTypeId=" + coinTypeId + "&fee=" + fee + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1
                + "&paypassword=" + Md5Util.encode(password)
                + Constants.SECREKEY1;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("address", address);
        params.put("balance", balance);
        params.put("coinTypeId", coinTypeId);
        params.put("fee", fee);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("paypassword", Md5Util.encode(password));
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.post(Api.MINING_WITH_BOX, params, Api.MINING_WITH_BOX_ID, this);
    }


    public void query() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.MINING_DDL_BOX, params, Api.MINING_DDL_BOX_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.MINING_WITH_BOX_ID:
                        et_num.setText("");
                        text_address.setText("");
                        ToastUtil.showToast(commonality.getErrorDesc() + "");
                        finish();
                        break;
                    case Api.MINING_DDL_BOX_ID:
                        typeitems = JsonParse.getATypeitems(object);
                        usdinfos = JsonParse.getATypeitemsUsdt(object);
                        if (typeitems != null && typeitems.size() > 0) {
                            update();
                        }
                        if (usdinfos != null && usdinfos.size() > 0) {
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

    /*****余额*****/
    double userable, serviceFee;
    double minBalance, maxBalance;
    Usdinfo usdinfo;

    private void updateView() {
        for (int i = 0; i < usdinfos.size(); i++) {
            Usdinfo bean = usdinfos.get(i);
            String type=coinTypeId;
            if ("TRCUSDT".equals(type)){
                type="USDT";
            }
            if (type.equals(bean.getCoinTypeId())) {
                userable = bean.getUserable();
                if (1 == bean.getIsLock()) {
                    DialogUtils.showPassword1(this);
                    text_copy.setVisibility(View.GONE);
                } else {
                    text_copy.setVisibility(View.VISIBLE);
                }
            }

            if ("ETH".equals(bean.getCoinTypeId())) {
                usdinfo = usdinfos.get(i);
            }
        }
    }

    private void update() {
        for (int i = 0; i < typeitems.size(); i++) {
            Typeitems bean = typeitems.get(i);
            String id=coinTypeId;
            if ("TRCUSDT".equals(id)){
                id="USDT";
            }
            if (id.equals(bean.getCoinTypeId())) {
                serviceFee = bean.getServiceFee();
                minBalance = bean.getMinBalance();
                maxBalance = bean.getMaxBalance();
                text_service.setText(BigDecimalUtils.round(new BigDecimal(bean.getServiceFee()), 6) + "");
                et_num.setHint("最小提币数" + minBalance);
            }
        }
    }

    public void showUsdt() {
        Dialog dialog = new Dialog(this, R.style.dialog_bottom_full);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_map, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.share_animation);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        dialog.setContentView(view);
        view.findViewById(R.id.text_usd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usdtBean != null && usdtBean.getUsdt() != null) {
                    coinTypeId = usdtBean.getUsdt().getCoinTypeId();
                    text_dig.setText(usdtBean.getUsdt().getCoinTypeName());
                    text_num_box.setText(usdtBean.getUsdt().getCoinTypeName());
                    text_user.setText("可用" + usdtBean.getUsdt().getUserable() + "");
                    if (1 == usdtBean.getUsdt().getIsLock()) {
                        DialogUtils.showPassword1(DrawActivity.this);
                        text_copy.setVisibility(View.GONE);
                    } else {
                        text_copy.setVisibility(View.VISIBLE);
                    }
                    update();
                }
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.text_bc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usdtBean != null && usdtBean.getBox() != null) {
                    coinTypeId = usdtBean.getBox().getCoinTypeId();
                    text_dig.setText(usdtBean.getBox().getCoinTypeName());
                    text_num_box.setText(usdtBean.getBox().getCoinTypeName());
                    text_user.setText("可用" + usdtBean.getBox().getUserable() + "");
                    if (1 == usdtBean.getBox().getIsLock()) {
                        DialogUtils.showPassword1(DrawActivity.this);
                        text_copy.setVisibility(View.GONE);
                    } else {
                        text_copy.setVisibility(View.VISIBLE);
                    }
                    update();
                }
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.text_eth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usdinfo != null) {
                    coinTypeId = usdinfo.getCoinTypeId();
                    text_dig.setText(usdinfo.getCoinTypeName());
                    text_num_box.setText(usdinfo.getCoinTypeName());
                    text_user.setText("可用" + usdinfo.getUserable() + "");
                    update();
                }
                dialog.dismiss();
            }
        });


        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
