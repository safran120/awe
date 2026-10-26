plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.safranpulse.pulsechat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.safranpulse.pulsechat"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            // NOTE: no applicationIdSuffix here. The Firebase project ("awer-57524") only has an
            // Android app registered for "com.safranpulse.pulsechat", and the google-services
            // plugin fails the build when the applicationId of the variant is not registered:
            //   "No matching client found for package name '<applicationId>'"
            // Register a second Android app in Firebase + re-download google-services.json
            // before re-adding applicationIdSuffix = ".debug".
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.22"))
    implementation("androidx.compose.ui:ui:1.5.10")
    implementation("androidx.compose.ui:ui-graphics:1.5.10")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.10")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.foundation:foundation:1.5.10")
    implementation("androidx.compose.foundation:foundation-layout:1.5.10")
    
    // Dependency Injection
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Image Loading
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    // WebRTC (maintained drop-in for org.webrtc, published on Maven Central)
    implementation("io.github.webrtc-sdk:android:150.7871.01")
    // Compose Animation
    implementation("androidx.compose.animation:animation:1.5.10")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.10")
}