package com.treasure.million.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.treasure.million.R;
import com.treasure.million.adapter.AdvertListAdapter;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.bean.MinVo;
import com.treasure.million.bean.MoneryBean;
import com.treasure.million.bean.UsdBean;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.util.BigDecimalUtils;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MiningDeilActivity
 * @Description: java类作用描述
 * @Author: zt
 * @Date: 2021/10/28 15:12
 */
public class MiningDeilActivity extends BaseActivity implements NetWorkListener, OnRefreshListener, OnLoadMoreListener {
    private TextView btn_redeem, btn_add, btn_buy;
    private TextView title_text_tv, title_left_text;
    private RecyclerView recyclerView;
    private AdvertListAdapter adapter;
    private List<MinVo> data = new ArrayList<>();
    private MoneryBean moneryBean;
    private TextView text_available, text_power, text_machine, text_eth, text_balance, text_tab1, text_tab2,text_num;
    private int page = 1;
    private int limit = 10;
    private boolean isRefresh;
    private SwipeToLoadLayout swipeToLoadLayout;
    private UsdBean usdBean;
    private Button btn_next;

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_miningdeil);
    }

    @Override
    protected void initView() {
        text_num= getView(R.id.text_num);
        btn_next = getView(R.id.btn_next);
        swipeToLoadLayout = getView(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        text_available = getView(R.id.text_available);
        text_power = getView(R.id.text_power);
        text_machine = getView(R.id.text_machine);
        text_eth = getView(R.id.text_eth);
        text_balance = getView(R.id.text_balance);
        text_tab1 = getView(R.id.text_tab1);
        text_tab2 = getView(R.id.text_tab2);
        btn_next.setOnClickListener(this);

        recyclerView = getView(R.id.recyclerView);
        btn_buy = getView(R.id.btn_buy);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_text = getView(R.id.title_left_text);
        title_left_text.setOnClickListener(this);
        btn_redeem = getView(R.id.btn_redeem);
        btn_add = getView(R.id.btn_add);
        btn_redeem.setOnClickListener(this);
        btn_buy.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        title_text_tv.setText("矿机详情");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {


    }

    @Override
    protected void onResume() {
        super.onResume();
        moneryBean = (MoneryBean) getIntent().getSerializableExtra("bean");
        if (moneryBean != null) {
            String name=moneryBean.getName();
            title_text_tv.setText(moneryBean.getName());
            if (name.contains("低配版")) {
                text_num.setText("提示:最小提币数量0.1ETH");
            }else if (name.contains("标准版")){
                text_num.setText("提示:最小提币数量0.2ETH");
            }else if (name.contains("高配版")){
                text_num.setText("提示:最小提币数量0.3ETH");
            }
            query();
            queryList();
            load();
        }
    }

    private void query() {
        String sign = "id=" + moneryBean.getId() + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("memberid", SaveUtils.getSaveInfo().getId() + "");
        params.put("id", moneryBean.getId() + "");
        params.put("partnerid", Constants.PARTNERID1);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.post(Api.REDE_LIST_DET, params, Api.REDE_LIST_DETY_ID, this);
    }


    private void queryList() {
        String sign = "financeid=" + moneryBean.getId() + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("memberid", SaveUtils.getSaveInfo().getId() + "");
        params.put("financeid", moneryBean.getId() + "");
        params.put("limit", limit + "");
        params.put("page", page + "");
        params.put("partnerid", Constants.PARTNERID1);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.post(Api.REDE_LIST_PROF, params, Api.REDE_LIST_PROF_ID, this);
    }

    public void load() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        Map<String, String> params = okHttpModel.getParams();
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.MINING_BAL_BOX, params, Api.GETRECORD_BOX_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.REDE_LIST_DETY_ID:
                        updateView(object);
                        break;
                    case Api.REDE_LIST_PROF_ID:
                        List<MinVo> data = JsonParse.getMinVoJson(object);
                        if (data != null && data.size() > 0) {
                            setAdapter(data);
                        } else {
                            if (isRefresh && page > 0) {
                                ToastUtil.showToast("无更多数据");
                            }
                        }
                        break;
                    case Api.GETRECORD_BOX_ID:
                        usdBean = JsonParse.getJSONObjectUsdtBean(object);
                        break;
                }
            }
        }
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }

    private void setAdapter(List<MinVo> infos) {
        if (!isRefresh) {
            data.clear();
            data.addAll(infos);
            adapter = new AdvertListAdapter(this, data);
            recyclerView.setAdapter(adapter);
        } else {
            data.addAll(infos);
            adapter.setData(data);
            adapter.notifyDataSetChanged();
        }
    }

    String balance;

    private void updateView(JSONObject object) {
        JSONObject jsonObject = object.optJSONObject("result");
        if (jsonObject != null) {
            String totalProfit = jsonObject.optString("totalProfit");
            balance = jsonObject.optString("balance");
            String days = jsonObject.optString("days");
            String power = jsonObject.optString("power");
            String profit = jsonObject.optString("profit");
            String totalPower = jsonObject.optString("totalPower");
            text_eth.setText(BigDecimalUtils.round(new BigDecimal(profit),6).toPlainString() + "");
            text_balance.setText(balance + "");
            text_power.setText("已挖矿：" + days + "天");
            text_machine.setText(power + "算力");
            text_tab1.setText("矿场算力:" + totalPower);
            text_tab2.setText("挖出ETH:" + BigDecimalUtils.round(new BigDecimal(totalProfit),6).toPlainString());
            text_available.setText("挖矿周期：" + moneryBean.getDays() + "天");
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_redeem:
                Intent intent = new Intent(this, RedListActivity.class);
                intent.putExtra("id", moneryBean.getId());
                intent.putExtra("name", moneryBean.getName());
                startActivity(intent);
                break;
            case R.id.title_left_text:
                finish();
                break;
            case R.id.btn_add:
                finish();
                break;
            case R.id.btn_buy:
                if (moneryBean != null) {
                    Intent intent1 = new Intent(this, RedListActivity.class);
                    intent1.putExtra("id", moneryBean.getId());
                    intent1.putExtra("name", moneryBean.getName());
                    startActivity(intent1);
                }

                break;
            case R.id.btn_next:
                double num=0;
                boolean isUsdt=false;
                String name=moneryBean.getName();
                if (usdBean != null && usdBean.getEth() != null) {
                    if (!Utility.isEmpty(balance)) {
                        num = Double.parseDouble(balance);
                    }
                    if (name.contains("低配版")) {
                          if (num<0.1){
                              ToastUtil.showToast("最新提币数量0.1ETH");
                              isUsdt=true;
                          }
                    }else if (name.contains("标准版")){
                        if (num<0.2){
                            ToastUtil.showToast("最新提币数量0.2ETH");
                            isUsdt=true;
                        }
                    }else if (name.contains("高配版")){
                        if (num<0.3){
                            ToastUtil.showToast("最新提币数量0.3ETH");
                            isUsdt=true;
                        }
                    }
                    if (!isUsdt){
                        intent = new Intent(this, DrawActivity.class);
                        intent.putExtra("usdtBean", usdBean);
                        intent.putExtra("coinTypeId", usdBean.getEth().getCoinTypeId());
                        startActivity(intent);
                    }
                }
                break;

        }
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
        queryList();
    }


    @Override
    public void onLoadMore() {
        isRefresh = true;
        page++;
        queryList();
    }

}
