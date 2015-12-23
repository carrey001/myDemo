# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\environment\sdk\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn **

#webview
-keep class com.wondersgroup.hs.healthcloud.common.WebViewFragment$*{*;}

-keep class * extends java.lang.annotation.Annotation { *; }

# 实体类，可能需要fastJson反射
-keep class com.wondersgroup.hs.healthcloud.common.entity.**{*;}
-keep class com.wondersgroup.hs.healthcloud.patient.entity.**{*;}

# R文件可能被第三方lib通过发射调用
-keepclassmembers class **.R$* {
    public static <fields>;
}

# fastjson
-keep class com.alibaba.fastjson.** { *; }
-keep class sun.misc.Unsafe { *; }
-keepattributes Signature,*Annotation*,InnerClasses

# 小米推送
-keep class com.wondersgroup.hs.healthcloud.patient.component.push.XiaomiPushMessageReceiver {*;}

#eventbus
-keepclassmembers class ** {
    public void onEvent*(**);
}

#科大讯飞
-keep class com.iflytek.**{*;}

# shareSDK
-keep class com.tencent.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class m.framework.**{*;}

#百度地图
-keep class  com.baidu.** {*;}
-keep class  vi.com.** {*;}

#环信聊天
-keep class com.easemob.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.apache.** {*;}
#环信表情
-keep class com.wondersgroup.hs.healthcloud.common.view.chat.utils.SmileUtils {*;}
#友盟
-keep public class * extends com.umeng.**
-keep class com.umeng.** { *; }
#医费通
-keep class com.wondersgroup.yftpay.** {
    <fields>;
    <methods>;
}
#js回调本地代码
-keep class com.wondersgroup.hs.healthcloud.patient.module.JsCallBack { *; }
#汇到
-keep class mobile.wonders.octopus.webcontainer.** { *; }
#Trinity统计
-keep class com.wonders.bud.budtrinity.** { *; }
-keep class org.openudid.** { *; }
