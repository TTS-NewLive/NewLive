package com.xiaoyu.schoolelive.util;

import android.content.Context;

import com.xiaoyu.schoolelive.util.ACache;

/**
 * Created by Administrator on 2017-10-30.
 */

public class Inform_cache {

    public static void set_inform_id(Context context,String username){
        ACache aCache = ACache.get(context);
        aCache.put("inform_id",username);
    }
    public static String get_inform_id(Context context){
        ACache aCache = ACache.get(context);
        String login_username = aCache.getAsString("inform_id");
        return login_username;
    }

}
