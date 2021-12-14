package com.treasure.million.config;

public class Config1 {
    enum Environment {
        DEVELOPMENT, // 开发环境
        TEST, //
        PRODUCTION, // 线上环境
    }

    // 当前环境MI
    public static Environment env = Environment.PRODUCTION;
    // 开发环境地
    public static String DEVELOPMENT_PUBLIC_SERVER_URL = "http://47.119.169.98:8083";  //挖矿相关域名
    // 测试环境地址
    public static String TEST_PUBLIC_SERVER_URL = "http://47.119.169.98:8083";
    // 线上环境地址
    public static String PRODUCTION_PUBLIC_SERVER_URL = "http://kb.jkabe.com";

    /********************新版本Api********************/
    public static String getOpenNewApi() {
        if (Config1.env == Environment.DEVELOPMENT) {
            return Config1.DEVELOPMENT_PUBLIC_SERVER_URL;
        } else if (Config1.env == Environment.TEST) {
            return Config1.TEST_PUBLIC_SERVER_URL;
        } else if (Config1.env == Environment.PRODUCTION) {
            return Config1.PRODUCTION_PUBLIC_SERVER_URL;
        }
        return "";
    }
}
