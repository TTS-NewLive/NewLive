package com.xiaoyu.schoolelive.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoyu.schoolelive.R;

public class UPActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up);
        textView=(TextView)findViewById(R.id.toolbarTitle);
        textView.setText("用户使用协议说明");
        imageView=(ImageView)findViewById(R.id.toolbarBack);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}