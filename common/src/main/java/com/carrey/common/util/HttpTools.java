package com.carrey.common.util;

import com.carrey.common.BaseConfig;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;

/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/12/24 10:48
 */

public class HttpTools {

    private HttpUtils httpUtils;

    public HttpTools() {
        httpUtils = new HttpUtils();
        httpUtils.configDefaultHttpCacheExpiry(1000);
    }
    public <T> HttpHandler<T> get(String path, RequestParams params, RequestCallBack<T> callBack) {
        return send(HttpRequest.HttpMethod.GET, path, params, callBack);
    }

    public <T> HttpHandler<T> post(String path, RequestParams params, RequestCallBack<T> callBack) {
        return send(HttpRequest.HttpMethod.POST, path, params, callBack);
    }

    public <T> HttpHandler<T> delete(String path, RequestParams params, RequestCallBack<T> callBack) {
        return send(HttpRequest.HttpMethod.DELETE, path, params, callBack);
    }

    public <T> HttpHandler<T> upload(String path, RequestParams params, RequestCallBack<T> callBack) {
        if (params != null) {
            params.setDefaultPostType(RequestParams.TYPE_MULTI_PART);
        }
        return post(path, params, callBack);
    }

    public <T> HttpHandler<T> send(HttpRequest.HttpMethod method, String path, RequestParams params, RequestCallBack<T> callBack) {
        String url = BaseConfig.getUrl() + path;
        if (params == null) {
            LogUtils.d(BaseConstant.LOG_PREFIX + "url(" + method + "):" + url);
        } else {
            if (HttpRequest.HttpMethod.GET.equals(method) || HttpRequest.HttpMethod.DELETE.equals(method)) {
                LogUtils.d(BaseConstant.LOG_PREFIX + "url(" + method + "):" + url +
                        "?" + params.getQueryString(false) + " ,headers:" + params.headers);
            } else if (HttpRequest.HttpMethod.POST.equals(method) || HttpRequest.HttpMethod.PUT.equals(method)) {
                if (RequestParams.TYPE_RAW_JSON.equals(params.getDefaultPostType())) {
                    LogUtils.d(BaseConstant.LOG_PREFIX + "url(" + method + "):" + url +
                            " ,body:" + params.getBodyJsonText() + " ,headers:" + params.headers);
                } else {
                    LogUtils.d(BaseConstant.LOG_PREFIX + "url(" + method + "):" + url +
                            " ,body:" + params.toString());
                }
            }
        }
        return httpUtils.send(method, url, params, callBack);
    }

    public ResponseStream sendSync(HttpRequest.HttpMethod method, String path, RequestParams params) throws HttpException {
        LogUtils.d(BaseConstant.LOG_PREFIX + method.name() + "->path:" + BaseConfig.getUrl() + path + ", params:" + params);
        return httpUtils.sendSync(method, BaseConfig.getUrl() + path, params);
    }

//    public <T> T sendSync(HttpRequest.HttpMethod method, String path, RequestParams params, Class<T> clazz) throws HttpException {
//        ResponseStream stream = sendSync(method,  path, params);
//        try {
//            String result = stream.readString();
////            BaseResponse response = JSON.parseObject(result, BaseResponse.class);
////            if (response.isSuccess()) {
////                T t = JSON.parseObject(response.data.toString(), clazz);
////                return t;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public <T> List<T> sendArraySync(HttpRequest.HttpMethod method, String path, RequestParams params, Class<T> clazz) throws HttpException {
//        ResponseStream stream = sendSync(method,   path, params);
//        try {
//            String result = stream.readString();
//            BaseResponse response = JSON.parseObject(result, BaseResponse.class);
//            if (response.isSuccess()) {
//                List<T> t = JSON.parseArray(response.data.toString(), clazz);
//                return t;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public HttpHandler<File> download(String path, String target, RequestParams params, RequestCallBack<File> callback) {
        LogUtils.d(BaseConstant.LOG_PREFIX + "download-->path:" + path + ", params:" + params);
        return httpUtils.download(BaseConfig.getUrl() + path, target, params, callback);
    }
}
