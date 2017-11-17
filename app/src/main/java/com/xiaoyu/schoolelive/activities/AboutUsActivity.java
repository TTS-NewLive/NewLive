package com.xiaoyu.schoolelive.activities;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.util.ConstantUtil;
import com.xiaoyu.schoolelive.util.Login_cache;
import com.xiaoyu.schoolelive.util.WidgetUtil;

public class AboutUsActivity extends AppCompatActivity {

    private TextView tv;
    private ImageView ig,logo_one,logo_two;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ig = (ImageView)findViewById(R.id.toolbarBack);
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv = (TextView)findViewById(R.id.toolbarTitle);
        tv.setText("关于我们");

        logo_one = (ImageView)findViewById(R.id.logo_one);
        logo_two = (ImageView)findViewById(R.id.logo_two);

        Glide.with(this)//将选中的图片放到imageview 中
                .load(R.mipmap.logo_xiaoyu_2)
                .error(R.drawable.qq_login)
                .into(logo_one);
        Glide.with(this)//将选中的图片放到imageview 中
                .load(R.mipmap.logo_xiaoyu_3)
                .error(R.drawable.qq_login)
                .into(logo_two);
    }
}
