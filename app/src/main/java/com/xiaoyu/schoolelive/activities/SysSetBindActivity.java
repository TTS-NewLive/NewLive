package com.xiaoyu.schoolelive.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.xiaoyu.schoolelive.R;

import java.util.ArrayList;
import java.util.List;

public class SysSetBindActivity extends AppCompatActivity {
    private ListView setBind_listView;
    private LayoutInflater layoutInflater;
    private View mTitleView,mView;
    private  AlertDialog.Builder builder;
    private AlertDialog dialog;
    private WindowManager.LayoutParams params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_set_bind);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("账号设置");

        setBind_listView = (ListView)findViewById(R.id.sysset_bind_listview);
        final List<String> list = new ArrayList<String>();
        list.add("手机绑定");
        list.add("邮箱绑定");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SysSetBindActivity.this,android.R.layout.simple_list_item_1,list);
        setBind_listView.setAdapter(adapter);

        setBind_listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://密码修改
                        layoutInflater = LayoutInflater.from(SysSetBindActivity.this);
                        //自定义对话框标题栏
                        mTitleView = layoutInflater.inflate(R.layout.custom_bindphone_dialog, null);

                        mView = layoutInflater.inflate(R.layout.bind_phone_content,null);
                        //创建对话框
                        builder = new AlertDialog.Builder(SysSetBindActivity.this);

                        builder.setCustomTitle(mTitleView);
                        builder.setView(mView);
                        dialog = builder.show();
                        //设置宽度和高度
                        params = dialog.getWindow().getAttributes();
                        params.width = 1000;
                        params.height =800;
                        dialog.getWindow().setAttributes(params);
//
                        break;
                    case 1://账号绑定
                        layoutInflater = LayoutInflater.from(SysSetBindActivity.this);
                        //自定义对话框标题栏
                        mTitleView = layoutInflater.inflate(R.layout.custom_changepass_dialog, null);

                        mView = layoutInflater.inflate(R.layout.bind_adress_content,null);
                        //创建对话框
                        builder = new AlertDialog.Builder(SysSetBindActivity.this);

                        builder.setCustomTitle(mTitleView);
                        builder.setView(mView);
                        dialog = builder.show();
                        //设置宽度和高度
                        params = dialog.getWindow().getAttributes();
                        params.width = 1000;
                        params.height =800;
                        dialog.getWindow().setAttributes(params);

                        break;
                }
            }
        });
    }
    //标题栏菜单点击逻辑
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
