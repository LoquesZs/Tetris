package com.example.tetris

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

const val appThemeStorage = "Application Theme"
const val darkAppTheme = "Dark Application Theme"
const val lightAppTheme = "Light Application Theme"

class StartMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        when (getPreferences(Context.MODE_PRIVATE).getString(appThemeStorage, lightAppTheme)) {
            lightAppTheme -> setTheme(R.style.Light)
            darkAppTheme -> setTheme(R.style.Dark)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_menu_activity)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }
}