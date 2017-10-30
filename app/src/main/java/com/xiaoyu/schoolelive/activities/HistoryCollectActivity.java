package com.xiaoyu.schoolelive.activities;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoyu.schoolelive.R;

/**
 * Created by Administrator on 2017/7/11.
 */


public class HistoryCollectActivity extends AppCompatActivity {
    public RecyclerView history_recyclerview;
    TextView toolbarTitle;
    ImageView toolbarBack;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_collect);

        history_recyclerview = (RecyclerView)findViewById(R.id.history_recyclerview);

        toolbarTitle = (TextView)findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("足迹");
        toolbarBack = (ImageView)findViewById(R.id.toolbarBack);
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
