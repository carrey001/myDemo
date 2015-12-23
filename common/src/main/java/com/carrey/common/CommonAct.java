package com.carrey.common;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 类描述：  activity 的基类
 * 创建人：carrey
 * 创建时间：2015/12/22 17:30
 */

public abstract class CommonAct extends AppCompatActivity {


    /**
     * 设置通用的一些act共性
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 固定竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        //app 启动
        CommonApp.getsApp().start();

        if (isValidate()) {

            convertDataToBundle();
        } else {
            finish();
        }


    }

    /**
     * 把data的数据转换为bundle的数据
     * scheme
     */
    private void convertDataToBundle() {
        Uri uri = getIntent().getData();
        if (uri != null && Intent.ACTION_VIEW.equals(getIntent().getAction())) {







        }

    }

    /**
     * 是否有效的
     */
    private boolean isValidate() {
        return true;
    }
}
