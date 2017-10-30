package com.xiaoyu.schoolelive.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.base.BaseSlideBack;
import com.xiaoyu.schoolelive.custom.CustomBar;
import com.xiaoyu.schoolelive.custom.CustomImageDialogView;
import com.xiaoyu.schoolelive.util.ACache;
import com.xiaoyu.schoolelive.util.BitmapUtils;
import com.xiaoyu.schoolelive.util.ConstantUtil;
import com.xiaoyu.schoolelive.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

                    Glide.with(UserInfo.this)//将选中的图片放到imageview中
                            .load(photo)
                            .into(mPhoto);

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
    public long uid ;//用户的id
    private CustomBar custom_fname, custom_about, custom_fav, custom_tname, custom_sex, custom_bri, custom_address;
    private CircleImageView mPhoto;
    private LinearLayout mPhotoLinearLayout;
    private Intent get_uid;
    private static int output_X = 600;
    private static int output_Y = 600;
    private  int sex_status = -1;
    private Handler handler;
    private ACache mCache;
    int[] screenSize = null;
    int mWidth ;
    int mHeigh ;
    final String[] items = new String[]{"拍照", "从手机相册选择", "查看头像"};
    public static Bitmap bigImg = null;
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;//本地
    private static final int CODE_CAMERA_REQUEST = 0xa1;//拍照
//    public static final int CODE_APP_REQUEST = 0xa2;//APP
    private static final int CODE_RESULT_REQUEST = 0xa2;//最终裁剪后的结果

    private TextView tv;
    private ImageView ig;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        tv = (TextView)findViewById(R.id.toolbarTitle);
        tv.setText("编辑资料");
        ig = (ImageView)findViewById(R.id.toolbarBack);
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        handler = new MyHandler();

        mPhoto = (CircleImageView) findViewById(R.id.mPhoto);//图像对象
        //mPhoto.setOnClickListener(this);
        mPhotoLinearLayout = (LinearLayout)findViewById(R.id.mPhotoLinearLayout);
        mPhotoLinearLayout.setOnClickListener(this);

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
        uid = get_uid.getLongExtra("uid",0);

        mCache = ACache.get(this);//初始花缓存类ACache

        //获取当前屏幕宽高
        screenSize = BitmapUtils.getScreenSize(UserInfo.this);
        mWidth = screenSize[0];
        mHeigh = screenSize[1];

        String str = mCache.getAsString("photo_path");
        if (str != null) {//初始化图像，从缓存中获取
            Glide.with(UserInfo.this)//将选中的图片放到imageview中
                    .load(str)
                    .error(R.drawable.qq_login)
                    .into(mPhoto);
        } else {
            Glide.with(UserInfo.this)//将选中的图片放到imageview中
                    .load(R.drawable.menu_info_dw)
                    .into(mPhoto);
        }

        init();//进入个人信息界面的时候初始话信息
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mPhotoLinearLayout:
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
//                            case 2:
//                                choseHeadImageFromApp();//从app中选择图片
//                                break;
                            case 2://查看图片
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
                                        .add("uid", String.valueOf(uid))//uid
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
                                        .add("uid", String.valueOf(uid))//uid
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
                                        .add("uid", String.valueOf(uid))//uid
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
                                            .add("uid", String.valueOf(uid))//uid
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
                .add("uid", String.valueOf(uid))//uid
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
//    private void choseHeadImageFromApp() {
//        Intent intentFromApp = new Intent();
//        intentFromApp.setClass(getApplication(), UserAlbumActivity.class);
//        //标记是从头像选择页面跳转过去的
//        intentFromApp.putExtra("motive", CODE_APP_REQUEST);
//        startActivity(intentFromApp);
//        //startActivityForResult(intentFromApp, CODE_APP_REQUEST);
//    }
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
                Glide.with(UserInfo.this)//将选中的图片放到imageview 中
                        .load(intent.getData())
                        .error(R.drawable.qq_login)
                        .into(mPhoto);

                bigImg = BitmapUtils.decodeUri(UserInfo.this, intent.getData(), mWidth, mHeigh);

                //新建文件夹 先选好路径 再调用mkdir函数 现在是根目录下面的Ask文件夹
//                try {
//                    File image = saveFile(BitmapFactory.decodeFile(intent.getData().toString()), "photo");
//                    uploadMultiFile(uid, image);//上传文件到服务器
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                //cropRawPhoto(intent.getData());//直接裁剪图片
                break;
            case CODE_CAMERA_REQUEST://如果是来自摄像头
                if (hasSdcard()) {
                    File tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                    Toast.makeText(UserInfo.this, tempFile.toString(), Toast.LENGTH_LONG).show();
                    //Bitmap bm= BitmapFactory.decodeFile(tempFile.toString());
                    //mPhoto.setImageBitmap(bm);

                    //将选中的图片放到头像中
                    Glide.with(UserInfo.this)
                            .load(Uri.fromFile(tempFile))
                            .error(R.drawable.qq_login)
                            .into(mPhoto);
                    //将选中的图像保存
                    bigImg = BitmapUtils.decodeUri(UserInfo.this, Uri.fromFile(tempFile), mWidth, mHeigh);
                    //cropRawPhoto();
                    //新建文件夹 先选好路径 再调用mkdir函数 现在是根目录下面的Ask文件夹
//                    try {
//                        File image = saveFile(BitmapFactory.decodeFile(tempFile.toString()), "photo");
//                        uploadMultiFile(uid, image);//上传文件到服务器
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }
                break;
//            case CODE_APP_REQUEST:
//                Toast.makeText(getApplication(), "这是APP", Toast.LENGTH_SHORT).show();
//                Uri imgUri = Uri.parse(intent.getStringExtra("image_uri"));
//                cropRawPhoto(imgUri);
//                break;
//            case CODE_RESULT_REQUEST:
//                if (intent != null) {
//                    setImageToHeadView(intent);//设置图片框
//                }
//                break;
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
        bigImg = BitmapUtils.decodeUri(UserInfo.this, uri, mWidth, mHeigh);

        startActivityForResult(intent, CODE_RESULT_REQUEST);

    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            mPhoto.setImageBitmap(photo);
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
