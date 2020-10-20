package com.example.tetris.screens.pausescreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.tetris.*
import com.example.tetris.APP_THEME_STORAGE
import com.example.tetris.DARK_APP_THEME
import com.example.tetris.LIGHT_APP_THEME
import com.example.tetris.databinding.PauseMenuActivityBinding
import kotlinx.android.synthetic.main.pause_menu_activity.*

class PauseMenu: AppCompatActivity() {
    private lateinit var binding: PauseMenuActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        when(getSharedPreferences("StartMenu", Context.MODE_PRIVATE).getString(
            APP_THEME_STORAGE,
            LIGHT_APP_THEME
        ))
        {
            LIGHT_APP_THEME -> setTheme(R.style.Light_Translucent)
            DARK_APP_THEME -> setTheme(R.style.Dark_Translucent)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.pause_menu_activity)
        binding = DataBindingUtil.setContentView(this, R.layout.pause_menu_activity)

        binding.resumeButtonPauseMenu.setOnClickListener {
            this.finish()
        }
        binding.quitButtonPauseMenu.setOnClickListener {
            val quitIntent = Intent(this, StartMenu::class.java)
            startActivity(quitIntent)
        }
    }
}