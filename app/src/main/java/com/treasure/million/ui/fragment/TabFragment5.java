package com.treasure.million.ui.fragment;

import android.widget.TextView;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.treasure.million.R;
import com.treasure.million.base.BaseFragment1;
import com.treasure.million.bean.ActivateBean;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.Utility;

import org.json.JSONObject;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/10/20
 * @name:我的社群
 */
public class TabFragment5 extends BaseFragment1 implements NetWorkListener, OnRefreshListener {
    private TextView text_tab1, text_tab2, text_tab3, text_tab4, text_tab5,text_usd;
    private TextView text_tab_direct, text_tab1_direct, text_tab2_direct;
    private TextView text_tab_usdt, text_tab1_usdt, text_tab1_box, text_tab2_box;
    private ActivateBean activateBean;
    private SwipeToLoadLayout swipeToLoadLayout;

    @Override
    protected void loadData() {
        query();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab5;
    }

    @Override
    protected void initView() {
        swipeToLoadLayout= getView(rootView, R.id.swipeToLoadLayout);
        text_usd = getView(rootView, R.id.text_usd);
        text_tab1 = getView(rootView, R.id.text_tab1);
        text_tab2 = getView(rootView, R.id.text_tab2);
        text_tab3 = getView(rootView, R.id.text_tab3);
        text_tab4 = getView(rootView, R.id.text_tab4);
        text_tab5 = getView(rootView, R.id.text_tab5);
        swipeToLoadLayout.setOnRefreshListener(this);
        text_tab_direct = getView(rootView, R.id.text_tab_direct);
        text_tab1_direct = getView(rootView, R.id.text_tab1_direct);
        text_tab2_direct = getView(rootView, R.id.text_tab2_direct);

        text_tab_usdt = getView(rootView, R.id.text_tab_usdt);
        text_tab1_usdt = getView(rootView, R.id.text_tab1_usdt);
        text_tab1_box = getView(rootView, R.id.text_tab1_box);
        text_tab2_box = getView(rootView, R.id.text_tab2_box);
    }

    public void query() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        showProgressDialog(getActivity(), false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.REDE_REPORT_BOX, params, Api.REDE_REPORT_BOX_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.REDE_REPORT_BOX_ID:
                        activateBean = JsonParse.getActivateBeanJson(object);
                        if (activateBean != null) {
                            updateView();
                        }
                        break;

                }
            }
        }
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
    }

    private void updateView() {
        text_tab1.setText(activateBean.getOne_level()+"");
        text_tab2.setText(activateBean.getTwo_level()+"");
        text_tab3.setText(activateBean.getThree_level()+"");
        text_tab4.setText(activateBean.getFour_level()+"");
        text_tab5.setText(activateBean.getFive_level()+"");
        String level=activateBean.getLevel();
        switch (level){
            case "0":
                text_usd.setText("暂无社区");
                break;
            case "1":
                text_usd.setText("V1");
                break;
            case "2":
                text_usd.setText("V2");
                break;
            case "3":
                text_usd.setText("V3");
                break;
            case "4":
                text_usd.setText("V4");
                break;
            case "5":
                text_usd.setText("V4");
                break;
        }
        text_tab_direct.setText(activateBean.getDirect_num()+"");
        text_tab1_direct.setText(activateBean.getDirect_active_num()+"");
        text_tab2_direct.setText(activateBean.getTeam_active_num()+"");
        text_tab_usdt.setText(activateBean.getDirect_balance()+"");
        text_tab1_usdt.setText(activateBean.getTeam_balance()+"");
        text_tab1_box.setText(activateBean.getTotal_box()+"");
        text_tab2_box.setText(activateBean.getTotal_hold_box()+"");
    }

    @Override
    public void onFail() {
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onError(Exception e) {
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        query();
    }
}
