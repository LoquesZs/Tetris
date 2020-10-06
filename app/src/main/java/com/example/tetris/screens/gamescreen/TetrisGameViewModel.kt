package com.example.tetris.screens.gamescreen

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tetris.X_CELL_COUNT
import com.example.tetris.Y_CELL_COUNT
import com.example.tetris.screens.gamescreen.utils.PausableDispatcher
import com.example.tetris.utils.ScoreHolder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt

private const val START_X_POSITION = 4
private const val START_Y_POSITION = 0
private const val START_SPEED = 400L
private const val SPEED_MAGNIFIER = 5

class TetrisGameViewModel(private val scoreHolder: ScoreHolder) : ViewModel() {

    private var globalX = START_X_POSITION // X position of figure on tetris field
    private var globalY = START_Y_POSITION // Y position of figure on tetris field


    private var speed = START_SPEED // start delay time in ms
    private val speedMagnifier = SPEED_MAGNIFIER // changes speed by its value

    private var nextDrop = false // indicate if next figure have to dropdown

    private val pausableDispatcher = PausableDispatcher(Handler(Looper.getMainLooper()))

    /**
     * dispatcher for figuresFallCoroutine that allows pauses in executing coroutine
     * */

    private lateinit var figuresFallCoroutine: Job  // coroutine that drops down figure

    private var figureIndex = 0 // index of current figure on field
    private val figures = arrayListOf (
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
    ) // array of all available figures
    private var figure = getRandomFigure() // x to y

    private var isSpeedAccelerated = false

    var isGameOver: Boolean = false
    val gameField = Array(X_CELL_COUNT) { Array(Y_CELL_COUNT) { 0 } }

    fun startGame() {
        isGameOver = false
        figuresFallCoroutine = GlobalScope.launch(pausableDispatcher) { figureDrop() }
        clearField()
        globalX = START_X_POSITION
        globalY = START_Y_POSITION
        figure = getRandomFigure()
        speed = START_SPEED
        figuresFallCoroutine.start()
    }

    fun endGame() {
        isGameOver = true
        figuresFallCoroutine.cancel()
        scoreHolder.saveBestScore()
    }

    fun pause() {
        pausableDispatcher.pause()
    }

    fun resume() {
        pausableDispatcher.resume()
    }

    /** Methods for button presses **/

    fun moveLeft() {
        if (globalY + figure.last().second !in 0 until Y_CELL_COUNT ||
            globalX == 0 ||
            isLeftCellFilled()) return
        else {
            clearFieldFromFigure()
            globalX--
            nextDropSetter()
            drawFigure()
        }
    }

    fun moveRight() {
        if (globalY + figure.last().second !in (0 until Y_CELL_COUNT) ||
            globalX + figure.getRightXIndex() == X_CELL_COUNT - 1 ||
            isRightCellFilled()) return
        else {
            clearFieldFromFigure()
            globalX++
            nextDropSetter()
            drawFigure()
        }
    }

    fun rotateFigure() {
        val currentFigureState = getCurrentFigureState()
        val rotatedFigureIndex = if (currentFigureState == 1) figureIndex + 3 else figureIndex - 1
        val nextFigure = figures[rotatedFigureIndex]
        try {
            for (point in nextFigure.indices) {
                val yIndex = globalY + nextFigure[point].second
                val xIndex = globalX + nextFigure[point].first
                if (gameField[xIndex][yIndex] == 1
                    && gameField[xIndex][yIndex + 1] == 1
                    && !figure.isIndicesInFigure(nextFigure[point].first, nextFigure[point].second)) return
            }
            clearFieldFromFigure()
            figure = figures[rotatedFigureIndex]
            figureIndex = rotatedFigureIndex
            nextDropSetter()
            drawFigure()
        } catch(e: ArrayIndexOutOfBoundsException) {
            return
        }

    }

    fun speedUp() {
        isSpeedAccelerated = true
        speed /= speedMagnifier
    }

    fun slowDown() {
        Log.d("Speed","$speed")
        speed *= speedMagnifier
        isSpeedAccelerated = false
        Log.d("Speed","$speed")
    }

    /** Tetris utils **/

    private fun clearField() {
        for (x in gameField.indices) {
            for (y in gameField[x].indices) {
                gameField[x][y] = 0
            }
        }
    }

    private suspend fun figureDrop() {
        scoreUp(4)
        for (y in 0 until Y_CELL_COUNT) {
            nextDropCheck()
            if (!nextDrop) {
                if (y in 1 until Y_CELL_COUNT) clearFieldFromFigure()
                globalY = y
                drawFigure()
                nextDropSetter()
            }
            delay(speed)
            if (nextDrop && y == 0) {
                endGame()
                return
            }
            nextDropCheck()
        }
    }

    private fun drawFigure() {
        for (point in figure.indices) {
            val yIndex = globalY + figure[point].second
            val xIndex = globalX + figure[point].first
            if (yIndex in 0 until Y_CELL_COUNT) gameField[xIndex][yIndex] = 1
        }
    }

    private fun nextDropSetter() {
        var nextDropCheckSum = 0
        for (point in figure.indices) {
            val yIndex  = globalY + figure[point].second
            val xIndex = globalX + figure[point].first
            if (
                yIndex in 0 until Y_CELL_COUNT - 1 &&
                !figure.isIndicesInFigure (figure[point].first, figure[point].second + 1) &&
                (gameField[xIndex][yIndex + 1] == 1))  nextDropCheckSum++
        }
        nextDrop = when {
            nextDropCheckSum == 0 && globalY in 0 until Y_CELL_COUNT - 1 -> false
            else -> true
        }
    }

    private fun clearFieldFromFigure() {
        for (point in figure.indices) {
            val yIndex = globalY + figure[point].second
            val xIndex = globalX + figure[point].first
            if (yIndex in 0 until Y_CELL_COUNT) gameField[xIndex][yIndex] = 0
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
        for (y in 0 until Y_CELL_COUNT) {
            for (x in 0 until X_CELL_COUNT) {
                sum += gameField[x][y]
                if (x == (X_CELL_COUNT -1) && sum == X_CELL_COUNT) {
                    delay (speed)
                    for (innerY in y downTo 0) {
                        for (innerX in 0 until X_CELL_COUNT) {
                            if (innerY > 0) gameField[innerX][innerY] = gameField[innerX][innerY - 1]
                        }
                    }
                    scoreUp(100)
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
            if (yIndex in 1 until Y_CELL_COUNT) {
                if(xIndex in 1 until X_CELL_COUNT
                    && !figure.isIndicesInFigure((figure[point].first - 1), (figure[point].second))
                    && gameField[xIndex - 1][yIndex] == 1) isLeftCellFilled = true
            }
        }
        return isLeftCellFilled
    }

    private fun isRightCellFilled(): Boolean {
        var isRightCellFilled = false
        for (point in figure.indices) {
            val xIndex = globalX + figure[point].first
            val yIndex = globalY + figure[point].second
            if (globalY in 1 until Y_CELL_COUNT) {
                if(xIndex in 0 until X_CELL_COUNT -1
                    && !figure.isIndicesInFigure((figure[point].first + 1), (figure[point].second))
                    && gameField[xIndex + 1][yIndex] == 1) isRightCellFilled = true
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
            value == 100 && isSpeedAccelerated -> 1.toLong()
            value == 100 && !isSpeedAccelerated -> speedMagnifier.toLong()
            else -> 0
        }
        scoreHolder.currentScore += value
    }

    private suspend fun nextDropCheck() {
        if (nextDrop) {
            globalX = START_X_POSITION
            lineFillCheck()
            nextDrop = false
            figure = getRandomFigure()
            figureDrop()
        }
    }
}