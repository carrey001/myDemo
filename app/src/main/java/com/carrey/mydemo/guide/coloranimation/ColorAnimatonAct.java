package com.carrey.mydemo.guide.coloranimation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.carrey.common.util.ApiCompatibleUtil;
import com.carrey.mydemo.BaseAct;
import com.carrey.mydemo.R;

/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/12/24 17:05
 */

public class ColorAnimatonAct extends BaseAct {

    private ColorAnimationView colorAnimationView;
    private ViewPager viewPager;

    private static final int[] images = {R.mipmap.i1, R.mipmap.i2, R.mipmap.i3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (ApiCompatibleUtil.hasKitKat()) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected boolean isShowTitleBar() {
        return false;
    }

    /**
     * 初始化View
     */
    @Override
    protected void initViews() {
        setContentView(R.layout.act_guid_list);
        colorAnimationView = (ColorAnimationView) findViewById(R.id.colorAnimationView);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPager.setAdapter(new MyFragmentStatePager(getSupportFragmentManager()));

        colorAnimationView.setmViewPager(viewPager, images.length);
        colorAnimationView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class MyFragmentStatePager
            extends FragmentStatePagerAdapter {

        public MyFragmentStatePager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new MyFragment(position);
        }

        @Override
        public int getCount() {
            return images.length;
        }
    }

    @SuppressLint("ValidFragment")
    public class MyFragment
            extends Fragment {
        private int position;

        public MyFragment(int position) {
            this.position = position;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(images[position]);
            return imageView;
        }
    }
}