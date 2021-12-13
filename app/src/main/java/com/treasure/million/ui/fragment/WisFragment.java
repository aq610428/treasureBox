package com.treasure.million.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.treasure.million.R;
import com.treasure.million.adapter.AsetsAdapter1;
import com.treasure.million.base.BaseFragment;
import com.treasure.million.bean.AssetsBean;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.bean.UsdtInfo;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.ui.DrawActivity1;
import com.treasure.million.util.BigDecimalUtils;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import com.treasure.million.weight.NoDataView;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import crossoverone.statuslib.StatusUtil;

/**
 * @ClassName: WisFragment
 * @Description: 惠购车
 * @Author: zt
 * @Date: 2021/9/24 15:26
 */
public class WisFragment extends BaseFragment implements NetWorkListener, OnRefreshListener, View.OnClickListener, OnLoadMoreListener {
    private View rootView;
    private RecyclerView recyclerView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private int limit = 10;
    private int page = 1;
    private boolean isRefresh;
    private List<AssetsBean> data = new ArrayList<>();
    private AsetsAdapter1 asetsAdapter;
    private UsdtInfo usdBean;
    private TextView text_usdt, text_usdt_carry, text_yes;
    private NoDataView noDataView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_wis, container, false);
            initView();
        }
        return rootView;
    }

    private void initView() {
        noDataView = getView(rootView, R.id.noDataView);
        noDataView.textView.setText("暂无交易数据");
        text_usdt_carry = getView(rootView, R.id.text_usdt_carry);
        text_usdt = getView(rootView, R.id.text_usdt);
        text_yes = getView(rootView, R.id.text_yes);
        swipeToLoadLayout = getView(rootView, R.id.swipeToLoadLayout);
        recyclerView = getView(rootView, R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setNestedScrollingEnabled(false);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        text_usdt_carry.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        query1();
        onRefresh();
        StatusUtil.setUseStatusBarColor(getActivity(), Color.parseColor("#FFFFFF"));
        StatusUtil.setSystemStatus(getActivity(), false, true);
    }


    public void query() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("limit", limit + "");
        params.put("page", page + "");
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.post(Api.REDE_BLANCE_RECORD, params, Api.REDE_BLANCE_RECORD_ID, this);
    }

    public void query1() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        Map<String, String> params = okHttpModel.getParams();
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.post(Api.REDE_BLANCE_USDT, params, Api.REDE_BLANCE_USDT_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.REDE_BLANCE_RECORD_ID:
                        List<AssetsBean> assetsBeans = JsonParse.getJSONObjectAssetsBean(object);
                        if (assetsBeans != null && assetsBeans.size() > 0) {
                            setAdapter(assetsBeans);
                        } else {
                            if (isRefresh && page > 0) {
                                ToastUtil.showToast("无更多数据");
                            }
                            if (!isRefresh && page ==1) {
                                swipeToLoadLayout.setVisibility(View.GONE);
                                noDataView.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                    case Api.REDE_BLANCE_USDT_ID:
                        usdBean = JsonParse.getJSONObUsdtInfo(object);
                        if (usdBean != null) {
                            updateView();
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
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }

    private void setAdapter(List<AssetsBean> assetsBeans) {
        swipeToLoadLayout.setVisibility(View.VISIBLE);
        noDataView.setVisibility(View.GONE);

        if (!isRefresh) {
            data.clear();
            data.addAll(assetsBeans);
            asetsAdapter = new AsetsAdapter1(getContext(), data);
            recyclerView.setAdapter(asetsAdapter);
        } else {
            data.addAll(assetsBeans);
            asetsAdapter.setData(data);
            asetsAdapter.notifyDataSetChanged();
        }


    }

    private void updateView() {
        if (usdBean != null) {
            text_usdt.setText(BigDecimalUtils.round(new BigDecimal(usdBean.getUserable()), 4).toPlainString() + "");
            text_yes.setText(BigDecimalUtils.round(new BigDecimal(usdBean.getYestodayUserable()), 4).toPlainString() + "");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_usdt_carry:
                if (usdBean != null) {
                    Intent intent = new Intent(getContext(), DrawActivity1.class);
                    intent.putExtra("usdBean", usdBean);
                    startActivity(intent);
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
        query();
    }

    @Override
    public void onLoadMore() {
        isRefresh = true;
        page++;
        query();
    }

}
