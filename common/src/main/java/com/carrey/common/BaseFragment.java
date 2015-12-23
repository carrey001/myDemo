package com.carrey.common;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/12/23 11:03
 */

public abstract class BaseFragment extends Fragment {

    protected View mRootView;
    protected CommonAct mBaseActivity;

    private boolean mIsVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseActivity = (CommonAct) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsVisible) {
            onVisible();
            mIsVisible = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mIsVisible) {
            onInVisible();
            mIsVisible = false;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        } else {
            mRootView = this.onCreateView(inflater, savedInstanceState);
            this.initViews();
            this.initData(savedInstanceState);
        }

        return mRootView;

    }


    protected View findViewById(int id) {
        return mRootView.findViewById(id);
    }

    public View getRootView() {
        return mRootView;
    }

    /**
     * 懒加载
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mRootView != null) {
            if (isVisibleToUser) {
                if (!mIsVisible) {
                    onVisible();
                    mIsVisible = true;
                }
            } else {
                if (mIsVisible) {
                    onInVisible();
                    mIsVisible = false;
                }
            }
        }
    }

    /**
     * 屏幕关闭
     */
    protected void onInVisible() {
    }

    /**
     * 屏幕显示
     */
    protected void onVisible() {
    }

    public void startActivity(Intent intent, boolean useDefaultFlag) {
        if (useDefaultFlag) {
            super.startActivity(intent);
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        super.startActivity(intent);
    }

    public void startActivityWithNewTask(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        super.startActivity(intent);
    }

    /**
     * 创建View
     * onCreateView
     *
     * @param inflater
     * @return
     */
    protected abstract View onCreateView(LayoutInflater inflater, Bundle savedInstanceState);

    /**
     * 初始化数据
     * initData
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 初始化view
     * initViews
     */
    protected abstract void initViews();
}
