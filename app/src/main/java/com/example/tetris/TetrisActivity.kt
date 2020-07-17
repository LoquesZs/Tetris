package com.example.tetris

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.tetris_activity.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt

class TetrisActivity : AppCompatActivity() {

    private val bestScoreStorage = "Best Score Storage"
    private lateinit var sharedPreferences: SharedPreferences
    private var bestScore = "0"
    private var score = 0
    private var globalX = 4
    private val startSpeed = 400L
    private var speed = startSpeed
    private var globalY = 0
    private var nextDrop = false
    private val pausableDispatcher = PausableDispatcher(Handler(Looper.getMainLooper()))
    private var figuresFallCoroutine = GlobalScope.launch(pausableDispatcher) { figureDrop() }
    private var figureIndex = 0
    private var figures = arrayListOf (
        arrayListOf(0 to 0, 0 to -1, 0 to -2, 0 to -3),     // |      фигуры
        arrayListOf(0 to 0, 1 to 0, 2 to 0, 3 to 0),        // ....
        arrayListOf(0 to 0, 0 to -1, 0 to -2, 0 to -3),     // |
        arrayListOf(0 to 0, 1 to 0, 2 to 0, 3 to 0),        // ....

        arrayListOf(0 to 0, 1 to 0, 2 to 0, 1 to -1),       // .:.
        arrayListOf(1 to 0, 0 to -1, 1 to -1, 1 to -2),     // -|
        arrayListOf(1 to 0, 0 to -1, 1 to -1, 2 to -1),     // *:*
        arrayListOf(0 to 0, 0 to -1, 1 to -1, 0 to -2),     // |-

        arrayListOf(0 to 0, 1 to 0, 1 to -1, 2 to -1),      // .:'
        arrayListOf(1 to 0, 0 to -1, 1 to -1, 0 to -2),     // ',
        arrayListOf(0 to 0, 1 to 0, 1 to -1, 2 to -1),      // .:'
        arrayListOf(1 to 0, 0 to -1, 1 to -1, 0 to -2),     // ',

        arrayListOf(0 to 0, 1 to 0, 0 to -1, 1 to -1),      // ::
        arrayListOf(0 to 0, 1 to 0, 0 to -1, 1 to -1),      // ::
        arrayListOf(0 to 0, 1 to 0, 0 to -1, 1 to -1),      // ::
        arrayListOf(0 to 0, 1 to 0, 0 to -1, 1 to -1),      // ::

        arrayListOf(0 to 0, 0 to -1, 1 to -1, 1 to -2),     // ,'
        arrayListOf(1 to 0, 2 to 0, 0 to -1, 1 to -1),      // *:.
        arrayListOf(0 to 0, 0 to -1, 1 to -1, 1 to -2),     // ,'
        arrayListOf(1 to 0, 2 to 0, 0 to -1, 1 to -1),      // *:.

        arrayListOf(0 to 0, 1 to 0, 2 to 0, 0 to -1),       // :..
        arrayListOf(0 to 0, 1 to 0, 1 to -1, 1 to -2),      // .|
        arrayListOf(2 to 0, 0 to -1, 1 to -1, 2 to -1),     // **:
        arrayListOf(0 to 0, 0 to -1, 0 to -2, 1 to -2),     // |*

        arrayListOf(0 to 0, 1 to 0, 2 to 0, 2 to -1),       // ..:
        arrayListOf(1 to 0, 1 to -1, 0 to -2, 1 to -2),     // *|
        arrayListOf(0 to 0, 0 to -1, 1 to -1, 2 to -1),     // :**
        arrayListOf(0 to 0, 1 to 0, 0 to -1, 0 to -2)       // |_
    )
    private var figure = getRandomFigure() // x to y
    private val speedMagnifier = 5
    private var isSpeedUpButtonDown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tetris_activity)

        score_display.text = score.toString()

        loadBestScore()
        best_score_display.text = bestScore

        left_button.setOnClickListener {
            if (globalY + figure.last().second !in 0 until yCellCount ||
                globalX == 0 ||
                isLeftCellFilled()) return@setOnClickListener else {
                clearFieldFromFigure()
                globalX--
                nextDropSetter()
                drawFigure()
            }
        }

        rotate_button.setOnClickListener {
            val currentFigureState = getCurrentFigureState()
            val rotatedFigureIndex = if (currentFigureState == 1) figureIndex + 3 else figureIndex - 1
            val nextFigure = figures[rotatedFigureIndex]
            try {
                for (point in nextFigure.indices) {
                    val yIndex = globalY + nextFigure[point].second
                    val xIndex = globalX + nextFigure[point].first
                    if (tetrisField.field[xIndex][yIndex] == 1
                        && tetrisField.field[xIndex][yIndex + 1] == 1
                        && !figure.isIndicesInFigure(nextFigure[point].first, nextFigure[point].second)) return@setOnClickListener
                }
                clearFieldFromFigure()
                figure = figures[rotatedFigureIndex]
                figureIndex = rotatedFigureIndex
                nextDropSetter()
                drawFigure()
            } catch(e: ArrayIndexOutOfBoundsException) {
                return@setOnClickListener
            }
        }

        right_button.setOnClickListener {
            if (globalY + figure.last().second !in (0 until yCellCount) ||
                globalX + figure.getRightXIndex() == xCellCount - 1 ||
                isRightCellFilled()) return@setOnClickListener else {
                clearFieldFromFigure()
                globalX++
                nextDropSetter()
                drawFigure()
            }
        }

        new_game_button.setOnClickListener {
            figuresFallCoroutine.cancel()
            saveBestScore()
            figure = getRandomFigure()
            tetrisField.clearArray()
            tetrisField.invalidate()
            score = 0
            score_display.text = score.toString()
            speed = startSpeed
            globalX = 4
            figuresFallCoroutine = GlobalScope.launch(pausableDispatcher) { figureDrop() }
        }

        speed_up_button.setOnTouchListener { v, event ->
            v.performClick()
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    isSpeedUpButtonDown = true
                    speed /= speedMagnifier
                }

                MotionEvent.ACTION_UP -> {
                    speed *= speedMagnifier
                    isSpeedUpButtonDown = false
                }
            }
            v?.onTouchEvent(event) ?: true
        }

        pause_button.setOnClickListener {
            val pauseIntent = Intent(this, PauseMenu::class.java)
            startActivity(pauseIntent)
        }

        figuresFallCoroutine
    }

    override fun onPause() {
        super.onPause()
        pausableDispatcher.pause()
        saveBestScore()
    }

    override fun onResume() {
        super.onResume()
        pausableDispatcher.resume()
        loadBestScore()
    }
    
    /***********************************************

    tetris utils

     ************************************************/

    private suspend fun figureDrop() {
        scoreUp(4)
        score_display.text = score.toString()
        for (y in 0 until yCellCount) {
            Log.d("Speed",speed.toString() + speed_up_button.isInTouchMode.toString())
            nextDropCheck()
            setButtonsState(true)
            if (!nextDrop) {
                if (y in 1 until yCellCount) clearFieldFromFigure()
                globalY = y
                drawFigure()
                nextDropSetter()
            }
            tetrisField.invalidate()
            delay(speed)
            setButtonsState(false)
            if (nextDrop && y == 0) {
                figuresFallCoroutine.cancel()
                return
            }
            nextDropCheck()
            tetrisField.invalidate()
        }
    }

    private fun drawFigure() {
        for (point in figure.indices) {
            val yIndex = globalY + figure[point].second
            val xIndex = globalX + figure[point].first
            if (yIndex in 0 until yCellCount) tetrisField.field[xIndex][yIndex] = 1
        }

        tetrisField.invalidate()
    }

    private fun nextDropSetter() {
        var nextDropCheckSum = 0
        for (point in figure.indices) {
            val yIndex  = globalY + figure[point].second
            val xIndex = globalX + figure[point].first
            if (
                yIndex in 0 until yCellCount-1 &&
                !figure.isIndicesInFigure (figure[point].first, figure[point].second + 1) &&
                (tetrisField.field[xIndex][yIndex + 1] == 1))  nextDropCheckSum++
        }
        nextDrop = when {
            nextDropCheckSum == 0 && globalY in 0 until yCellCount-1 -> false
            else -> true
        }

        tetrisField.invalidate()
    }

    private fun clearFieldFromFigure() {
        for (point in figure.indices) {
            val yIndex = globalY + figure[point].second
            val xIndex = globalX + figure[point].first
            if (yIndex in 0 until yCellCount) tetrisField.field[xIndex][yIndex] = 0
        }
    }

    private fun ArrayList<Pair<Int, Int>>.isIndicesInFigure(x: Int, y: Int): Boolean {
        var isIndicesInFigure = false
        this.forEach { if (it.first == x && it.second == y) {
            isIndicesInFigure = true
            return@forEach
        } }
        return isIndicesInFigure
    }

    private fun ArrayList<Pair<Int, Int>>.getRightXIndex(): Int {
        var rightXIndex = -1
        this.forEach {
            if (it.first > rightXIndex) rightXIndex = it.first
        }
        return rightXIndex
    }

    private suspend fun lineFillCheck() {
        var sum = 0
        for (y in 0 until yCellCount) {
            for (x in 0 until xCellCount) {
                sum += tetrisField.field[x][y]
                if (x == (xCellCount-1) && sum == xCellCount) {
                    delay (speed)
                    tetrisField.invalidate()
                    for (innerY in y downTo 0) {
                        for (innerX in 0 until xCellCount) {
                            if (innerY > 0) tetrisField.field[innerX][innerY] = tetrisField.field[innerX][innerY - 1]
                        }
                    }
                    scoreUp(100)
                    score_display.text = score.toString()
                    tetrisField.invalidate()
                }
            }
            sum = 0
        }
    }

    private fun getRandomFigure(): ArrayList<Pair<Int, Int>> {
        figureIndex = Random.nextInt(0..figures.lastIndex)
        return figures[figureIndex]
    }

    private fun isLeftCellFilled(): Boolean {
        var isLeftCellFilled = false
        for (point in figure.indices) {
            val xIndex = globalX + figure[point].first
            val yIndex = globalY + figure[point].second
            if (yIndex in 1 until yCellCount) {
                if(xIndex in 1 until xCellCount
                    && !figure.isIndicesInFigure((figure[point].first - 1), (figure[point].second))
                    && tetrisField.field[xIndex - 1][yIndex] == 1) isLeftCellFilled = true
            }
        }
        return isLeftCellFilled
    }

    private fun isRightCellFilled(): Boolean {
        var isRightCellFilled = false
        for (point in figure.indices) {
            val xIndex = globalX + figure[point].first
            val yIndex = globalY + figure[point].second
            if (globalY in 1 until yCellCount) {
                if(xIndex in 0 until xCellCount-1
                    && !figure.isIndicesInFigure((figure[point].first + 1), (figure[point].second))
                    && tetrisField.field[xIndex + 1][yIndex] == 1) isRightCellFilled = true
            }
        }
        return isRightCellFilled
    }

    private fun getCurrentFigureState(): Int {
        return when ((figureIndex + 1) % 4) {
            1 -> 1
            2 -> 2
            3 -> 3
            else -> 4
        }
    }

    private fun scoreUp(value: Int) {
        speed -= when {
            value == 100 && isSpeedUpButtonDown -> 1.toLong()
            value == 100 && !isSpeedUpButtonDown -> speedMagnifier.toLong()
            else -> 0
        }
        score += value
        score_display.text = score.toString()
    }

    //TODO: переделать эту функцию
    private suspend fun nextDropCheck() {
        if (nextDrop) {
            globalX = 4
            lineFillCheck()
            nextDrop = false
            figure = getRandomFigure()
            figureDrop()
        }
    }

    private fun saveBestScore() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        if (bestScore == "0" || bestScore.toInt() < score) bestScore = score.toString()
        editor.putString(bestScoreStorage, bestScore)
        editor.apply()
        best_score_display.text = bestScore
    }

    private fun loadBestScore() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        bestScore = sharedPreferences.getString(bestScoreStorage, "0") ?: "0"
    }

    private fun setButtonsState(isClickable: Boolean) {
        when (isClickable) {
            true -> {
                left_button.isClickable = true
                right_button.isClickable = true
                rotate_button.isClickable = true
                speed_up_button.isClickable = true
            }
            false -> {
                left_button.isClickable = false
                right_button.isClickable = false
                rotate_button.isClickable = false
                speed_up_button.isClickable = false
            }
        }
    }
}

//TODO: Pop up, полное сохранение текущего состояния поля, для возможности продолжения
//TODO: Анимации
//TODO: Настройки
//TODO: Смена цветовых схем
//TODO: Добавить поддержку старых версий