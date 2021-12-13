package com.treasure.million.weight;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.treasure.million.R;
import com.treasure.million.base.BaseApplication;
import com.treasure.million.glide.KilomeListener;
import com.treasure.million.ui.ActivationActivity;
import com.treasure.million.ui.LicenseActivity;
import com.treasure.million.ui.LoginActivity;
import com.treasure.million.ui.MainActivity;
import com.treasure.million.ui.TransactionActivity;
import com.treasure.million.ui.fragment.MoneyFragment;
import com.treasure.million.util.Constants;
import com.treasure.million.util.SaveUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;


public class DialogUtils {


    public static void showBoxMsg(Activity activity) {
        Dialog dialog = new Dialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_layout_box, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if ("0".equals(SaveUtils.getSaveInfo().getIsPayPassword() + "")) {//未设置
                    DialogUtils.showPassword(activity);
                }
            }
        });
        dialog.show();
    }


    public static void showBind(int type, Activity activity) {
        Dialog dialog = new Dialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_layout_ming, null);
        TextView text_name = view.findViewById(R.id.text_name);
        if (type == 1) {
            text_name.setText("您尚未购买卡贝车宝矿机，快去购买吧");
        } else {
            text_name.setText("您尚未激活box,快去激活吧~");
        }
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
                if (type == 1) {
                    if (activity != null) {
                        MainActivity activity1 = (MainActivity) activity;
                        if (activity1 != null) {
                            activity1.setCurrentTab(1);
                        }
                    }
                } else {
                    activity.startActivity(new Intent(activity, ActivationActivity.class));
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void showPay(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout_ming, null);
        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText("您当前的挖矿已结束，是否要再次激活？");
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
                context.startActivity(new Intent(context, ActivationActivity.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void showPay1(MoneyFragment fragment, KilomeListener kilomeListener) {
        if (fragment.getActivity()==null){
            return;
        }
        Dialog mDialog = new Dialog(fragment.getActivity(), R.style.dialog_bottom_full);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//可取消 为true
        Window window = mDialog.getWindow();      // 得到dialog的窗体
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.share_animation);
        View view = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.dialog_layout_pass, null);
        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏

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
                kilomeListener.onKilomeListener(pass);
            }
        });
    }

    public static void showPassword(Context context) {
        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout_ming, null);
        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText("您尚未设置交易密码，快去设置吧~");
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
                context.startActivity(new Intent(context, TransactionActivity.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void showPassword1(Activity activity) {
        Dialog dialog = new Dialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_layout_ming, null);
        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText("您的账户已锁仓");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        view.findViewById(R.id.cancel).setVisibility(View.GONE);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void showPassword2(Activity activity) {
        Dialog dialog = new Dialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_layout_ming, null);
        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText("您的账户已锁仓");
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        view.findViewById(R.id.cancel).setVisibility(View.GONE);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showLogin(Context context, String msg) {
        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout_ming, null);
        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText(msg + "");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        view.findViewById(R.id.cancel).setVisibility(View.GONE);
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseApplication.activityTaskManager.closeAllActivityExceptOne("LoginActivity");
                context.startActivity(new Intent(context, LoginActivity.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void showDialog(final String phoneNum, Activity activity) {
        final Dialog dialog = new Dialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_layout1, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        ((TextView) view.findViewById(R.id.title)).setText("温馨提示");
        ((TextView) view.findViewById(R.id.message)).setText("确定退出卡贝车保?");
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    public static void showTipDialog(Context context, String msg) {
        final Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout1, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        ((TextView) view.findViewById(R.id.title)).setText("温馨提示");
        ((TextView) view.findViewById(R.id.message)).setText(msg);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LicenseActivity.class);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
