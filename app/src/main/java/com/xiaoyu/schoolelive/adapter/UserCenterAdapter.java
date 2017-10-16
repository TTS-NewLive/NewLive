package com.xiaoyu.schoolelive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.data.UserCenter;

import java.util.List;

/**
 * Created by NeekChaw on 2017-07-19.
 */
public class UserCenterAdapter extends BaseAdapter {
    Context context;
    List<UserCenter> data;

    public UserCenterAdapter(Context c, List<UserCenter> data) {
        this.context = c;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        // 重用convertView
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_user_msg, null);

            //初始化按钮
            holder.btn_user_share = (ImageButton) convertView.findViewById(R.id.pub_zhuanfa_icon);
            holder.btn_user_comment = (ImageButton) convertView.findViewById(R.id.pub_comment_icon);
            holder.btn_user_like = (ImageButton) convertView.findViewById(R.id.pub_like_icon);
            //holder.btn_user_more = (ImageButton) convertView.findViewById(R.id.pub_icon_more);
            //初始化信息
            holder.user_content = (TextView) convertView.findViewById(R.id.words_msg);
            holder.user_year = (TextView) convertView.findViewById(R.id.msg_year);
            holder.user_month = (TextView) convertView.findViewById(R.id.msg_month);
            holder.user_day = (TextView) convertView.findViewById(R.id.msg_day);
            holder.user_comment_count = (TextView) convertView.findViewById(R.id.pub_comment_count);
            holder.user_like_count = (TextView) convertView.findViewById(R.id.pub_like_count);
            holder.user_share_count = (TextView) convertView.findViewById(R.id.pub_zhuanfa_count);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 适配数据
        holder.user_content.setText(data.get(i).getMsg_content());
        holder.user_year.setText(data.get(i).getMsg_year());
        holder.user_month.setText(data.get(i).getMsg_month());
        holder.user_day.setText(data.get(i).getMsg_day());
        //holder.user_comment_count.setText(data.get(i).getComment_count());
//        holder.user_share_count.setText(data.get(i).getShare_count());
        //holder.user_like_count.setText(data.get(i).getLike_count());

        holder.btn_user_like.setTag(i);
        holder.btn_user_comment.setTag(i);
        holder.btn_user_share.setTag(i);
        return convertView;
    }

    //发布动态
    public void addCenterMsg(UserCenter userCenter) {
        data.add(userCenter);
        notifyDataSetChanged();
    }

    /**
     * 静态类，便于GC回收
     */
    public static class ViewHolder {
        TextView user_content;
        TextView user_year;
        TextView user_month;
        TextView user_day;
        TextView user_comment_count;
        TextView user_like_count;
        TextView user_share_count;
        ImageButton btn_user_like;
        ImageButton btn_user_comment;
        ImageButton btn_user_share;
        //ImageButton btn_user_more;
    }
}
