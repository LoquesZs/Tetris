object Dependencies {

    object Android {
        const val appCompat = "androidx.appcompat:appcompat:${Versions.Android.appCompat}"
        const val constraint = "androidx.constraintlayout:constraintlayout:${Versions.Android.constraint}"
        const val legacy = "androidx.legacy:legacy-support-v4:${Versions.Android.legacy}"
    }

    object Kotlin {
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