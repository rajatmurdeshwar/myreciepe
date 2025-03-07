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
# Keep your application class
-keep public class com.murdeshwar.myrecipe.RecipeApplication

# Keep all classes in a specific package
-keep class com.murdeshwar.myrecipe.** { *; }

# Keep all classes used in serialization/reflection
-keep class * implements java.io.Serializable { *; }

# Keep Hilt-generated classes
-keep class dagger.hilt.** { *; }
-keep class * implements dagger.hilt.components.** { *; }
-keep class * implements dagger.hilt.internal.** { *; }

# Keep RepositoryImpl classe
# Keep local and network models
-keep class com.murdeshwar.myrecipe.di.** { *; }
-keep class com.murdeshwar.myrecipe.data.** { *; }


# Keep Gson TypeAdapter classes (if using Gson)
-keep class com.google.gson.** { *; }
-keep class com.squareup.moshi.** { *; }

# Preserve generic type parameters (important for Retrofit)
-keepattributes Signature

# Keep stack traces readable
-keepattributes SourceFile,LineNumberTable