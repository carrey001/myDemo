package com.carrey.mydemo.photoview;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.carrey.common.view.photoview.PhotoView;
import com.carrey.mydemo.BaseAct;
import com.carrey.mydemo.R;

/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/12/25 16:56
 */

public class PhotoAct extends BaseAct {


    private int[] data = {R.mipmap.i1, R.mipmap.i2, R.mipmap.i3, R.mipmap.i4};

    private ViewPager viewPager;

    /**
     * 初始化View
     */
    @Override
    protected void initViews() {
        setContentView(R.layout.act_photo);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MyViewAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    class MyViewAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return data.length;
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setImageResource(data[position]);
            container.addView(photoView);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}
