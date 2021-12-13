package com.treasure.million.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.treasure.million.adapter.ActiiveAdapter;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.base.BaseApplication;
import com.treasure.million.bean.ActiveBean;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/5/27
 * @name:赠送挖矿
 */
public class ActiveActivity extends BaseActivity implements NetWorkListener {
    private TextView title_text_tv, title_left_btn;
    private List<ActiveBean> activeBeans = new ArrayList<>();
    private ActiiveAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ctive1);
        BaseApplication.activityTaskManager.putActivity("ActiveActivity", this);
    }

    @Override
    protected void initView() {
        recyclerView= getView(R.id.recyclerView);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_text_tv.setText("赠送挖矿");
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


    @Override
    protected void initData() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        query();
    }


    public void query() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.PAY_REMOVE_DATA, params, Api.PAY_REMOVE_DATA_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.PAY_REMOVE_DATA_ID:
                        activeBeans = JsonParse.getactiveBeans(object);
                        if (activeBeans != null && activeBeans.size() > 0) {
                            setAdapter();
                        }
                        break;
                }
            }
        }
        stopProgressDialog();
    }

    private void setAdapter() {
        adapter=new ActiiveAdapter(this,activeBeans);
        recyclerView.setAdapter(adapter);
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
