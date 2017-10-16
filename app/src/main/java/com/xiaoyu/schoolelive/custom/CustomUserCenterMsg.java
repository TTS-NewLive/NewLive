package com.xiaoyu.schoolelive.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoyu.schoolelive.R;

/**
 * Created by Administrator on 2017/7/11.
 */
public class CustomUserCenterMsg extends LinearLayout {
    private TextView msg_year, msg_day, msg_month, msg_date, msg_type, words_msg,
            zhuanfa_count, comment_count, like_count;

    private ImageButton zhuanfa_icon, comment_icon, like_icon;
    private ImageView icon_msg,icon_msg2,icon_msg3;

    public CustomUserCenterMsg(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_user_msg, this, true);

        msg_year = (TextView) findViewById(R.id.msg_year);
        msg_month = (TextView) findViewById(R.id.msg_month);
        msg_day = (TextView) findViewById(R.id.msg_day);
        msg_date = (TextView) findViewById(R.id.msg_date);
        msg_type = (TextView) findViewById(R.id.msg_type);
        words_msg = (TextView) findViewById(R.id.words_msg);
        zhuanfa_count = (TextView) findViewById(R.id.pub_zhuanfa_count);
        comment_count = (TextView) findViewById(R.id.pub_comment_count);
        like_count = (TextView) findViewById(R.id.pub_like_count);
//        icon_msg = (ImageView)findViewById(R.id.icon_msg);
//        icon_msg2 = (ImageView)findViewById(R.id.icon_msg2);
//        icon_msg3 = (ImageView)findViewById(R.id.icon_msg3);

        zhuanfa_icon = (ImageButton) findViewById(R.id.pub_zhuanfa_icon);
        comment_icon = (ImageButton) findViewById(R.id.pub_comment_icon);
        like_icon = (ImageButton) findViewById(R.id.pub_like_icon);

        TypedArray typedArray = context.
                obtainStyledAttributes(attrs, R.styleable.Custom_User_Msg);

        //定义年月日
        if (typedArray != null) {
            String get_year = typedArray.getString(R.styleable.Custom_User_Msg_msg_year);
            if (!TextUtils.isEmpty(get_year)) {
                msg_year.setText(get_year);
            }
            String get_month = typedArray.getString(R.styleable.Custom_User_Msg_msg_month);
            if (!TextUtils.isEmpty(get_month)) {
                msg_month.setText(get_month);
            }
            String get_day = typedArray.getString(R.styleable.Custom_User_Msg_msg_day);
            if (!TextUtils.isEmpty(get_day)) {
                msg_day.setText(get_day);
            }
            String get_date = typedArray.getString(R.styleable.Custom_User_Msg_msg_date);
            if (!TextUtils.isEmpty(get_date)) {
                msg_date.setText(get_date);
            }

            //定义文字类型
            String get_type = typedArray.getString(R.styleable.Custom_User_Msg_msg_type);
            if (!TextUtils.isEmpty(get_type)) {
                msg_type.setText(get_type);
            }
            //定义文字
            String get_words = typedArray.getString(R.styleable.Custom_User_Msg_words_msg);
            if (!TextUtils.isEmpty(get_words)) {
                words_msg.setText(get_words);
            }
            //定义内容图片
            int icon_msg_dw = typedArray.
                    getResourceId(R.styleable.Custom_User_Msg_icon_msg, -1);
            if (icon_msg_dw != -1) {
                icon_msg.setVisibility(VISIBLE);
                icon_msg.setBackgroundResource(icon_msg_dw);
            }

            int icon_msg_dw2 = typedArray.
                    getResourceId(R.styleable.Custom_User_Msg_icon_msg2, -1);
            if (icon_msg_dw2 != -1) {
                icon_msg2.setVisibility(VISIBLE);
                icon_msg2.setBackgroundResource(icon_msg_dw2);
            }

            int icon_msg_dw3 = typedArray.
                    getResourceId(R.styleable.Custom_User_Msg_icon_msg3, -1);
            if (icon_msg_dw3 != -1) {
                icon_msg3.setVisibility(VISIBLE);
                icon_msg3.setBackgroundResource(icon_msg_dw3);
            }

            //定义转发数量
            String get_zhuanfa_count = typedArray.getString(R.styleable.Custom_User_Msg_zhuanfa_count);
            if (!TextUtils.isEmpty(get_zhuanfa_count)) {
                zhuanfa_count.setText(get_zhuanfa_count);
            }

            //定义评论数量
            String get_comment_count = typedArray.getString(R.styleable.Custom_User_Msg_comment_count);
            if (!TextUtils.isEmpty(get_comment_count)) {
                comment_count.setText(get_comment_count);
            }

            //定义点赞数量
            String get_like_count = typedArray.getString(R.styleable.Custom_User_Msg_like_count);
            if (!TextUtils.isEmpty(get_like_count)) {
                like_count.setText(get_like_count);
            }

            //定义图标
            //转发
            int get_zhuanfa_icon = typedArray.
                    getResourceId(R.styleable.Custom_User_Msg_zhuanfa_icon, -1);
            if (get_zhuanfa_icon != -1) {
                zhuanfa_icon.setBackgroundResource(get_zhuanfa_icon);
            }
            //评论
            int get_comment_icon = typedArray.
                    getResourceId(R.styleable.Custom_User_Msg_comment_icon, -1);
            if (get_comment_icon != -1) {
                comment_icon.setBackgroundResource(get_comment_icon);
            }
            //点赞
            int get_like_icon = typedArray.
                    getResourceId(R.styleable.Custom_User_Msg_like_icon, -1);
            if (get_like_icon != -1) {
                like_icon.setBackgroundResource(get_like_icon);
            }

        }
    }
}