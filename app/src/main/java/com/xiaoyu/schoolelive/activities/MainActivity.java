package com.xiaoyu.schoolelive.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.base.BaseMainSlide;
import com.xiaoyu.schoolelive.custom.CustomFloatingDraftButton;
import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseMainSlide{
    private Intent intent,get_Intent;
    private long uid;//用户的id
    public static boolean boo = false;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private SecondHandFragment secondHandFragment = new SecondHandFragment();
    private SysInformFragment sysInformFragment = new SysInformFragment();
    private BottomNavigationView navigation;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


    @Bind(R.id.floatingActionButton)
    CustomFloatingDraftButton floatingDraftButton;

    //引入侧滑栏布局
    public void mainInitSlidView(){
        //获取侧滑栏的头部
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View view = navigationView.inflateHeaderView(R.layout.include_nav_header_main);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //drawer.closeDrawer(GravityCompat.START);
                intent = new Intent(MainActivity.this,UserInfo.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

        //侧滑栏菜单点击逻辑
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem i) {
                switch (i.getItemId()){
                    case R.id.order:
                        /*
                        * 订单历史
                        * */
                        break;
                    case R.id.history:
                        intent = new Intent();
                        intent.putExtra("str","history");
                        intent.setClass(MainActivity.this,HistoryCollectActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.report:
                        intent = new Intent(MainActivity.this,UserReportActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.SystemSetting:
                        intent = new Intent(MainActivity.this,SystemSettingActivity.class);
                        startActivity(intent);
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    //引入悬浮按钮
    public void mainInitFloatBar() {
        ButterKnife.bind(this);

        floatingDraftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出动态button
                intent = new Intent(MainActivity.this,UserAddGoodsActivity.class);
                startActivity(intent);
            }
        });
    }
    //引入标题栏
    private void mainInitToolBar(){
        //标题栏
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //判断用户是否登录，
        if (!boo){
            //显示未登录图像
            actionBar.setHomeAsUpIndicator(R.drawable.icon_main_home);
        }else{
            //显示已登录用户的头像
        }
    }
    //在程序中加入默认Fragment
    public void mainAddFragment(){
        fragmentManager = getSupportFragmentManager();
        //开启一个Fragment事务
        fragmentTransaction = fragmentManager.beginTransaction();
        secondHandFragment = new SecondHandFragment();
        fragmentTransaction.add(R.id.main_menu_content, secondHandFragment,"secondhand");
        fragmentTransaction.commit();
    }
    //引入底部菜单
    public void mainInitBottomBar(){
        //实例化BottomNavigationView菜单
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //BottomNavigationView菜单点击逻辑
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //mainFragmentAllRemove();
                //在程序中加入Fragment
                fragmentManager = getSupportFragmentManager();
                //开启一个Fragment事务
                fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_secondhand:
                        mainSetSecondFrament();
                        return true;
                    case R.id.navigation_sysinform:
                        mainSetSysinformFrament();
                        return true;
                }
                return false;
            }
        });
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //导入菜单项
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //标题栏菜单点击逻辑
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (!boo){
                    intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    drawer.openDrawer(GravityCompat.START);
                }
                return true;

            case R.id.findButton:
                intent = new Intent(MainActivity.this,FindActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void mainSetSecondFrament(){
        getSupportActionBar().setTitle("旧货");
        SysInformFragment.SysInformIsDisplay = false;
        secondHandFragment = new SecondHandFragment();
        fragmentTransaction.replace(R.id.main_menu_content, secondHandFragment, "secondhand").commit();

    }
    public void mainSetSysinformFrament(){
        getSupportActionBar().setTitle("通知");
        SysInformFragment.SysInformIsDisplay = true;
        sysInformFragment = new SysInformFragment();
        fragmentTransaction.replace(R.id.main_menu_content, sysInformFragment,"inform").commit();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);

        mainAddFragment();
        //引入侧滑栏布局
        mainInitSlidView();
        //引入悬浮按钮
        mainInitFloatBar();
        //引入标题栏
        mainInitToolBar();
        //引入底部菜单
        mainInitBottomBar();
    }
}
