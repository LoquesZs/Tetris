package com.example.tetris.screens.gamescreen.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat.getColor
import com.example.tetris.R
import com.example.tetris.X_CELL_COUNT
import com.example.tetris.Y_CELL_COUNT

class TetrisFieldView(context: Context, attributeSet: AttributeSet?): View(context, attributeSet) {

    private val styledAttributes = context.obtainStyledAttributes(attributeSet,
        R.styleable.TetrisFieldView
    )
    private val filledCellColor = styledAttributes.getColor(
        R.styleable.TetrisFieldView_filledCellColor, getColor(this.context,
            R.color.colorLightSchemeFilledCell
        )
    )
    private val emptyCellColor = styledAttributes.getColor(
        R.styleable.TetrisFieldView_emptyCellColor,getColor(this.context,
            R.color.colorLightSchemeEmptyCell
        )
    )

    private var emptyCellSize: Float = 0F
    private var filledCellSize: Float = 0F
    private var viewWeight = 1F
    private var paint = Paint()
    lateinit var field: Array<Array<Int>>

    private fun drawTetrisField(array: Array<Array<Int>>, canvas: Canvas?) {
        for(index in array.indices) {
            for (item in array[index].indices) {
                if (array[index][item] == 0) {
                    paint.color = emptyCellColor
                } else paint.color = filledCellColor
                canvas?.drawPoint(
                    (index + 1) * emptyCellSize,
                    (item + 1) * emptyCellSize, paint
                )
            }
        }
    }

    private fun setPaintParameters(){
        paint.color = Color.CYAN
        paint.strokeWidth = filledCellSize
    }

    private fun setTableConstants() {
        val screenSize = resources.displayMetrics.widthPixels
        emptyCellSize = (screenSize * viewWeight / (X_CELL_COUNT + 1))
        filledCellSize = emptyCellSize - 5
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setPaintParameters()
        drawTetrisField(field, canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setTableConstants()
        val screenHeight = MeasureSpec.getSize(heightMeasureSpec)
        val viewWidth = emptyCellSize  * (X_CELL_COUNT + 1)
        val viewHeight = emptyCellSize  * (Y_CELL_COUNT + 1)
        if(viewHeight > screenHeight * 0.9) {
            viewWeight -= 0.05F
            setTableConstants()
            measure(widthMeasureSpec, heightMeasureSpec)
        }
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(viewWidth.toInt(), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight.toInt(), MeasureSpec.EXACTLY)
        )
    }

}