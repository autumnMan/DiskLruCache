# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\ADT\android-sdk-windows/tools/proguard/proguard-android.txt
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
-dontpreverify
-dontoptimize
-useuniqueclassmembernames
-ignorewarnings
-dontusemixedcaseclassnames
-dontshrink

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes *JavascriptInterface*
-keepattributes Signature

-keepparameternames

-keep class z.disklru.cache.lib.inter.**

-keep interface z.disklru.cache.lib.scanner.**{*;}

-keep class z.disklru.cache.lib.scanner.** {
    public <fields>;
    public <methods>;
}

-keep class z.disklru.cache.lib.Md5Util{
    public <methods>;
}

-keep class z.disklru.cache.lib.LruCache {
    <init>(...);
    public protected <methods>;
}

-keep class * implements z.disklru.cache.lib.inter.DiskCache {
    <init>(...);
    public protected *;
}

-keep class * implements z.disklru.cache.lib.inter.DiskCacheKey {
    <init>(...);
    public protected *;
}

-keep class * implements z.disklru.cache.lib.inter.DiskCacheLog {
    <init>(...);
    public protected *;
}
