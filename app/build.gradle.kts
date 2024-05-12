import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import io.netty.util.ReferenceCountUtil.release
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}



android {
    namespace = "com.koreatech.kotrip_android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.koreatech.kotrip_android"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "kakao_native_app_key", getPropertyKey("kakao_native_app_key"))
        buildConfigField("String", "client_secret_key", getPropertyKey("client_secret_key"))
        buildConfigField("String", "base_url", getPropertyKey("base_url"))
        resValue("string", "kakao_oauth_host", getPropertyKey("kakao_oauth_host"))
        resValue("string", "naver_map_key", getPropertyKey("naver_map_key"))
    }

    signingConfigs {
        create("release") {
            storeFile = file("kotrip.jks")
            storePassword = getPropertyKey("key_password")
            keyAlias = getPropertyKey("key_alias")
            keyPassword = getPropertyKey("key_password")
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    compileOptions {
        // Enable support for the new language APIs
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.compose.ui:ui-tooling-preview-android:1.6.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // compose dependencies
    val composeBom = platform("androidx.compose:compose-bom:2024.02.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)


    // Material Design 3
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")

    // Optional - Integration with activities
    implementation("androidx.activity:activity-compose:1.8.2")
    // Optional - Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
    // Optional - Integration with LiveData
    implementation("androidx.compose.runtime:runtime-livedata")
    // Optional - Integration with RxJava
    implementation("androidx.compose.runtime:runtime-rxjava2")

    // Lottie
    implementation("com.airbnb.android:lottie-compose:6.4.0")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.5.2")

    // Orbit
    implementation("org.orbit-mvi:orbit-core:4.4.0")
    implementation("org.orbit-mvi:orbit-viewmodel:4.4.0")
    implementation("org.orbit-mvi:orbit-compose:4.4.0")
    testImplementation("org.orbit-mvi:orbit-test:4.4.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.2.1")
    testImplementation("junit:junit:4.12")

    // kakao login
    implementation("com.kakao.sdk:v2-user:2.20.0")

    // timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // koin
    implementation("io.insert-koin:koin-core:3.1.5")
    implementation("io.insert-koin:koin-android:3.1.5")

    // accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")

    // calendar
    // The view calendar library
    implementation("com.kizitonwose.calendar:view:2.5.0")

    // The compose calendar library
    implementation("com.kizitonwose.calendar:compose:2.5.0")

    // naver map
    implementation("com.naver.maps:map-sdk:3.17.0")

    // compose naver
    implementation("io.github.fornewid:naver-map-compose:1.5.5")

    // 위치추적 compose naver
    implementation("com.google.android.gms:play-services-location:16.0.0")
    implementation("io.github.fornewid:naver-map-location:16.0.0")

    // preferences datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
}

fun getPropertyKey(propertyKey: String): String {
    val nullableProperty: String? =
        gradleLocalProperties(rootDir).getProperty(propertyKey)
    return nullableProperty ?: "null"
}
