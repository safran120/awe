# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\pop\Android\Sdk\build-tools\...\proguard-android-optimize.txt
# and can be overridden here.
# If you want to enable ProGuard in your debug build, uncomment the following line
# and modify the proguard-rules.pro file to suit your needs.
# -keep class com.safranpulse.pulsechat.** { *; }

# Keep WebRTC classes
-keep class org.webrtc.** { *; }
-keep interface org.webrtc.** { *; }

# Keep Firebase classes
-keep class com.google.firebase.** { *; }

# Keep Hilt
-keep class com.safranpulse.pulsechat.di.** { *; }
-keep @dagger.hilt.android.components.DefaultLifecycleComponents class * { *; }

