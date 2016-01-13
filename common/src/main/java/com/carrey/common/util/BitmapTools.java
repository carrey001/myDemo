package com.carrey.common.util;

import android.app.ActivityManager;
import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.carrey.common.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.core.BitmapSize;

/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/12/23 17:13
 */

public class BitmapTools {


    private BitmapUtils bitmapUtils;
    private Context context;
    private int screenWidth;
    private int screenHeight;
    private int halfStandardPadding;

    public BitmapTools(Context context) {
        this.context = context.getApplicationContext();
        bitmapUtils = new BitmapUtils(this.context);
        screenWidth = SystemUtil.getScreenWidth();
        screenHeight = SystemUtil.getScreenHeight();
        halfStandardPadding = context.getResources().getDimensionPixelOffset(R.dimen.M);

//        AlphaAnimation animation = new AlphaAnimation(0, 1);
//        animation.setDuration(200);
//        bitmapUtils.configDefaultImageLoadAnimation(animation);
//
//        bitmapUtils.getGlobalConfig().setMemoryCacheSize(((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() * 131072);


    }

    public BitmapUtils getBitmapUtils() {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(context);
            AlphaAnimation animation = new AlphaAnimation(0, 1);
            animation.setDuration(200);
            bitmapUtils.configDefaultImageLoadAnimation(animation);

            bitmapUtils.getGlobalConfig().setMemoryCacheSize(((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() * 131072);

        }
        return bitmapUtils;
    }
    public <T extends View> void display(T container, String uri) {
        display(container, uri, 0, 0, SizeType.LARGE, 0);
    }
    public <T extends View> void display(T container, String uri, SizeType sizeType) {
        display(container, uri, 0, 0, sizeType, 0);
    }

    public <T extends View> void display(T container, String uri, SizeType sizeType, int defaultDrawableRes) {
        display(container, uri, 0, 0, sizeType, defaultDrawableRes);
    }

    public <T extends View> void display(T container, String uri, int width, int height, int defaultDrawableRes) {
        display(container, uri, width, height, SizeType.CUSTOM, defaultDrawableRes);
    }

    public <T extends View> void display(T container, String uri, int width, int height) {
        display(container, uri, width, height, SizeType.CUSTOM, 0);
    }

    public <T extends View> void display(T container, String url, int width, int height, final SizeType sizeType, int defaultDrawableRes) {
        BitmapDisplayConfig config = bitmapUtils.getDefaultDisplayConfig().cloneNew();
        switch (sizeType) {
            case LARGE:
                if (defaultDrawableRes == 0 && config.getLoadingDrawable() == null) {
                    defaultDrawableRes = R.drawable.ic_default_large;
                }
                if (config.getBitmapMaxSize().equals(BitmapSize.ZERO)) {
                    config.setBitmapMaxSize(new BitmapSize(screenWidth, screenHeight));
                }
                break;
            case MEDIUM:
                if (defaultDrawableRes == 0 && config.getLoadingDrawable() == null) {
                    defaultDrawableRes = R.drawable.ic_default_normal;
                }
                if (config.getBitmapMaxSize().equals(BitmapSize.ZERO)) {
                    int mMaxWidth = screenWidth / 2 - halfStandardPadding * 3;
                    config.setBitmapMaxSize(new BitmapSize(mMaxWidth, mMaxWidth));
                }
                break;
            case SMALL:
                if (defaultDrawableRes == 0 && config.getLoadingDrawable() == null) {
                    defaultDrawableRes = R.drawable.ic_default_small;
                }
                if (config.getBitmapMaxSize().equals(BitmapSize.ZERO)) {
                    int sMaxWidth = screenWidth / 4 - halfStandardPadding * 5 / 2;
                    config.setBitmapMaxSize(new BitmapSize(sMaxWidth, sMaxWidth));
                }
                break;
            case CUSTOM:
                if (defaultDrawableRes == 0 && config.getLoadingDrawable() == null) {
                    if (width > screenWidth / 2) {
                        defaultDrawableRes = R.drawable.ic_default_large;
                    } else if (width > screenWidth / 3) {
                        defaultDrawableRes = R.drawable.ic_default_normal;
                    } else {
                        defaultDrawableRes = R.drawable.ic_default_small;
                    }
                }
                if( width != 0 && height != 0) {
                    config.setBitmapMaxSize(new BitmapSize(width, height));
                } else {
                    config.setBitmapMaxSize(new BitmapSize(screenWidth, screenWidth));
                }
                break;
            case ORIGINAL:
				config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.ic_default_normal));
				config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.ic_default_normal));
                config.setShowOriginal(true);
                break;
        }
        if (defaultDrawableRes != 0) {
            bitmapUtils.configDefaultLoadFailedImage(context.getResources().getDrawable(defaultDrawableRes)) ;
            bitmapUtils.configDefaultLoadingImage(context.getResources().getDrawable(defaultDrawableRes));
        }
        bitmapUtils.display(container, url, config);
    }

    public <T extends View> void display(T container, String uri, BitmapLoadCallBack<T> callBack) {
        bitmapUtils.display(container, uri, callBack);
    }

    public enum SizeType {
        LARGE, MEDIUM, SMALL, CUSTOM, ORIGINAL
    }

}
