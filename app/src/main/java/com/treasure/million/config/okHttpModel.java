package com.treasure.million.config;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;
import com.treasure.million.base.BaseApplication;
import com.treasure.million.bean.CommonalityModel;
import com.treasure.million.util.LogUtils;
import com.treasure.million.util.NetWorkUtils;
import com.treasure.million.util.ToastUtil;
import com.treasure.million.util.Utility;

import org.json.JSONObject;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/9.
 */
public class okHttpModel {

    public static Map<String, String> map;

    public static Map<String, String> getMap() {
        if (map == null) {
            map = new LinkedHashMap<>();
        }
        map.clear();
        return map;
    }

    public static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Connection","close");
        return headers;
    }

    /****
     * 添加公用参数
     *
     * @param
     * @return
     */
    public static Map<String, String> getParams() {
        Map<String, String> paramsMap = getMap();
        return paramsMap;
    }




    /**
     * okHttp网络请求
     */
    public static void post(String url, Map<String, String> map, final int ids, final NetWorkListener listener) {
        LogUtils.e("接口：" + url + "?" + map);
        if (!NetWorkUtils.isNetWorkAvailable()) {
            ToastUtil.showToast("无网络连接");
        }
        OkGo.<String>post(url).tag(BaseApplication.getContext()).params(map).headers(getHttpHeaders()).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("接口返回数据:" + response.body().toString());
                if (listener != null && response != null) {
                    if (response != null && !Utility.isEmpty(response.body())) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object != null && !object.isNull("resultcode")) {
                                String code = object.optString("resultcode");
                                String message = object.optString("message");
                                CommonalityModel commonality = new CommonalityModel();
                                commonality.setStatusCode(code);
                                commonality.setErrorDesc(message);
                                listener.onSucceed(object, ids, commonality);//请求成功后通过接口返回数据
                            } else {
                                listener.onFail();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onError(e);
                        }
                    } else {
                        listener.onFail();
                    }
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                if (listener != null && response != null && response.getException() != null) {
                    listener.onError((Exception) response.getException());
                    if (!Utility.isEmpty(response.getException().getMessage())) {
                        LogUtils.e(response.getException().getMessage() + "response.code=" + response.code());
//                        ToastUtil.showToast(response.getException().getMessage());
                    }
                }
            }
        });
    }


    public static void get(String url, Map<String, String> map, final int ids, final NetWorkListener listener) {
        LogUtils.e("接口：" + url + "?" + map);
        if (!NetWorkUtils.isNetWorkAvailable()) {
            ToastUtil.showToast("无网络连接");
        }
        OkGo.<String>get(url).tag(BaseApplication.getContext()).params(map).headers(getHttpHeaders()).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("接口返回数据:" + response.body().toString());
                if (listener != null && response != null) {
                    if (!Utility.isEmpty(response.body())) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object != null && !object.isNull("resultcode")) {
                                String code = object.optString("resultcode");
                                String message = object.optString("message");
                                CommonalityModel commonality = new CommonalityModel();
                                commonality.setStatusCode(code);
                                commonality.setErrorDesc(message);
                                listener.onSucceed(object, ids, commonality);//请求成功后通过接口返回数据
                            } else {
                                listener.onFail();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onFail();
                        }
                    } else {
                        listener.onFail();
                    }
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                if (listener != null && response != null && response.getException() != null) {
                    listener.onError((Exception) response.getException());
                    if (!Utility.isEmpty(response.getException().getMessage())) {
                        LogUtils.e(response.getException().getMessage() + "response.code=" + response.code());
//                        ToastUtil.showToast(response.getException().getMessage());
                    }
                }
            }

        });
    }


}
