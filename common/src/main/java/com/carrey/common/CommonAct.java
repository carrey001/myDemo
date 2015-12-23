package com.carrey.common;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * 类描述：  activity 的基类
 * 创建人：carrey
 * 创建时间：2015/12/22 17:30
 */

public abstract class CommonAct extends AppCompatActivity {

    //    protected View mStatusBarTintView;
    private FrameLayout mContentLayout;
    public boolean mIsResume;

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


            //初始化头部
            initTopView();
            //初始化布局
            initViews();
            //初始化数据
            initData(savedInstanceState);

        } else {
            finish();
        }


    }

    /**
     * launchMode为singleTask的时候，
     * 通过Intent启到一个Activity,如果系统已经存在一个实例，
     * 系统就会将请求发送到这个实例上，但这个时候，系统就不会再调用通常情况下我们处理请求数据的onCreate方法
     * ，而是调用onNewIntent方法
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        convertDataToBundle();

    }

    /**
     * Activity布局完成，彻底跑起来之后，调用获取当前Activity的窗口中位置大小等
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


    @Override
    protected void onResume() {
        super.onResume();
        mIsResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsResume = false;
    }

    /**
     * act销毁 以后 释放资源
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * 使用自己的方法还是父类的方法
     *
     * @param useDefaultFlag
     */
    public void startActivity(Intent intent, boolean useDefaultFlag) {
        if (useDefaultFlag) {
            super.startActivity(intent);
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        //清除栈顶 把当前设置为栈顶
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        super.startActivity(intent);
    }

    /**
     * 用指定的View填充主界面(默认有标题)
     *
     * @param contentView 指定的View
     */
    public void setContentView(View contentView) {

        if (isUseCustomContent()) {
            mContentLayout.removeAllViews();
            mContentLayout.addView(contentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

        } else {
            super.setContentView(contentView);
        }

    }

    public void setContentView(int resId) {
        setContentView(LayoutInflater.from(this).inflate(resId, null));
    }

    public View getContentView() {
        if (isShowTitleBar()) {
            return mContentLayout;
        } else {
            return getWindow().getDecorView();
        }
    }

    /**
     * 默认使用titlebar
     */
    protected boolean isShowTitleBar() {
        return true;
    }

    // 根布局是否使用自定义布局
    private boolean isUseCustomContent() {
        return true;
//        return isShowTitleBar() || (useTintStatusBar() && ApiCompatibleUtil.hasKitKat());
    }


    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 初始化View
     */
    protected abstract void initViews();

    protected abstract void initTopView();


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
