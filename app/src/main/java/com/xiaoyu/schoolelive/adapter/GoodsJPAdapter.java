package com.xiaoyu.schoolelive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.data.JPUser;

import java.util.List;

/**
 * Created by NeekChaw on 2017-10-21.
 */

public class GoodsJPAdapter extends BaseAdapter {
    Context mContext;
    List<JPUser> mDatas;

    public GoodsJPAdapter(Context context, List<JPUser> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        // 重用convertView
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.custom_jp_user, null);
            //初始化
            holder.userName = (TextView) convertView.findViewById(R.id.jp_UserName);
            holder.price = (TextView) convertView.findViewById(R.id.jp_UserPrice);
            holder.date = (TextView) convertView.findViewById(R.id.jp_Date);
            holder.userImage = (ImageView) convertView.findViewById(R.id.jp_UserImage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 适配数据
        holder.userName.setText(mDatas.get(i).getName());
        holder.date.setText(mDatas.get(i).getDate());
        holder.userImage.setImageResource(mDatas.get(i).getImage());
        holder.price.setText(mDatas.get(i).getPrice());

        return convertView;
    }

    /**
     * 添加一条出价
     *
     * @param jpUser
     */
    public void addMyPrice(JPUser jpUser) {
        mDatas.add(jpUser);
        notifyDataSetChanged();
    }

    /**
     * 静态类，便于GC回收
     */
    public static class ViewHolder {
        TextView userName;
        TextView date;
        TextView price;
        ImageView userImage;
    }
}
