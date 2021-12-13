package com.treasure.million.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.treasure.million.R;
import com.treasure.million.adapter.IncomeAdapter;
import com.treasure.million.base.BaseFragment1;
import com.treasure.million.bean.BoxInfo;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.ui.ActivationActivity;
import com.treasure.million.ui.BindActivity;
import com.treasure.million.ui.MainActivity;
import com.treasure.million.util.BigDecimalUtils;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.TypefaceUtil;
import com.treasure.million.util.Utility;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/10/20
 * @name:TabFragment1
 */
public class TabFragment2 extends BaseFragment1 implements NetWorkListener, OnRefreshListener, OnLoadMoreListener, View.OnClickListener {
    private SwipeToLoadLayout swipeToLoadLayout;
    private TextView text_total, text_day, text_mining, text_machinery, text_name, text_gold,text_investment,text_compound;
    private ImageView iv_chrome;
    private RecyclerView recyclerView;
    private int page = 1;
    private int limit = 10;
    private boolean isRefresh;
    private List<BoxInfo> beans = new ArrayList<>();
    private IncomeAdapter adapter;
    private AnimationDrawable animationDrawable;

    @Override
    protected void loadData() {
        query1();
        query();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab2;
    }

    @Override
    protected void initView() {
        text_compound= getView(rootView, R.id.text_compound);
        text_investment= getView(rootView, R.id.text_investment);
        text_gold = getView(rootView, R.id.text_gold);
        text_name = getView(rootView, R.id.text_name);
        recyclerView = getView(rootView, R.id.recyclerView);
        iv_chrome = getView(rootView, R.id.iv_chrome);
        text_day = getView(rootView, R.id.text_day);
        text_mining = getView(rootView, R.id.text_mining);
        text_machinery = getView(rootView, R.id.text_machinery);
        text_total = getView(rootView, R.id.text_total);
        swipeToLoadLayout = getView(rootView, R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        text_gold.setOnClickListener(this);

        TypefaceUtil.setTextType(getContext(), "DINOT-Bold.ttf", text_day);
        TypefaceUtil.setTextType(getContext(), "DINOT-Bold.ttf", text_mining);
        TypefaceUtil.setTextType(getContext(), "DINOT-Bold.ttf", text_machinery);
        TypefaceUtil.setTextType(getContext(), "DINOT-Bold.ttf", text_total);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
    }


    public void query() {
        showProgressDialog(getActivity(), false);
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_UNMODEL_BOX, params, Api.GET_UNMODEL_BOX_ID, this);
    }

    public void query1() {
        showProgressDialog(getActivity(), false);
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
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
                    case Api.GET_UNMODEL_BOX_ID:
                        updateJson(object);
                        break;
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
            }
        }
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }


    private void setAdapter(List<BoxInfo> boxVoList) {
        if (!isRefresh) {
            beans.clear();
            beans.addAll(boxVoList);
            adapter = new IncomeAdapter(getContext(), beans);
            recyclerView.setAdapter(adapter);
        } else {
            beans.addAll(boxVoList);
            adapter.setData(beans);
            adapter.notifyDataSetChanged();
        }
    }


    private void updateJson(JSONObject object) {
        JSONObject jsonObject = object.optJSONObject("result");
        if (jsonObject != null) {
            String yestodayBox = jsonObject.optString("yestodayBox");
            String yestodayActiveCount = jsonObject.optString("yestodayActiveCount");
            String totalBox = jsonObject.optString("totalBox");
            String activeCount = jsonObject.optString("activeCount");
            String miningMachineName = jsonObject.optString("miningMachineName");
            String yestodayRepeatActiveCount  = jsonObject.optString("yestodayRepeatActiveCount");
            String repeatActiveCount  = jsonObject.optString("repeatActiveCount");

            text_compound.setText(repeatActiveCount+"");
            text_investment.setText(yestodayRepeatActiveCount+"");
            if (!Utility.isEmpty(yestodayBox)){
                BigDecimal decimal1 = BigDecimalUtils.round(new BigDecimal(yestodayBox), 6);
                text_day.setText(decimal1.toPlainString() + "");
            }
            text_total.setText(totalBox + "");
            text_mining.setText(yestodayActiveCount + "");
            text_machinery.setText(activeCount + "");
            text_name.setText(miningMachineName + "");
            int stat = jsonObject.optInt("state");//1.首次挖矿中2挖矿已结束，3再次激活挖矿中
            switch (stat) {
                case 0:
                    if (SaveUtils.getCar() == null || Utility.isEmpty(SaveUtils.getCar().getImeicode())) {
                        text_gold.setText("去绑定");
                    } else {
                        text_gold.setText("激活挖矿");
                    }
                    iv_chrome.setImageResource(R.mipmap.icon_gold_san);
                    text_gold.setBackgroundResource(R.mipmap.ic_gold_btn);
                    break;
                case 1:
                    text_gold.setText("首次挖矿中");
                    text_gold.setBackgroundResource(0);
                    iv_chrome.setImageResource(0);
                    startGold();
                    break;
                case 2:
                    text_name.setText("您当前的挖矿已结束");
                    text_gold.setText("激活挖矿");
                    text_gold.setBackgroundResource(R.mipmap.ic_gold_btn);
                    iv_chrome.setImageResource(R.mipmap.icon_gold_san);
                    break;
                case 3:
                    text_gold.setText("正在挖矿中");
                    text_gold.setBackgroundResource(0);
                    iv_chrome.setImageResource(0);
                    startGold();
                    break;
            }
        }
    }


    public void startGold() {
        iv_chrome.setImageResource(R.drawable.chrome);
        animationDrawable = (AnimationDrawable) iv_chrome.getDrawable();
        animationDrawable.start();
    }


    public void stopGold() {
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        stopGold();
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
        query1();
    }


    @Override
    public void onLoadMore() {
        isRefresh = true;
        page++;
        query1();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_gold:
                String name = text_gold.getText().toString();
                if ("激活挖矿".equals(name)) {
                    startActivity(new Intent(getContext(), ActivationActivity.class));
                } else if ("去绑定".equals(name)) {
                    startActivity(new Intent(getContext(), BindActivity.class));
                }
                break;
        }
    }
}
