package com.xiaoyu.schoolelive.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.data.Goods;
import com.xiaoyu.schoolelive.data.ImageBean;
import com.xiaoyu.schoolelive.util.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by NeekChaw on 2017-07-28.
 */
public class WaterFallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public DisplayImageOptions mNormalImageOptions;
    private Context mContext;
    //private List<ImageBean> mList = new ArrayList<>();
    private List<Goods> mData = new ArrayList<>();
    private List<Integer> mHeights;

    private LayoutInflater mLayoutInflater;
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    private int mHeaderCount = 1;//头部View个数

    private String[] mItems = {"关注卖家", "收藏宝贝", "举报"};
    final String[] mAgainstItems = new String[]{"泄露隐私", "人身攻击", "淫秽色情", "垃圾广告", "敏感信息", "其他"};

    public WaterFallAdapter(Context context, List<Goods> datas) {
        this.mContext = context;
        this.mData = datas;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void getRandomHeight(List<Goods> mList) {
        mHeights = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            //随机的获取一个范围为200-600直接的高度
            mHeights.add((int) (500 + Math.random() * 400));
        }
    }

    //判断当前item是否是HeadView
    public boolean isHeaderView(int position) {
        return mHeaderCount != 0 && position < mHeaderCount;
    }

    //内容长度
    public int getContentItemCount() {
        return mData == null ? 0 : mData.size() - mHeaderCount;
    }

    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();
        if (mHeaderCount != 0 && position < mHeaderCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            View v = mLayoutInflater.inflate(R.layout.include_secondhand_head, parent, false);
            BGABanner mAdImages;
            mAdImages = (BGABanner) v.findViewById(R.id.goods_Ad_Banner);

            mAdImages.setAdapter(new BGABanner.Adapter<ImageView, String>() {
                @Override
                public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                    Glide.with(mContext)
                            .load(model)
                            .placeholder(R.drawable.gif_ad_holder)
                            .error(R.mipmap.ic_launcher)
                            .centerCrop()
                            .dontAnimate()
                            .into(itemView);
                }
            });

            List<String> Image_List = new ArrayList<>();
            List<String> wordsList = new ArrayList<>();
            Image_List.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503914804469&di=2cb048b4199e2dafed2601f347209771&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01fce15544624e0000019ae9559969.jpg%40900w_1l_2o_100sh.jpg");
            Image_List.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504509481&di=90f71e005c64f0ce96a0c70a88ddda31&imgtype=jpg&er=1&src=http%3A%2F%2Fi.dimg.cc%2F98%2Fb6%2F89%2F5d%2F7b%2Fcc%2F5a%2F99%2Fa6%2F71%2F51%2F94%2Fdc%2Fdf%2Fd7%2Fa6.jpg");
            Image_List.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503914946615&di=538843919443b3d379d340d44b51fa73&imgtype=0&src=http%3A%2F%2Ff1.diyitui.com%2Fcf%2F9a%2Ff8%2F88%2F0f%2Ffc%2F34%2F89%2Fb1%2Faf%2F34%2Fec%2F9e%2Fee%2F09%2F77.jpg");
            wordsList.add("虚位以待");
            wordsList.add("虚位以待");
            wordsList.add("虚位以待");
            mAdImages.setData(Image_List, wordsList);

            return new HeaderViewHolder(v);
        } else if (viewType == ITEM_TYPE_CONTENT) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.custom_goods_thum, parent, false));
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size() + 1;
    }

    //头部 ViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.imageview)
        ImageView mImageView;
        //        @Bind(R.id.goods_top)
//        TextView mGoodsTop;
//        @Bind(R.id.goods_hot)
//        TextView mGoodsHot;
//        @Bind(R.id.goods_new)
//        TextView mGoodsNew;
//        @Bind(R.id.goods_attention_count_thum)
//        TextView mGoodsPageViews;
        @Bind(R.id.goods_name)
        TextView mGoodsName;
        @Bind(R.id.goods_thum_price)
        TextView mGoodsPrice;

        public ViewHolder(View view) {
            //需要设置super
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public List<Goods> getList() {
        return mData;
    }

    public static DisplayImageOptions NORMAL_OPTION = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageOnLoading(R.mipmap.ic_launcher)
            .showImageForEmptyUri(R.mipmap.ic_launcher)
            .showImageOnFail(R.mipmap.ic_launcher)
            .build();

    //    public void addGoods(Goods goods) {
//        mData.add(goods);
//        notifyDataSetChanged();
//    }
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {

        } else if (holder instanceof ViewHolder) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            //int height = 500 + (int)(Math.random()*650);//350 到 650之间的随机数
            layoutParams.height = 730;
            holder.itemView.setLayoutParams(layoutParams);

            initImageLoader(mContext);
            Goods goods = mData.get(position - 1);
            //获取封面图片
            ImageBean bean = goods.getTopImages();
            //获取商品类型
            //int GoodsStyle = goods.getGoodsStyle();
            //setGoodsStyle((ViewHolder) holder, GoodsStyle);
            //获取商品名称
            ((ViewHolder) holder).mGoodsName.setText(goods.getGoodsName());
            //设置价格
            setGoodsPrice((ViewHolder) holder, goods, goods.getGoodsType());
            //获取商品价格
            //((ViewHolder) holder).mGoodsPrice.setText(goods.getPrice());
            //获取商品浏览量
            //((ViewHolder) holder).mGoodsPageViews.setText(goods.getPageViews() + "");
            //显示图片

            ImageLoader.getInstance().displayImage(bean.getImgsrc(),
                    ((ViewHolder) holder).mImageView, NORMAL_OPTION);//设置图片

            // 如果设置了回调，则设置点击事件
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition() - 1;
                        onItemClickListener.onItemClick(holder.itemView, position);

                    }
                });
                //LongClick
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getLayoutPosition() - 1;
                        onItemClickListener.onItemLongClick(holder.itemView, position);
                        return false;
                    }
                });
            }
        }
    }

    private void setGoodsPrice(ViewHolder holder, Goods goods, int type) {
        if (type == ConstantUtil.Goods_Type_ykj) {
            holder.mGoodsPrice.setText(String.valueOf(goods.getPrice()));
        } else if (type == ConstantUtil.Goods_Type_pai) {
            holder.mGoodsPrice.setText(String.valueOf(goods.getNowPrice()));
        }
    }

//    private void setGoodsStyle(ViewHolder holder, int type) {
//        if (type == ConstantUtil.Goods_All) {
//            holder.mGoodsHot.setVisibility(View.VISIBLE);
//            holder.mGoodsNew.setVisibility(View.VISIBLE);
//            holder.mGoodsTop.setVisibility(View.VISIBLE);
//        } else if (type == ConstantUtil.Goods_Hot) {
//            holder.mGoodsHot.setVisibility(View.VISIBLE);
//        } else if (type == ConstantUtil.Goods_New) {
//            holder.mGoodsNew.setVisibility(View.VISIBLE);
//        } else if (type == ConstantUtil.Goods_Top) {
//            holder.mGoodsTop.setVisibility(View.VISIBLE);
//        } else if (type == ConstantUtil.Goods_Top_Hot) {
//            holder.mGoodsTop.setVisibility(View.VISIBLE);
//            holder.mGoodsHot.setVisibility(View.VISIBLE);
//        } else if (type == ConstantUtil.Goods_Hot_New) {
//            holder.mGoodsHot.setVisibility(View.VISIBLE);
//            holder.mGoodsNew.setVisibility(View.VISIBLE);
//        } else if (type == ConstantUtil.Goods_Top_New) {
//            holder.mGoodsTop.setVisibility(View.VISIBLE);
//            holder.mGoodsNew.setVisibility(View.VISIBLE);
//        }
//    }

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

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && holder.getLayoutPosition() == 0) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }
}