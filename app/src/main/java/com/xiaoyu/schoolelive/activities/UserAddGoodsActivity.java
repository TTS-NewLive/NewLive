package com.xiaoyu.schoolelive.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.adapter.PhotoAdapter;
import com.xiaoyu.schoolelive.base.BaseSlideBack;
import com.xiaoyu.schoolelive.data.Goods;
import com.xiaoyu.schoolelive.data.ImageBean;
import com.xiaoyu.schoolelive.util.BitmapUtils;
import com.xiaoyu.schoolelive.util.Common_msg_cache;
import com.xiaoyu.schoolelive.util.ConstantUtil;
import com.xiaoyu.schoolelive.util.Login_cache;
import com.xiaoyu.schoolelive.util.RecyclerItemClickListener;
import com.xiaoyu.schoolelive.util.WidgetUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by NeekChaw on 2017-07-30.
 */
public class UserAddGoodsActivity extends BaseSlideBack implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup radioGroup;
    private RadioButton fixed_price, auction, negotiable;
    private LinearLayout price_ll, auction_ll, kyj_ll;

    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    //private ArrayList<String> onLongClickListData = new ArrayList<>();
    private ImageView iv_crop;
    private RecyclerView recyclerView;
    private Button publish_goods;
    private EditText goods_name;//货物名称
    private EditText goods_description;//货物介绍
    private EditText goods_price, goods_kyj_price, goods_jp_baseprice, goods_jp_minadd;
    private int goods_type;
    private List<String> photos = null;
    private ProgressDialog progressDialog;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    price_ll.setVisibility(View.VISIBLE);
                    auction_ll.setVisibility(View.GONE);
                    kyj_ll.setVisibility(View.GONE);
                    break;
                case 2:
                    price_ll.setVisibility(View.GONE);
                    auction_ll.setVisibility(View.VISIBLE);
                    kyj_ll.setVisibility(View.GONE);
                    break;
                case 3:
                    kyj_ll.setVisibility(View.VISIBLE);
                    auction_ll.setVisibility(View.GONE);
                    price_ll.setVisibility(View.GONE);
                    break;
                case 4:
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("toAddGoods", 2);
                    startActivity(intent);
                    break;

            }
            super.handleMessage(msg);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_publish);
        //初始化控件
        initView();
        //方式选择
        initChoose();
    }

    //初始化控件
    public void initView() {
        goods_name = (EditText) findViewById(R.id.goods_name);
        goods_description = (EditText) findViewById(R.id.goods_description);
        goods_price = (EditText) findViewById(R.id.goods_price);

        goods_jp_baseprice = (EditText) findViewById(R.id.goods_jp_baseprice);
        goods_jp_minadd = (EditText) findViewById(R.id.goods_jp_minadd);

        goods_kyj_price = (EditText) findViewById(R.id.goods_kyj_price);
        publish_goods = (Button) findViewById(R.id.price_button);

        iv_crop = (ImageView) findViewById(R.id.iv_crop);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        photoAdapter = new PhotoAdapter(this, selectedPhotos);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
                            PhotoPicker.builder()
                                    .setPhotoCount(PhotoAdapter.MAX)
                                    .setShowCamera(true)
                                    .setPreviewEnabled(true)
                                    .setSelected(selectedPhotos)
                                    .start(UserAddGoodsActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(UserAddGoodsActivity.this);
                        }
                    }
                }));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择返回
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            iv_crop.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);


            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }
        //拍照功能或者裁剪功能返回
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.CROP_CODE) {
            iv_crop.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(Uri.fromFile(new File(data.getStringExtra(PhotoPicker.KEY_CAMEAR_PATH)))).into(iv_crop);
        }
    }

    //方式选择
    public void initChoose() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        fixed_price = (RadioButton) findViewById(R.id.fixed_price);//一口价
        negotiable = (RadioButton) findViewById(R.id.negotiable);//可议价
        kyj_ll = (LinearLayout) findViewById(R.id.kyj_ll);
        price_ll = (LinearLayout) findViewById(R.id.price_ll);

        auction = (RadioButton) findViewById(R.id.auction);//竞拍
        auction_ll = (LinearLayout) findViewById(R.id.auction_ll);

        radioGroup.setOnCheckedChangeListener(this);

        publish_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Login_cache.get_login_status(getApplicationContext()).equals("false")){
                    Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (goods_name.getText().toString().isEmpty() || goods_description.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "商品名称或者介绍不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String select = WidgetUtil.get_radio_select(radioGroup);//得到商品出售方式
                if (select.equals("一口价")) {
                    if (goods_price.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "商品价格不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (select.equals("可议价")) {
                    if (goods_kyj_price.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "商品价格不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (select.equals("竞拍")) {
                    if (goods_jp_minadd.getText().toString().isEmpty() || goods_jp_minadd.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "商品价格不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (photos == null) {
                    Toast.makeText(getApplicationContext(), "请选择商品的图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = new ProgressDialog(UserAddGoodsActivity.this);
                progressDialog.setMessage("上传中...");
                progressDialog.setCancelable(true);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                upLoad_goods_info();
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        Message message = new Message();
        if (checkedId == fixed_price.getId()) {
            message.what = 1;
            handler.sendMessage(message);
        } else if (checkedId == auction.getId()) {
            message.what = 2;
            handler.sendMessage(message);
        } else if (checkedId == negotiable.getId()) {
            message.what = 3;
            handler.sendMessage(message);
        }
    }

    private void upLoad_goods_info() {
        //上传商品信息到服务器(图文混合)
        final String url = ConstantUtil.SERVICE_PATH + "goods_upload.php";
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i < photos.size(); i++) {
            File file = new File(photos.get(i));//得到选择的图片
            String str = BitmapUtils.compressImageUpload(file.getPath());//得到压缩过的文件路径
            File compress_file = new File(str);//得到新文件
            mbody.addFormDataPart("image" + i, i + compress_file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), compress_file));//添加到mbody中
        }
        //Toast.makeText(getApplicationContext(), WidgetUtil.get_radio_select(radioGroup),Toast.LENGTH_SHORT).show();
        String select = WidgetUtil.get_radio_select(radioGroup);//得到商品出售方式
        if (select.equals("一口价")) {
            mbody.addFormDataPart("goods_type", ConstantUtil.Goods_Type_ykj + "");
            goods_type = ConstantUtil.Goods_Type_ykj;
        } else if (select.equals("可议价")) {
            mbody.addFormDataPart("goods_type", ConstantUtil.Goods_Type_yj + "");
            goods_type = ConstantUtil.Goods_Type_yj;
        } else if (select.equals("竞拍")) {
            mbody.addFormDataPart("goods_type", ConstantUtil.Goods_Type_pai + "");
            goods_type = ConstantUtil.Goods_Type_pai;
        }
        mbody.addFormDataPart("goods_style", ConstantUtil.Goods_New + "");//发布的时候商品属性为新

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        final String date = sDateFormat.format(new java.util.Date());//得到系统当前时间作为商品的id
        mbody.addFormDataPart("goods_id", date);
        mbody.addFormDataPart("goods_name", goods_name.getText().toString());//发送商品名称
        mbody.addFormDataPart("uid",Login_cache.get_login_username(getApplicationContext()));//发送当前登录用户的id
        if (goods_type == ConstantUtil.Goods_Type_ykj) {
            mbody.addFormDataPart("goods_price", goods_price.getText().toString());//发送商品价格
        } else if (goods_type == ConstantUtil.Goods_Type_yj) {
            mbody.addFormDataPart("goods_price", goods_price.getText().toString());//发送商品价格
        } else if (goods_type == ConstantUtil.Goods_Type_pai) {
            mbody.addFormDataPart("goods_price", goods_price.getText().toString());//发送商品价格
        }

        mbody.addFormDataPart("goods_description", goods_description.getText().toString());//发送商品描述
        RequestBody requestBody = mbody.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                //设置超时
                .connectTimeout(3000, TimeUnit.MINUTES)
                .readTimeout(3000, TimeUnit.MINUTES)
                .writeTimeout(3000, TimeUnit.MINUTES)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("iii", "uploadMultiFile() e=" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Goods goods = new Goods();
                String path = response.body().string();//得到返回来的封面路径
                String str = ConstantUtil.SERVICE_PATH + WidgetUtil.str_trim(path);
                ImageBean bean = new ImageBean();
                bean.setImgsrc(str);
                goods.setGoods_id(date);
                goods.setTopImage(bean);//设置封面图片
                goods.setGoodsName(goods_name.getText().toString());//设置名称
                goods.setGoodsIntro(goods_description.getText().toString());//设置介绍
                //goods.setPageViews(Integer.valueOf(goods_price.getText().toString()));  //设置商品浏览量
                goods.setGoodsStyle(ConstantUtil.Goods_New); //设置顶热新商品属性
                goods.setGoodsType(goods_type);
                setGoodsPrice(goods, goods_type);
                Common_msg_cache.add_goods_Cache(getApplicationContext(), goods);
                //请求到数据之后在handler里面更新ui
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);

            }
        });

    }

    //设置商品价格
    public void setGoodsPrice(Goods goods, int goods_type) {
        if (goods_type == ConstantUtil.Goods_Type_ykj) {
            goods.setPrice(Integer.valueOf(goods_price.getText().toString()));
        } else if (goods_type == ConstantUtil.Goods_Type_pai) {
            goods.setBasePrice(Integer.valueOf(goods_jp_baseprice.getText().toString()));
            goods.setNowPrice(Integer.valueOf(goods_jp_baseprice.getText().toString()));
            goods.setMinPrice(Integer.valueOf(goods_jp_minadd.getText().toString()));
        } else if (goods_type == ConstantUtil.Goods_Type_yj) {
            goods.setRefPrice(Integer.valueOf(goods_kyj_price.getText().toString()));
        }
    }
}