package com.carrey.mydemo;

import com.carrey.common.CommonApp;
import com.carrey.common.util.BaseConstant;
import com.carrey.common.util.FileUtil;
import com.carrey.common.util.LogUtil;

import java.io.File;

;

/**
 * 应用全局上下文
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/12/22 16:34
 */

public class BaseApp extends CommonApp {


    /**
     * 启动app 初始化一些参数
     * 比如三方lib的初始化
     */
    @Override
    protected void onInit() {
        super.onInit();
        checkApp();

        LogUtil.setLogEnable(BuildConfig.LOG_DEBUG);//设置日志是否开启
        ModuleConfig.setIP(BuildConfig.SERVER_IP_TYPE);//设置服务器ip

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //上传错误日志
        uploadErrorLog();

        // 释放一些资源

    }

    //上传错误日志
    private void uploadErrorLog() {
        File file = new File(BaseConstant.LOG_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f != null && f.isFile() && !f.getName().contains("_upload")) {
                    //读文件
                    String errorLog = FileUtil.readStr(f.getPath());
                    String[] splits = errorLog.split("\n");
                    String log = splits[0];
                    String trace = errorLog.substring(log.length() + 1);
                    //// TODO: 2015/12/22  开线程上传错误日志 成功以后重命名
                    File newFile = new File(BaseConstant.LOG_PATH + File.separator + f.getName() + "_upload");
                    f.renameTo(newFile);
                }

            }
        }

    }

    /**
     * 如果签名信息改变。不让进入app
     */
    private void checkApp() {
//        String sign
    }
}
