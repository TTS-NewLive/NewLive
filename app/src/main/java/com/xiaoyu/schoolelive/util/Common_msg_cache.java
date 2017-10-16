package com.xiaoyu.schoolelive.util;
import android.content.Context;
import android.widget.Toast;

import com.xiaoyu.schoolelive.data.Goods;
import com.xiaoyu.schoolelive.data.PartJob;
import com.xiaoyu.schoolelive.data.Publish;
import java.util.ArrayList;
/**
 * Created by Administrator on 2017/8/4.
 */
/*
*
*保存帖子有关的缓存,将ArrayList<Publish>数据保存到缓存中
*
*
*  Common_msg_cache common_msg_cache = new Common_msg_cache();
                        common_msg_cache.setCache(getActivity(),date);
                        ArrayList<Publish> date = common_msg_cache.getCache(getActivity());
* */
public  class Common_msg_cache {
    //设置帖子的缓存
    public static void set_msg_Cache(Context context, ArrayList<Publish> list){//序列化之后才能添加
        ACache aCache = ACache.get(context);
        aCache.put("msg_cache",list);
    }
    public static ArrayList<Publish> get_msg_Cache(Context context){
        ACache aCache = ACache.get(context);
        ArrayList<Publish> publish = (ArrayList<Publish>)aCache.getAsObject("msg_cache");
        return publish;
    }
    //设置旧货的缓存
    public static void set_goods_Cache(Context context, ArrayList<Goods> list){//序列化之后才能添加
        ACache aCache = ACache.get(context);
        aCache.put("goods_cache",list);
    }
    public static ArrayList<Goods> get_goods_Cache(Context context){//得到缓存的商品信息
        ACache aCache = ACache.get(context);
        ArrayList<Goods> cache_goods = (ArrayList<Goods>)aCache.getAsObject("goods_cache");
        return cache_goods;
    }
    public static void add_goods_Cache(Context context,Goods e){//加入一条商品信息到缓存中
        ArrayList<Goods> cache_goods = get_goods_Cache(context);
        if(cache_goods != null){
            cache_goods.add(e);
        }
        set_goods_Cache(context,cache_goods);//新加入的数据再存入缓存中
    }
    public static void set_goods_cache_status(Context context,int toIndex){//设置当前加载的状态(即记录缓存加载到第几条了)
        ACache aCache = ACache.get(context);
        aCache.put("toIndex",toIndex+"");
    }
    public static int get_goods_cache_status(Context context){//设置当前加载的状态(即记录缓存加载到第几条了)
        ACache aCache = ACache.get(context);
        int toIndex = Integer.valueOf(aCache.getAsString("toIndex"));
        return toIndex;
    }
    public static void add_goods_cache_status(Context context,int toIndex){//更新当前加载状态
        set_goods_cache_status(context,toIndex);
    }
    //设置兼职的缓存
    public static void add_jobs_Cache(Context context, ArrayList<PartJob> list){//序列化之后才能添加
        ACache aCache = ACache.get(context);
        aCache.put("jobs_cache",list);
    }
    //得到兼职的缓存
    public static ArrayList<PartJob> get_jobs_Cache(Context context){//得到缓存的商品信息
        ACache aCache = ACache.get(context);
        ArrayList<PartJob> cache_goods = (ArrayList<PartJob>)aCache.getAsObject("jobs_cache");
        return cache_goods;
    }


    /*
    * 更新缓存
    * */
    public static void refresh_jobs_Caches(Context context,ArrayList<PartJob> partJobs){
        add_jobs_Cache(context,partJobs);
    }

    public static void refresh_goods_Caches(Context context,ArrayList<Goods> goods){
        set_goods_Cache(context,goods);

    }

}
