package com.carrey.mydemo;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;

import com.carrey.common.CommonAct;

import java.util.List;

/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/12/24 15:32
 */

public abstract class BaseAct extends CommonAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            //app 进入后台
            //全局变量isActive = false 记录当前已经进入后台
//            if(Settings.archive_gesture_checked){
//                Settings.mIsActive=false;
//                Settings.archive_gesture_checked=false;
//            }
        }else{
//            Settings.mIsActive=true;
        }
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
