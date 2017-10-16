package com.xiaoyu.schoolelive.util;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by xiaoyao on 2017/8/18.
 */
/*
* 与控件有关的方法
* */
public class WidgetUtil {
    public static String get_radio_select(RadioGroup radioGroup){//得到radiogroup所选择的选项
        for (int i = 0; i < radioGroup.getChildCount();i++){
            RadioButton radioButton = (RadioButton)radioGroup.getChildAt(i);
            if(radioButton.isChecked()){
               return radioButton.getText().toString();
            }
        }
        return null;
    }
    public static String str_trim(String str) {//去除字符串中的所有空格(用来去掉服务器返回路径中的空格)
        return str.replaceAll(" ", "");
    }
}
