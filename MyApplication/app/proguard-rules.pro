# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/donghui/Documents/android-sdk-macosx/tools/proguard/proguard-android.txt
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
-dontwarn butterknife.internal.**

-keep class **$$ViewInjector { *; }

-keepnames class * { @butterknife.InjectView *;}

-dontwarn butterknife.Views$InjectViewProcessor

-dontwarn com.gc.materialdesign.views.**

-dontwarn okio.**


#  混淆报错  增加混淆规则    https://github.com/square/okio/issues/60
#   http://stackoverflow.com/questions/29192874/proguard-while-butterknife-library-and-other-warnings