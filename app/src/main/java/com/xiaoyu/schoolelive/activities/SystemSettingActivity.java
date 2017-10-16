package com.xiaoyu.schoolelive.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.base.BaseSlideBack;

/**
 * Created by Administrator on 2017/7/11.
 */
public class SystemSettingActivity extends BaseSlideBack {

    private ListView listView;
    private Intent intent;
    private Button backLogin;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("系统设置");

        listView = (ListView)findViewById(R.id.systemSetting_listview);
        final List<String> list = new ArrayList<String>();
        list.add("密码修改");
        list.add("切换账号");
        list.add("主题设置");
        list.add("推荐分享");
        list.add("用户使用协议说明");
        list.add("关于我们");
        list.add("清除缓存");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SystemSettingActivity.this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://密码修改
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

//                        Button true_button = (Button)findViewById(R.id.changepass_true_button);
//                        Button false_button = (Button)findViewById(R.id.changepass_false_button);
//                        true_button.setOnClickListener(new View.OnClickListener() {
//                            public void onClick(View v) {
//                            }
//                        });
//                        false_button.setOnClickListener(new View.OnClickListener() {
//                            public void onClick(View v) {
//                            }
//                        });
                        break;
                    case 1://切换账号
                        if (MainActivity.boo){
                            new AlertDialog.Builder(SystemSettingActivity.this)
                                    .setTitle("是否切换用户？")
                                    .setIcon(R.drawable.side_nav_bar)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            MainActivity.boo = false;
                                            Intent intent = new Intent(SystemSettingActivity.this,LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                        }else{
                            Toast.makeText(SystemSettingActivity.this,"尚未登录！",Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 2://主题设置

                        break;
                    case 3://推荐分享
                        intent = new Intent(SystemSettingActivity.this,SysSetShareActivity.class);
                        startActivity(intent);
                        break;
                    case 4://用户使用协议说明

                        break;
                    case 5://关于我们

                        break;
                    case 6://消除缓存

                        break;
                }
            }
        });

        backLogin = (Button)findViewById(R.id.sysset_back_login);
        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.boo){
                    new AlertDialog.Builder(SystemSettingActivity.this)
                            .setTitle("是否确定退出？")
                            .setIcon(R.drawable.side_nav_bar)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.boo = false;
                                    Toast.makeText(SystemSettingActivity.this,"已退出！",Toast.LENGTH_LONG).show();
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
    //标题栏菜单点击逻辑
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

