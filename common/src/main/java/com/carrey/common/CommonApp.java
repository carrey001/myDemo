package com.carrey.common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.carrey.common.util.BaseConstant;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 应用全局上下文
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/12/22 14:37
 */

public class CommonApp extends Application {

    private static final String META_DEBUGGABLE = "ISDEBUG";
    private static CommonApp sApp;
    private boolean mIsStarted;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        init();
    }

    public static CommonApp getsApp() {
        return sApp;
    }

    /**
     * 应用初始化
     */
    protected void onInit() {
    }

    /**
     * 每次启动程序是需要调用
     * start
     */
    protected void onStart() {
    }

    /**
     * 程序退出时候需要调用该方法，释放一些无用资源
     * stop
     */
    protected void onStop() {
    }

    // 每次启动程序是需要调用
    public final void start() {
        if (!mIsStarted) {
            mIsStarted = true;
            onStart();
        }
    }

    //  程序退出时候需要调用该方法，释放一些无用资源
    public final void stop() {
        if (mIsStarted) {
            mIsStarted = false;
            onStop();
        }
    }

    /**
     * 初始化应用程序
     */
    private void init() {
        if (shouleInit()) {
            //设置异常处理
            Thread.setDefaultUncaughtExceptionHandler(new ExHandler(Thread.getDefaultUncaughtExceptionHandler()));
            initMetaData();
            onInit();
        }
    }

    private void initMetaData() {
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            boolean isDebug = info.metaData.getBoolean(META_DEBUGGABLE, false);
            BaseConfig.setDebuggable(isDebug);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
    }

    //捕获程序崩溃的异常,记录log(可以考虑将异常信息发回服务器)
    private class ExHandler implements Thread.UncaughtExceptionHandler {

        private Thread.UncaughtExceptionHandler internal;

        private ExHandler(Thread.UncaughtExceptionHandler eh) {
            internal = eh;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            File file = new File(BaseConstant.LOG_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String fileName = dateFormat.format(new Date());
            try {
                PrintStream printStream = new PrintStream(file.getAbsolutePath() + File.separator + fileName);
                printStream.println(ex.getMessage());
                ex.printStackTrace();
                printStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            internal.uncaughtException(thread, ex);
        }
    }

    // 判断是否主进程启动App
    private boolean shouleInit() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : appProcesses) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
