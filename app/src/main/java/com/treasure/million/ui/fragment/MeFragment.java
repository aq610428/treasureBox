package com.treasure.million.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.treasure.million.R;
import com.lihang.ShadowLayout;
import com.treasure.million.adapter.MeAdapter;
import com.treasure.million.base.BaseApplication;
import com.treasure.million.base.BaseFragment;
import com.treasure.million.bean.Block;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.bean.Massage;
import com.treasure.million.bean.UserInfo;
import com.treasure.million.config.Api;
import com.treasure.million.config.Config;
import com.treasure.million.config.NetWorkListener;
import com.treasure.million.config.okHttpModel;
import com.treasure.million.glide.GlideUtils;
import com.treasure.million.ui.AboutActivity;
import com.treasure.million.ui.ActivationActivity;
import com.treasure.million.ui.InvitationActivity;
import com.treasure.million.ui.LoginActivity;
import com.treasure.million.ui.MsgActivity;
import com.treasure.million.ui.PassworadActivity;
import com.treasure.million.ui.PreviewActivity;
import com.treasure.million.ui.SalesActivity;
import com.treasure.million.ui.UserActivity;
import com.treasure.million.util.Constants;
import com.treasure.million.util.JsonParse;
import com.treasure.million.util.LogUtils;
import com.treasure.million.util.Md5Util;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;
import com.treasure.million.weight.MarqueeTextView;
import com.treasure.million.weight.PreferenceUtils;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import crossoverone.statuslib.StatusUtil;

/**
 * @author: zt
 * @date: 2020/7/2
 * @name:我的
 */
public class MeFragment extends BaseFragment implements NetWorkListener, View.OnClickListener {
    private View rootView;
    private TextView text_name, text_edit;
    private UserInfo info;
    private ImageView icon_head;
    private RecyclerView recyclerView, recyclerView1;
    private List<Block> array = new ArrayList<>();
    private List<Block> list = new ArrayList<>();
    private MeAdapter adapter, adapter1;
    private ShadowLayout rl_note;
    private MarqueeTextView marqueeTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_me, container, false);
            initView();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        StatusUtil.setUseStatusBarColor(getActivity(), Color.parseColor("#5476FF"));
        StatusUtil.setSystemStatus(getActivity(), false, false);
        info = SaveUtils.getSaveInfo();
        if (Utility.isEmpty(info.getNickname())) {
            text_name.setText(info.getMobile());
        } else {
            text_name.setText(info.getNickname());
        }
        if (!Utility.isEmpty(info.getUsericon())) {
            GlideUtils.CreateImageCircular(info.getUsericon(), icon_head, 5);
        }
        query();
    }

    private void initView() {
        text_edit = getView(rootView, R.id.text_edit);
        recyclerView1 = getView(rootView, R.id.recyclerView1);
        rl_note = getView(rootView, R.id.rl_note);
        marqueeTextView = getView(rootView, R.id.mENoticeView);
        recyclerView = getView(rootView, R.id.recyclerView);
        icon_head = getView(rootView, R.id.icon_head);
        text_name = getView(rootView, R.id.text_name);
        text_edit.setOnClickListener(this);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(manager);

        GridLayoutManager manager1 = new GridLayoutManager(getContext(), 4);
        recyclerView1.setLayoutManager(manager1);

        list = SaveUtils.getblocks();
        adapter1 = new MeAdapter(getContext(), list);
        recyclerView1.setAdapter(adapter1);

        adapter1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = list.get(position).getName();
                switch (name) {
                    case "我的团队":
                        Intent intent1 = new Intent(getContext(), PreviewActivity.class);
                        intent1.putExtra("name", "我的团队");
                        intent1.putExtra("url", Config.DEVELOPMENT_PUBLIC_SERVER_URL+"/mining/myteam?friendcode=" + SaveUtils.getSaveInfo().getRmcode() + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&apptype=" + Constants.TYPE);
                        startActivity(intent1);
                        break;
                    case "邀请好友":
                        startActivity(new Intent(getContext(), InvitationActivity.class));
                        break;
                    case "激活挖矿":
                        startActivity(new Intent(getContext(), ActivationActivity.class));
                        break;
                    case "流量查询":
                        startActivity(new Intent(getContext(), PassworadActivity.class));
                        break;
                }
            }
        });
        array = SaveUtils.getArray();
        adapter = new MeAdapter(getContext(), array);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = array.get(position).getName();
                switch (name) {
                    case "交易密码":
                        startActivity(new Intent(getContext(), SalesActivity.class));
                        break;
                    case "联系我们":
                        Intent intent = new Intent(getContext(), PreviewActivity.class);
                        intent.putExtra("name", "加入社群");
                        intent.putExtra("url", "http://8.134.127.246:8083/resource/about");
                        startActivity(intent);
                        break;
                    case "设备解除":
                        showBindDialog();
                        break;
                    case "关于我们":
                        startActivity(new Intent(getContext(), AboutActivity.class));
                        break;
                    case "消息中心":
                        startActivity(new Intent(getContext(), MsgActivity.class));
                        break;
                    case "退出登录":
                        showDialog();
                        break;
                    case "我的资产":
                        startActivity(new Intent(getContext(), AssetsFragmnt.class));
                        break;
                    case "邀请好友":
                        startActivity(new Intent(getContext(), InvitationActivity.class));
                        break;

                    case "我的团队":
                        Intent intent1 = new Intent(getContext(), PreviewActivity.class);
                        intent1.putExtra("name", "我的团队");
                        intent1.putExtra("url", Config.getOpenNewApi()+"/box/myteam/box/myteam?friendcode=" + SaveUtils.getSaveInfo().getRmcode() + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&apptype=" + Constants.TYPE);
                        startActivity(intent1);
                        break;
                }
            }
        });
    }


    private void bind(String code) {
        if ( SaveUtils.getCar()==null){
            return;
        }
        String sign = "id=" + SaveUtils.getCar().getId() + "&imeicode=" + SaveUtils.getCar().getImeicode() + "&loginname=" +
                SaveUtils.getSaveInfo().getLoginname()
                + "&memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID + "&vcode=" + code + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("apptype", Constants.TYPE);
        params.put("id", SaveUtils.getCar().getId() + "");
        params.put("imeicode", SaveUtils.getCar().getImeicode() + "");
        params.put("loginname", SaveUtils.getSaveInfo().getLoginname() + "");
        params.put("memberid", SaveUtils.getSaveInfo().getId() + "");
        params.put("partnerid", Constants.PARTNERID);
        params.put("vcode", code + "");
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_UNMODEL_BIND, params, Api.GET_UNMODEL_BIND_ID, this);
    }

    public void queryCode() {
        String sign = "mobile=" + SaveUtils.getSaveInfo().getMobile() + "&partnerid=" + Constants.PARTNERID + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("mobile", SaveUtils.getSaveInfo().getMobile());
        params.put("apptype", Constants.TYPE);
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_MOBILCODE, params, Api.GET_MOBILCODE_ID, this);
    }


    public void query() {
        String sign = "memberid=" + SaveUtils.getSaveInfo().getId() + "&partnerid=" + Constants.PARTNERID + "&type=5" + Constants.SECREKEY;
        Map<String, String> params = okHttpModel.getParams();
        params.put("memberid", SaveUtils.getSaveInfo().getId());
        params.put("type", "5");
        params.put("apptype", Constants.TYPE);
        params.put("partnerid", Constants.PARTNERID);
        params.put("sign", Md5Util.encode(sign));
        okHttpModel.get(Api.GET_MOBILCODE_NOTE, params, Api.GET_UPDATE_ID, this);
    }


    @Override
    public void onSucceed(JSONObject object, int id, CommonalityModel commonality) {
        if (object != null && commonality != null && !Utility.isEmpty(commonality.getStatusCode())) {
            if (Constants.SUCESSCODE.equals(commonality.getStatusCode())) {
                switch (id) {
                    case Api.GET_UNMODEL_BIND_ID:
                        ToastUtil.showToast(commonality.getErrorDesc());
                        SaveUtils.saveCar(null);
                        break;
                    case Api.GET_MOBILCODE_ID:
                        ToastUtil.showToast(commonality.getErrorDesc());
                        break;
                    case Api.GET_UPDATE_ID:
                        Massage massage = JsonParse.getEarlyInfoJson2(object);
                        updateView(massage);
                        break;
                }
            } else {
                ToastUtil.showToast(commonality.getErrorDesc());
            }
        }
    }

    /********公告******/
    private void updateView(Massage massage) {
        if (massage != null) {
            rl_note.setVisibility(View.VISIBLE);
            marqueeTextView.setText(massage.getContent().replaceAll("<p>","").replaceAll("<br>","").replaceAll("<br/>","").replaceAll("</p>","")+"");
            marqueeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MsgActivity.class);
                    intent.putExtra("index", 2);
                    startActivity(intent);
                }
            });
        } else {
            rl_note.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFail() {
        stopProgressDialog();
    }

    @Override
    public void onError(Exception e) {
        stopProgressDialog();
    }

    CountDownTimer timer;
    private void countDown(TextView btn_code) {
        timer = new CountDownTimer(90000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btn_code.setEnabled(false);
                btn_code.setText("已发送(" + millisUntilFinished / 1000 + ")");
            }

            @Override
            public void onFinish() {
                btn_code.setEnabled(true);
                btn_code.setText("重新获取验证码");

            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_edit:
                startActivity(new Intent(getContext(), UserActivity.class));
                break;
        }
    }

    public void showDialog() {
        Dialog dialog = new Dialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout_mine, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveUtils.clealCacheDisk();
                PreferenceUtils.setPrefString(getContext(), Constants.TOKEN, "");
                String mobile = PreferenceUtils.getPrefString(getContext(), Constants.MOBILE, "");
                String password = PreferenceUtils.getPrefString(getContext(), Constants.PASSWORD, "");
                final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("north", Context.MODE_PRIVATE);
                PreferenceUtils.clearPreference(getContext(), sharedPreferences);
                PreferenceUtils.setPrefString(getContext(), Constants.MOBILE, mobile);
                PreferenceUtils.setPrefString(getContext(), Constants.PASSWORD, password);
                BaseApplication.activityTaskManager.closeAllActivityExceptOne("LoginActivity");
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void showBindDialog() {
        Dialog dialog = new Dialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout_bind, null);
        EditText et_code = view.findViewById(R.id.et_code);
        TextView btn_code = view.findViewById(R.id.btn_code);
        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryCode();
                countDown(btn_code);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = et_code.getText().toString();
                if (Utility.isEmpty(code)) {
                    ToastUtil.showToast("验证码不能为空");
                    return;
                }
                bind(code);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
