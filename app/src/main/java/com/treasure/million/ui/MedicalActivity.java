package com.treasure.million.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.treasure.million.adapter.HealthAdapter;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.HealthItemVO;
import com.treasure.million.bean.HealthProjectVO;
import com.treasure.million.glide.GlideUtils;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.StatusBarUtil;
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
 * @date: 2020/7/16
 * @name:体检
 */
public class MedicalActivity extends BaseActivity implements NetWorkListener {
    private TextView title_left_btn, text_work, text_begin;
    private HealthItemVO healthItemVO;
    private List<HealthProjectVO> list = new ArrayList<>();
    private HealthAdapter adapter;
    private RecyclerView recyclerView;
    private Handler mHandler = new Handler();
    private Runnable runnable;
    private ImageView iv_car;



    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_medical);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtil.setTranslucentStatus(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        GlideUtils.clearImageMemoryCache(this);
    }

    @Override
    protected void initView() {
        iv_car = getView(R.id.iv_car);
        text_begin = getView(R.id.text_begin);
        text_work = getView(R.id.text_work);
        title_left_btn = getView(R.id.title_left_btn);
        recyclerView = getView(R.id.recyclerView);
        title_left_btn.setOnClickListener(this);
        text_begin.setOnClickListener(this);
        Glide.with(this).load(R.drawable.icon_gif).listener(new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(iv_car);
    }

    @Override
    protected void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        query();
    }


    private void query() {
        String sign = "imeicode=" + SaveUtils.getCar().getImeicode() + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId() + "");
        params.put("imeicode", SaveUtils.getCar().getImeicode() + "");
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_TRACK_RESULT, params, Api.GET_TRACK_RESULT_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.GET_TRACK_RESULT_ID:
                        healthItemVO = JsonParse.getHealthItemVOJson(object);
                        if (healthItemVO != null) {
                            updateView();
                        }
                        break;
                }
            } else {
                ToastUtil.showToast(commonality.getErrorDesc());
            }
        }
        stopProgressDialog();
    }

    private void updateView() {
        Map<String, String> map = healthItemVO.getItems();
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                HealthProjectVO healthProjectVO = new HealthProjectVO();
                healthProjectVO.setName(key);
                healthProjectVO.setValue(value);
                list.add(healthProjectVO);
            }
            adapter = new HealthAdapter(this, list);
            recyclerView.setAdapter(adapter);
            beginView();
        }
    }

    int count = 0;

    private void beginView() {
        runnable = new Runnable() {
            @Override
            public void run() {
                count++;
                if (list.size() > count) {
                    adapter.startLaunch(count - 1);
                    recyclerView.scrollToPosition(count - 1);
                } else {
                    adapter.endLaunch();
                    mHandler.removeCallbacks(runnable);
                    if (Utility.isEmpty(adapter.msg)){
                        ToastUtil.showSnackbar(text_begin, "您的车辆检测已完成，车辆无故障");
                    }else{
                        ToastUtil.showSnackbar(text_begin, "您的车辆检测已完成，车辆异常"+adapter.msg);
                    }
                    return;
                }
                mHandler.postDelayed(this, 700);
            }
        };
        mHandler.post(runnable);
    }


    @Override
    public void onFail() {
        stopProgressDialog();
    }

    @Override
    public void onError(Exception e) {
        stopProgressDialog();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_btn:
                finish();
                break;
            case R.id.text_begin:
                text_work.setVisibility(View.VISIBLE);
                text_begin.setVisibility(View.GONE);
                break;
        }
    }


}
