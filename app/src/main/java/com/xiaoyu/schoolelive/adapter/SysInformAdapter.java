package com.xiaoyu.schoolelive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thinkcool.circletextimageview.CircleTextImageView;
import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.data.SysInform;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/13.
 */

public class SysInformAdapter extends RecyclerView.Adapter<SysInformAdapter.ViewHolder> {
    private Context mContext;
    private List<SysInform> mData;
    private static final int NOTIFICATION_FLAG = 1;
    private SysInformAdapter.OnItemClickListener onItemClickListener;

    public SysInformAdapter(Context c, List<SysInform> data) {
        this.mContext = c;
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleTextImageView sysInform_image;
        TextView sysInform_name;
        TextView sysInform_data;
        TextView sysInform_content;

        public ViewHolder(View itemView) {
            super(itemView);
            sysInform_image = (CircleTextImageView) itemView.findViewById(R.id.sysInform_image);
            sysInform_name = (TextView) itemView.findViewById(R.id.sysInform_name);
            sysInform_data = (TextView) itemView.findViewById(R.id.sysInform_data);
            sysInform_content = (TextView) itemView.findViewById(R.id.sysInform_content);

            ButterKnife.bind(this, itemView);
        }
    }

    public List<SysInform> getList() {
        return mData;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的View
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_sysinform, parent, false);
        // 实例化viewholder
        SysInformAdapter.ViewHolder viewHolder = new SysInformAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SysInformAdapter.ViewHolder holder,final int position) {

        holder.sysInform_image.setImageResource(mData.get(position).getSysInform_image());
        holder.sysInform_name.setText(mData.get(position).getSysInform_name());
        holder.sysInform_data.setText(mData.get(position).getSysInform_data());
        holder.sysInform_content.setText(mData.get(position).getSysInform_content());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);

                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
                } //表示此事件已经消费，不会触发单击事件
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(SysInformAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
