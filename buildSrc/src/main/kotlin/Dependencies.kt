object Dependencies {

    object Gradle {
        const val gradle = "com.android.tools.build:gradle:${Versions.Gradle.version}"
    }

    object Android {
        const val appCompat = "androidx.appcompat:appcompat:${Versions.Android.appCompat}"
        const val constraint = "androidx.constraintlayout:constraintlayout:${Versions.Android.constraint}"
        const val legacy = "androidx.legacy:legacy-support-v4:${Versions.Android.legacy}"
    }

    object Kotlin {
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin.gradlePlugin}"
        const val core = "androidx.core:core-ktx:${Versions.Kotlin.core}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.coroutines}"
    }
    
    object Lifecycle {
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.Lifecycle.viewModelKtx}"
    }

    object Navigation {
        const val fragmentKtx = "android.arch.navigation:navigation-fragment-ktx:${Versions.Navigation.version}"
        const val uiKtx = "android.arch.navigation:navigation-ui-ktx:${Versions.Navigation.version}"
    }

}