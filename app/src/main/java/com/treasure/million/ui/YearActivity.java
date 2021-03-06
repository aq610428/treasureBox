package com.treasure.million.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.treasure.million.adapter.InsurAdapter2;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.OilInfo;
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
import com.treasure.million.weight.NoDataView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/8/10
 * @name:行程数据区块
 */
public class YearActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, NetWorkListener {
    private TextView title_text_tv, title_left_btn;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView swipe_target;
    private int limit = 20;
    private int page = 1;
    private boolean isRefresh;
    private NoDataView mNoDataView;
    private List<OilInfo> travrts = new ArrayList<>();
    private InsurAdapter2 tripAdapter;


    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_trip);
    }

    @Override
    protected void initView() {
        swipeToLoadLayout = getView(R.id.swipeToLoadLayout);
        mNoDataView = getView(R.id.mNoDataView);
        swipe_target = getView(R.id.swipe_target);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        title_text_tv.setText("加油数据区块");
        mNoDataView.textView.setText("暂无加油数据区块");
    }

    @Override
    protected void initData() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        swipe_target.setLayoutManager(gridLayoutManager);
        query();
    }

    public void query() {
        String sign = "partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("partnerid", Constants.PARTNERID);
        params.put("limit", limit + "");
        params.put("page", page + "");
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.RESET_TOKEN_OIL, params, Api.RESET_TOKEN_TRIP_ID, this);
    }


    @Override
    public void onRefresh() {
        isRefresh = false;
        page = 1;
        query();
    }

    @Override
    public void onLoadMore() {
        isRefresh = true;
        page++;
        query();
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.RESET_TOKEN_TRIP_ID:
                        List<OilInfo> travrts = JsonParse.getOilInfoJson2(object);
                        if (travrts != null && travrts.size() > 0) {
                            setAdapter(travrts);
                        } else {
                            if (!isRefresh && page == 1) {
                                mNoDataView.setVisibility(View.VISIBLE);
                                swipeToLoadLayout.setVisibility(View.GONE);
                            }
                        }
                        break;
                }
            } else {
                ToastUtil.showToast(commonality.getErrorDesc());
            }
        }
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }


    private void setAdapter(List<OilInfo> voList) {
        mNoDataView.setVisibility(View.GONE);
        swipeToLoadLayout.setVisibility(View.VISIBLE);

        if (!isRefresh) {
            travrts.clear();
            travrts.addAll(voList);
            tripAdapter = new InsurAdapter2(this, travrts);
            swipe_target.setAdapter(tripAdapter);
        } else {
            travrts.addAll(voList);
            tripAdapter.setData(travrts);
        }

        tripAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(YearActivity.this, OilDeilActivity.class);
                intent.putExtra("travrt",travrts.get(position));
                startActivity(intent);
            }
        });
    }


    @Override
    public void onFail() {
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onError(Exception e) {
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
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
}
