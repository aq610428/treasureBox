package com.treasure.million.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import com.amap.api.maps.MapsInitializer;
import com.treasure.million.R;
import com.treasure.million.base.BaseActivity1;
import com.treasure.million.base.BaseApplication;
import com.treasure.million.bean.UserInfo;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.Utility;


/***
 *
 * 启动页面
 *
 *
 */
public class StartActivity extends BaseActivity1 {
    private UserInfo info;

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_start);
        BaseApplication.activityTaskManager.putActivity("StartActivity", this);
        info = SaveUtils.getSaveInfo();

    }

    @Override
    protected void initView() {
        String map = SaveUtils.getMap();
        if (Utility.isEmpty(map)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    privacyCompliance();
                }
            }, 1500);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            }, 2000);
        }

    }


    public void start() {
        if (info != null) {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
        }
        finish();
    }


    private void privacyCompliance() {
        MapsInitializer.updatePrivacyShow(StartActivity.this, true, true);
        SpannableStringBuilder spannable = new SpannableStringBuilder("亲，感谢您对玖亿宝盒一直以来的信任！我们依据最新的监管要求更新了玖亿宝盒《隐私权政策》，特向您说明如下\n1.为向您提供交易相关基本功能，我们会收集、使用必要的信息；\n2.基于您的明示授权，我们可能会获取您的位置（为您提供附近的商品、店铺及优惠资讯等）等信息，您有权拒绝或取消授权；\n3.我们会采取业界先进的安全措施保护您的信息安全；\n4.未经您同意，我们不会从第三方处获取、共享或向提供您的信息；\n");
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 35, 42, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        new AlertDialog.Builder(this)
                .setTitle("温馨提示(隐私合规示例)")
                .setMessage(spannable)
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SaveUtils.saveMap("true");
                        MapsInitializer.updatePrivacyAgree(StartActivity.this, true);
                        start();
                    }
                })
                .setNegativeButton("不同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SaveUtils.saveMap("true");
                        MapsInitializer.updatePrivacyAgree(StartActivity.this, true);
                        start();
                    }
                })
                .show();
    }


    @Override
    protected void initData() {

    }
}
