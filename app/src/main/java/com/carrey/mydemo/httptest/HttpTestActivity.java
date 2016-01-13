package com.carrey.mydemo.httptest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.carrey.common.util.BitmapTools;
import com.carrey.common.util.HttpTools;
import com.carrey.mydemo.BaseAct;
import com.carrey.mydemo.R;
import com.carrey.mydemo.doman.SignRequest;
import com.carrey.mydemo.doman.Weather;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2016/1/13 11:13
 */

public class HttpTestActivity extends BaseAct {
    private View pic;
    private TextView tv;
    private HttpTools httpTools;
    private BitmapTools bitmapTools;

    /**
     * 初始化View
     */
    @Override
    protected void initViews() {
        setContentView(R.layout.activity_http_test);
        httpTools = new HttpTools();
        bitmapTools = new BitmapTools(this);
        pic = findViewById(R.id.pic);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapTools.display(pic, "http://res.javaapk.com/wp-content/uploads/2016/01/QQ%E6%88%AA%E5%9B%BE20160103161522.jpg");
            }
        });
        tv = (TextView) findViewById(R.id.text);
    }

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    @Override
    protected void initData(Bundle savedInstanceState) {
        httpTools.get("http://www.weather.com.cn/adat/sk/101010100.html", new SignRequest(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Weather weather = JSON.parseObject(responseInfo.result,Weather.class);
                Log.d("222", weather.toString());
                tv.setText("城市"+weather.weatherinfo.city+"\n 风向"+weather.weatherinfo.WD+"\n 风级"+
                weather.weatherinfo.WS);
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }
}
