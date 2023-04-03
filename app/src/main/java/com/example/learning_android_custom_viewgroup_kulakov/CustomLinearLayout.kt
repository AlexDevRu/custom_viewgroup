package com.example.learning_android_custom_viewgroup_kulakov

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
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
            child.measure(
                MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.UNSPECIFIED),
            )

            if (orientation == LinearLayout.HORIZONTAL) {
                desiredWidth += child.measuredWidth
                if (child.measuredHeight > desiredHeight)
                    desiredHeight = child.measuredHeight
            } else {
                desiredHeight += child.measuredHeight
                if (child.measuredWidth > desiredWidth)
                    desiredWidth = child.measuredWidth
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
        val viewportWidth = childRight - childLeft
        val viewportHeight = childBottom - childTop

        var left = childLeft
        var top = childTop

        for (child in children) {
            if (!child.isVisible) continue
            child.measure(
                MeasureSpec.makeMeasureSpec(viewportWidth, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(viewportHeight, MeasureSpec.UNSPECIFIED),
            )
            if (orientation == LinearLayout.HORIZONTAL) {
                child.layout(left, top, left + child.measuredWidth, top + child.measuredHeight)
                left += child.measuredWidth
            } else {
                child.layout(left, top, left + child.measuredWidth, top + child.measuredHeight)
                top += child.measuredHeight
            }
        }
    }

    companion object {
        private const val TAG = "CustomLinearLayout"
    }

}