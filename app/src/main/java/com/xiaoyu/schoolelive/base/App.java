package com.xiaoyu.schoolelive.base;

import android.app.Application;
import android.util.Log;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class App extends Application {

    public void onCreate() {
        super.onCreate();
        RongIM.init(this);

    }

}
