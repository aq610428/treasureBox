package com.treasure.million.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.treasure.million.R;
import com.treasure.million.adapter.SafeAdapter;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.CarSafeVO;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/7/15
 * @name:SafeActivity
 */
public class SafeActivity extends BaseActivity implements NetWorkListener {
    private TextView title_text_tv, title_left_btn;
    private RecyclerView recyclerView;
    private SafeAdapter safeAdapter;
    private List<CarSafeVO> infos = new ArrayList<>();

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_safe);
    }

    @Override
    protected void initView() {
        recyclerView = getView(R.id.recyclerView);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_text_tv.setText("安全预警设置");
    }

    @Override
    protected void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        qury();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_btn:
                finish();
                break;
        }
    }


    private void qury() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId() + "");
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_AGE_DEVICE_INFO, params, Api.GET_AGE_DEVICE_INFO_ID, this);
    }


    public void bind(CarSafeVO inventory) {
        String sign = "id=" + SaveUtils.getCar().getId() +"&key="+inventory.getKey()+"&memberid="+SaveUtils.getSaveInfo().getId()+ "&partnerid=" + Constants.PARTNERID +"&value="+inventory.getValue()+ Constants.SECREKEY;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("id", SaveUtils.getCar().getId() + "");
        params.put("key", inventory.getKey() + "");
        params.put("memberid",SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID);
        params.put("value", inventory.getValue() + "");
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_AGE_DEVICE_MISS, params, Api.GET_AGE_DEVICE_MISS_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.GET_AGE_DEVICE_INFO_ID:
                        infos = JsonParse.getCarSafeVOJson(object);
                        if (infos != null && infos.size() > 0) {
                            setAdapter();
                        }
                        break;
                    case Api.GET_AGE_DEVICE_MISS_ID:
                        ToastUtil.showToast(commonality.getErrorDesc());
                        qury();
                        break;
                }
            }else{
                ToastUtil.showToast(commonality.getErrorDesc());
            }
        }
        stopProgressDialog();
    }

    private void setAdapter() {
        safeAdapter = new SafeAdapter(this, infos);
        recyclerView.setAdapter(safeAdapter);
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
