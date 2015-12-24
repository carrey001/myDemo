package com.carrey.mydemo.guide.vectorguide;

import android.os.Bundle;

import com.carrey.mydemo.R;


public class TutorialActivity extends BaseTutorialActivity {

    private final int PAGE_NUM = 3;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void init(Bundle savedInstanceState) {
        // 展示样式，依据图片位置共三种
        setStyle(BaseTutorialActivity.IMAGE_BOTTOM);
        setBottomStyle(BaseTutorialActivity.BOTTOM_CUSTOM);
        showSkipButton(false);
    }

    @Override
    protected DataConfig initDataConfig() {
        DataConfig dataConfig = new DataConfig(PAGE_NUM);

        // 背景色
        dataConfig.setBackgroundColors(new int[]{
                R.color.bc1,
                R.color.bc_tutorial1,
                R.color.bc_tutorial2});

        // 图片
        dataConfig.setImageResources(new int[]{
                R.mipmap.i1,
                R.mipmap.i2,
                R.mipmap.i3});

        // 文描标题，可以为空
        dataConfig.setTitles(new String[]{
                        "健康云正式入驻社区",
                        "附近的免费测量点",
                        "健康档案全新升级"}
        );

        //  文描内容，可以为空
        dataConfig.setContents(new String[]{
                "三大社区抢先上线",
                "目前嘉定社区全面覆盖",
                "炫酷体验等你发现"});

        return dataConfig;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

}