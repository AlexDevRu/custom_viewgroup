package com.example.learning_android_custom_viewgroup_kulakov;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class WrapLayout1 extends ViewGroup {

    public WrapLayout1(Context context) {
        this(context, null, 0);
    }

    public WrapLayout1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapLayout1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
    }

}
