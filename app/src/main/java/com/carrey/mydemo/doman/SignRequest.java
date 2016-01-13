package com.carrey.mydemo.doman;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.carrey.common.util.SchemeUtil;
import com.carrey.common.util.StringUtil;
import com.carrey.common.util.SystemUtil;
import com.carrey.mydemo.BaseApp;
import com.lidroid.xutils.http.RequestParams;

import org.apache.http.NameValuePair;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2016/1/13 11:43
 */

public class SignRequest extends RequestParams {
    public static final String API_VERSION = "2.3";

    public SignRequest() {
        setDefaultPostType(TYPE_RAW_JSON);
        Context context = BaseApp.getApp();
        setHeader("version", API_VERSION);
        setHeader("app-version", SystemUtil.getVersionName(context));
        setHeader("device", SystemUtil.getIMEI(context));
        setHeader("screen-width", SystemUtil.getScreenWidth() + "");
        setHeader("screen-height", SystemUtil.getScreenHeight() + "");
        setHeader("platform", "1");
        setHeader("os-version", SystemUtil.getOSVersion());
        setHeader("model", SystemUtil.getDeviceModel());
//        setHeader("access-token", UserManager.getInstance().getUser().token);
        ApplicationInfo appInfo = null;
        String channel;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            channel = appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (Exception e) {
            channel = "channel";
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(channel)) {
            channel = "channel";
        }
        setHeader("channel", channel);
    }


    @Override
    public List<HeaderItem> getHeaders() {
        genSign();
        return super.getHeaders();
    }

    /**
     * 设置json格式的body
     *
     * @param t
     */
    public void setJsonBody(Object t) {
        bodyText = JSON.toJSONString(t);
    }

//    public void addBodyJsonParameter(String name, Object value) {
//
//        addBodyParameter(name, JSON.toJSONString(value));
//    }

    // 生成签名
    private void genSign() {
        StringBuilder sb = new StringBuilder();
        // 拼接header
        Collections.sort(headers, new Comparator<HeaderItem>() {
            @Override
            public int compare(HeaderItem t0, HeaderItem t1) {
                return t0.header.getName().compareTo(t1.header.getName());
            }
        });
        for (HeaderItem item : headers) {
            if (!TextUtils.isEmpty(item.header.getValue())) {
                sb.append(item.header.getName()).append("=").append(item.header.getValue()).append("&");
            }
        }
        if (sb.toString().endsWith("&")) {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append("+");

        // 拼接query
        sb.append(getQueryString(true));

        // 拼接body
        if (TYPE_MULTI_PART.equals(contentType)) {
            Collections.sort(bodyParams, new Comparator<NameValuePair>() {
                @Override
                public int compare(NameValuePair t0, NameValuePair t1) {
                    return t0.getName().compareTo(t1.getName());
                }
            });
            for (NameValuePair item : bodyParams) {
                if (!TextUtils.isEmpty(item.getValue())) {
                    sb.append(item.getName()).append("=").append(item.getValue()).append("&");
                }
            }
            if (sb.toString().endsWith("&")) {
                sb.deleteCharAt(sb.length() - 1);
            }
        } else {
            String bodyStr = getBodyJsonText();
            if (!TextUtils.isEmpty(bodyStr)) {
                sb.append(bodyStr);
            }
        }

        // 拼接key
        sb.append("+");
//        if (!TextUtils.isEmpty(UserManager.getInstance().getUser().key)) {
//            sb.append(UserManager.getInstance().getUser().key);
//        }
//        LogUtils.d("bacy->" + sb.toString() + ",sign->" + StringUtil.stringToMD5(sb.toString()));
        setHeader("signature", StringUtil.stringToMD5(sb.toString()));
    }

    public void formatRequest(String url) {
        Uri uri = Uri.parse(url);
        Set<String> paramSet = SchemeUtil.getQueryParameterNames(Uri.parse(url));
        if (paramSet != null) {
            Iterator<String> iterator = paramSet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = uri.getQueryParameter(key);
                if (!TextUtils.isEmpty(value)) {
                    addQueryStringParameter(key, value);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "queryStringParams->" + queryStringParams + ",bodyParams->" + bodyParams
                + ",fileParams->" + fileParams + ",heades->" + headers;
    }
}
