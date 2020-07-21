package com.example.tetris

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.start_menu_activity.*

class StartMenu : AppCompatActivity() {

    private val appThemeStorage = "Application Theme"
    private val darkAppTheme = "Dark Application Theme"
    private val lightAppTheme = "Light Application Theme"

    override fun onCreate(savedInstanceState: Bundle?) {

        when(getPreferences(Context.MODE_PRIVATE).getString(appThemeStorage, lightAppTheme)){
            lightAppTheme -> setTheme(R.style.Light)
            darkAppTheme -> setTheme(R.style.Dark)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_menu_activity)

        when(getPreferences(Context.MODE_PRIVATE).getString(appThemeStorage, lightAppTheme)) {
            lightAppTheme -> switch_theme_button.isChecked = false
            darkAppTheme -> switch_theme_button.isChecked = true
        }

        start_button.setOnClickListener {
            val startGameIntent = Intent(this, TetrisActivity::class.java)
            startActivity(startGameIntent)
        }

        switch_theme_button.setOnClickListener {
            when (getPreferences(Context.MODE_PRIVATE).getString(appThemeStorage, lightAppTheme)) {
                lightAppTheme -> getPreferences(Context.MODE_PRIVATE).edit().putString(appThemeStorage, darkAppTheme).apply()
                darkAppTheme -> getPreferences(Context.MODE_PRIVATE).edit().putString(appThemeStorage, lightAppTheme).apply()
            }
            val appIntent = intent
            this.finish()
            startActivity(appIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("TetrisActivity", Context.MODE_PRIVATE)

        best_score_display_start_menu.text = sharedPreferences.getString("Best Score Storage", "0")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }
}