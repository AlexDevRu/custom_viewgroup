package com.example.learning_android_custom_viewgroup_kulakov

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class SimpleCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): View(context, attrs, defStyle) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 100
        val desiredHeight = 100

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        //Measure Width
        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> {
                //Must be this size
                Log.d(TAG, "onMeasure: width EXACTLY $widthSize")
                widthSize
            }
            MeasureSpec.AT_MOST -> {
                //Can't be bigger than...
                Log.d(TAG, "onMeasure: width AT_MOST $widthSize")
                Math.min(desiredWidth, widthSize)
            }
            else -> {
                Log.d(TAG, "onMeasure: width UNSPECIFIED $widthSize")
                //Be whatever you want
                desiredWidth
            }
        }

        //Measure Height
        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                //Must be this size
                Log.d(TAG, "onMeasure: height EXACTLY $heightSize")
                heightSize
            }
            MeasureSpec.AT_MOST -> {
                //Can't be bigger than...
                Log.d(TAG, "onMeasure: height AT_MOST $heightSize")
                Math.min(desiredHeight, heightSize)
            }
            else -> {
                //Be whatever you want
                Log.d(TAG, "onMeasure: height UNSPECIFIED $heightSize")
                desiredHeight
            }
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height)
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawOval(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    companion object {
        private const val TAG = "SimpleCustomView"
    }

}