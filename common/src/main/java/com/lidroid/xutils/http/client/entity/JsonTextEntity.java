package com.lidroid.xutils.http.client.entity;

import com.lidroid.xutils.http.RequestParams;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * 类描述：
 * 创建人：Bob
 * 创建时间：2015/7/23 17:55
 */
public class JsonTextEntity extends StringEntity {

    public JsonTextEntity(String s, String charset) throws UnsupportedEncodingException {
        super(s, charset);
        setContentType(RequestParams.TYPE_RAW_JSON + ";charset=" + charset);
        setContentEncoding(charset);
    }

    public JsonTextEntity(String s) throws UnsupportedEncodingException {
        super(s);
        setContentType(RequestParams.TYPE_RAW_JSON);
    }
}
