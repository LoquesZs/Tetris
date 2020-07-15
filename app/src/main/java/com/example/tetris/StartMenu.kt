package com.example.tetris

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.start_menu_activity.*

class StartMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_menu_activity)

        val startGameIntent = Intent(this, TetrisActivity::class.java)
        start_button.setOnClickListener {
            startActivity(startGameIntent)
        }

        settings_button.setOnClickListener {
            Toast.makeText(this, "В разработке", Toast.LENGTH_SHORT).show()
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