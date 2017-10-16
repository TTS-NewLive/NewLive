package com.xiaoyu.schoolelive.util;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by NeekChaw on 2017-08-03.
 */
public class SquareImageViewUtil extends android.support.v7.widget.AppCompatImageView {
    public SquareImageViewUtil(Context context) {
        super(context);
    }

    public SquareImageViewUtil(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageViewUtil(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
