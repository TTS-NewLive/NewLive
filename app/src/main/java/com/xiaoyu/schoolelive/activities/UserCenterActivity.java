package com.xiaoyu.schoolelive.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.adapter.UserCenterAdapter;
import com.xiaoyu.schoolelive.base.BaseSlideBack;
import com.xiaoyu.schoolelive.custom.CustomImageDialogView;
import com.xiaoyu.schoolelive.data.UserCenter;
import com.xiaoyu.schoolelive.util.ACache;
import com.xiaoyu.schoolelive.util.BitmapUtils;
import com.xiaoyu.schoolelive.util.ConstantUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserCenterActivity extends BaseSlideBack {
    public static Bitmap bigImg = null;
    int[] screenSize = null;
    int mWidth;
    int mHeigh;
    private ListView usercenter_list;
    private UserCenterAdapter userCenterAdapter;
    private List<UserCenter> data;
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;//本地
    private static final int CODE_CAMERA_REQUEST = 0xa1;//拍照
    public static final int CODE_APP_REQUEST = 0xa2;//APP
    private static final int CODE_RESULT_REQUEST = 0xa3;//最终裁剪后的结果

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 600;
    private static int output_Y = 600;
    private long uid;
    private ImageView imageView;
    private ACache mCache;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String photo = (String) msg.obj;
            //Toast.makeText(UserCenterActivity.this,photo,Toast.LENGTH_LONG).show();
            Log.i("aa", photo);
            Glide.with(UserCenterActivity.this)//将选中的图片放到imageview中
                    .load(ConstantUtil.SERVICE_PATH + str_trim(photo))
                    .into(imageView);
            mCache.put("photo_path", ConstantUtil.SERVICE_PATH + str_trim(photo), 15 * ACache.TIME_DAY);//将路径放到缓存中,保存15天
            BitmapUtils.deleteCacheFile();//清除缓存
        }
    };
    final String[] items = new String[]{"拍照", "从手机相册选择",
            "从e生活相册选择", "查看头像"};


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        imageView = (ImageView) findViewById(R.id.user_center_imageview);
        //初始化查看头像的图片
        //initBigPhoto();

        Intent intent = getIntent();
        if (intent.getIntExtra("acFrom", 0) == ConstantUtil.MAIN_ACTIVITY) {
            uid = intent.getLongExtra("uid", 0);
        } else if (intent.getExtras().getInt("acFrom") == ConstantUtil.USERALBUM_ACTIVITY) {
            Glide.with(this)
                    //.load(getIntent().getStringExtra("photoUrl"))
                    .load(getIntent().getExtras().getByteArray("photoByte"))
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                            //设置裁剪后的头像
                            imageView.setImageBitmap(bitmap);
                        }
                    });
        }
        mCache = ACache.get(this);//初始花缓存类ACache

        //获取当前屏幕宽高
        screenSize = BitmapUtils.getScreenSize(UserCenterActivity.this);
        mWidth = screenSize[0];
        mHeigh = screenSize[1];

        //设置折叠式标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.user_center_toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.user_center_layout);


        //initView();
        String str = mCache.getAsString("photo_path");
        if (str != null) {//初始化图像，从缓存中获取
            Glide.with(UserCenterActivity.this)//将选中的图片放到imageview中
                    .load(str)
                    .error(R.drawable.qq_login)
                    .into(imageView);
        } else {
            Glide.with(UserCenterActivity.this)//将选中的图片放到imageview中
                    .load(R.drawable.qq_login)
                    .into(imageView);
        }

        collapsingToolbar.setTitle("紫炎");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbar.setExpandedTitleGravity(Gravity.BOTTOM | Gravity.CENTER);
        collapsingToolbar.setExpandedTitleColor(Color.BLACK);
        collapsingToolbar.setCollapsedTitleGravity(Gravity.LEFT);
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(UserCenterActivity.this);
                //自定义对话框标题栏
                View mTitleView = layoutInflater.inflate(R.layout.custom_user_center_dialog, null);
                //创建对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(UserCenterActivity.this);
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
                                CustomImageDialogView.Builder dialogBuild = new CustomImageDialogView.Builder(UserCenterActivity.this);
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
                params.width = 600;
                params.height = 750;
                dialog.getWindow().setAttributes(params);
            }
        });

        setUserenterFloatingButton();
    }

    private void initBigPhoto() {
        Matrix max = new Matrix();
        //设置是否开启缓存
        imageView.setDrawingCacheEnabled(true);
        //直接获得imageview中的缓存
        bigImg = imageView.getDrawingCache();
        //需要在获得缓存以后setDrawingCacheEnabled设置为false，因为这样才能让之前的缓存去掉，不会影响后来新的缓存。
        imageView.setDrawingCacheEnabled(false);
    }

    //悬浮按钮
    private void setUserenterFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserCenterActivity.this, UserInfo.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
    }

    private void initView() {

        // 初始化评论列表
        usercenter_list = (ListView) findViewById(R.id.usercenter_list);
        // 初始化数据
        data = new ArrayList<>();
        // 初始化适配器
        userCenterAdapter = new UserCenterAdapter(this, data);
        // 为评论列表设置适配器
        usercenter_list.setAdapter(userCenterAdapter);

        addMsg();
    }

    public void addMsg() {
        UserCenter userCenter = new UserCenter();
        userCenter.setMsg_content("这是校园e生活第一条动态");
        userCenter.setMsg_type("#个人中心#");
        //获取年月日
        SimpleDateFormat year = new SimpleDateFormat("yyyy" + "年");
        Date curYear = new Date(System.currentTimeMillis());
        String str = year.format(curYear);
        userCenter.setMsg_year(str);

        SimpleDateFormat month = new SimpleDateFormat("MM" + "月");
        Date curMonth = new Date(System.currentTimeMillis());
        String str2 = month.format(curMonth);
        userCenter.setMsg_month(str2);

        SimpleDateFormat day = new SimpleDateFormat("dd");
        Date curDay = new Date(System.currentTimeMillis());
        String str3 = day.format(curDay);
        userCenter.setMsg_month(str3);

        userCenterAdapter.addCenterMsg(userCenter);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {//取消
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode) {
            case CODE_GALLERY_REQUEST://如果是来自本地的
                cropRawPhoto(intent.getData());//直接裁剪图片
                break;
            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                    Toast.makeText(UserCenterActivity.this, tempFile.toString(), Toast.LENGTH_LONG).show();

                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            case CODE_APP_REQUEST:
                Toast.makeText(getApplication(), "这是APP", Toast.LENGTH_SHORT).show();
                Uri imgUri = Uri.parse(intent.getStringExtra("image_uri"));
                cropRawPhoto(imgUri);
                break;
            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);//设置图片框
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //把裁剪的数据填入里面
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);
        bigImg = BitmapUtils.decodeUri(UserCenterActivity.this, uri, mWidth, mHeigh);
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            imageView.setImageBitmap(photo);
            //新建文件夹 先选好路径 再调用mkdir函数 现在是根目录下面的Ask文件夹
            try {
                File image = saveFile(photo, "photo");
                uploadMultiFile(uid, image);//上传文件到服务器
            } catch (IOException e) {
                e.printStackTrace();
            }
            //File image = saveFile(photo, "photo");
            //Toast.makeText(UserCenterActivity.this,image.getPath(),Toast.LENGTH_LONG).show();
            //  uploadMultiFile(2015404, file);//上传文件到服务器
            //Toast.makeText(UserCenterActivity.this,file.getPath(),Toast.LENGTH_LONG).show();
            /* File nf = new File(Environment.getExternalStorageDirectory() + "/Ask");
            nf.mkdir();
            //在根目录下面的ASk文件夹下 创建okkk.jpg文件
            File f = new File(Environment.getExternalStorageDirectory() + "/Ask", "okkk.png");
            FileOutputStream out = null;
            try {//打开输出流 将图片数据填入文件中
                out = new FileOutputStream(f);
                photo.compress(Bitmap.CompressFormat.PNG, 90, out);
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
        }
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

    public File saveFile(Bitmap bm, String fileName) throws IOException {//将Bitmap类型的图片转化成file类型，便于上传到服务器
        String path = Environment.getExternalStorageDirectory() + "/photo";//用户图像
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName + ".PNG");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }

    public void uploadMultiFile(long uid, File file) {//将图片发送到服务器,一张图片(处理头像)
        final String url = ConstantUtil.SERVICE_PATH + "photo_design.php";
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        String str = BitmapUtils.compressImageUpload(file.getPath());//得到压缩过的文件路径
        File compress_file = new File(str);//得到新文件
        mbody.addFormDataPart("image", compress_file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), compress_file));//添加到mbody中
        mbody.addFormDataPart("uid", uid + "");
        RequestBody requestBody = mbody.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        final OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                //设置超时
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                Log.e("error", "uploadMultiFile() e=" + e);
            }

            public void onResponse(Call call, Response response) throws IOException {
                Message msg = new Message();
                msg.obj = response.body().string();
                handler.sendMessage(msg);
            }
        });
    }

    public String str_trim(String str) {//去除字符串中的所有空格(用来去掉服务器返回路径中的空格)
        return str.replaceAll(" ", "");
    }
}

