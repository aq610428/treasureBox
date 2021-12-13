package com.treasure.million.ui;

import android.Manifest;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.treasure.million.adapter.OpemAdapter;
import com.treasure.million.base.BaseActivity1;
import com.treasure.million.bean.CarInfo;
import com.treasure.million.bean.PackageBean;
import com.treasure.million.bean.icadBean;
import com.treasure.million.glide.GlideUtils;
import com.treasure.million.util.BigDecimalUtils;
import com.treasure.million.util.Constants;
import com.treasure.million.util.ImageFactory;
import com.treasure.million.util.JsonParse;
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
import org.json.JSONObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/9/8
 * @name:PassworadActivity
 */
public class PassworadActivity extends BaseActivity1 implements NetWorkListener {
    private icadBean bean;
    private TextView text_mobile, btn_code, text_power, text_condition, text_surplus, text_surplus_kb;
    private TextView text_surplus_user, text_surplus_cycle, text_surplus_open, text_surplus_user_end;
    private TextView text_pay;
    private List<PackageBean> beans = new ArrayList<>();
    private ImageView iv_colse, iv_scod, iv_head;
    private RelativeLayout rl_code, rl_tab;


    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_passworad);
    }

    @Override
    protected void initView() {
        iv_head = getView(R.id.iv_head);
        rl_tab = getView(R.id.rl_tab);
        iv_colse = getView(R.id.iv_colse);
        iv_scod = getView(R.id.iv_scod);
        rl_code = getView(R.id.rl_code);
        text_pay = getView(R.id.text_pay);
        text_mobile = getView(R.id.text_mobile);
        btn_code = getView(R.id.btn_code);
        text_surplus = getView(R.id.text_surplus);
        text_power = getView(R.id.text_power);
        text_condition = getView(R.id.text_condition);
        text_surplus_kb = getView(R.id.text_surplus_kb);
        text_surplus_user = getView(R.id.text_surplus_user);
        text_surplus_cycle = getView(R.id.text_surplus_cycle);
        text_surplus_open = getView(R.id.text_surplus_open);
        text_surplus_user_end = getView(R.id.text_surplus_user_end);
        text_pay.setOnClickListener(this);
        iv_colse.setOnClickListener(this);
        rl_tab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                applyPermission();
                return false;
            }
        });
        if (!Utility.isEmpty(SaveUtils.getSaveInfo().getUsericon())) {
            GlideUtils.CreateImageCircular(SaveUtils.getSaveInfo().getUsericon(), iv_head, 5);
        }
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "DINOT-Bold.ttf");
        text_surplus.setTypeface(typeface);
        text_surplus_kb.setTypeface(typeface);
        text_surplus_user.setTypeface(typeface);
        text_surplus_cycle.setTypeface(typeface);
        text_surplus_open.setTypeface(typeface);
        text_surplus_user_end.setTypeface(typeface);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.text_pay:
                if (beans != null && beans.size() > 0) {
                    showDialog();
                }else{
                    ToastUtil.showToast("您当前未购买套餐，请联系客服人员!");
                }
                break;
            case R.id.iv_colse:
                rl_code.setVisibility(View.GONE);
                break;

        }
    }


    public void applyPermission() {
        PermissionX.init(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                if (allGranted){
                    saveCode();
                }else{
                    showSettingDialog();
                }
            }
        });

    }


    /******长按保存*****/
    private void saveCode() {
        Bitmap bitmap = ImageFactory.getConvertViewToBitmap(rl_tab);
        if (bitmap != null) {
            boolean isSuccess = ImageFactory.saveImageT(this, bitmap);
            if (isSuccess) {
                ToastUtil.showToast("保存成功");
            }else{
                ToastUtil.showToast("图片保存失败，请打开写入权限！");
            }
        }
    }


    @Override
    protected void initData() {
        CarInfo carInfo = SaveUtils.getCar();
        if (carInfo != null && !Utility.isEmpty(carInfo.getIccid())) {
            query();
            queryList();
        } else {
            if (carInfo == null) {
                ToastUtil.showLongToast("抱歉，您未绑定车辆，快去绑定吧~");
            } else {
                ToastUtil.showLongToast("抱歉，未能识别到设备流量卡号，请联系客服人员!");
            }
        }
    }


    public void query() {
        String sign = "iccid=" + SaveUtils.getCar().getIccid() + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("iccid", SaveUtils.getCar().getIccid());
        params.put("apptype", Constants.TYPE);
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.RESET_FLOW_DEVICE, params, Api.RESET_FLOW_DEVICEE_ID, this);
    }


    public void queryList() {
        String sign = "iccid=" + SaveUtils.getCar().getIccid() + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("iccid", SaveUtils.getCar().getIccid());
        params.put("apptype", Constants.TYPE);
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.RESET_FLOW_List, params, Api.RESET_FLOW_List_ID, this);
    }


    String packagesn;

    public void queryCode() {
        if (beans != null && beans.size() > 0) {
            String sign = "iccid=" + SaveUtils.getCar().getIccid() + "&packagesn=" + packagesn + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
            showProgressDialog(this, false);
            Map<String, String> params = okHttpModel.getParams();
            params.put("iccid", SaveUtils.getCar().getIccid());
            params.put("apptype", Constants.TYPE);
            params.put("packagesn", packagesn + "");
            params.put("partnerid", Constants.PARTNERID);
            params.put("sign", Md5Util.encode(sign));
            okHttpModel.get(Api.RESET_FLOW_CODE, params, Api.RESET_FLOW_CODE_ID, this);
        } else {
            ToastUtil.showToast("您当前未购买套餐，请联系客服人员!");
        }

    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.RESET_FLOW_DEVICEE_ID:
                        JSONObject jsonObject1 = object.optJSONObject("result");
                        String state=jsonObject1.optString("state");
                        if ("0".equals(state)){
                            String data = jsonObject1.optString("data");
                            ToastUtil.showToast(data + "");
                        }else{
                            bean = JsonParse.getJSONicon(object);
                            if (bean != null) {
                                updateView();
                            }
                        }

                        break;
                    case Api.RESET_FLOW_List_ID:
                        beans = JsonParse.getStoreListJson(object);
                        break;
                    case Api.RESET_FLOW_CODE_ID:
                        JSONObject jsonObject = object.optJSONObject("result");
                        String qrcode_url = jsonObject.optString("qrcode_url");
                        if (!Utility.isEmpty(qrcode_url)) {
                            rl_code.setVisibility(View.VISIBLE);
                            GlideUtils.setImageUrl(qrcode_url, iv_scod);
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
        if ("1".equals(bean.getState())) {
            text_power.setText("开机");
        } else {
            text_power.setText("停机");
        }
        if (SaveUtils.getSaveInfo() != null) {
            text_mobile.setText(SaveUtils.getSaveInfo().getMobile() + "");
        }
        btn_code.setText("ICCID:" + bean.getData().getIccid() + "");
        text_surplus.setText(bean.getData().getBalance() + "");


        if (!Utility.isEmpty(bean.getData().getSurplus_usage())) {
            double usage = BigDecimalUtils.div(new BigDecimal(bean.getData().getSurplus_usage() + ""), new BigDecimal(1024 + ""), 2).doubleValue();
            text_surplus_kb.setText(usage + "");
        }
        if (!Utility.isEmpty(bean.getData().getDone_usage())) {
            double done = BigDecimalUtils.div(new BigDecimal(bean.getData().getDone_usage() + ""), new BigDecimal(1024 + ""), 2).doubleValue();
            text_surplus_user.setText(done + "");
        }
        if (!Utility.isEmpty(bean.getData().getAmount_usage())) {
            double amout = BigDecimalUtils.div(new BigDecimal(bean.getData().getAmount_usage() + ""), new BigDecimal(1024 + ""), 2).doubleValue();
            text_surplus_cycle.setText(amout + "");
        }

        text_surplus_open.setText(bean.getData().getOpen_time() + "");
        text_surplus_user_end.setText(bean.getData().getExpire_date() + "");
    }

    @Override
    public void onFail() {
        stopProgressDialog();
    }

    @Override
    public void onError(Exception e) {
        stopProgressDialog();
    }


    public void showDialog() {
        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout_card, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        OpemAdapter opemAdapter = new OpemAdapter(this, beans);
        recyclerView.setAdapter(opemAdapter);
        opemAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                packagesn = beans.get(position).getPackagesn();
                opemAdapter.setmPosition(position);
                dialog.dismiss();
                if ("szmg".equals(bean.getCardtype()+"")) {
                    queryCode();
                }else{
                    if (beans.get(position).getPackage_name().contains("年")){
                        SystemTools.openBrowser(PassworadActivity.this,bean.getYear_payurl());
                    }else{
                        SystemTools.openBrowser(PassworadActivity.this,bean.getMonth_payurl());
                    }
                }
            }
        });
        dialog.show();
    }

}
