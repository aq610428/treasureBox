package com.treasure.million.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.treasure.million.R;
import com.treasure.million.adapter.CarAdapter;
import com.treasure.million.base.BaseFragment;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.bean.OreInfo;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.ui.MsgActivity;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.Utility;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import crossoverone.statuslib.StatusUtil;

/**
 * @author: zt
 * @date: 2020/8/17
 * @name:矿场
 */
public class OreFragment extends BaseFragment implements NetWorkListener, OnRefreshListener{
    private View rootView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView swipe_target;
    private List<OreInfo> oreInfos = new ArrayList<>();
    private CarAdapter carAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_ore, container, false);
            initView();
            query();
        }
        return rootView;
    }

    private void initView() {
        swipe_target = rootView.findViewById(R.id.mListView);
        swipeToLoadLayout = rootView.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
    }


    /*******虚拟货币列表
     * @param ********/
    public void query() {
        String sign = "partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_COINS_LIST, params, Api.GET_COINS_LIST_ID, this);
    }


    @Override
    public void onResume() {
        super.onResume();
        query();
    }




    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.GET_COINS_LIST_ID:
                        oreInfos = JsonParse.getBespokemoniesJson1(object);
                        if (oreInfos != null && oreInfos.size() > 0) {
                            setAdapter();
                        }
                        break;
                }
            }
        }
        swipeToLoadLayout.setRefreshing(false);
    }


    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        swipe_target.setLayoutManager(layoutManager);
        carAdapter = new CarAdapter(this, oreInfos);
        swipe_target.setAdapter(carAdapter);
    }


    @Override
    public void onFail() {
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onError(Exception e) {
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        query();
    }
}
