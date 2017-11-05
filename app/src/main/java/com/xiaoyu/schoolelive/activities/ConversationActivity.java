package com.xiaoyu.schoolelive.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoyu.schoolelive.R;

public class ConversationActivity extends FragmentActivity {

    private TextView mName;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.conversation);
        mName = (TextView)findViewById(R.id.title);
        getIntent().getData().getQueryParameter("targetId");//获得id
        String sName =  getIntent().getData().getQueryParameter("title");//获得昵称（需要实现内容提供者）
        if(!TextUtils.isEmpty(sName)){
            mName.setText(sName);
           // Toast.makeText(this, sName, Toast.LENGTH_SHORT).show();
        }else{
            mName.setText("hxy");
            Toast.makeText(this, "hxy", Toast.LENGTH_SHORT).show();
        }



    }

}