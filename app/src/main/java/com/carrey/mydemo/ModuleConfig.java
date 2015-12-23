package com.carrey.mydemo;
/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/12/22 15:30
 */

public class ModuleConfig {

    private static final String DEVELOP_SERVER_IP = "DEVELOP_SERVER_IP";

    public static void setIP(int ipType) {
        switch (ipType) {
            case 0:
                com.carrey.common.BaseConfig.setServerIP(DEVELOP_SERVER_IP);
                break;
            case 1:
                com.carrey.common.BaseConfig.setServerIP(DEVELOP_SERVER_IP);
                break;
            case 2:
                com.carrey.common.BaseConfig.setServerIP(DEVELOP_SERVER_IP);
                break;
            default:
                com.carrey.common.BaseConfig.setServerIP(DEVELOP_SERVER_IP);
                break;


        }
    }
}
