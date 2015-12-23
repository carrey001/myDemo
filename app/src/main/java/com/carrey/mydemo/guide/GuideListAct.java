package com.carrey.mydemo.guide;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.carrey.common.view.ColorAnimationView;
import com.carrey.mydemo.R;

/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/12/22 10:56
 */

public class GuideListAct extends AppCompatActivity {

    private ColorAnimationView colorAnimationView;
    private ViewPager viewPager;

    private static final int[] images = {R.mipmap.i1, R.mipmap.i2, R.mipmap.i3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
