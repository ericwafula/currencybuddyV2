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

# Keep Kotlin metadata (recommended for Kotlin projects)
-keepattributes KotlinMetadata

# Keep the NotificationHandler interface and all its methods
-keep interface tech.ericwathome.core.notification.NotificationHandler {
    *;
}

# Keep the sealed class NotificationType (nested inside NotificationHandler)
-keep class tech.ericwathome.core.notification.NotificationHandler$NotificationType {
    *;
}

# Keep the General and Sync data objects (nested inside NotificationType)
-keep class tech.ericwathome.core.notification.NotificationHandler$NotificationType$General {
    *;
}
-keep class tech.ericwathome.core.notification.NotificationHandler$NotificationType$Sync {
    *;
}

# Optionally, keep the companion object if used at runtime
-keep class tech.ericwathome.core.notification.NotificationHandler$NotificationType$Companion {
    *;
}

# Keep Kotlin metadata (recommended for Kotlin projects)
-keepattributes KotlinMetadata

# Keep the DefaultNotificationHandler class and all its members
-keep class tech.ericwathome.core.notification.impl.DefaultNotificationHandler {
    *;
}
