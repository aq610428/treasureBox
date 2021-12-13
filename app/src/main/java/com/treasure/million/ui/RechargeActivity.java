package com.treasure.million.ui;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.AddressInfo;
import com.treasure.million.bean.UsdBean;
import com.treasure.million.util.ClipboardUtils;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.QRCodeUtil;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author: zt
 * @date: 2020/7/21
 * @name:充币
 */
public class RechargeActivity extends BaseActivity implements NetWorkListener {
    private TextView title_text_tv, title_left_btn, text_style, text_copy, text_dig;
    private ImageView iv_code;
    private UsdBean usdtBean;
    private List<AddressInfo> infos = new ArrayList<>();
    private String coinTypeId = "2";
    private TextView text_trc, text_erc;
    private int stats = 1;


    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_recharge);
    }

    @Override
    protected void initView() {
        text_erc = getView(R.id.text_erc);
        text_trc = getView(R.id.text_trc);
        text_dig = getView(R.id.text_dig);
        text_copy = getView(R.id.text_copy);
        iv_code = getView(R.id.iv_code);
        text_style = getView(R.id.text_style);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_text_tv.setText("充币");
        text_copy.setOnClickListener(this);
        text_dig.setOnClickListener(this);
        iv_code.setOnClickListener(this);
        text_erc.setOnClickListener(this);
        text_trc.setOnClickListener(this);
        usdtBean = (UsdBean) getIntent().getSerializableExtra("usdtBean");
        coinTypeId = getIntent().getStringExtra("coinTypeId");
        if (!Utility.isEmpty(coinTypeId)) {
            if ("USDT".equals(coinTypeId)) {
                text_dig.setText(usdtBean.getUsdt().getCoinTypeName());
            }else{
                text_dig.setText(usdtBean.getEth().getCoinTypeName());
            }
        }
    }

    @Override
    protected void initData() {
        query();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_btn:
                finish();
                break;
            case R.id.text_copy:
                String text = text_style.getText().toString();
                ClipboardUtils.copyText(text);
                ToastUtil.showToast("复制成功");
                break;
            case R.id.text_dig:
                if (stats == 1) {
                    showUsdt();
                }
                break;
            case R.id.text_trc:
                coinTypeId = "TRCUSDT";
                stats = 0;
                text_dig.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                text_trc.setTextColor(Color.parseColor("#3F63F4"));
                text_trc.setBackgroundResource(R.drawable.erc_shape);
                text_erc.setTextColor(Color.parseColor("#333333"));
                text_erc.setBackgroundResource(R.drawable.trc_shape);
                updateView();
                break;
            case R.id.text_erc:
                coinTypeId = "USDT";
                stats = 1;
                text_dig.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_right_arrow, 0);
                text_erc.setTextColor(Color.parseColor("#3F63F4"));
                text_erc.setBackgroundResource(R.drawable.erc_shape);
                text_trc.setTextColor(Color.parseColor("#333333"));
                text_trc.setBackgroundResource(R.drawable.trc_shape);
                updateView();
                break;
        }
    }

    public void query() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.MINING_ADDRESS_BOX, params, Api.MINING_ADDRESS_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.MINING_ADDRESS_ID:
                        infos = JsonParse.getAddressInfo(object);
                        if (infos != null && infos.size() > 0) {
                            updateView();
                            update();
                        }
                        break;
                }
            }
        }
        stopProgressDialog();
    }

    private void update() {
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i).getCoinTypeId().equals("ETH")) {
                addressInfo = infos.get(i);
            }
        }
    }

    AddressInfo addressInfo;
    private void updateView() {
        for (int i = 0; i < infos.size(); i++) {
            if (coinTypeId.equals(infos.get(i).getCoinTypeId() + "")) {
                text_style.setText(infos.get(i).getAddress() + "");
                Bitmap mBitmap = QRCodeUtil.createQRCode(infos.get(i).getAddress(), 600);
                iv_code.setImageBitmap(mBitmap);
            }
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
                    updateView();
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
                    updateView();
                }
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.text_eth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressInfo != null ) {
                    coinTypeId = addressInfo.getCoinTypeId();
                    text_dig.setText(addressInfo.getCoinTypeName());
                    updateView();
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
}
