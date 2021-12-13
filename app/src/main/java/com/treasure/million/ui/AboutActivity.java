package com.treasure.million.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.bugly.beta.UpgradeInfo;
import com.treasure.million.R;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.tencent.bugly.beta.Beta;
import com.treasure.million.base.BaseActivity;
import com.treasure.million.base.BaseApplication;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.bean.Verison;
import com.treasure.million.config.Api;
import com.treasure.million.config.Config;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.LogUtils;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SystemTools;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import com.treasure.million.weight.UpdateManager;
import org.json.JSONObject;
import java.util.List;
import java.util.Map;

/**
 * @author: zt
 * @date: 2020/5/27
 * @name:关于我们
 */
public class AboutActivity extends BaseActivity implements NetWorkListener {
    private TextView title_text_tv, title_left_btn, text_edition;
    private RelativeLayout rl_edition, rl_tab, rl_tab1;
    private Verison verison;

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about);
        BaseApplication.activityTaskManager.putActivity("AboutActivity", this);
    }

    @Override
    protected void initView() {
        rl_tab1 = getView(R.id.rl_tab1);
        text_edition = getView(R.id.text_edition);
        rl_tab = getView(R.id.rl_tab);
        rl_edition = getView(R.id.rl_edition);
        title_text_tv = getView(R.id.title_text_tv);
        title_left_btn = getView(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_text_tv.setText("关于我们");
        rl_edition.setOnClickListener(this);
        rl_tab.setOnClickListener(this);
        text_edition.setText("当前版本 v" + SystemTools.getAppVersionName(this));
        rl_tab1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_btn:
                finish();
                break;
            case R.id.rl_edition:
                Beta.checkUpgrade();
                UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
                if (upgradeInfo != null) {
                    LogUtils.e(upgradeInfo.apkUrl);
                    return;
                }
//                query();
                break;
            case R.id.rl_tab1:
                Intent intent = new Intent(this, PreviewActivity.class);
                intent.putExtra("name", "用户协议");
                intent.putExtra("url", Config.DEVELOPMENT_PUBLIC_SERVER_URL+"/resource/useragreement");
                startActivity(intent);
                break;
        }
    }


    /*****检测是否具有读写权限******/
    public void applyPermission() {
        PermissionX.init(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                if (allGranted) {
                    UpdateVerison();
                } else {
                    showSettingDialog();
                }
            }
        });
    }


    /*****检测是否具有安装未知来源的权限******/
    public void UpdateVerison() {
        new UpdateManager(this).checkForceUpdate(verison);
    }


    @Override
    protected void initData() {

    }

    /*******查询首页数据
     * @param ********/
    public void query() {
        String sign = "partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        showProgressDialog(this, false);
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_INTERGRAL_VERSION, params, Api.GET_INTERGRAL_VERSION_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.GET_INTERGRAL_VERSION_ID:
                        verison = JsonParse.getVerisonUserInfo(object);
                        if (verison != null) {
                            int code = SystemTools.getAppVersionCode(this);
                            if (!Utility.isEmpty(verison.getVersionIndex())) {
                                int versionCode = Integer.parseInt(verison.getVersionIndex());
                                if (versionCode > code) {
                                    applyPermission();
                                } else {
                                    ToastUtil.showToast("暂无新版本");
                                }
                            }

                        }
                        break;
                }
            }
        }
        stopProgressDialog();
    }

    @Override
    public void onFail() {
        stopProgressDialog();
    }

    @Override
    public void onError(Exception e) {
        stopProgressDialog();
    }
}
