package com.example.tetris

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.pause_menu_activity.*

class PauseMenu: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
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