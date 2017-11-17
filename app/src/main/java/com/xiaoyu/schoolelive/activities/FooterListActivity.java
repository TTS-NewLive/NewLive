package com.xiaoyu.schoolelive.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.adapter.DealAdapter;
import com.xiaoyu.schoolelive.adapter.FooterAdapter;
import com.xiaoyu.schoolelive.data.Deal;
import com.xiaoyu.schoolelive.data.Footer;
import com.xiaoyu.schoolelive.data.Goods;
import com.xiaoyu.schoolelive.util.Common_msg_cache;
import com.xiaoyu.schoolelive.util.ConstantUtil;
import com.xiaoyu.schoolelive.util.HttpUtil;
import com.xiaoyu.schoolelive.util.Login_cache;
import com.xiaoyu.schoolelive.util.WidgetUtil;

import org.hamcrest.core.IsNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mob.MobSDK.getContext;

/**
 * Created by lenovo on 2017/11/3.
 */

public class FooterListActivity extends AppCompatActivity{
    ListView mFooterList;
    FooterAdapter mAdapter;
    List<Footer> mDates;
    private TextView tv;
    private ImageView ig;
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String footer_data = (String) msg.obj;
            ArrayList<Footer> cache_footer = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(footer_data);
                //从服务器端得到商品数据
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String goods_info=jsonObject.getString("goods_description");//商品信息
                    String goods_name = jsonObject.getString("goods_name");//商品名称
                    String goods_price = jsonObject.getString("goods_price");//商品价格
                    String top_image = jsonObject.getString("top_image");//封面图片
                    String style=jsonObject.getString("goods_style");//商品风格
                    String type=jsonObject.getString("goods_type");//商品类型
                    String goods_id=jsonObject.getString("goods_id");//商品id
                    int goods_style=Integer.valueOf(style);
                    int goods_type=Integer.valueOf(type);

                    Footer footerItem = new Footer();
                    final String str = ConstantUtil.SERVICE_PATH + WidgetUtil.str_trim(top_image);
                    footerItem.setTopImage(str);//设置封面图片
                    footerItem.setGoodsName(goods_name);//设置商品名称
                    footerItem.setGoodsPrice("¥ " + goods_price);//设置商品价格
                    footerItem.setGoodsInfo(""+goods_info);//设置商品信息
                    footerItem.setGoodsId(goods_id);//设置商品id
                    footerItem.setGoodsStyle(goods_style);
                    footerItem.setGoodsType(goods_type);

                    cache_footer.add(footerItem);
                }
                Common_msg_cache.set_footer_Cache(getApplicationContext(), cache_footer);//将商品信息存入缓存
                //Common_msg_cache.set_goods_cache_status(getApplicationContext(), ConstantUtil.Goods_Piece);//第一次将数据添加到缓存中的时候，将加载状态设置为0

                for (int i = 0; i < cache_footer.size(); i++) {
                    mAdapter.getList().add(cache_footer.get(i));//第一次进来加载5条
                }
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footer_list);

        ig = (ImageView)findViewById(R.id.toolbarBack);
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv = (TextView)findViewById(R.id.toolbarTitle);
        tv.setText("足迹");

        mFooterList = (ListView) findViewById(R.id.footer_list);
        mDates = new ArrayList<>();
        //初始化列表数据
        getData();

        mAdapter = new FooterAdapter(this, mDates);
        mFooterList.setAdapter(mAdapter);

        //设置item监听
        setOnItemClickListener();
    }

    public void setOnItemClickListener() {
        mFooterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Footer footer = new Footer();
                ArrayList<Footer> cache_footer = Common_msg_cache.get_footer_Cache(getApplicationContext());
                footer = cache_footer.get(position);
                Toast.makeText(FooterListActivity.this, footer.getGoodsName()+"", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FooterListActivity.this, GoodsInfoActivity.class);
                intent.putExtra("tmp_goodsid", footer.getGoodsId());//传递商品id
                intent.putExtra("tmp_goodsname", footer.getGoodsName());//传递商品名称
                intent.putExtra("tmp_intro", footer.getGoodsInfo());//传递商品介绍
                intent.putExtra("tmp_pageViews", String.valueOf(footer.getPageViews()));
                intent.putExtra("tmp_goodsStyle", footer.getGoodsStyle());
                intent.putExtra("tmp_goodsType", footer.getGoodsType());
                toIntentPrice(intent, footer);
                startActivity(intent);
            }
        });
    }
    private void toIntentPrice(Intent intent, Footer footer) {
        if (footer.getGoodsType() == ConstantUtil.Goods_Type_ykj) {
            intent.putExtra("tmp_ykjPrice", String.valueOf(footer.getGoodsPrice()));
        } else if (footer.getGoodsType() == ConstantUtil.Goods_Type_yj) {
            // intent.putExtra("tmp_yjPrice", String.valueOf(goods.getRefPrice()));
            intent.putExtra("tmp_yjPrice", String.valueOf(footer.getGoodsPrice()));
        } else if (footer.getGoodsType() == ConstantUtil.Goods_Type_pai) {
            // intent.putExtra("tmp_basePrice", String.valueOf(goods.getBasePrice()));
            // intent.putExtra("tmp_nowPrice", String.valueOf(goods.getNowPrice()));
            // intent.putExtra("tmp_minPrice", String.valueOf(goods.getMinPrice()));
            intent.putExtra("tmp_basePrice", String.valueOf(footer.getGoodsPrice()));
            intent.putExtra("tmp_nowPrice", String.valueOf(footer.getGoodsPrice()));
            intent.putExtra("tmp_minPrice", String.valueOf(footer.getGoodsPrice()));
        }
    }

    public void getData() {
        if (Login_cache.get_login_status(getApplicationContext()).equals("true")) {
            String uid = Login_cache.get_login_username(getApplicationContext());
            RequestBody requestBody = new FormBody.Builder()
                    .add("uid", uid)
                    .build();
            HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH + "query_footer_item.php", requestBody, new Callback() {
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(FooterListActivity.this, "网络好像出问题了...", Toast.LENGTH_SHORT).show();
                }
                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    Message msg = new Message();
                    String result = str;
                    Log.d("FooterActivity:", result);
                    msg.obj = str;
                    handler.sendMessage(msg);
                }
            });
        } else {
            Toast.makeText(this, "请登录账号", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


//    public void initData() {
//        if (Common_msg_cache.get_footer_Cache(getContext()) != null) {//判断缓存中是否存在旧货信息
//            ArrayList<Footer> cache_footer = Common_msg_cache.get_footer_Cache(getContext());
//            int index = Common_msg_cache.get_goods_cache_status(getContext());//得到上次读取到第几条数据
//            mAdapter.getList().clear();//加载之前将原来的list删除
//            for (int i = 0; i < index; i++) {
//                mAdapter.addFooterItem(cache_footer.get(i));
//            }
//            mAdapter.notifyDataSetChanged();
//        } else {
//            getData();//从服务器获取数据，并存入缓存中
//        }
//        getData();
//    }
}
