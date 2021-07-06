plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {

    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    buildFeatures {
        dataBinding = true
    }

    defaultConfig {
        applicationId = "com.example.tetris"
        minSdkVersion(19)
        targetSdkVersion(30)
        versionCode = 620
        versionName = "6.2.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

}

dependencies {
    implementation(fileTree(mapOf("dir".to("libs"), "include".to(listOf("*.jar")))))

    //Android
    Dependencies
    implementation (Dependencies.Android.appCompat)
    implementation (Dependencies.Android.constraint)
    implementation (Dependencies.Android.legacy)

    //Kotlin
    implementation (Dependencies.Kotlin.core)
    implementation (Dependencies.Kotlin.coroutines)

    //Lifecycle
    implementation (Dependencies.Lifecycle.viewModelKtx)

    //Navigation
    implementation (Dependencies.Navigation.fragmentKtx)
    implementation (Dependencies.Navigation.uiKtx)
}
