package com.treasure.million.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.treasure.million.adapter.AdvertAdapter;
import com.treasure.million.bean.Massage;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.Utility;
import com.treasure.million.base.BaseFragment;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.treasure.million.R;
import com.treasure.million.weight.NoDataView;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author: zt
 * @date: 2020/8/21
 * @name:PoliceFragment
 */
public class PoliceFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener, NetWorkListener {
    private View rootView;
    private RecyclerView swipe_target;
    private SwipeToLoadLayout swipeToLoadLayout;
    private int limit = 10;
    private int page = 1;
    private boolean isRefresh;
    private NoDataView mNoDataView;
    private List<Massage> list = new ArrayList<>();
    private AdvertAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_received, container, false);
            initView();
        }
        return rootView;
    }

    private void initView() {
        swipeToLoadLayout = getView(rootView, R.id.swipeToLoadLayout);
        swipe_target = getView(rootView, R.id.swipe_target);
        mNoDataView = getView(rootView, R.id.mNoDataView);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        swipe_target.setLayoutManager(layoutManager);
        mNoDataView.textView.setText("????????????");
        qury();
    }

    private void qury() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID+"&type=2" + Constants.SECREKEY;
        showProgressDialog(getActivity(), false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("type",  "2");
        params.put("memberid", SaveUtils.getSaveInfo().getId() + "");
        params.put("limit", limit + "");
        params.put("page", page + "");
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_AGE_MSG, params, Api.GET_AGE_MSGID, this);
    }

    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.GET_AGE_MSGID:
                        List<Massage> infos = JsonParse.getEarlyInfoJson1(object);
                        if (infos != null && infos.size() > 0) {
                            setAdapter(infos);
                        } else {
                            if (page == 1 && !isRefresh) {
                                swipe_target.setVisibility(View.GONE);
                                mNoDataView.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                }
            }
        }
        stopProgressDialog();
        swipeToLoadLayout.setLoadingMore(false);
        swipeToLoadLayout.setRefreshing(false);
    }

    private void setAdapter(List<Massage> voList) {
        mNoDataView.setVisibility(View.GONE);
        swipe_target.setVisibility(View.VISIBLE);
        if (!isRefresh) {
            list.clear();
            list.addAll(voList);
            adapter = new AdvertAdapter(getContext(), list);
            swipe_target.setAdapter(adapter);
        } else {
            list.addAll(voList);
            adapter.setData(list);
        }
    }

    @Override
    public void onFail() {
        stopProgressDialog();
        swipeToLoadLayout.setLoadingMore(false);
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onError(Exception e) {
        stopProgressDialog();
        swipeToLoadLayout.setLoadingMore(false);
        swipeToLoadLayout.setRefreshing(false);
    }


    @Override
    public void onLoadMore() {
        isRefresh = true;
        page++;
        qury();
    }

    @Override
    public void onRefresh() {
        isRefresh = false;
        page = 1;
        qury();
    }


}
