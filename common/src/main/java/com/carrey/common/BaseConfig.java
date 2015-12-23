package com.carrey.common;

/**
 * 配置服务器地址
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/12/22 15:03
 */

public class BaseConfig {
    /**
     * 服务器ip
     */
    private static String SERVER_IP;// = BuildConfig.SERVER_IP; //DEVELOP_SERVER_IP;

    public static void setServerIP(String serverIP) {
        SERVER_IP = serverIP;
    }

    /**
     * @return 返回服务器的地址
     */
    public static String getUrl() {
        return SERVER_IP;
    }

    /**
     * 是否支持debug
     */
    private static boolean debuggable;

    public static boolean isDebuggable() {
        return debuggable;
    }

    public static void setDebuggable(boolean debuggable) {
        BaseConfig.debuggable = debuggable;
    }
}
