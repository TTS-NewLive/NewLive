package com.xiaoyu.schoolelive.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.xiaoyu.schoolelive.data.Footer;

import java.util.List;

import static com.xiaoyu.schoolelive.adapter.WaterFallAdapter.NORMAL_OPTION;

/**
 * Created by lenovo on 2017/11/3.
 */

public class FooterAdapter extends BaseAdapter {
    DisplayImageOptions mNormalImageOptions;
    private List<Footer> mDatas;
    Context mContext;

    public FooterAdapter(Context context, List<Footer> data){
        this.mContext = context;
        this.mDatas = data;
    }

    public List<Footer> getList() {
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        // 重用convertView
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.custom_footer, null);
            //初始化
            holder.goodsName = (TextView) convertView.findViewById(R.id.footer_goods_name);
            holder.goodsPrice = (TextView) convertView.findViewById(R.id.footer_goods_price);
            holder.topImage = (ImageView) convertView.findViewById(R.id.footer_goods_topimage);
            holder.goodsInfo=(TextView)convertView.findViewById(R.id.footer_goods_info);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 适配数据
        holder.goodsName.setText(mDatas.get(i).getGoodsName());
        holder.goodsPrice.setText(mDatas.get(i).getGoodsPrice());
        holder.goodsInfo.setText(mDatas.get(i).getGoodsInfo());
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
     * @param footer
     */
    public void addFooterItem(Footer footer) {
        mDatas.add(footer);
        notifyDataSetChanged();
    }

    /**
     * 静态类，便于GC回收
     */
    public static class ViewHolder {
        TextView goodsName;
        TextView goodsPrice;
        ImageView topImage;
        TextView goodsInfo;
    }
}
