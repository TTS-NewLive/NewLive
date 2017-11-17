package com.xiaoyu.schoolelive.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.custom.CircleImageView;
import com.xiaoyu.schoolelive.custom.CustomFloatingDraftButton;
import com.xiaoyu.schoolelive.data.User;
import com.xiaoyu.schoolelive.util.ConstantUtil;
import com.xiaoyu.schoolelive.util.HttpUtil;
import com.xiaoyu.schoolelive.util.Login_cache;
import com.xiaoyu.schoolelive.util.User_cache;
import com.xiaoyu.schoolelive.util.WidgetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
//极光推送所需要的类
  class TagAliasBean {
    int action;
    Set<String> tags;
    String alias;
    boolean isAliasAction;

    @Override
    public String toString() {
        return "TagAliasBean{" +
                "action=" + action +
                ", tags=" + tags +
                ", alias='" + alias + '\'' +
                ", isAliasAction=" + isAliasAction +
                '}';
    }
}

public class MainActivity extends AppCompatActivity implements RongIM.UserInfoProvider{
    private Intent intent,get_Intent;
    private long uid;//用户的id
    private DrawerLayout drawer;
    private TextView maintitle;
    private ImageView find;
    private CircleImageView intoLogin;
    private Fragment mConversationList;
    private Fragment mConversationFragment = null;
    private SecondHandFragment secondHandFragment = new SecondHandFragment();
    private SysInformFragment sysInformFragment = new SysInformFragment();
    private BottomNavigationView navigation;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String login_status = "false";
    private String token = null;
    private String photo;
    private ImageView imageView;
    private ImageView add_inform;
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String token = (String) msg.obj;
            connectRongServer(token);

        }
    };

    @Bind(R.id.floatingActionButton)
    CustomFloatingDraftButton floatingDraftButton;

    //引入侧滑栏布局
    public void mainInitSlidView() {
        //获取侧滑栏的头部
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View view = navigationView.inflateHeaderView(R.layout.include_nav_header_main);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //drawer.closeDrawer(GravityCompat.START);
                intent = new Intent(MainActivity.this, UserInfo.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        //侧滑栏菜单点击逻辑
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem i) {
                switch (i.getItemId()){
                    case R.id.order:
                        intent = new Intent(MainActivity.this,DealListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.history:
                        intent = new Intent();
                        intent.setClass(MainActivity.this,FooterListActivity.class);
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
                intent = new Intent(MainActivity.this, UserAddGoodsActivity.class);
                startActivity(intent);
            }
        });
    }

    //引入标题栏
    private void mainInitToolBar() {
        //标题栏
        intoLogin = (CircleImageView) findViewById(R.id.intoLogin);
        maintitle = (TextView)findViewById(R.id.mainTitle);
        find = (ImageView)findViewById(R.id.find);
        add_inform =(ImageView)findViewById(R.id.add_inform);

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

        add_inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,AddInform.class);
                startActivity(intent);
            }
        });

    }

    //在程序中加入默认Fragment
    public void mainAddFragment() {
        fragmentManager = getSupportFragmentManager();
        //开启一个Fragment事务
        fragmentTransaction = fragmentManager.beginTransaction();
        secondHandFragment = new SecondHandFragment();
        fragmentTransaction.add(R.id.main_menu_content, secondHandFragment, "secondhand");
        fragmentTransaction.commit();
    }

    //引入底部菜单
    public void mainInitBottomBar() {
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
                    case R.id.navigation_message:
                        mainSetMessageFrament();
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

    public void showFloatButton(){
        if (ConstantUtil.SHOW_FLOATBUTTON == 0){
            floatingDraftButton.setVisibility(View.INVISIBLE);
        }else{
            floatingDraftButton.setVisibility(View.VISIBLE);
        }
    }

    public void mainSetSecondFrament() {
        maintitle.setText("旧货");
        ConstantUtil.SHOW_FLOATBUTTON = 1;
        showFloatButton();
        secondHandFragment = new SecondHandFragment();
        fragmentTransaction.replace(R.id.main_menu_content, secondHandFragment, "secondhand").commit();

    }

    public void mainSetSysinformFrament() {
        maintitle.setText("通知");
        ConstantUtil.SHOW_FLOATBUTTON = 0;
        showFloatButton();
        sysInformFragment = new SysInformFragment();
        fragmentTransaction.replace(R.id.main_menu_content, sysInformFragment, "inform").commit();
    }

    public void mainSetMessageFrament(){
        maintitle.setText("会话");
        ConstantUtil.SHOW_FLOATBUTTON = 0;
        showFloatButton();

        mConversationList = initConversationList();//获得融云会话列表的对象
        fragmentTransaction.replace(R.id.main_menu_content,mConversationList,"message").commit();
    }

    private Fragment initConversationList(){
        if(mConversationFragment == null){
            ConversationListFragment listFragment = new ConversationListFragment();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
                    .build();
            listFragment.setUri(uri);
            return listFragment;
        }else{
            return mConversationList;
        }
    }

    private void get_token() {//通过用户id得到token(融云所需要的)
        String is_loginActivity_in = getIntent().getStringExtra("login_to_main");
        if (is_loginActivity_in != null) {
            if (is_loginActivity_in.equals("success")) {//如果是由LoginActivity跳转而来

                //将信息（uid）发送给极光推送服务器
                TagAliasBean tagAliasBean = new TagAliasBean();
                tagAliasBean.alias = getIntent().getStringExtra("uid");
                JPushInterface.setAlias(getApplicationContext(),1,tagAliasBean.alias);


                final RequestBody requestBody = new FormBody.Builder()
                        .add("uid", getIntent().getStringExtra("uid"))
                        .add("name", getIntent().getStringExtra("uid"))//默认名称为uid
                        .build();
                HttpUtil.sendHttpRequest("http://39.106.31.44/API/get_token.php", requestBody, new Callback() {
                    public void onFailure(Call call, IOException e) {
                        Log.e("Succ",e.getMessage());
                    }
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            String token = jsonObject.getString("token");
                            Log.e("Succ",token+"kk");
                            if (token != null) {
                                Login_cache.set_token(getApplicationContext(), getIntent().getStringExtra("uid"), token);//将token存入缓存中
                            }
                            Message msg = Message.obtain();
                            msg.obj = token;
                            myHandler.sendMessage(msg);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                });
            }
        }
    }
    private void connectRongServer(String token){//通过token连接融云的服务器
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
                    public void onTokenIncorrect() {
                        Log.e("Succ","kkk");
                    }
                    public void onSuccess(String s) {
                        Log.e("Succ",s+"kk");
                    }
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Log.e("Succ","kk");
                    }
                });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //添加默认Fragment
        mainAddFragment();
        //引入侧滑栏布局
        mainInitSlidView();
        //引入悬浮按钮
        mainInitFloatBar();
        //引入标题栏
        mainInitToolBar();
        //引入底部菜单
        mainInitBottomBar();
        //判断登录状态进行相关操作
        about_login();

        get_Intent = getIntent();
        uid = get_Intent.getLongExtra("uid",0);
        int toMainActivity = get_Intent.getIntExtra("toAddGoods",0);
        if(toMainActivity == 2){
            Toast.makeText(this, "发布商品成功", Toast.LENGTH_SHORT).show();
        }
        if (Login_cache.get_login_status(getApplicationContext()) == null){
            Login_cache.set_login_false(getApplicationContext());
        }
        get_token();//刚登陆成功的时候通过uid得到token,并存入缓存中
        //实现融云的消息内容提供者，即将用户的信息缓存到融云的服务器上
        RongIM.setUserInfoProvider(this,true);
    }

    @Override
    public io.rong.imlib.model.UserInfo getUserInfo(String s) {//将信息缓存到融云的服务器
        String encryption_s =  s.substring(0,3)+"****"+s.substring(7,11);
        io.rong.imlib.model.UserInfo userInfo = new io.rong.imlib.model.UserInfo(s,encryption_s,Uri.parse("http://bpic.588ku.com/element_pic/01/54/05/335746fd1e7f644.jpg!/fw/208/quality/90/unsharp/true/compress/true"));
        return userInfo;
    }

    Handler handler22 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String user_info = (String)msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(user_info);
                JSONObject jsobj = jsonArray.getJSONObject(0);
                photo = jsobj.getString("photo");
                if (photo == null){
                    imageView.setImageResource(R.drawable.qq_login);
                    intoLogin.setImageResource(R.drawable.qq_login);
                }else{
                    Login_cache.set_photo(getApplicationContext(),WidgetUtil.str_trim(ConstantUtil.SERVICE_PATH+photo));
                    Log.e("token",Login_cache.get_photo(getApplicationContext()));
                    Glide.with(MainActivity.this)//将选中的图片放到imageview 中
                            .load(Login_cache.get_photo(getApplicationContext()))
                            .error(R.drawable.qq_login)
                            .into(intoLogin);
                    Glide.with(MainActivity.this)//将选中的图片放到imageview 中
                            .load(Login_cache.get_photo(getApplicationContext()))
                            .error(R.drawable.qq_login)
                            .into(imageView);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void about_login(){
        if (Login_cache.get_login_status(getApplicationContext()) == null) {//如果缓存中不存在登录状态，设置为空
            Login_cache.set_login_false(getApplicationContext());
        }
        if (Login_cache.get_login_status(getApplicationContext()).equals("false")){
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            intoLogin.setImageResource(R.drawable.login);
            imageView.setImageResource(R.drawable.login);
        }
        if (Login_cache.get_login_status(getApplicationContext()).equals("true")){
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            User user = User_cache.get_user_info_Cache(getApplicationContext());
            if (Login_cache.get_photo(getApplicationContext()) == null || user.getUid() != Login_cache.get_login_username(getApplicationContext())){
                RequestBody requestBody = new FormBody.Builder()
                        .add("uid", String.valueOf(Login_cache.get_login_username(getApplicationContext())))
                        .build();
                HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH+"query_data.php", requestBody, new okhttp3.Callback() {
                    public void onFailure(Call call, IOException e) {
                        Log.e("error",e.getMessage());
                    }
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Message msg = Message.obtain();
                        msg.obj = responseData;
                        handler22.sendMessage(msg);
                    }
                });
            }else{
                Glide.with(MainActivity.this)//将选中的图片放到imageview 中
                        .load(Login_cache.get_photo(getApplicationContext()))
                        .error(R.drawable.qq_login)
                        .into(intoLogin);
                Glide.with(MainActivity.this)//将选中的图片放到imageview 中
                        .load(Login_cache.get_photo(getApplicationContext()))
                        .error(R.drawable.qq_login)
                        .into(imageView);
            }
        }
    }

}