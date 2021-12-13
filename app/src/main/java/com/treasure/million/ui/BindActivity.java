package com.treasure.million.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.LogUtils;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.SystemTools;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.R;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.treasure.million.weight.VehicleKeyboardHelper;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONObject;
import java.util.List;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/7/17
 * @name:绑定车辆
 */
public class BindActivity extends BaseActivity implements NetWorkListener {
    private TextView title_text_tv, title_left_btn, text_bind;
    private TextView text_brand;
    private ImageView iv_code;
    private EditText cardName, cardNum, et_code, et_course, et_discern, et_engine;


    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bind);
    }

    @Override
    protected void initView() {
        et_engine = getView(R.id.et_engine);
        et_discern = getView(R.id.et_discern);
        et_course = getView(R.id.et_course);
        et_code = getView(R.id.et_code);
        text_bind = getView(R.id.text_bind);
        cardNum = getView(R.id.et_cardnum);
        cardName = getView(R.id.et_cardName);
        iv_code = getView(R.id.iv_code);
        text_brand = getView(R.id.text_brand);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        text_brand.setOnClickListener(this);
        iv_code.setOnClickListener(this);
        text_bind.setOnClickListener(this);
        title_text_tv.setText("绑定车辆");
        VehicleKeyboardHelper.bind(cardNum, this);
    }

    @Override
    protected void initData() {

    }

    boolean isChoose = false;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        VehicleKeyboardHelper.hideCustomInput(cardNum);
        switch (v.getId()) {
            case R.id.title_left_btn:
                finish();
                break;
            case R.id.text_brand:
                Intent intent = new Intent(this, BrandCarActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.iv_code:
                checkPermission();
                break;
            case R.id.text_bind:
                checkData();
                break;
        }
    }


    private String yearmodelid, factory, model, yearmodel;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) {
            yearmodelid = data.getStringExtra("business");
            factory = data.getStringExtra("factory");
            model = data.getStringExtra("model");
            yearmodel = data.getStringExtra("yearmodel");
            text_brand.setText(factory + model + yearmodel);
        } else if (requestCode == 101 && resultCode == RESULT_OK) {
            String result = data.getExtras().getString(CodeUtils.RESULT_STRING);
            if (!Utility.isEmpty(result)) {
                String str[] = result.split("#");
                if (str != null && str.length == 2) {
                    cardName.setText(str[0]);
                    et_code.setText(str[1]);
                } else if (str != null && str.length == 1) {
                    if (str[0].length() <= 12) {
                        cardName.setText(str[0]);
                        et_code.setText("1234");
                    } else if (str[0].length() > 12) {
                        cardName.setText(str[0].substring(0, 12));
                        et_code.setText(str[0].substring(12, str[0].length()));
                    }
                }
            }
            LogUtils.e("解码结果： \n" + result);
        }
    }


    private void checkData() {
        String imeicode = cardName.getText().toString().trim();//设备号
        String carcard = cardNum.getText().toString().trim();//车牌
        String activecode = et_code.getText().toString().trim();//激活码
        String yearmodelid = text_brand.getText().toString().trim();//车辆品牌
        String initmileage = et_course.getText().toString().trim();
        String vinno = et_discern.getText().toString().trim();
        String engineno = et_engine.getText().toString().trim();

        if (Utility.isEmpty(imeicode)) {
            ToastUtil.showToast("设备编号不能为空");
            return;
        }
        if (Utility.isEmpty(carcard)) {
            ToastUtil.showToast("车牌号不能为空");
            return;
        }
        if (!SystemTools.isCarnumberNO(carcard)) {
            ToastUtil.showToast("车牌号不合法,请重新输入");
            return;
        }

        if (Utility.isEmpty(activecode)) {
            ToastUtil.showToast("设备激活码不能为空");
            return;
        }

        if (Utility.isEmpty(yearmodelid)) {
            ToastUtil.showToast("车辆品牌不能为空");
            return;
        }

        if (Utility.isEmpty(initmileage)) {
            ToastUtil.showToast("车辆里程不能为空");
            return;
        }


        if (Utility.isEmpty(vinno)) {
            ToastUtil.showToast("车辆车辆识别代码不能为空");
            return;
        }


        if (Utility.isEmpty(engineno)) {
            ToastUtil.showToast("车辆发动机号不能为空");
            return;
        }
        query();
    }


    private void query() {
        String imeicode = cardName.getText().toString().trim();//设备号
        String carcard = cardNum.getText().toString().trim();//车牌
        String activecode = et_code.getText().toString().trim();//激活码
        String initmileage = et_course.getText().toString().trim();
        String vinno = et_discern.getText().toString().trim();
        String engineno = et_engine.getText().toString().trim();
        String sign = "activecode=" + activecode + "&carcard=" + carcard + "&engineno=" + engineno + "&imeicode=" + imeicode
                + "&initmileage=" + initmileage + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID
                + "&vinno=" + vinno + "&yearmodelid=" + yearmodelid + Constants.SECREKEY;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("activecode", activecode + "");
        params.put("carcard", carcard + "");
        params.put("engineno", engineno + "");
        params.put("imeicode", imeicode + "");
        params.put("initmileage", initmileage + "");
        params.put("memberid", SaveUtils.getSaveInfo().getId() + "");
        params.put("partnerid", Constants.PARTNERID);
        params.put("vinno", vinno + "");
        params.put("yearmodelid", yearmodelid + "");
        params.put("apptype", Constants.TYPE);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_MODEL_BIND, params, Api.GET_MODEL_BIND_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.PAY_CHANCE_CARF_ID:
                    case Api.GET_MODEL_BIND_ID:
                        ToastUtil.showToast(commonality.getErrorDesc());
                        finish();
                        break;

                }
            } else {
                ToastUtil.showToast(commonality.getErrorDesc());
            }
        }
        stopProgressDialog();
    }


    private void checkPermission() {
        PermissionX.init(this).permissions(Manifest.permission.CAMERA).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                if (allGranted){
                    Intent intent = new Intent(BindActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, 101);
                }else{
                    showSettingDialog();
                }
            }
        });
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
