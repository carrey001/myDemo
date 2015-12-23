package com.carrey.common.util;

/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/12/22 16:40
 */

public class LogUtil {
    private static boolean log_enable;

    public static  boolean getLogEnable() {
        return log_enable;
    }

    public static void setLogEnable(boolean enable) {
        log_enable = enable;
    }
}
