package com.xiaoyu.schoolelive.util;
import android.content.Context;
import android.widget.Toast;

import com.xiaoyu.schoolelive.data.Deal;
import com.xiaoyu.schoolelive.data.Footer;
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
    public static void set_deal_Cache(Context context, ArrayList<Deal> list){//序列化之后才能添加
        ACache aCache = ACache.get(context);
        aCache.put("deal_cache",list);
    }
    public static ArrayList<Deal> get_deal_Cache(Context context){
        ACache aCache = ACache.get(context);
        ArrayList<Deal> cache_deal = (ArrayList<Deal>)aCache.getAsObject("deal_cache");
        return cache_deal;
    }
    public static void add_goods_cache_status(Context context,int toIndex){//更新当前加载状态
        set_goods_cache_status(context,toIndex);
    }
    public static void refresh_goods_Caches(Context context,ArrayList<Goods> goods){
        set_goods_Cache(context,goods);
    }
//    public static void set_login_true(Context context,String uid){//设置当前用户登录状态并将帐号保存在缓存中
//        ACache aCache = ACache.get(context);
//        aCache.put("login_status","true");
//        aCache.put("now_login",uid);
//    }
//    public static void set_login_false(Context context){
//        ACache aCache = ACache.get(context);
//        aCache.put("login_status","false");
//        aCache.put("now_login","");
//    }
//    public  static String get_login_status(Context context){
//        ACache aCache = ACache.get(context);
//        String login_staus = aCache.getAsString("login_status");
//        return login_staus;
//    }
//    public static String get_now_uid(Context context){//得到当前登录id
//        ACache aCache = ACache.get(context);
//        String now_uid = aCache.getAsString("now_login");
//        return now_uid;
//    }

    public static void set_footer_Cache(Context context, ArrayList<Footer> list){//序列化之后才能添加
        ACache aCache = ACache.get(context);//添加对足迹的缓存
        aCache.put("footer_cache",list);
    }
    public static ArrayList<Footer> get_footer_Cache(Context context){
        ACache aCache = ACache.get(context);
        ArrayList<Footer> cache_footer = (ArrayList<Footer>)aCache.getAsObject("footer_cache");
        //得到足迹的缓存
        return cache_footer;
    }

}
