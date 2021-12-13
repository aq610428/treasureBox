package com.treasure.million.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.treasure.million.R;
import com.treasure.million.adapter.AsetsAdapter;
import com.treasure.million.base.BaseFragment1;
import com.treasure.million.bean.AssetsBean;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.bean.UsdBean;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.ui.DrawActivity;
import com.treasure.million.ui.RechargeActivity;
import com.treasure.million.ui.SwitchActivity;
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
 * @author: zt
 * @date: 2020/10/20
 * @name:TabFragment1
 */
public class TabFragment4 extends BaseFragment1 implements NetWorkListener, OnRefreshListener, View.OnClickListener, OnLoadMoreListener {
    private int limit = 10;
    private int page = 1;
    private boolean isRefresh;
    private UsdBean usdBean;
    private TextView text_usd, text_usdt_charge, text_usdt_carry, text_box_charge, text_box_carry,text_eth_charge,text_eth_carry;
    private TextView text_usdt, text_box,text_eth,text_exchange1,text_exchange;
    private String coinTypeId = "USDT";
    private List<AssetsBean> data = new ArrayList<>();
    private AsetsAdapter asetsAdapter;
    private RecyclerView recyclerView;
    private SwipeToLoadLayout swipeToLoadLayout;

    @Override
    protected void loadData() {
        query();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab4;
    }

    @Override
    protected void initView() {
        text_exchange= getView(rootView, R.id.text_exchange);
        text_exchange1= getView(rootView, R.id.text_exchange1);
        text_eth_carry= getView(rootView, R.id.text_eth_carry);
        text_eth_charge= getView(rootView, R.id.text_eth_charge);
        text_eth= getView(rootView, R.id.text_eth);
        swipeToLoadLayout = getView(rootView, R.id.swipeToLoadLayout);
        recyclerView = getView(rootView, R.id.recyclerView);
        text_box = getView(rootView, R.id.text_box);
        text_usdt = getView(rootView, R.id.text_usdt);
        text_usdt_charge = getView(rootView, R.id.text_usdt_charge);
        text_usdt_carry = getView(rootView, R.id.text_usdt_carry);
        text_box_charge = getView(rootView, R.id.text_box_charge);
        text_box_carry = getView(rootView, R.id.text_box_carry);
        text_usd = getView(rootView, R.id.text_usd);
        text_usd.setOnClickListener(this);
        text_usdt_charge.setOnClickListener(this);
        text_usdt_carry.setOnClickListener(this);
        text_box_charge.setOnClickListener(this);
        text_box_carry.setOnClickListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setNestedScrollingEnabled(false);
        text_eth_carry.setOnClickListener(this);
        text_eth_charge.setOnClickListener(this);
        text_exchange.setOnClickListener(this);
        text_exchange1.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    public void load() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        showProgressDialog(getActivity(), false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("limit", limit + "");
        params.put("page", page + "");
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.MINING_BAL_BOX, params, Api.GETRECORD_BOX_ID, this);
    }


    public void query() {
        String sign = "coinTypeId=" + coinTypeId + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        showProgressDialog(getActivity(), false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("coinTypeId", coinTypeId + "");
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID1);
        params.put("limit", limit + "");
        params.put("page", page + "");
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GETRECORD_BOX, params, Api.MINING_BOX_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.GETRECORD_BOX_ID:
                        usdBean = JsonParse.getJSONObjectUsdtBean(object);
                        if (usdBean != null) {
                            updateView();
                        }
                        break;
                    case Api.MINING_BOX_ID:
                        List<AssetsBean> assetsBeans = JsonParse.getJSONObjectAssetsBean(object);
                        if (assetsBeans != null && assetsBeans.size() > 0) {
                            setAdapter(assetsBeans);
                        } else {
                            if (isRefresh && page > 0) {
                                ToastUtil.showToast("无更多数据");
                            }
                            if (!isRefresh&&page==1){
                                data.clear();
                                asetsAdapter.setData(data);
                                asetsAdapter.notifyDataSetChanged();
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


    private void setAdapter(List<AssetsBean> assetsBeans) {
        if (!isRefresh) {
            data.clear();
            data.addAll(assetsBeans);
            asetsAdapter = new AsetsAdapter(getContext(), data);
            recyclerView.setAdapter(asetsAdapter);
        } else {
            data.addAll(assetsBeans);
            asetsAdapter.setData(data);
            asetsAdapter.notifyDataSetChanged();
        }
    }

    private void updateView() {
        if (usdBean.getUsdt()!= null) {
            text_usdt.setText(BigDecimalUtils.round(new BigDecimal(usdBean.getUsdt().getUserable()),6).toPlainString()+ "");
        }
        if (usdBean.getEth() != null) {
            text_eth.setText(BigDecimalUtils.round(new BigDecimal(usdBean.getEth().getUserable()),6).toPlainString() + "");
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
        load();
        query();
    }

    @Override
    public void onLoadMore() {
        isRefresh = true;
        page++;
        query();
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.text_exchange1:
                if (usdBean != null && usdBean.getEth() != null) {
                    intent = new Intent(getContext(), SwitchActivity.class);
                    intent.putExtra("usdtBean", usdBean);
                    intent.putExtra("coinTypeId", usdBean.getEth().getCoinTypeId());
                    startActivity(intent);
                }

                break;
            case R.id.text_exchange:
                if (usdBean != null && usdBean.getUsdt() != null) {
                    intent = new Intent(getContext(), SwitchActivity.class);
                    intent.putExtra("usdtBean", usdBean);
                    intent.putExtra("coinTypeId", usdBean.getUsdt().getCoinTypeId());
                    startActivity(intent);
                }
                break;


            case R.id.text_usdt_charge:
                if (usdBean != null && usdBean.getUsdt() != null) {
                    intent = new Intent(getContext(), RechargeActivity.class);
                    intent.putExtra("usdtBean", usdBean);
                    intent.putExtra("coinTypeId", usdBean.getUsdt().getCoinTypeId());
                    startActivity(intent);
                }
                break;
            case R.id.text_usdt_carry:
                if (usdBean != null && usdBean.getUsdt() != null) {
                    intent = new Intent(getContext(), DrawActivity.class);
                    intent.putExtra("usdtBean", usdBean);
                    intent.putExtra("coinTypeId", usdBean.getUsdt().getCoinTypeId());
                    startActivity(intent);
                }
                break;
            case R.id.text_box_charge:
                if (usdBean != null && usdBean.getBox() != null) {
                    intent = new Intent(getContext(), RechargeActivity.class);
                    intent.putExtra("usdtBean", usdBean);
                    intent.putExtra("coinTypeId", usdBean.getBox().getCoinTypeId());
                    startActivity(intent);
                }
                break;
            case R.id.text_box_carry:
                if (usdBean != null && usdBean.getBox() != null) {
                    intent = new Intent(getContext(), DrawActivity.class);
                    intent.putExtra("usdtBean", usdBean);
                    intent.putExtra("coinTypeId", usdBean.getBox().getCoinTypeId());
                    startActivity(intent);
                }
                break;
            case R.id.text_eth_charge:
                if (usdBean != null && usdBean.getEth() != null) {
                    intent = new Intent(getContext(), RechargeActivity.class);
                    intent.putExtra("usdtBean", usdBean);
                    intent.putExtra("coinTypeId", usdBean.getEth().getCoinTypeId());
                    startActivity(intent);
                }
                break;

            case R.id.text_eth_carry:
                if (usdBean != null && usdBean.getEth() != null) {
                    intent = new Intent(getContext(), DrawActivity.class);
                    intent.putExtra("usdtBean", usdBean);
                    intent.putExtra("coinTypeId", usdBean.getEth().getCoinTypeId());
                    startActivity(intent);
                }
                break;

            case R.id.text_usd:
                showUsdt();
                break;

        }
    }


    public void showUsdt() {
        Dialog dialog = new Dialog(getContext(), R.style.dialog_bottom_full);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_map, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.share_animation);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        view.findViewById(R.id.text_usd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usdBean != null && usdBean.getUsdt() != null) {
                    coinTypeId = usdBean.getUsdt().getCoinTypeId();
                    text_usd.setText(usdBean.getUsdt().getCoinTypeName());
                    isRefresh = false;
                    page = 1;
                    query();
                }
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.text_bc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usdBean != null && usdBean.getBox() != null) {
                    coinTypeId = usdBean.getBox().getCoinTypeId();
                    text_usd.setText(usdBean.getBox().getCoinTypeName());
                    isRefresh = false;
                    page = 1;
                    query();
                }
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.text_eth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usdBean != null && usdBean.getEth() != null) {
                    coinTypeId = usdBean.getEth().getCoinTypeId();
                    text_usd.setText(usdBean.getEth().getCoinTypeName());
                    isRefresh = false;
                    page = 1;
                    query();
                }
                dialog.dismiss();
            }
        });


        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
