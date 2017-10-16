package com.xiaoyu.schoolelive.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.base.BaseSlideBack;
import com.xiaoyu.schoolelive.util.SmoothImageViewUtil;

import java.util.ArrayList;

import static com.xiaoyu.schoolelive.activities.UserCenterActivity.CODE_APP_REQUEST;

/**
 * Created by Administrator on 2017/7/11.
 */

public class AlbumImageDetailActivity extends BaseSlideBack {

    private ArrayList<String> mDatas;
    private int mPosition;
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    Bitmap bp = null;
    SmoothImageViewUtil imageView = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatas = (ArrayList<String>) getIntent().getSerializableExtra("images");
        mPosition = getIntent().getIntExtra("position", 0);
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);

        imageView = new SmoothImageViewUtil(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        //选择头像
        if (getIntent().getIntExtra("motive", 0) == CODE_APP_REQUEST) {
            setContentView(R.layout.activity_album_headimage_sel);
            imageView = (SmoothImageViewUtil) findViewById(R.id.detail_image);
            ImageLoader.getInstance().displayImage(mDatas.get(mPosition), imageView);
            ImageButton btn_cancel = (ImageButton) findViewById(R.id.cancel);
            ImageButton btn_ok = (ImageButton) findViewById(R.id.ok);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    String selImageUri = intent.getStringExtra("imgUri");
                    Intent selHeadImage = new Intent(AlbumImageDetailActivity.this, UserCenterActivity.class);
                    selHeadImage.putExtra("headImage", selHeadImage);
                    startActivity(selHeadImage);
                    finish();
                }
            });
        } else {
            //如果不是选择头像
            setContentView(R.layout.activity_album_image_detail);
            imageView = (SmoothImageViewUtil) findViewById(R.id.detail_image);
            ImageLoader.getInstance().displayImage(mDatas.get(mPosition), imageView);
            //
        }
        //此处可以加动画效果
    }

    @Override
    public void onBackPressed() {
        imageView.setOnTransformListener(new SmoothImageViewUtil.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == 2) {
                    finish();
                }
            }
        });
        imageView.transformOut();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }

    //点击当前activity销毁
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

}

