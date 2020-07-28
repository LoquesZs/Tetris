package com.example.tetris

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.pause_menu_activity.*

class PauseMenu: AppCompatActivity() {

    private val appThemeStorage = "Application Theme"
    private val darkAppTheme = "Dark Application Theme"
    private val lightAppTheme = "Light Application Theme"

    override fun onCreate(savedInstanceState: Bundle?) {

        when(getSharedPreferences("StartMenu", Context.MODE_PRIVATE).getString(appThemeStorage, lightAppTheme)) {
            lightAppTheme -> setTheme(R.style.Light_Translucent)
            darkAppTheme -> setTheme(R.style.Dark_Translucent)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.pause_menu_activity)

        resume_button_pause_menu.setOnClickListener {
            this.finish()
        }
        quit_button_pause_menu.setOnClickListener {
            val quitIntent = Intent(this, StartMenu::class.java)
            startActivity(quitIntent)
        }
    }
}