package com.xiaoyu.schoolelive.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
public class CustomFindHistory extends ListView {
    public CustomFindHistory(Context context) {
    super(context);
  }

    public CustomFindHistory(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

    public CustomFindHistory(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
    }
    //在父元素正要放置该控件是调用
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
