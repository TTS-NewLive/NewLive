package com.xiaoyu.schoolelive.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.base.BaseHisColSlide;
import com.xiaoyu.schoolelive.base.BaseSlideBack;

/**
 * Created by Administrator on 2017/7/11.
 */


public class HistoryCollectActivity extends BaseHisColSlide {
    private TabHost tabHost;
    public RecyclerView history_recyclerview,collect_recyclerview;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_collect);

        history_recyclerview = (RecyclerView)findViewById(R.id.history_recyclerview);
        collect_recyclerview = (RecyclerView)findViewById(R.id.collect_recyclerview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("  足迹  |  收藏  ");

        //找到TabHost的标签集合
        tabHost = (TabHost) this.findViewById(R.id.tabhost);
        /*如果没有继承TabActivity时，通过下面这种方法加载启动tabHost.这一句在源代码中,
        会根据findviewbyId()找到对应的TabWidget,还需要根据findViewById()找到
        这个TabWidget下面对应的标签页的内容.也就是FrameLayout这个显示控件.*/
        tabHost.setup();

        //TabSpec这个是标签页对象.
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("page1");//新建一个标签页对象.
        tabSpec.setIndicator(("足迹"));//设置这个标签页的标题和图片
        tabSpec.setContent(R.id.page1);//指定标签页的内容页.
        tabHost.addTab(tabSpec);//把这个标签页,添加到标签对象tabHost中.

        tabSpec = tabHost.newTabSpec("page2");
        tabSpec.setIndicator("收藏");
        tabSpec.setContent(R.id.page2);
        tabHost.addTab(tabSpec);

    }

    public void getIntentInfo(){
        //判断主页所传的值，对应显示选项卡
        Intent intent = getIntent();
        String str = intent.getStringExtra("str");
        if (str.equals("history")) {
        } else if(str.equals("collect")){
            if (tabHost.getCurrentTab() == 0){
                tabHost.setCurrentTab(1);
            }
        }


    }
    //标题栏菜单点击逻辑
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
