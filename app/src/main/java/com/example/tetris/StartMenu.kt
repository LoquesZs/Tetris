package com.example.tetris

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.start_menu_activity.*

class StartMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_menu_activity)
        val startGameIntent = Intent(this, TetrisActivity::class.java)
        start_button.setOnClickListener {
            startActivity(startGameIntent)
        }
    }
}