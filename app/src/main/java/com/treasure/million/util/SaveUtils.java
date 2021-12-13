package com.treasure.million.util;

import com.treasure.million.R;
import com.treasure.million.bean.Block;
import com.treasure.million.bean.CarInfo;
import com.treasure.million.bean.TripVo;
import com.treasure.million.bean.UserInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zt
 * @date: 2020/5/26
 * @name:SaveUtils
 */
public class SaveUtils {
    /******用户基本信息*****/
    public static void saveInfo(UserInfo info) {
        CacheDiskUtils cacheDiskUtils = CacheDiskUtils.getInstance();
        cacheDiskUtils.put("userInfo", (Serializable) info);
    }


    /******用户基本信息*****/
    public static UserInfo getSaveInfo() {
        UserInfo userInfo = (UserInfo) CacheDiskUtils.getInstance().getSerializable("userInfo");
        return userInfo;
    }


    /******用户基本信息*****/
    public static List<TripVo>  getListTripVo() {
        List<TripVo> voList  = (List<TripVo>) CacheDiskUtils.getInstance().getSerializable("userInfo");
        return voList;
    }


    /******清除缓存*****/
    public static void clealCacheDisk() {
        CacheDiskUtils cacheDiskUtils = CacheDiskUtils.getInstance();
        cacheDiskUtils.remove("userInfo");
        cacheDiskUtils.remove("carInfo");
    }

    /******清除缓存*****/
    public static void clealDisk() {
        CacheDiskUtils cacheDiskUtils = CacheDiskUtils.getInstance();
        cacheDiskUtils.remove("carInfo");
    }


    /******用户车辆信息信息*****/
    public static void saveMap(String map) {
        CacheDiskUtils cacheDiskUtils = CacheDiskUtils.getInstance();
        cacheDiskUtils.put("map", map);
    }

    /******用户车辆信息信息*****/
    public static String getMap() {
        String map =  CacheDiskUtils.getInstance().getString("map");
        return map;
    }



    /******用户车辆信息信息*****/
    public static void saveCar(CarInfo carInfo) {
        CacheDiskUtils cacheDiskUtils = CacheDiskUtils.getInstance();
        cacheDiskUtils.put("carInfo", carInfo);
    }

    /******用户车辆信息信息*****/
    public static CarInfo getCar() {
        CarInfo carInfo = (CarInfo) CacheDiskUtils.getInstance().getSerializable("carInfo");
        return carInfo;
    }


    public static List<Block> getList() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block("邀请好友", R.mipmap.icon_tab2_me));
        blocks.add(new Block("我的团队", R.mipmap.icon_tab3_me));
        blocks.add(new Block("添加地址", R.mipmap.icon_address_me));
        blocks.add(new Block("交易密码", R.mipmap.icon_pass_usd1));
        blocks.add(new Block("联系我们", R.mipmap.icon_contoct));
        blocks.add(new Block("设备解除", R.mipmap.icon_unbind));
        blocks.add(new Block("关于我们", R.mipmap.icon_about_new));
        blocks.add(new Block("消息中心", R.mipmap.icon_goad));
        blocks.add(new Block("退出登录", R.mipmap.icon_out));
        return blocks;
    }

    public static List<Block> getblocks() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block("激活挖矿", R.mipmap.ic_shoe));
        blocks.add(new Block("我的团队", R.mipmap.icon_tab3_me));
        blocks.add(new Block("邀请好友", R.mipmap.icon_tab2_me));
        blocks.add(new Block("流量查询", R.mipmap.ic_mb));
        return blocks;
    }


    public static List<Block> getblock() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block("行程数据", R.mipmap.icon_trip_usd));
        blocks.add(new Block("维保数据", R.mipmap.icon_maint_usd));
        blocks.add(new Block("保险数据", R.mipmap.icon_insurance_usd));
        blocks.add(new Block("违章数据", R.mipmap.icon_break_usd));
        blocks.add(new Block("加油数据", R.mipmap.icon_oil_usd));
        blocks.add(new Block("过户数据", R.mipmap.icon_use_usd));
        return blocks;
    }


    public static List<Block> getArray() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block("交易密码", R.mipmap.icon_pass_usd1));
        blocks.add(new Block("联系我们", R.mipmap.icon_contoct));
        blocks.add(new Block("设备解除", R.mipmap.icon_unbind));
        blocks.add(new Block("关于我们", R.mipmap.icon_about_new));
        blocks.add(new Block("消息中心", R.mipmap.icon_goad));
        blocks.add(new Block("退出登录", R.mipmap.icon_out));
        return blocks;
    }
}
