# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keeppackagenames

-keep @kotlinx.serialization.Serializable class * {*;}

# Keep all built-in serializers and related top-level functions in the kotlinx.serialization.builtins package
-keep class timber.log.Timber { *; }

# Keep Kotlinx Serialization metadata
-keepattributes *Annotation*
-keepattributes EnclosingMethod

# Keep all classes in the kotlinx.serialization package
-keep class kotlinx.serialization.** { *; }

# Keep generated serializer classes (they usually have a $$serializer suffix)
-keepclassmembers class **$$serializer { *; }

-dontobfuscate