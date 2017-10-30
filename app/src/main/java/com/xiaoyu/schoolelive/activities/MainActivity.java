package com.xiaoyu.schoolelive.activities;

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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.base.BaseMainSlide;
import com.xiaoyu.schoolelive.custom.CustomFloatingDraftButton;
import com.xiaoyu.schoolelive.util.ACache;
import com.xiaoyu.schoolelive.util.Common_msg_cache;
import com.xiaoyu.schoolelive.util.Login_cache;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseMainSlide{
    private Intent intent,get_Intent;
    private long uid;//用户的id
    private DrawerLayout drawer;
    private TextView maintitle;
    private ImageView find;
    private ImageView intoLogin;

    private SecondHandFragment secondHandFragment ;
    private SysInformFragment sysInformFragment;
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
                        intent.setClass(MainActivity.this,HistoryCollectActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.report:
                        intent = new Intent(MainActivity.this,UserReportActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.SystemSetting:
                        intent = new Intent(MainActivity.this,SystemSettingActivity.class);
                        intent.putExtra("uid",uid);
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
        intoLogin = (ImageView)findViewById(R.id.intoLogin);
        maintitle = (TextView)findViewById(R.id.mainTitle);
        find = (ImageView)findViewById(R.id.find);

        intoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login_cache.get_login_status(getApplicationContext()).equals("false")){
                    intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,FindActivity.class);
                startActivity(intent);
            }
        });
        //判断用户是否登录，
        if (Login_cache.get_login_status(getApplicationContext()).equals("false")){
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            intoLogin.setImageResource(R.drawable.ic_menu_share);
        }else{
            intoLogin.setImageResource(R.drawable.menu_info_dw);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
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
        sysInformFragment = new SysInformFragment();
        secondHandFragment = new SecondHandFragment();

        get_Intent = getIntent();
        uid = get_Intent.getLongExtra("uid",0);

        if (Login_cache.get_login_status(getApplicationContext()) == null){
            Login_cache.set_login_false(getApplicationContext());
        }

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
