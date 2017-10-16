package com.xiaoyu.schoolelive.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoyu.schoolelive.R;

/**
 * Created by NeekChaw on 2017-07-12.
 */
public class CustomUserPubMsg extends LinearLayout {
    private TextView pub_nickname,
            pub_words_msg, pub_comment_count, pub_like_count, pub_zhuanfa_count;

    private CircleImageView pub_user_head;
    private ImageView pub_image_msg;
    public CustomUserPubMsg(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_user_pub_msg, this, true);

//        pub_user_head = (CircleImageView) findViewById(R.id.user_head);
//        pub_nickname = (TextView) findViewById(R.id.pub_nickname);
//
//        pub_words_msg = (TextView) findViewById(R.id.words_msg);
//        pub_zhuanfa_count = (TextView) findViewById(R.id.pub_zhuanfa_count);
//        pub_comment_count = (TextView) findViewById(R.id.pub_comment_count);
//        pub_like_count = (TextView) findViewById(R.id.pub_like_count);
//        pub_image_msg = (ImageView) findViewById(R.id.icon_msg);
//
//
//        TypedArray typedArray = context.
//                obtainStyledAttributes(attrs, R.styleable.Custom_User_Pub_Msg);
//
//        //定义年月日
//        if (typedArray != null) {
//            String get_name = typedArray.getString(R.styleable.Custom_User_Pub_Msg_pub_nickname);
//            if (!TextUtils.isEmpty(get_name)) {
//                pub_nickname.setText(get_name);
//            }
////            String get_year = typedArray.getString(R.styleable.Custom_User_Pub_Msg_pub_year);
////            if (!TextUtils.isEmpty(get_year)) {
////                pub_year.setText(get_year + "-");
////            }
////            String get_month = typedArray.getString(R.styleable.Custom_User_Pub_Msg_pub_month);
////            if (!TextUtils.isEmpty(get_month)) {
////                pub_month.setText(get_month + "-");
////            }
////            String get_date = typedArray.getString(R.styleable.Custom_User_Pub_Msg_pub_date);
////            if (!TextUtils.isEmpty(get_date)) {
////                pub_date.setText(get_date);
////            }
////            String get_day = typedArray.getString(R.styleable.Custom_User_Pub_Msg_pub_day);
////            if (!TextUtils.isEmpty(get_day)) {
////                pub_day.setText(get_day + " ");
////            }
//
//            //定义文字
//            String get_words = typedArray.getString(R.styleable.Custom_User_Pub_Msg_pub_words_msg);
//            if (!TextUtils.isEmpty(get_words)) {
//                pub_words_msg.setText(get_words);
//            }
//            //定义内容图片
//            int get_image_msg = typedArray.
//                    getResourceId(R.styleable.Custom_User_Pub_Msg_pub_image_msg, -1);
//            if (get_image_msg != -1) {
//                pub_image_msg.setVisibility(VISIBLE);
//                pub_image_msg.setBackgroundResource(get_image_msg);
//            }
//
//            //用户头像
//            int get_user_head = typedArray.
//                    getResourceId(R.styleable.Custom_User_Pub_Msg_pub_user_head, -1);
//            if (get_user_head != -1) {
//                pub_user_head.setImageResource(get_user_head);
//            }
//
//            //定义转发数量
//            String get_zhuanfa_count = typedArray.getString(R.styleable.Custom_User_Pub_Msg_pub_zhuanfa_count);
//            if (!TextUtils.isEmpty(get_zhuanfa_count)) {
//                pub_zhuanfa_count.setText(get_zhuanfa_count);
//            }
//
//            //定义评论数量
//            String get_comment_count = typedArray.getString(R.styleable.Custom_User_Pub_Msg_pub_comment_count);
//            if (!TextUtils.isEmpty(get_comment_count)) {
//                pub_comment_count.setText(get_comment_count);
//            }
//
//            //定义点赞数量
//            String get_like_count = typedArray.getString(R.styleable.Custom_User_Pub_Msg_pub_like_count);
//            if (!TextUtils.isEmpty(get_like_count)) {
//                pub_like_count.setText(get_like_count);
//            }
//
//        }
//    }
}}
