package com.treasure.million.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.fragment.app.FragmentTabHost;

import com.amap.api.maps.MapsInitializer;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.treasure.million.R;
import com.treasure.million.base.BaseActivity1;
import com.treasure.million.base.BaseApplication;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.bean.UserInfo;
import com.treasure.million.bean.Verison;
import com.treasure.million.config.Api;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.ui.fragment.CarFragment;
import com.treasure.million.ui.fragment.MeFragment;
import com.treasure.million.ui.fragment.MoneyFragment;
import com.treasure.million.ui.fragment.OreLostragment;
import com.treasure.million.ui.fragment.WisFragment;
import com.treasure.million.util.ActivityManager;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.LogUtils;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.SystemTools;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import com.treasure.million.weight.DialogUtils;
import com.treasure.million.weight.UpdateManager;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/*****
 *
 * 首页
 *
 */
public class MainActivity extends BaseActivity1 implements NetWorkListener {
    public Class fragments[] = {CarFragment.class, WisFragment.class, OreLostragment.class, MoneyFragment.class, MeFragment.class};
    public int drawables[] = {R.drawable.book_drawable, R.drawable.chosen_drawable1, R.drawable.chosen_drawable, R.drawable.chosen_drawable2, R.drawable.me_drawable};
    public String textviewArray[] = {"玖亿宝盒", "惠购车", "矿池", "理财", "我的"};
    public FragmentTabHost mTabHost;
    public Verison verison;

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        BaseApplication.activityTaskManager.putActivity("MainActivity", this);
    }

    @Override
    protected void initView() {
        mTabHost = getView(R.id.mTabHost);
        queryUser();
        query();
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
        if (upgradeInfo != null) {
            LogUtils.e(upgradeInfo.apkUrl);
            return;
        }
    }



    @Override
    protected void initData() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.mFrameLayout);
        for (int i = 0; i < fragments.length; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(textviewArray[i]).setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, fragments[i], null);
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.colorAccent);
        }
        TabHost.OnTabChangeListener l = tabId -> {
            try {
                if (tabId.equals(textviewArray[0])) {
                    clearViewColor();
                    TextView tv_home = mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.textview);
                    tv_home.setTextColor(Color.parseColor("#3F69F4"));
                }
                if (tabId.equals(textviewArray[1])) {
                    clearViewColor();
                    TextView tv_order = mTabHost.getTabWidget().getChildAt(1).findViewById(R.id.textview);
                    tv_order.setTextColor(Color.parseColor("#3F69F4"));
                }

                if (tabId.equals(textviewArray[2])) {
                    clearViewColor();
                    TextView tv_mine = mTabHost.getTabWidget().getChildAt(2).findViewById(R.id.textview);
                    tv_mine.setTextColor(Color.parseColor("#3F69F4"));
                }

                if (tabId.equals(textviewArray[3])) {
                    clearViewColor();
                    TextView tv_mine = mTabHost.getTabWidget().getChildAt(3).findViewById(R.id.textview);
                    tv_mine.setTextColor(Color.parseColor("#3F69F4"));
                }

                if (tabId.equals(textviewArray[4])) {
                    clearViewColor();
                    TextView tv_mine = mTabHost.getTabWidget().getChildAt(4).findViewById(R.id.textview);
                    tv_mine.setTextColor(Color.parseColor("#3F69F4"));
                }
            } catch (Exception e) {
            }
        };
        int index = getIntent().getIntExtra("index", 0);
        setCurrentTab(index);
        mTabHost.setOnTabChangedListener(l);
        mTabHost.getTabWidget().setDividerDrawable(null);
    }


    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item_view, null);
        ImageView imageView = view.findViewById(R.id.imageview);
        imageView.setImageResource(drawables[index]);
        TextView textView = view.findViewById(R.id.textview);
        textView.setText(textviewArray[index]);
        return view;
    }


    public void setCurrentTab(int index) {
        mTabHost.setCurrentTab(index);
        clearViewColor();
        TextView tv_order = mTabHost.getTabWidget().getChildAt(index).findViewById(R.id.textview);
        tv_order.setTextColor(Color.parseColor("#3F69F4"));
    }


    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                ToastUtil.showToast("再按一次后退键退出程序");
                firstTime = secondTime;
                return true;
            } else {
                OnEventExit();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    /**
     * 退出应用
     */
    public void OnEventExit() {
        try {
            ActivityManager.getInstance().finishAllActivity();
            finish();
        } catch (Exception e) {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }


    /******
     * 清除所有的颜色
     */
    public void clearViewColor() {
        TextView tv_home = mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.textview);
        tv_home.setTextColor(Color.parseColor("#666666"));
        TextView tv_order = mTabHost.getTabWidget().getChildAt(1).findViewById(R.id.textview);
        tv_order.setTextColor(Color.parseColor("#666666"));
        TextView tv_mine2 = mTabHost.getTabWidget().getChildAt(2).findViewById(R.id.textview);
        tv_mine2.setTextColor(Color.parseColor("#666666"));
        TextView tv_mine3 = mTabHost.getTabWidget().getChildAt(3).findViewById(R.id.textview);
        tv_mine3.setTextColor(Color.parseColor("#666666"));
        TextView tv_mine4 = mTabHost.getTabWidget().getChildAt(4).findViewById(R.id.textview);
        tv_mine4.setTextColor(Color.parseColor("#666666"));
    }

    /*******绑定Jpush
     * @param ********/
    public void queryPush() {
        String registrationID = JPushInterface.getRegistrationID(this);
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID + "&registerid=" + registrationID + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("partnerid", Constants.PARTNERID);
        params.put("memberid", SaveUtils.getSaveInfo().getId() + "");
        params.put("registerid", registrationID + "");
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_PUSH_VERSION, params, Api.GET_PUSH_VERSION_ID, this);
    }


    /******版本升级*****/
    public void query() {
        String sign = "partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_INTERGRAL_VERSION, params, Api.GET_INTERGRAL_VERSION_ID, this);
    }


    /******查询个人资料*****/
    public void queryUser() {
        if (SaveUtils.getSaveInfo() == null) {
            return;
        }
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("partnerid", Constants.PARTNERID);
        params.put("apptype", Constants.TYPE);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_MEID_USER, params, Api.GET_MEID_USER_ID, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Beta.checkUpgrade(false, false);
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
                                if (versionCode < code) {
                                    applyPermission();
                                }
                            }

                        }
                        break;
                    case Api.GET_MEID_USER_ID:
                        UserInfo info = JsonParse.getUserInfo(object);
                        if (info != null) {
                            SaveUtils.saveInfo(info);
                            queryPush();
                        }
                        break;
                }
            } else {
                if (id == Api.RESET_TOKEN_ID) {
                    DialogUtils.showLogin(this, commonality.getErrorDesc());
                } else {
                    ToastUtil.showToast(commonality.getErrorDesc());
                }
            }

        }
        stopProgressDialog();
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
    public void onFail() {
        stopProgressDialog();
    }

    @Override
    public void onError(Exception e) {
        stopProgressDialog();
    }
}
