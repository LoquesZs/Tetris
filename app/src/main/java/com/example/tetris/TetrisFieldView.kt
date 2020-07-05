package com.example.tetris
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.getColor

const val xCellCount = 10
const val yCellCount = 20

class TetrisFieldView(context: Context, attributeSet: AttributeSet?): View(context, attributeSet) {
    private val filledCellColor = getColor(this.context, R.color.colorLightSchemeFilledCell)
    private val emptyCellColor = getColor(this.context, R.color.colorLightSchemeEmptyCell)

    var field:  Array<Array<Int>> = Array(xCellCount) { Array(yCellCount) { 0 } }

    private var emptyCellSize: Float = 0F
    private var filledCellSize: Float = 0F
    private var viewWeight = 1F
    private var paint = Paint()


    private fun Canvas.drawArray(array: Array<Array<Int>>) {
        for(index in array.indices){
            for (item in array[index].indices) {
                if (array[index][item] == 0) {
                    paint.color = emptyCellColor
                } else paint.color = filledCellColor
                this.drawPoint((index + 1) * emptyCellSize, (item + 1) * emptyCellSize, paint)
            }
        }
    }

    fun clearArray(){
        field = Array(xCellCount) { Array(yCellCount) { 0 } }
    }

    private fun setPaintParameters(){
        paint.color = Color.CYAN
        paint.strokeWidth = filledCellSize
    }

    private fun setTableConstants() {
        val screenSize = resources.displayMetrics.widthPixels
        emptyCellSize = (screenSize * viewWeight / (xCellCount + 1))
        filledCellSize = emptyCellSize - 5
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setPaintParameters()
        canvas?.drawArray(field)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setTableConstants()
        val screenHeight = MeasureSpec.getSize(heightMeasureSpec)
        val screenWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewWidth = emptyCellSize  * (xCellCount + 1)
        val viewHeight = emptyCellSize  * (yCellCount + 1)
        Log.d("Measures", "Width: $viewWidth, WidthSpec: $screenWidth")
        Log.d("Measures", "Height: $viewHeight, HeightSpec: $screenHeight")
        if(viewHeight > screenHeight * 0.9) {
            viewWeight -= 0.05F
            setTableConstants()
            measure(widthMeasureSpec, heightMeasureSpec)
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(viewWidth.toInt(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(viewHeight.toInt(), MeasureSpec.EXACTLY))
    }

}