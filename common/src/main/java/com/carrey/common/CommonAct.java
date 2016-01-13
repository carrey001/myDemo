package com.carrey.common;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.carrey.common.util.AndroidBug5497Workaround;
import com.carrey.common.util.ApiCompatibleUtil;
import com.carrey.common.util.SchemeUtil;
import com.carrey.common.util.SystemUtil;
import com.carrey.common.util.UIUtil;
import com.carrey.common.view.swipeback.SwipeBackActivityHelper;

import java.util.Set;

/**
 * 类描述：  activity 的基类
 * 创建人：carrey
 * 创建时间：2015/12/22 17:30
 */

public abstract class CommonAct extends AppCompatActivity {
    protected View mTitleBar;
    protected View mStatusBarTintView;
    private FrameLayout mContentLayout;
    public boolean mIsResume;

    private SwipeBackActivityHelper mHelper;

    /**
     * 设置通用的一些act共性
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 固定竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        //app 启动
        CommonApp.getApp().start();

        if (isValidate()) {

            // 开启滑动关闭界面
            if (useSwipeBackLayout()) {
                mHelper = new SwipeBackActivityHelper(this);
                mHelper.onActivityCreate();
            }

            convertDataToBundle();
            if (useTintStatusBar()) {
//                getWindow().getDecorView().setFitsSystemWindows(true);
                if (ApiCompatibleUtil.hasKitKat() && !ApiCompatibleUtil.hasLollipop()) {
                    //透明状态栏
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    //透明导航栏
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                } else if (ApiCompatibleUtil.hasLollipop()) {
                    Window window = getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.TRANSPARENT);
                }
            }

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
     * 是否使用沉浸式
     */
    protected boolean useTintStatusBar() {
        return true;
    }

    /**
     * 是否开启滑动返回
     */
    protected boolean useSwipeBackLayout() {
        return true;
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
        if (mHelper != null) {
            mHelper.onPostCreate();
        }

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
        UIUtil.hideProgressBar(this);

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
        return isShowTitleBar() || (useTintStatusBar() && ApiCompatibleUtil.hasKitKat());
    }


    /**
     * 初始化View
     */
    protected abstract void initViews();

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    protected abstract void initData(Bundle savedInstanceState);

    protected void initTopView() {
        if (isUseCustomContent()) {
            // 最外层布局
            RelativeLayout base_view = new RelativeLayout(this);
            if (false) {
//            if (isShowTitleBar()) {
                initTitleBar();
                // 填入View
                base_view.addView(mTitleBar);
            } else if (isShowTintStatusBar() && ApiCompatibleUtil.hasKitKat()) {
                initTintStatusBar();
                // 填入View
                base_view.addView(mStatusBarTintView);
            }
            // 内容布局
            mContentLayout = new FrameLayout(this);
            mContentLayout.setId(R.id.content);
//            mContentLayout.setPadding(0, 0, 0, ApiCompatibleUtil.hasLollipop() ? SystemUtil.getNavigationBarHeight(this) : 0);
            RelativeLayout.LayoutParams layoutParamsContent = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParamsContent.addRule(RelativeLayout.BELOW, R.id.titlebar);
            base_view.addView(mContentLayout, layoutParamsContent);

            // 设置ContentView
            setContentView(base_view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            if (SystemUtil.isTintStatusBarAvailable(this)) {
                if ((getWindow().getAttributes().softInputMode & WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) != 0) {
                    AndroidBug5497Workaround.assistActivity(this);
                }
            }
        }

    }

    private void initTintStatusBar() {
        mStatusBarTintView = new View(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, SystemUtil.getStatusBarHeight());
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setId(R.id.titlebar);
    }

    /**
     * 是否使用默认的statusbar
     *
     * @return
     */
    protected boolean isShowTintStatusBar() {
        return true;
    }

    /**
     * 初始化头部
     */
    protected void initTitleBar() {
        // 主标题栏
        mTitleBar = new View(this);
        mTitleBar.setId(R.id.titlebar);
        mTitleBar.setPadding(0, SystemUtil.isTintStatusBarAvailable(this) ? SystemUtil.getStatusBarHeight() : 0, 0, 0);
//        mTitleBar.setLeftClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }


    /**
     * 把data的数据转换为bundle的数据
     * scheme
     */
    private void convertDataToBundle() {
        Uri uri = getIntent().getData();
        if (uri != null && Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            Set<String> params = SchemeUtil.getQueryParameterNames(uri);
            if (params != null) {
                for (String key : params) {
                    String value = uri.getQueryParameter(key);
                    if ("true".equals(value) || "false".equals(value)) {
                        getIntent().putExtra(key, Boolean.parseBoolean(value));
                    } else {
                        getIntent().putExtra(key, value);
                    }
                }
            }
        }
    }

    /**
     * 是否有效的
     */
    private boolean isValidate() {
        return true;
    }
}
