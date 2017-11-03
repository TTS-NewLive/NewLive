package com.xiaoyu.schoolelive.activities;


import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v7.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.base.BaseSlideBack;
import com.xiaoyu.schoolelive.util.Common_msg_cache;
import com.xiaoyu.schoolelive.util.ConstantUtil;
import com.xiaoyu.schoolelive.util.HttpUtil;
import com.xiaoyu.schoolelive.util.Login_cache;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/11.
 */
public class SystemSettingActivity extends BaseSlideBack {
    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            String data = result.substring(0,1);

            if( String.valueOf(0).equals(data)){//如果返回 说明该用户不存在
                Toast.makeText(SystemSettingActivity.this,"用户或者密码错误!!!", Toast.LENGTH_SHORT).show();
            }else if (String.valueOf(1).equals(data)){
                new AlertDialog.Builder(SystemSettingActivity.this)
                        .setTitle("密码修改成功，是否跳转至登录界面？")
                        .setIcon(R.drawable.side_nav_bar)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Login_cache.set_login_false(getApplicationContext());
                                Login_cache.set_login_password(getApplicationContext(),"");
                                Intent intent = new Intent(SystemSettingActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        }
    };

    private ListView listView;
    private Intent intent;
    private Long uid;
    private Button backLogin;
    private ImageView ig;
    private TextView tv;
    private EditText old_password,new_password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);

        ig = (ImageView)findViewById(R.id.toolbarBack);
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv = (TextView)findViewById(R.id.toolbarTitle);
        tv.setText("系统设置");

        intent = getIntent();
        uid = intent.getLongExtra("uid",0);

        listView = (ListView)findViewById(R.id.systemSetting_listview);
        final List<String> list = new ArrayList<String>();
        list.add("密码修改");
        list.add("用户使用协议说明");
        list.add("关于我们");
        list.add("清除缓存");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SystemSettingActivity.this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://密码修改
                        if (Login_cache.get_login_status(getApplicationContext()).equals("true")){
                            LayoutInflater layoutInflater = LayoutInflater.from(SystemSettingActivity.this);
                            //自定义对话框标题栏
                            View mTitleView = layoutInflater.inflate(R.layout.custom_changepass_dialog, null);

                            View mView = layoutInflater.inflate(R.layout.change_password_content,null);
                            //创建对话框
                            AlertDialog.Builder builder = new AlertDialog.Builder(SystemSettingActivity.this);

                            builder.setCustomTitle(mTitleView);
                            builder.setView(mView);
                            final AlertDialog dialog = builder.show();
                            //设置宽度和高度
                            WindowManager.LayoutParams params =
                                    dialog.getWindow().getAttributes();
                            params.width = 1000;
                            params.height =800;
                            dialog.getWindow().setAttributes(params);

                            Button true_button = (Button)mView.findViewById(R.id.changepass_true_button);
                            Button false_button = (Button)mView.findViewById(R.id.changepass_false_button);
                            old_password = (EditText)mView.findViewById(R.id.old_password);
                            new_password = (EditText)mView.findViewById(R.id.new_password);

                            true_button.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.hide();
                                    RequestBody requestBody = new FormBody.Builder()
                                            .add("uid", String.valueOf(uid))
                                            .add("old_password", old_password.getText().toString())
                                            .add("new_password", new_password.getText().toString())
                                            .build();
                                    HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH+"change_pass.php", requestBody, new okhttp3.Callback() {
                                        public void onFailure(Call call, IOException e) {
                                            Log.e("error",e.getMessage());
                                        }
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String data = response.body().string();
                                            Message msg = Message.obtain();
                                            msg.obj = data;
                                            myhandler.sendMessage(msg);
                                        }
                                    });
                                }
                            });
                            false_button.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {

                                }
                            });
                        }else{
                            Toast.makeText(SystemSettingActivity.this,"尚未登录！",Toast.LENGTH_LONG).show();
                        }
                        break;
//                    case 1://切换账号
//                        if (MainActivity.boo){
//                            new AlertDialog.Builder(SystemSettingActivity.this)
//                                    .setTitle("是否切换用户？")
//                                    .setIcon(R.drawable.side_nav_bar)
//                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            MainActivity.boo = false;
//                                            Intent intent = new Intent(SystemSettingActivity.this,LoginActivity.class);
//                                            startActivity(intent);
//                                        }
//                                    })
//                                    .setNegativeButton("取消", null)
//                                    .show();
//                        }else{
//                            Toast.makeText(SystemSettingActivity.this,"尚未登录！",Toast.LENGTH_LONG).show();
//                        }
//                        break;
//                    case 2://主题设置
//
//                        break;
//                    case 3://推荐分享
//                        intent = new Intent(SystemSettingActivity.this,SysSetShareActivity.class);
//                        startActivity(intent);
//                        break;
                    case 1://用户使用协议说明
                        Intent i = new Intent(SystemSettingActivity.this,UPActivity.class);
                        startActivity(i);
                        break;
                    case 2://关于我们

                        break;
                    case 3://消除缓存

                        break;
                }
            }
        });

        backLogin = (Button)findViewById(R.id.sysset_back_login);
        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login_cache.get_login_status(getApplicationContext()).equals("true")){
                    new AlertDialog.Builder(SystemSettingActivity.this)
                            .setTitle("是否确定退出？")
                            .setIcon(R.drawable.side_nav_bar)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(SystemSettingActivity.this,"已退出！",Toast.LENGTH_LONG).show();
                                    Login_cache.set_login_false(getApplicationContext());
                                    Intent intent = new Intent(SystemSettingActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }else{
                    Toast.makeText(SystemSettingActivity.this,"尚未登录！",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

