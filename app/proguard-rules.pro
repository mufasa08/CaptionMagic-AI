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


# Preserve RemoteMessage's name as RemoteMessage is used as Parcelable in Intent to `PushService`.
# Its name can be used beyond app update and we need to keep same name between app versions.
-keepnames class com.google.firebase.messaging.RemoteMessage
-keep class androidx.compose.ui.*

# issue with serialized fields being obfuscated

-keep class com.devinjapan.shared.data.response.TextCompletionRequestBody { *; }
-keep class com.devinjapan.shared.data.request.TextCompletionDto { *; }