package com.xiaoyu.schoolelive.util;

import android.content.Context;

/**
 * Created by Administrator on 2017-10-30.
 */

public class Login_cache {
    /*
    * 设置用户的账号，密码缓存
    * 获取缓存中用户的账号与密码
    * */
    public static void set_login_username(Context context,String username){
        ACache aCache = ACache.get(context);
        aCache.put("login_username",username);
    }
    public static void set_login_password(Context context,String password){
        ACache aCache = ACache.get(context);
        aCache.put("login_password",password);
    }
    public static String get_login_username(Context context){
        ACache aCache = ACache.get(context);
        String login_username = aCache.getAsString("login_username");
        return login_username;
    }
    public static String get_login_password(Context context){
        ACache aCache = ACache.get(context);
        String login_password= aCache.getAsString("login_password");
        return login_password;
    }
    /*
    * 设置及获取记住密码是否选中
    * */
    public static void set_login_chose_true(Context context){
        ACache aCache = ACache.get(context);
        aCache.put("chose_status","true");
    }
    public static void set_login_chose_false(Context context){
        ACache aCache = ACache.get(context);
        aCache.put("chose_status","false");
    }
    public static String get_login_chose_status(Context context){
        ACache aCache = ACache.get(context);
        String chose_staus = aCache.getAsString("chose_status");
        return chose_staus;
    }

    /*
   * 1.设置登录状态为true
   * 2.设置登录状态为false
   * 3.获取登录状态
   * */
    public static void set_login_true(Context context){
        ACache aCache = ACache.get(context);
        aCache.put("login_status","true");
    }
    public static void set_login_false(Context context){
        ACache aCache = ACache.get(context);
        aCache.put("login_status","false");
    }
    public static String get_login_status(Context context){
        ACache aCache = ACache.get(context);
        String login_staus = aCache.getAsString("login_status");
        return login_staus;
    }

    public static void set_token(Context context,String uid,String token){//将token存入缓存中
        ACache aCache = ACache.get(context);
        aCache.put(uid,token);
    }
    public  static  String get_token(Context context,String uid){
        ACache aCache = ACache.get(context);
        String token = aCache.getAsString(uid);
        return token;
    }

    public static void set_photo(Context context,String str){
        ACache aCache = ACache.get(context);
        aCache.put("photo",str);
    }
    public static String get_photo(Context context){
        ACache aCache = ACache.get(context);
        String photo = aCache.getAsString("photo");
        return photo;
    }
}
