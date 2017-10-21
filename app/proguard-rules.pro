
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/sszvip/ide/android/android-sdk-macosx/tools/proguard/proguard-android.txt

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
-keepattributes EnclosingMethod
=======
# proguard

# ------------------------ leancloud sdk ------------------------
-keepattributes Signature
-dontwarn com.jcraft.jzlib.**
-keep class com.jcraft.jzlib.**  { *;}

-dontwarn sun.misc.**
-keep class sun.misc.** { *;}

-dontwarn com.alibaba.fastjson.**
-dontnote com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *;}

-dontwarn sun.security.**
-keep class sun.security.** { *; }

-dontwarn com.google.**
-keep class com.google.** { *;}

-dontwarn com.avos.**
-dontnote com.avos.**
-keep class com.avos.** { *;}

-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient
-dontnote android.net.http.**

-dontwarn android.support.**

-dontwarn org.apache.**
-dontnote org.apache.**
-keep class org.apache.** { *;}

-dontwarn org.jivesoftware.smack.**
-keep class org.jivesoftware.smack.** { *;}

-dontwarn com.loopj.**
-keep class com.loopj.** { *;}

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-keep interface com.squareup.okhttp.** { *; }

-dontwarn okio.**

-keep class com.google.protobuf.** { *; }
-keep public class * extends com.google.protobuf.** { *; }
-dontwarn com.google.protobuf.**

-dontwarn org.xbill.**
-keep class org.xbill.** { *;}

-keepattributes *Annotation*


# ------------------------ ChatKit ------------------------
-dontwarn cn.leancloud.chatkit.**
-keep class cn.leancloud.chatkit.** { *;}
-dontnote cn.leancloud.chatkit.**

# ------------------------ picasso ------------------------
-dontwarn com.squareup.picasso**
-keep class com.squareup.picasso.**{*;}

# ------------------------ eventbus ------------------------
-keepclassmembers class ** {
    public void onEvent*(**);
}

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

-dontnote org.greenrobot.eventbus.*
