package com.xiaoyu.schoolelive.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.data.Deal;

import java.util.List;

import static com.xiaoyu.schoolelive.adapter.WaterFallAdapter.NORMAL_OPTION;

/**
 * Created by NeekChaw on 2017-10-23.
 */

public class DealAdapter extends BaseAdapter {
    DisplayImageOptions mNormalImageOptions;
    Context mContext;
    List<Deal> mDatas;

    public DealAdapter(Context context, List<Deal> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    public List<Deal> getList() {
        return mDatas;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.custom_deal, null);
            //初始化
            holder.dealDate = (TextView) convertView.findViewById(R.id.deal_sell_time);
            holder.sellerName = (TextView) convertView.findViewById(R.id.deal_seller_name);
            holder.goodsName = (TextView) convertView.findViewById(R.id.deal_goods_name);
            holder.goodsPrice = (TextView) convertView.findViewById(R.id.deal_goods_price);
            holder.topImage = (ImageView) convertView.findViewById(R.id.deal_goods_topimage);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 适配数据
        holder.dealDate.setText(mDatas.get(i).getSellDate());
        holder.sellerName.setText(mDatas.get(i).getSellerName());
        holder.goodsName.setText(mDatas.get(i).getGoodsName());
        holder.goodsPrice.setText(mDatas.get(i).getGoodsPrice());
        ImageLoader.getInstance().displayImage(mDatas.get(i).getTopImage(),
                holder.topImage, NORMAL_OPTION);//设置图片

        return convertView;
    }

    private void initImageLoader(Context context) {//初始化ImageLoader
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 5);
        MemoryCache memoryCache;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            memoryCache = new LruMemoryCache(memoryCacheSize);
        } else {
            memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
        }

        mNormalImageOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisc(true)
                .resetViewBeforeLoading(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(mNormalImageOptions)
                //.denyCacheImageMultipleSizesInMemory().discCache(new UnlimitedDiskCache(new File(IMAGES_FOLDER)))
                // .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(memoryCache)
                // .memoryCacheSize(memoryCacheSize)
                .tasksProcessingOrder(QueueProcessingType.LIFO).threadPriority(Thread.NORM_PRIORITY - 2).threadPoolSize(3).build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    /**
     * 添加一条交易记录
     *
     * @param deal
     */
    public void addDealItem(Deal deal) {
        mDatas.add(deal);
        notifyDataSetChanged();
    }

    /**
     * 静态类，便于GC回收
     */
    public static class ViewHolder {
        TextView goodsName;
        TextView dealDate;
        TextView goodsPrice;
        TextView sellerName;
        ImageView topImage;
    }
}
