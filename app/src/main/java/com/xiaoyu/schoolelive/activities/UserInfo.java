package com.xiaoyu.schoolelive.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.base.BaseSlideBack;
import com.xiaoyu.schoolelive.custom.CustomBar;
import com.xiaoyu.schoolelive.custom.CustomImageDialogView;
import com.xiaoyu.schoolelive.util.ConstantUtil;
import com.xiaoyu.schoolelive.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public  class UserInfo extends BaseSlideBack implements View.OnClickListener {
    class MyHandler extends Handler {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            int index = b.getInt("index");
            String info = b.getString("info");
            String data = b.getString("data");
            String sex = b.getString("sex");
            switch (index){
                case 0://初始化
                    String photo = b.getString("photo");
                    String name = b.getString("name");
                    String real_name = b.getString("real_name");
                    String signature = b.getString("signature");
                    custom_photo.setInfo_menu_info(photo);
                    custom_fname.setInfo_menu_info(name);
                    custom_about.setInfo_menu_info(signature);
                    custom_tname.setInfo_menu_info(real_name);
                    if (String.valueOf("0").equals(sex)) {
                        custom_sex.setInfo_menu_info("男");
                    } else if (String.valueOf("1").equals(sex)) {
                        custom_sex.setInfo_menu_info("女");
                    }
                    Toast.makeText(UserInfo.this,"初始化信息成功", Toast.LENGTH_SHORT).show();
                    break;
                case 1://昵称
                    if(String.valueOf("226").equals(data)){
                        custom_fname.setInfo_menu_info(info);
                        Toast.makeText(UserInfo.this,"修改昵称成功", Toast.LENGTH_SHORT).show();
                        break;
                    }else {
                        Toast.makeText(UserInfo.this,data, Toast.LENGTH_LONG).show();
                        break;
                    }
                case 2://个性签名
                    if(String.valueOf("226").equals(data)){
                        custom_about.setInfo_menu_info(info);
                        Toast.makeText(UserInfo.this,"修改个性签名成功", Toast.LENGTH_SHORT).show();
                        break;
                    }else {
                        Toast.makeText(UserInfo.this,data, Toast.LENGTH_LONG).show();
                        break;
                    }
                case 3://真实姓名
                    if(String.valueOf("226").equals(data)){
                        custom_tname.setInfo_menu_info(info);

                        Toast.makeText(UserInfo.this,"修改真实成功", Toast.LENGTH_SHORT).show();
                        break;
                    }else {
                        Toast.makeText(UserInfo.this,data, Toast.LENGTH_LONG).show();
                        break;
                    }
                case 4://性别
                    if(String.valueOf("226").equals(data)){
                        if (String.valueOf("0").equals(sex)) {
                            custom_sex.setInfo_menu_info("男");
                        } else if (String.valueOf("1").equals(sex)) {
                           custom_sex.setInfo_menu_info("女");
                        }
                        break;
                    }else {
                        Toast.makeText(UserInfo.this,data, Toast.LENGTH_LONG).show();
                        break;
                    }
            }
        }
    }
    public long UID ;//用户的id
    private CustomBar custom_photo, custom_fname, custom_about, custom_fav, custom_tname, custom_sex, custom_bri, custom_address;
    private Intent intent;
    private Intent get_uid;
    private  int sex_status = -1;
    private long Uid;
    private Handler handler;
    final String[] items = new String[]{"拍照", "从手机相册选择",
            "从e生活相册选择", "查看头像"};
    public static Bitmap bigImg = null;
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;//本地
    private static final int CODE_CAMERA_REQUEST = 0xa1;//拍照
    public static final int CODE_APP_REQUEST = 0xa2;//APP
    private static final int CODE_RESULT_REQUEST = 0xa3;//最终裁剪后的结果
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        //标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("编辑资料");
        handler = new MyHandler();
        custom_photo = (CustomBar) findViewById(R.id.custom_photo);//图像对象
        custom_photo.setOnClickListener(this);
        custom_fname = (CustomBar) findViewById(R.id.custom_fname);//昵称对象
        custom_fname.setOnClickListener(this);
        custom_about = (CustomBar) findViewById(R.id.custom_about);//个性签名
        custom_about.setOnClickListener(this);
        custom_tname = (CustomBar) findViewById(R.id.custom_tname);//真实姓名
        custom_tname.setOnClickListener(this);
        custom_sex = (CustomBar) findViewById(R.id.custom_sex);//性别
        custom_sex.setOnClickListener(this);
        custom_bri = (CustomBar) findViewById(R.id.custom_bri);//生日
        custom_bri.setOnClickListener(this);
        custom_address = (CustomBar) findViewById(R.id.custom_address);//地址
        custom_address.setOnClickListener(this);
        get_uid = getIntent();
        Uid = get_uid.getLongExtra("uid",0);

        init();//进入个人信息界面的时候初始话信息
    }
    public void onClick(View v) {
       switch (v.getId()) {
            case R.id.custom_photo:
                LayoutInflater layoutInflater = LayoutInflater.from(UserInfo.this);
                //自定义对话框标题栏
                View mTitleView = layoutInflater.inflate(R.layout.custom_user_center_dialog, null);
                //创建对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfo.this);
                builder.setCustomTitle(mTitleView);
                builder.setTitle("选择");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    //注册点击事件
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choseHeadImageFromCameraCapture();//手机拍照选择图片
                                break;
                            case 1:
                                choseHeadImageFromGallery();//相册中选择图片
                                break;
                            case 2:
                                choseHeadImageFromApp();//从app中选择图片
                                break;
                            case 3://查看图片
                                //Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                CustomImageDialogView.Builder dialogBuild = new CustomImageDialogView.Builder(UserInfo.this);
                                dialogBuild.setImage(bigImg);
                                CustomImageDialogView img_dialog = dialogBuild.create();
                                img_dialog.setCanceledOnTouchOutside(true);// 点击外部区域关闭
                                img_dialog.show();
                                break;
                        }
                    }
                });

                AlertDialog dialog = builder.show();
                //设置宽度和高度
                WindowManager.LayoutParams params =
                        dialog.getWindow().getAttributes();
                params.width = 700;
                params.height = 1000;
                dialog.getWindow().setAttributes(params);
                break;
            case R.id.custom_fname:
                final EditText name_text = new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("请输入新的昵称")
                        .setIcon(R.drawable.side_nav_bar)
                        .setView(name_text)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String str = name_text.getText().toString();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("uid", String.valueOf(Uid))//uid
                                        .add("field","name")//字段
                                        .add("values",str)//值
                                        .build();
                                HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH+"update_info.php", requestBody, new Callback() {
                                    public void onFailure(Call call, IOException e) {
                                    }
                                    public void onResponse(Call call, Response response) throws IOException {
                                            String data = response.body().string();
                                            Message msg = Message.obtain();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("data",data);
                                            bundle.putString("info",str);
                                            bundle.putInt("index",1);
                                            msg.setData(bundle);
                                            handler.sendMessage(msg);
                                    }
                                });

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.custom_about:
                final EditText signature_text = new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("请输入新的个性签名")
                        .setIcon(R.drawable.side_nav_bar)
                        .setView(signature_text)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                              final String signature = signature_text.getText().toString();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("uid", String.valueOf(Uid))//uid
                                        .add("field","signature")//字段
                                        .add("values",signature)//值
                                        .build();
                                HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH+"update_info.php", requestBody, new Callback() {
                                    public void onFailure(Call call, IOException e) {
                                    }
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String data = response.body().string();
                                        Message msg = Message.obtain();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("data",data);
                                        bundle.putString("info",signature);
                                        bundle.putInt("index",2);
                                        msg.setData(bundle);
                                        handler.sendMessage(msg);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.custom_tname:
                final EditText rname_text = new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("请输入真实信息，方便同学联系你")
                        .setIcon(R.drawable.side_nav_bar)
                        .setView(rname_text)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String real_name = rname_text.getText().toString();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("uid", String.valueOf(Uid))//uid
                                        .add("field","real_name")//字段
                                        .add("values",real_name)//值
                                        .build();
                                HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH+"update_info.php", requestBody, new Callback() {
                                    public void onFailure(Call call, IOException e) {
                                    }
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String data = response.body().string();
                                        Message msg = Message.obtain();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("data",data);
                                        bundle.putString("info",real_name);
                                        bundle.putInt("index",3);
                                        msg.setData(bundle);
                                        handler.sendMessage(msg);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
           case R.id.custom_sex:

               new AlertDialog.Builder(this)
                        .setTitle("请选择你的性别")
                        .setIcon(R.drawable.side_nav_bar)
                        .setSingleChoiceItems(new String[]{"男", "女"}, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sex_status = i;
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (sex_status == -1) {
                                    Toast.makeText(UserInfo.this, "你还没有选择性别", Toast.LENGTH_SHORT);
                                } else {
                                    RequestBody requestBody = new FormBody.Builder()
                                            .add("uid", String.valueOf(Uid))//uid
                                            .add("field","sex")//字段
                                            .add("values", String.valueOf(sex_status))//值
                                            .build();
                                    HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH+"update_info.php", requestBody, new Callback() {
                                        public void onFailure(Call call, IOException e) {
                                        }
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String data = response.body().string();
                                            Message msg = Message.obtain();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("sex", String.valueOf(sex_status));
                                            bundle.putInt("index",4);
                                            bundle.putString("data",data);
                                            msg.setData(bundle);
                                            handler.sendMessage(msg);
                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sex_status = i;
                            }
                        })
                        .show();
                break;
            case R.id.custom_bri:
                new AlertDialog.Builder(this)
                        .setView(R.layout.datepicker)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", null)
                        .show();
                break;
//            case R.id.custom_address:
//                intent = new Intent(UserInfo.this, MySpinnerActivity.class);
//                startActivity(intent);
//               break;
        }
    }
    private void init(){//进入界面更新数据
        RequestBody requestBody = new FormBody.Builder()
                .add("uid", String.valueOf(Uid))//uid
                .build();
        HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH+"query_data.php", requestBody, new Callback() {
            public void onFailure(Call call, IOException e) {
            }
            public void onResponse(Call call, Response response) throws IOException {
           String info = response.body().string();
                try{
                    JSONArray jsonArray = new JSONArray(info);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String photo = jsonObject.getString("photo");
                    String name = jsonObject.getString("name");
                    String signature = jsonObject.getString("signature");
                    String real_name = jsonObject.getString("real_name");
                    String sex = jsonObject.getString("sex");
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("photo",photo);
                    bundle.putInt("index",0);//0代表初始话数据
                    bundle.putString("name",name);
                    bundle.putString("signature",signature);
                    bundle.putString("real_name",real_name);
                    bundle.putString("sex",sex);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        });
    }
    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");//选择图片
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        //如果你想在Activity中得到新打开Activity关闭后返回的数据，
        //你需要使用系统提供的startActivityForResult(Intent intent,int requestCode)方法打开新的Activity
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }

        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    // 从APP用户相册中选择图片作为头像
    private void choseHeadImageFromApp() {
        Intent intentFromApp = new Intent();
        intentFromApp.setClass(getApplication(), UserAlbumActivity.class);
        //标记是从头像选择页面跳转过去的
        intentFromApp.putExtra("motive", CODE_APP_REQUEST);
        startActivity(intentFromApp);
        //startActivityForResult(intentFromApp, CODE_APP_REQUEST);
    }
    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
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
