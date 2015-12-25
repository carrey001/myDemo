package com.carrey.mydemo.guide.vectorguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.carrey.common.util.ApiCompatibleUtil;
import com.carrey.mydemo.R;
import com.nineoldandroids.view.ViewHelper;

/**
 * 引导页基类
 * 仿google drive
 * Created by angelo on 2015/6/16.
 */
public abstract class BaseTutorialActivity extends AppCompatActivity {

    public static final int IMAGE_TOP = 0;
    public static final int IMAGE_CENTER = 1;
    public static final int IMAGE_BOTTOM = 2;
    public static final int BOTTOM_MULTI_WITH_LINE = 0;
    public static final int BOTTOM_SIMPLE = 1;
    public static final int BOTTOM_CUSTOM = 2;
    private int mStyle = IMAGE_TOP;
    private int mBottomStyle = BOTTOM_MULTI_WITH_LINE;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private ViewStub bottomStub;
    private LinearLayout mCircles;
    private Button mSkip;
    private View mDone;
    private ImageButton mNext;
    private boolean showSkip = true;
    private DataConfig mDataConfig;

    /**
     * 初始化引导页显示数据， 背景、图片、文案标题、内容
     */
    protected abstract DataConfig initDataConfig();

    protected abstract void init(Bundle savedInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ApiCompatibleUtil.hasKitKat()) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        mDataConfig = initDataConfig();
        if (mDataConfig == null && mDataConfig.getPageNum() <= 0) {
            finish();
            return;
        }

        setContentView(R.layout.activity_tutorial);

        init(savedInstanceState);
        initBottom();

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new CrossFadePageTransformer());
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
                if (position == mDataConfig.getPageNum() - 1) {
                    if (mBottomStyle == BOTTOM_CUSTOM) {
                        findViewById(R.id.bottom_container).setVisibility(View.GONE);
                    } else {
                        mSkip.setVisibility(View.INVISIBLE);
                        mNext.setVisibility(View.GONE);
                    }
                    mDone.setVisibility(View.VISIBLE);

                } else if (position < mDataConfig.getPageNum() - 1) {
                    if (mBottomStyle == BOTTOM_CUSTOM) {
                        findViewById(R.id.bottom_container).setVisibility(View.VISIBLE);
                    } else {
                        if (showSkip) {
                            mSkip.setVisibility(View.VISIBLE);
                        }
                        mNext.setVisibility(View.VISIBLE);
                    }
                    mDone.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        buildCircles();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPager != null){
            mPager.clearOnPageChangeListeners();
        }
    }

    /**
     * 设置是否显示skip按钮
     * @param showButton
     */
    public void showSkipButton(boolean showButton){
        this.showSkip = showButton;
    }

    /**
     * 设置展现形式
     * @param style
     */
    public void setStyle(int style) {
        this.mStyle = style;
    }

    /**
     * 设置底部形式
     * @param bottomStyle
     */
    public void setBottomStyle(int bottomStyle) {
        this.mBottomStyle = bottomStyle;
    }


    private void buildCircles(){
        mCircles = LinearLayout.class.cast(findViewById(R.id.circles));
 
        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (5 * scale + 0.5f);
 
        for(int i = 0 ; i < mDataConfig.getPageNum() ; i++){
            ImageView circle = new ImageView(this);
            circle.setImageResource(R.mipmap.ic_point);
            circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding, 0, padding, 0);
            mCircles.addView(circle);
        }
 
        setIndicator(0);
    }

    private void initBottom() {
        bottomStub = (ViewStub) findViewById(R.id.bottom_stub);
        switch (mBottomStyle) {
            case BOTTOM_MULTI_WITH_LINE:
                bottomStub.setLayoutResource(R.layout.tutorial_bottom_1);
                break;
            case BOTTOM_SIMPLE:
                bottomStub.setLayoutResource(R.layout.tutorial_bottom_2);
                break;
            case BOTTOM_CUSTOM:
                bottomStub.setLayoutResource(R.layout.tutorial_bottom_3);
                break;
            default:
                break;
        }
        bottomStub.inflate();

        mSkip = Button.class.cast(findViewById(R.id.skip));
        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTutorial();
            }
        });

        mNext = ImageButton.class.cast(findViewById(R.id.next));
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
            }
        });

        mDone = findViewById(R.id.done);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTutorial();
            }
        });

        if (!showSkip){
            mSkip.setVisibility(View.INVISIBLE);
        }
    }
 
    private void setIndicator(int index){
        if(index < mDataConfig.getPageNum()){
            for(int i = 0 ; i < mDataConfig.getPageNum() ; i++){
                ImageView circle = (ImageView) mCircles.getChildAt(i);
                if(i == index){
                	circle.setColorFilter(getResources().getColor(R.color.bc1));//tutorial_indicator_selected
                }else {
                	circle.setColorFilter(getResources().getColor(android.R.color.transparent));//  tc3
                }
            }
        }
    }
 
    private void endTutorial(){
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
 
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            return;
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
 
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
 
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }
 
        @Override
        public Fragment getItem(int position) {

            TutorialFragment fragment = TutorialFragment.newInstance(mStyle);
            fragment.setBackgroundColor(mDataConfig.getBackgroundColors()[position]);
            fragment.setHeadText(mDataConfig.getTitles()[position]);
            fragment.setContentText(mDataConfig.getContents()[position]);
            fragment.setImageResource(mDataConfig.getImageResources()[position]);
 
            return fragment;
        }
 
        @Override
        public int getCount() {
            return mDataConfig.getPageNum();
        }


    }

	public class CrossFadePageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();

            View backgroundView = page.findViewById(R.id.tutorial_container);
            View text_head = page.findViewById(R.id.heading);
            View text_content = page.findViewById(R.id.content);
            View welcomeImage = page.findViewById(R.id.welcome_img);


            if (0 <= position && position < 1) {
                ViewHelper.setTranslationX(page, pageWidth * -position);
            }
            if (-1 < position && position <= 0) {
                ViewHelper.setTranslationX(page, pageWidth * -position);
            }


            if (position <= -1.0f || position >= 1.0f) {
                return;
            } else if (position == 0.0f) {
                return;
            } else {
                if (backgroundView != null) {
                    ViewHelper.setAlpha(backgroundView, 1.0f - Math.abs(position));
                }

                if (text_head != null) {
                    ViewHelper.setTranslationX(text_head, pageWidth * position);
                    ViewHelper.setAlpha(text_head, 1.0f - Math.abs(position));
                }

                if (text_content != null) {
                    ViewHelper.setTranslationX(text_content, pageWidth * position);
                    ViewHelper.setAlpha(text_content, 1.0f - Math.abs(position));
                }

                if (welcomeImage != null) {
                    ViewHelper.setTranslationX(welcomeImage, (float) (pageWidth / 2 * position));
                    ViewHelper.setAlpha(welcomeImage, 1.0f - Math.abs(position));
                }

            }


        }
    }

}