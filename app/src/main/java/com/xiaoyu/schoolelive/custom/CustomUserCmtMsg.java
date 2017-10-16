package com.xiaoyu.schoolelive.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.xiaoyu.schoolelive.R;

/**
 * Created by NeekChaw on 2017-07-13.
 */
public class CustomUserCmtMsg extends LinearLayout {
//    private TextView cmt_nickname, cmt_msg, cmt_comment_count,
//            cmt_like_count, cmt_zhuanfa_count;
//
//    private CircleImageView cmt_user_head;

    public CustomUserCmtMsg(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_user_cmt_msg, this, true);

//        cmt_user_head = (CircleImageView) findViewById(R.id.user_head);
//        cmt_nickname = (TextView) findViewById(R.id.pub_nickname);
//        cmt_msg = (TextView) findViewById(R.id.comment_msg);
//        cmt_zhuanfa_count = (TextView) findViewById(R.id.zhuanfa_count);
//        cmt_comment_count = (TextView) findViewById(R.id.comment_count);
//        cmt_like_count = (TextView) findViewById(R.id.like_count);


//        TypedArray typedArray = context.
//                obtainStyledAttributes(attrs, R.styleable.Custom_User_Cmt_Msg);

//        //定义年月日
//        if (typedArray != null) {
//            String get_name = typedArray.getString(R.styleable.Custom_User_Cmt_Msg_cmt_nickname);
//            if (!TextUtils.isEmpty(get_name)) {
//                cmt_nickname.setText(get_name);
//            }
//            String get_year = typedArray.getString(R.styleable.Custom_User_Cmt_Msg_cmt_year);
//            if (!TextUtils.isEmpty(get_year)) {
//                cmt_year.setText(get_year + "-");
//            }
//            String get_month = typedArray.getString(R.styleable.Custom_User_Cmt_Msg_cmt_month);
//            if (!TextUtils.isEmpty(get_month)) {
//                cmt_month.setText(get_month + "-");
//            }
//            String get_date = typedArray.getString(R.styleable.Custom_User_Cmt_Msg_cmt_date);
//            if (!TextUtils.isEmpty(get_date)) {
//                cmt_date.setText(get_date);
//            }
//            String get_day = typedArray.getString(R.styleable.Custom_User_Cmt_Msg_cmt_day);
//            if (!TextUtils.isEmpty(get_day)) {
//                cmt_day.setText(get_day + " ");
//            }

//            //定义文字
//            String get_words = typedArray.getString(R.styleable.Custom_User_Cmt_Msg_cmt_msg);
//            if (!TextUtils.isEmpty(get_words)) {
//                cmt_msg.setText(get_words);
//            }
//
//            //用户头像
//            int get_user_head = typedArray.
//                    getResourceId(R.styleable.Custom_User_Cmt_Msg_cmt_user_head, -1);
//            if (get_user_head != -1) {
//                cmt_user_head.setImageResource(get_user_head);
//            }
//
//            //定义转发数量
//            String get_zhuanfa_count = typedArray.getString(R.styleable.Custom_User_Cmt_Msg_cmt_zhuanfa_count);
//            if (!TextUtils.isEmpty(get_zhuanfa_count)) {
//                cmt_zhuanfa_count.setText(get_zhuanfa_count);
//            }
//
//            //定义评论数量
//            String get_comment_count = typedArray.getString(R.styleable.Custom_User_Cmt_Msg_cmt_comment_count);
//            if (!TextUtils.isEmpty(get_comment_count)) {
//                cmt_comment_count.setText(get_comment_count);
//            }
//
//            //定义点赞数量
//            String get_like_count = typedArray.getString(R.styleable.Custom_User_Cmt_Msg_cmt_like_count);
//            if (!TextUtils.isEmpty(get_like_count)) {
//                cmt_like_count.setText(get_like_count);
//            }
//
//        }
    }
}
