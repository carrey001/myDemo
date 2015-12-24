package com.carrey.mydemo.zxing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.carrey.common.util.UIUtil;
import com.carrey.mydemo.R;


/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/8/18 11:32
 */

public final class CaptureErrorResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        initData(savedInstanceState);
    }

    protected void initViews() {
        setContentView(R.layout.activity_capture_error);
        UIUtil.showEmptyView((ViewGroup) getWindow().getDecorView(), "这是什么东东！\n \n 又拿三无产品给我！", null);
    }

//    protected void initTitleBar() {
//        super.initTitleBar();
//        mTitleBar.setLeftImageResource(R.mipmap.back_green);
//        mTitleBar.setBackgroundResource(R.color.bc4);
//        mTitleBar.setLeftTextColor(getResources().getColor(R.color.tc1));
//        mTitleBar.setActionTextColor(getResources().getColor(R.color.tc1));
//        mTitleBar.setTitleColor(getResources().getColor(R.color.tc1));
//    }

    protected void initData(Bundle savedInstanceState) {

    }
}
