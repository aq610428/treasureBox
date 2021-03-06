package com.treasure.million.glide;

import android.content.Context;
import android.net.Uri;
import android.os.Looper;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.treasure.million.R;
import com.treasure.million.base.BaseApplication;

import java.io.File;

/**
 * Created by Administrator on 2018/6/4.
 */

public class GlideUtils {
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String SEPARATOR = "/";


    // 将资源ID转为Uri
    public static Uri resourceIdToUri(int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + BaseApplication.getContext().getPackageName() + SEPARATOR + resourceId);
    }

    // 将资源ID转为Uri
    public static void loadFile(String resourceId, ImageView imageView) {
        Glide.with(BaseApplication.getContext()).load(new File(resourceId)).into(imageView);
    }

    // 加载drawable图片
    public static void loadResImage(int resId, ImageView imageView) {
        Glide.with(BaseApplication.getContext())
                .load(resourceIdToUri(resId))
                .into(imageView);
    }


    /******加载网络图片******/
    public static void setImageUrl(String filePath, ImageView make_photo, int defaultDrawble) {
        Glide.with(BaseApplication.getContext()).load(filePath).apply(RequestOptionUtils.getRequestOption(defaultDrawble)).into(make_photo);
    }

    public static void setImageUrl(String filePath, ImageView make_photo) {
        Glide.with(BaseApplication.getContext()).load(filePath).apply(RequestOptionUtils.getRequestOption()).into(make_photo);
    }

    /******加载圆形图片******/
    public static void CreateImageCircular(String filePath, ImageView make_photo, int defaultImage) {
        Glide.with(BaseApplication.getContext()).load(filePath).apply(RequestOptionUtils.getRequestOptionCircle(defaultImage)).into(make_photo);
    }

    /******加载圆角图片******/
    public static void CreateImageRound(String filePath, ImageView make_photo, int radius) {
        Glide.with(BaseApplication.getContext()).load(filePath).apply(RequestOptionUtils.getCircleTransformRound(radius)).into(make_photo);
    }


    /******加载圆角图片******/
    public static void CreateImageRound1(String filePath, ImageView make_photo, int radius) {
        Glide.with(BaseApplication.getContext()).load(filePath).apply(RequestOptionUtils.getCircleTransformRound1(radius)).into(make_photo);
    }


    public static void setImageGif( ImageView make_photo) {
        Glide.with(BaseApplication.getContext()).load(R.drawable.icon_gif).apply(RequestOptionUtils.getRequestOption()).into(make_photo);
    }





    /**
     * 清除图片内存缓存
     */
    public static void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
