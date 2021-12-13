package com.treasure.million.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.treasure.million.R;
import com.treasure.million.adapter.MoneryAdapter;
import com.treasure.million.base.BaseFragment1;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.bean.MoneryBean;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.glide.KilomeListener;
import com.treasure.million.ui.MiningDeilActivity;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import com.treasure.million.weight.DialogUtils;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import crossoverone.statuslib.StatusUtil;

/**
 * @author: zt
 * @date: 2020/10/20
 * @name:OreLostragment
 */
public class MoneyFragment extends BaseFragment1 implements NetWorkListener, OnRefreshListener, OnLoadMoreListener, KilomeListener {
    private RecyclerView recyclerView;
    private List<MoneryBean> data = new ArrayList<>();
    private MoneryAdapter adapter;
    private SwipeToLoadLayout swipeToLoadLayout;
    private int page = 1;
    private int limit = 10;
    private boolean isRefresh;
    private TextView text_available, text_power;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_morey;
    }

    @Override
    protected void initView() {
        text_power = getView(rootView, R.id.text_power);
        text_available = getView(rootView, R.id.text_available);
        recyclerView = getView(rootView, R.id.recyclerView);
        swipeToLoadLayout = getView(rootView, R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    protected void loadData() {

    }


    public void query() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        Map<String, String> params = okHttpModel.getParams();
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("limit", limit + "");
        params.put("page", page + "");
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.post(Api.REDE_LIST_RECORD, params, Api.REDE_LIST_RECORD_ID, this);
    }

    public void buy(String financeid, String buynum, String password) {
        showProgressDialog(getActivity(), false);
        String sign = "buynum=" + buynum + "&financeid=" + financeid + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1
                + "&paypassword=" + Md5Util.encode(password)
                + Constants.SECREKEY1;
        Map<String, String> params = okHttpModel.getParams();
        params.put("buynum", buynum + "");
        params.put("financeid", financeid + "");
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("paypassword", Md5Util.encode(password));
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.post(Api.REDE_LIST_BUY, params, Api.REDE_LIST_BUY_ID, this);
    }


    @Override
    public void onResume() {
        super.onResume();
        StatusUtil.setUseStatusBarColor(getActivity(), Color.parseColor("#FFFFFF"));
        StatusUtil.setSystemStatus(getActivity(), false, true);
        onRefresh();
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.REDE_LIST_RECORD_ID:
                        updateView(object);
                        List<MoneryBean> data = JsonParse.getMoneryBeanJson(object);
                        if (data != null && data.size() > 0) {
                            setAdapter(data);
                        } else {
                            if (isRefresh && page > 0) {
                                ToastUtil.showToast("无更多数据");
                            }
                        }
                        break;
                    case Api.REDE_LIST_BUY_ID:
                        onRefresh();
                        ToastUtil.showToast(commonality.getErrorDesc());
                        break;
                }
            } else {
                ToastUtil.showToast(commonality.getErrorDesc());
            }
        } else {
            ToastUtil.showToast(commonality.getErrorDesc());
        }
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }

    private void updateView(JSONObject object) {
        JSONObject jsonObject = object.optJSONObject("result");
        if (jsonObject != null) {
            text_available.setText("可用余额\n" + jsonObject.optString("balance") + "ETH");
            text_power.setText("我的矿机\n" + jsonObject.optString("power") + "算力");
        }
    }


    private void setAdapter(List<MoneryBean> infos) {
        if (!isRefresh) {
            data.clear();
            data.addAll(infos);
            adapter = new MoneryAdapter(this, data);
            recyclerView.setAdapter(adapter);
        } else {
            data.addAll(infos);
            adapter.setData(data);
            adapter.notifyDataSetChanged();
        }
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), MiningDeilActivity.class);
                intent.putExtra("bean", infos.get(position));
                startActivity(intent);
            }
        });
    }

    private String financeid, buynum;

    public void showPay(String financeid, String buynum) {
        this.financeid = financeid;
        this.buynum = buynum;
        DialogUtils.showPay1(this, this);
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
    public void onKilomeListener(String data) {
        buy(financeid, buynum, data);
    }
}
