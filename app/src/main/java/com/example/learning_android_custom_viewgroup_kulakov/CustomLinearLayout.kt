package com.example.learning_android_custom_viewgroup_kulakov

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.isVisible

class CustomLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private var orientation = LinearLayout.HORIZONTAL

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomLinearLayout,
            0, 0
        ).apply {
            try {
                orientation = getInt(R.styleable.CustomLinearLayout_android_orientation, LinearLayout.HORIZONTAL)
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var desiredWidth = 0
        var desiredHeight = 0

        for (child in children) {
            if (!child.isVisible) continue
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val lp = child.layoutParams as MarginLayoutParams
            if (orientation == LinearLayout.HORIZONTAL) {
                desiredWidth += child.measuredWidth + lp.leftMargin + lp.rightMargin
                if (child.measuredHeight > desiredHeight)
                    desiredHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
            } else {
                desiredHeight += child.measuredHeight + lp.topMargin + lp.bottomMargin
                if (child.measuredWidth > desiredWidth)
                    desiredWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            }
        }

        desiredHeight += paddingTop + paddingBottom
        desiredWidth += paddingLeft + paddingRight

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

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childLeft = paddingLeft
        val childRight = width - paddingRight
        val childTop = paddingTop
        val childBottom = height - paddingBottom

        var left = childLeft
        var top = childTop

        for (child in children) {
            if (!child.isVisible) continue
            val lp = child.layoutParams as MarginLayoutParams
            child.layout(left + lp.leftMargin, top + lp.topMargin, left + child.measuredWidth + lp.leftMargin, top + child.measuredHeight + lp.topMargin)
            if (orientation == LinearLayout.HORIZONTAL) {
                left += child.measuredWidth + lp.leftMargin + lp.rightMargin
            } else {
                top += child.measuredHeight + lp.topMargin + lp.bottomMargin
            }
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is MarginLayoutParams
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    companion object {
        private const val TAG = "CustomLinearLayout"
    }

}