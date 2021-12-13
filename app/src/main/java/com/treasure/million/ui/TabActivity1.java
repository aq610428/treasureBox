package com.treasure.million.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.treasure.million.adapter.IncomeAdapter;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.BoxInfo;
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
 * @date: 2020/11/4
 * @name:TabActivity
 */
public class TabActivity1 extends BaseActivity implements OnLoadMoreListener, OnRefreshListener, NetWorkListener {
    private TextView title_text_tv, title_left_btn;
    private int page = 1;
    private int limit = 10;
    private boolean isRefresh;
    private NoDataView noDataView;
    private RecyclerView swipe_target;
    private SwipeToLoadLayout swipeToLoadLayout;
    private List<BoxInfo> beans = new ArrayList<>();
    private IncomeAdapter adapter;

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tab);
    }

    @Override
    protected void initView() {
        noDataView = getView(R.id.noDataView);
        swipeToLoadLayout = getView(R.id.swipeToLoadLayout);
        swipe_target = getView(R.id.swipe_target);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_text_tv.setText("挖矿记录");
        title_left_btn.setOnClickListener(this);
        noDataView.textView.setText("暂无挖矿记录");
    }

    @Override
    protected void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        swipe_target.setLayoutManager(layoutManager);
        query();
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



    public void query() {
        showProgressDialog(this,false);
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID);
        params.put("limit", limit + "");
        params.put("page", page + "");
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.MINING_BOX, params, Api.MINING_BOX_ID, this);
    }



    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.MINING_BOX_ID:
                        List<BoxInfo> boxInfos = JsonParse.getBoxJson(object);
                        if (boxInfos != null && boxInfos.size() > 0) {
                            setAdapter(boxInfos);
                        } else {
                            if (isRefresh && page > 0) {
                                ToastUtil.showToast("无更多数据");
                            }
                        }
                        break;

                }
            } else {
                ToastUtil.showToast(commonality.getErrorDesc());
            }
        }
        swipeToLoadLayout.setLoadingMore(false);
        swipeToLoadLayout.setRefreshing(false);
        stopProgressDialog();
    }


    private void setAdapter(List<BoxInfo> boxVoList) {
        noDataView.setVisibility(View.GONE);
        swipeToLoadLayout.setVisibility(View.VISIBLE);
        if (!isRefresh) {
            beans.clear();
            beans.addAll(boxVoList);
            adapter = new IncomeAdapter(this, beans);
            swipe_target.setAdapter(adapter);
        } else {
            beans.addAll(boxVoList);
            adapter.setData(beans);
        }
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
    public void onFail() {
        swipeToLoadLayout.setLoadingMore(false);
        swipeToLoadLayout.setRefreshing(false);
        stopProgressDialog();
    }

    @Override
    public void onError(Exception e) {
        swipeToLoadLayout.setLoadingMore(false);
        swipeToLoadLayout.setRefreshing(false);
        stopProgressDialog();
    }
}
