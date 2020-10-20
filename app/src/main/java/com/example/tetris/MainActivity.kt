package com.example.tetris

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

internal const val APP_THEME_STORAGE = "Application Theme"
internal const val DARK_APP_THEME = "Dark Application Theme"
internal const val LIGHT_APP_THEME = "Light Application Theme"
internal const val Y_CELL_COUNT = 20
internal const val X_CELL_COUNT = 10
internal const val BEST_SCORE_STORAGE = "Best ScoreHolder Storage" // key to best score storage in shared preferences
internal const val RESULT_SCORE = "result"

class StartMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        when (getPreferences(Context.MODE_PRIVATE).getString(APP_THEME_STORAGE, LIGHT_APP_THEME)) {
            LIGHT_APP_THEME -> setTheme(R.style.Light)
            DARK_APP_THEME -> setTheme(R.style.Dark)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_menu_activity)
    }
}