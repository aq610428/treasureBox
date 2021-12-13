package com.treasure.million.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.treasure.million.R;
import com.treasure.million.adapter.RedAdapter;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.bean.RedVo;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import com.treasure.million.weight.NoDataView;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: RedListActivity
 * @Description: 已购列表
 * @Author: zt
 * @Date: 2021/10/28 17:52
 */
public class RedListActivity extends BaseActivity implements NetWorkListener, OnRefreshListener, OnLoadMoreListener {
    private TextView title_text_tv, title_left_btn;
    private RecyclerView recyclerView;
    private RedAdapter adapter;
    private List<RedVo> data = new ArrayList<>();
    private int page = 1;
    private int limit = 10;
    private boolean isRefresh;
    private SwipeToLoadLayout swipeToLoadLayout;
    private NoDataView noDataView;

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_red);
    }

    @Override
    protected void initView() {
        noDataView = getView(R.id.noDataView);
        recyclerView = getView(R.id.swipe_target);
        swipeToLoadLayout = getView(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        title_left_btn.setOnClickListener(this);
        noDataView.textView.setText("暂无购买记录");
        title_text_tv.setText("已购列表");
    }

    @Override
    protected void initData() {
        query();
    }

    private void query() {
        String id = getIntent().getStringExtra("id");
        String sign = "financeid=" + id + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1 + Constants.SECREKEY1;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("financeid", id + "");
        params.put("memberid", SaveUtils.getSaveInfo().getId() + "");
        params.put("partnerid", Constants.PARTNERID1);
        params.put("limit", limit + "");
        params.put("page", page + "");
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.post(Api.REDE_LIST_PROF1, params, Api.REDE_LIST_PROF1_ID, this);
    }


    private void redeem(String id,String password) {
        String sign = "id=" + id + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID1
                + "&paypassword=" + Md5Util.encode(password)
                + Constants.SECREKEY1;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("memberid", SaveUtils.getSaveInfo().getId() + "");
        params.put("id", id + "");
        params.put("partnerid", Constants.PARTNERID1);
        params.put("paypassword", Md5Util.encode(password));
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.post(Api.REDE_LIST_FINANCE, params, Api.REDE_LIST_FINANCE1_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.REDE_LIST_PROF1_ID:
                        List<RedVo> data = JsonParse.getRedVoJson(object);
                        if (data != null && data.size() > 0) {
                            setAdapter(data);
                        } else {
                            if (!isRefresh && page == 1) {
                                noDataView.setVisibility(View.VISIBLE);
                                swipeToLoadLayout.setVisibility(View.GONE);
                            }
                            if (isRefresh && page > 0) {
                                ToastUtil.showToast("无更多数据");
                            }
                        }
                        break;
                    case Api.REDE_LIST_FINANCE1_ID:
                        ToastUtil.showToast(commonality.getErrorDesc());
                        onRefresh();
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

    private void setAdapter(List<RedVo> infos) {
        noDataView.setVisibility(View.GONE);
        swipeToLoadLayout.setVisibility(View.VISIBLE);

        if (!isRefresh) {
            data.clear();
            data.addAll(infos);
            adapter = new RedAdapter(this, data);
            recyclerView.setAdapter(adapter);
        } else {
            data.addAll(infos);
            adapter.setData(data);
            adapter.notifyDataSetChanged();
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
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_btn:
                finish();
                break;
        }
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

    public void showPay1(String id) {
        Dialog mDialog = new Dialog(this, R.style.dialog_bottom_full);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);             //可取消 为true
        Window window = mDialog.getWindow();      // 得到dialog的窗体
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.share_animation);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout_pass, null);
        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(view);
        mDialog.show();
        EditText editText = view.findViewById(R.id.edit_pass);
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                String pass = editText.getText().toString();
                if (Utility.isEmpty(pass)) {
                    ToastUtil.showToast("资金密码不能为空");
                    return;
                }
                redeem(id,pass);
            }
        });

    }

}
