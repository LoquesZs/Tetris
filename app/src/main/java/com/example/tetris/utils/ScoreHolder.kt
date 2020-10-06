package com.example.tetris.utils

import android.app.Activity
import android.content.Context
import com.example.tetris.BEST_SCORE_STORAGE

class ScoreHolder(private val activity: Activity) {

    var bestScore: Int = loadBestScore()

    var currentScore: Int = 0

    fun saveBestScore() {
        val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        with(editor) {
            if (bestScore == 0 || bestScore < currentScore) bestScore = currentScore
            putInt(BEST_SCORE_STORAGE, bestScore)
            apply()
        }
    }

    private fun loadBestScore(): Int {
        val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPreferences.getInt(BEST_SCORE_STORAGE, 0)
    }

}