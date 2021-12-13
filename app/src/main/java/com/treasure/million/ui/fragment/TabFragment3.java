package com.treasure.million.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.treasure.million.R;
import com.treasure.million.adapter.DefiAdapter;
import com.treasure.million.base.BaseFragment1;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.bean.TabBean;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.ui.DefiActivity;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.TypefaceUtil;
import com.treasure.million.util.Utility;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/10/20
 * @name:TabFragment1
 */
public class TabFragment3 extends BaseFragment1 implements OnRefreshListener, NetWorkListener {
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView swipe_target;
    private TextView text_tab1, text_tab2, text_tab3;
    private DefiAdapter defiAdapter;
    private List<TabBean.ItemsBean> beans = new ArrayList<>();
    private TabBean tabBean;

    @Override
    protected void loadData() {
        query();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab3;
    }

    @Override
    protected void initView() {
        swipe_target = rootView.findViewById(R.id.mListView);
        swipeToLoadLayout = rootView.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipe_target.setNestedScrollingEnabled(false);
        text_tab1 = getView(rootView, R.id.text_tab1);
        text_tab2 = getView(rootView, R.id.text_tab2);
        text_tab3 = getView(rootView, R.id.text_tab3);
        TypefaceUtil.setTextType(getActivity(), "DINOT-Bold.ttf", text_tab1);
        TypefaceUtil.setTextType(getActivity(), "DINOT-Bold.ttf", text_tab2);
        TypefaceUtil.setTextType(getActivity(), "DINOT-Bold.ttf", text_tab3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        swipe_target.setLayoutManager(layoutManager);

    }


    @Override
    public void onRefresh() {
        query();
    }


    public void query() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.PAY_CHANCE_BOX, params, Api.PAY_CHANCE_BOX_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.PAY_CHANCE_BOX_ID:
                        tabBean = JsonParse.getTabBeanJSON(object);
                        if (tabBean != null) {
                            setAdapter();
                        }
                        break;
                }
            }
        }
        stopProgressDialog();
        swipeToLoadLayout.setRefreshing(false);
    }


    private void setAdapter() {
        text_tab1.setText(tabBean.getManage_amount() + "");
        text_tab2.setText(tabBean.getManage_num() + "");
        text_tab3.setText(tabBean.getProfit_amount() + "");
        beans = tabBean.getItems();
        if (beans != null && beans.size() > 0) {
            defiAdapter = new DefiAdapter(getContext(), beans);
            swipe_target.setAdapter(defiAdapter);
            defiAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getContext(), DefiActivity.class);
                    intent.putExtra("tabBean", tabBean);
                    intent.putExtra("type", beans.get(position).getType() + "");
                    intent.putExtra("mouth", beans.get(position).getZq() + "");
                    intent.putExtra("lv", beans.get(position).getLv() + "");
                    startActivity(intent);
                }
            });
        }


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
}
