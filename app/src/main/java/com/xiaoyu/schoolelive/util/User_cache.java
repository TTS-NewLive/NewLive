package com.xiaoyu.schoolelive.util;

import android.content.Context;

import com.xiaoyu.schoolelive.data.Goods;
import com.xiaoyu.schoolelive.data.User;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/5.
 */

public class User_cache {
    public static void set_user_info_Cache(Context context, User user){//序列化之后才能添加
        ACache aCache = ACache.get(context);
        aCache.put("user_info_cache",user);
    }
    public static User get_user_info_Cache(Context context){//得到缓存的商品信息
        ACache aCache = ACache.get(context);
        User user_info = (User)aCache.getAsObject("user_info_cache");
        return user_info;
    }
}
