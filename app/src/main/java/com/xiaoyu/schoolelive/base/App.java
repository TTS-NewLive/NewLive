package com.xiaoyu.schoolelive.base;

import android.app.Application;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class App extends Application {

    public void onCreate() {
        super.onCreate();
        //初始化融云服务
        RongIM.init(this);
        //初始化极光通信
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }

}
