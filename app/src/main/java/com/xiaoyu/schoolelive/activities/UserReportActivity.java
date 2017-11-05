package com.xiaoyu.schoolelive.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoyu.schoolelive.base.BaseSlideBack;

import com.xiaoyu.schoolelive.R;

/**
 * Created by Administrator on 2017/7/11.
 */
public class UserReportActivity extends BaseSlideBack {

    private TextView tv;
    private ImageView ig;
    private EditText editText;
    private Button button;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);

        tv = (TextView)findViewById(R.id.toolbarTitle);
        tv.setText("编辑资料");
        ig = (ImageView)findViewById(R.id.toolbarBack);
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        editText = (EditText)findViewById(R.id.userReportET);
        button = (Button) findViewById(R.id.userReportBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(UserReportActivity.this,editText.getText(),Toast.LENGTH_LONG).show();
            }
        });


    }
}
