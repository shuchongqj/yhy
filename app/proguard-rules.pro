# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Alice/Library/Android/sdk/tools/proguard/proguard-android.txt
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


# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# This dnsjava class uses old Sun API
-dontnote org.xbill.DNS.spi.DNSJavaNameServiceDescriptor
-dontwarn org.xbill.DNS.spi.DNSJavaNameServiceDescriptor

-ignorewarnings
-dontwarn org.codehaus.jackson.*

-dontwarn org.apache.commons.codec.binary.*
-dontwarn org.apache.log4j.*
-dontwarn net.sf.json.*
-dontwarn com.smart.sdk.client.ApiAccessor
-dontwarn com.smart.sdk.logger.Log4JLogger
-dontwarn com.sina.weibo.sdk.widget.*
-dontwarn org.codehaus.jackson.map.ext.*
-dontwarn com.tencent.mm.sdk.**
-dontwarn org.akita.util.JunitUtil
-dontwarn cn.jpush.**
-dontwarn com.tendcloud.tenddata.**
-dontwarn de.greenrobot.event.**
-dontwarn com.alibaba.fastjson.**
-dontwarn pl.droidsonroids.gif.**

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.quanyan.yhy.ui.common.city.bean.AddressBean

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable,Exceptions

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

-keep class * extends java.lang.annotation.Annotation { *; }
-keep class com.lidroid.xutils.** { *; }
-keep class cn.jpush.** { *; }
-keep class org.codehaus.jackson.** { *; }
-keep class org.jivesoftware.** { *; }
-keep class org.akita.** { *; }
-keep class android.support.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class com.jeremyfeinstein.** { *; }
-keep interface com.jeremyfeinstein.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keep class **.R
-keep class **.R$* {
    <fields>;
}

# As described in tools/proguard/examples/android.pro - ignore all warnings.
-dontwarn android.support.v4.**
#如果有其它包有warning，在报出warning的包加入下面类似的-dontwarn 报名
-dontwarn com.amap.api.**
-dontwarn com.aps.**
#高德相关混淆文件
#3D 地图
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.amap.mapcore.*{*;}
-keep   class com.autonavi.aps.amapapi.*{*;}
#Location
-keep   class com.amap.api.location.**{*;}
-keep   class com.aps.**{*;}
#Service
-keep   class com.amap.api.services.**{*;}

-keep class com.tencent.mm.sdk.openapi.**{*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage { *;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class org.akita.util.JsonMapper
-keep class android.support.multidex.*{*;}

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.mobilesecuritysdk.*
-keep class com.ut.*
-dontwarn com.alipay.apmobilesecuritysdk.face**
-keep class com.alipay.apmobilesecuritysdk.face.**{*;}

-keep class de.greenrobot.event.util.** { *;}
-keep class com.alibaba.fastjson.** { *;}

-dontwarn com.tencent.**
-keep class com.tencent.** {*;}
-keep public class pl.droidsonroids.gif.GifIOException{<init>(int);}
-keep class pl.droidsonroids.gif.GifInfoHandle{<init>(long,int,int,int);}
# andfix混淆
-keep class * extends java.lang.annotation.Annotation
-keepclasseswithmembernames class * {
    native <methods>;
}

# # -------------------------------------------
# #  ######## greenDao混淆  ##########
# # -------------------------------------------
#-libraryjars libs/greendao-1.3.7.jar
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties
##fast json
-dontwarn android.support.**
-dontwarn com.alibaba.fastjson.**
-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses
-keep class com.alibaba.fastjson.** { *; }
-keepclassmembers class * {
public <methods>;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    public <fields>;
}
-keepattributes Signture

-dontobfuscate
-keep class com.duanqu.qupai.jni.Releasable
-keep class com.duanqu.qupai.jni.ANativeObject
-dontwarn com.google.common.primitives.**
-dontwarn com.google.common.cache.**
-dontwarn com.google.auto.common.**
-dontwarn com.google.auto.factory.processor.**
-dontwarn com.fasterxml.jackson.**
-dontwarn net.jcip.annotations.**
-dontwarn javax.annotation.**
-dontwarn org.apache.http.client.utils.URIUtils
-keep class javax.annotation.** { *; }

-keep class * extends com.duanqu.qupai.jni.ANativeObject
-keep @com.duanqu.qupai.jni.AccessedByNative class *
-keep class com.duanqu.qupai.bean.DIYOverlaySubmit
-keep public interface com.duanqu.qupai.android.app.QupaiServiceImpl$QupaiService {*;}
-keep class com.duanqu.qupai.android.app.QupaiServiceImpl

-keep class com.duanqu.qupai.BeautySkinning
-keep class com.duanqu.qupai.render.BeautyRenderer
-keep public interface com.duanqu.qupai.render.BeautyRenderer$Renderer {*;}
-keepclassmembers @com.duanqu.qupai.jni.AccessedByNative class * {
    *;
}
-keepclassmembers class * {
    @com.duanqu.qupai.jni.AccessedByNative *;
}
-keepclassmembers class * {
    @com.duanqu.qupai.jni.CalledByNative *;
}

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclassmembers class * {
    native <methods>;
}
-keepclassmembers class com.duanqu.qupai.** {
    *;
}

-keep class com.duanqu.qupai.recorder.EditorCreateInfo$VideoSessionClientImpl {
    *;
}
-keep class com.duanqu.qupai.recorder.EditorCreateInfo$SessionClientFctoryImpl {
    *;
}
-keep class com.duanqu.qupai.recorder.EditorCreateInfo{
    *;
}

-keepattributes Signature
-keepnames class com.fasterxml.jackson.** { *; }
#UMeng
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class com.quanyan.yhy.R$*{
    public static final int *;
}

-keepclassmembers class * extends android.webkit.WebChromeClient{
    public void openFileChooser(...);
}
