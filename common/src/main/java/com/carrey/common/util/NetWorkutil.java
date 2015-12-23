package com.carrey.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.URL;

/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/12/23 15:34
 */

public class NetWorkUtil {
    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }


    /**
     * 判断WIFI网络是否可用 isWifiConnected
     *
     * @param context
     * @return
     * @since 1.0
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isConnected();// isConnected表示连接上,isAvailable表示可用
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用 isMobileConnected
     *
     * @param context
     * @return
     * @since 1.0
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isConnected();// isConnected表示连接上,isAvailable表示可用
            }
        }
        return false;
    }

    public static boolean checkURL(String url) {
        boolean value = false;
        try {
            new URL(url);
            value = true;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return value;
    }
}
