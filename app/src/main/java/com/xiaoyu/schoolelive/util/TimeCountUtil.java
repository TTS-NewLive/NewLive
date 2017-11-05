package com.xiaoyu.schoolelive.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.widget.Button;

/**
 * Created by NeekChaw on 2017-11-04.
 */

public class TimeCountUtil extends CountDownTimer {
    private Activity mActivity;
    private Button btn;//按钮

    // 在这个构造方法里需要传入三个参数，一个是Activity，一个是总的时间millisInFuture，一个是countDownInterval，然后就是你在哪个按钮上做这个是，就把这个按钮传过来就可以了
    //private long millisInFuture = 60000;//60s
    //private long countDownInterval = 1000;//每隔一秒
    public TimeCountUtil(Activity mActivity, long millisInFuture, long countDownInterval, Button btn) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.btn = btn;
    }

    /**
     * 倒计时中的逻辑放在onTick方法中
     *
     * @param millisUntilFinished
     */

    @SuppressLint("NewApi")
    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);//设置不能点击
        btn.setText("重新发送" + "(" + millisUntilFinished / 1000 + ")");//设置倒计时时间
        //设置按钮为灰色，这时是不能点击的
        //btn.setBackground(mActivity.getResources().getDrawable(R.drawable.send_code_backg));
        Spannable span = new SpannableString(btn.getText().toString());//获取按钮的文字
        //span.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为红色
        btn.setText(span);
    }

    /**
     * 倒计时结束后的逻辑放到onFinish方法中
     */
    @SuppressLint("NewApi")
    @Override
    public void onFinish() {
        btn.setText("重新获取");
        btn.setClickable(true);//重新获得点击
        //btn.setBackground(mActivity.getResources().getDrawable(R.drawable.codebackg));//还原背景色
    }
}
