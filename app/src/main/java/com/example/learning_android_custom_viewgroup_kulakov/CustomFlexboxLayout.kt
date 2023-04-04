package com.example.learning_android_custom_viewgroup_kulakov

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton

class CustomFlexboxLayout @JvmOverloads constructor(
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
        var rowHeight = 0
        var rowWidth = 0

        for (child in children) {
            if (!child.isVisible) continue
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val lp = child.layoutParams as MarginLayoutParams

            val widthWithMargins = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val heightWithMargins = child.measuredHeight + lp.topMargin + lp.bottomMargin

            if (orientation == LinearLayout.HORIZONTAL) {
                when (widthMode) {
                    MeasureSpec.EXACTLY, MeasureSpec.AT_MOST -> {
                        Log.d(TAG, "onMeasure: ${(child as? MaterialButton)?.text}")
                        if (rowWidth + widthWithMargins > widthSize) {
                            desiredHeight += rowHeight
                            rowWidth = 0
                            rowHeight = 0
                        }
                        rowWidth += widthWithMargins
                        if (rowWidth > desiredWidth)
                            desiredWidth = rowWidth
                        if (heightWithMargins > rowHeight)
                            rowHeight = heightWithMargins
                    }
                    else -> {
                        desiredWidth += child.measuredWidth + lp.leftMargin + lp.rightMargin
                        if ((child.measuredHeight + lp.topMargin + lp.bottomMargin) > desiredHeight)
                            desiredHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
                    }
                }
            } else {
                when (widthMode) {
                    MeasureSpec.EXACTLY, MeasureSpec.AT_MOST -> {
                        Log.d(TAG, "onMeasure: ${(child as? MaterialButton)?.text}")
                        if (rowHeight + heightWithMargins > heightSize) {
                            desiredWidth += rowWidth
                            rowWidth = 0
                            rowHeight = 0
                        }
                        rowHeight += heightWithMargins
                        if (rowHeight > desiredHeight)
                            desiredHeight = rowHeight
                        if (widthWithMargins > rowWidth)
                            rowWidth = widthWithMargins
                    }
                    else -> {
                        desiredHeight += heightWithMargins
                        if (widthWithMargins > desiredWidth)
                            desiredWidth = widthWithMargins
                    }
                }
            }
        }

        if (orientation == LinearLayout.HORIZONTAL) {
            desiredHeight += paddingTop + paddingBottom + rowHeight
            desiredWidth += paddingLeft + paddingRight
        } else {
            desiredHeight += paddingTop + paddingBottom
            desiredWidth += paddingLeft + paddingRight + rowWidth
        }

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
                desiredWidth.coerceAtMost(widthSize)
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
                desiredHeight.coerceAtMost(heightSize)
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
        var maxHeight = 0
        var maxWidth = 0

        for (child in children) {
            if (!child.isVisible) continue
            val lp = child.layoutParams as MarginLayoutParams

            if (orientation == LinearLayout.HORIZONTAL) {
                val viewRight = left + child.measuredWidth + lp.leftMargin + lp.rightMargin
                if (viewRight > childRight) {
                    top += maxHeight
                    child.layout(childLeft + lp.leftMargin, top + lp.topMargin, childLeft + child.measuredWidth + lp.leftMargin, top + child.measuredHeight + lp.topMargin)
                    left = childLeft + child.measuredWidth + lp.leftMargin + lp.rightMargin
                    maxHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
                } else {
                    child.layout(left + lp.leftMargin, top + lp.topMargin, left + child.measuredWidth + lp.leftMargin, top + child.measuredHeight + lp.topMargin)
                    left += child.measuredWidth + lp.leftMargin + lp.rightMargin
                }

                if (maxHeight < (child.measuredHeight + lp.topMargin + lp.bottomMargin)) {
                    maxHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
                }
            } else {
                val viewBottom = top + child.measuredHeight + lp.topMargin + lp.bottomMargin
                if (viewBottom > childBottom) {
                    left += maxWidth
                    child.layout(left + lp.leftMargin, childTop + lp.topMargin, left + child.measuredWidth + lp.leftMargin, childTop + child.measuredHeight + lp.topMargin)
                    top = childTop + child.measuredHeight + lp.topMargin + lp.bottomMargin
                    maxWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
                } else {
                    child.layout(left + lp.leftMargin, top + lp.topMargin, left + child.measuredWidth + lp.leftMargin, top + child.measuredHeight + lp.topMargin)
                    top += child.measuredHeight + lp.topMargin + lp.bottomMargin
                }

                if (maxWidth < (child.measuredWidth + lp.leftMargin + lp.rightMargin)) {
                    maxWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
                }
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
        return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    companion object {
        private const val TAG = "CustomFlexboxLayout"
    }

}