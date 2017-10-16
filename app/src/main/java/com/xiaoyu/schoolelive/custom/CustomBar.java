package com.xiaoyu.schoolelive.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoyu.schoolelive.R;

/**
 * Created by Administrator on 2017/7/11.
 */
public class CustomBar extends RelativeLayout {
    private TextView info_menu_name;
    private TextView info_menu_info;
    private ImageView info_menu_right;
    public CustomBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.custom_bar, this, true);

        info_menu_name = (TextView) findViewById(R.id.info_menu_name);
        info_menu_info = (TextView) findViewById(R.id.info_menu_info);
        info_menu_right = (ImageView) findViewById(R.id.info_menu_right);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.Custom_Bar);
        if (attributes != null) {
            //设置菜单名
            String menu_name = attributes.getString(R.styleable.Custom_Bar_name_text);
            if (!TextUtils.isEmpty(menu_name)) {
                info_menu_name.setText(menu_name);
            }
            //设置右侧图标
            int menu_right_dw = attributes.getResourceId(R.styleable.Custom_Bar_right_drawable, -1);
            if (menu_right_dw != -1) {
                info_menu_right.setBackgroundResource(menu_right_dw);
            }

            //设置中间内容 先获取内容是否为图片
            int menu_info = attributes.getResourceId(R.styleable.Custom_Bar_info_drawable, -1);
            if (menu_info != -1) {
                info_menu_info.setBackgroundResource(menu_info);
            } else {//如果不是图片  则显示文字
                String menu_info_text = attributes.getString(R.styleable.Custom_Bar_info_text);
                if (!TextUtils.isEmpty(menu_info_text)) {
                    info_menu_info.setText(menu_info_text);
                }
            }
        }
    }

    public void setInfo_menu_info(String info) {
        //this.info_menu_info = info_menu_info;
        this.info_menu_info.setText(info);
    }
    public void setInfo_photo_info(String photo){

    }
    public TextView getInfo_menu_info() {
        return info_menu_info;
    }
}

