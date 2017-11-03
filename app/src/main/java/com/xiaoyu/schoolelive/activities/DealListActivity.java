package com.xiaoyu.schoolelive.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.adapter.DealAdapter;
import com.xiaoyu.schoolelive.data.Deal;
import com.xiaoyu.schoolelive.util.Common_msg_cache;
import com.xiaoyu.schoolelive.util.ConstantUtil;
import com.xiaoyu.schoolelive.util.HttpUtil;
import com.xiaoyu.schoolelive.util.Login_cache;
import com.xiaoyu.schoolelive.util.WidgetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mob.MobSDK.getContext;


public class DealListActivity extends AppCompatActivity {
    ListView mDealList;
    DealAdapter mAdapter;
    List<Deal> mDates;
    private TextView tv;
    private ImageView ig;
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String deal_data = (String) msg.obj;
            ArrayList<Deal> cache_deal = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(deal_data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String goods_id = jsonObject.getString("goods_id");//商品id
                    String seller_id = jsonObject.getString("uid");//卖家id
                    String deal_time = jsonObject.getString("deal_time");//交易时间
                    String goods_name = jsonObject.getString("goods_name");//商品名称
                    String goods_price = jsonObject.getString("goods_price");//商品价格
                    String top_image = jsonObject.getString("top_image");//封面图片
                    //Goods goods = new Goods();
                    Deal dealItem = new Deal();
                    final String str = ConstantUtil.SERVICE_PATH + WidgetUtil.str_trim(top_image);
                    dealItem.setTopImage(str);//设置封面图片
                    dealItem.setGoodsName(goods_name);//设置商品名称
                    dealItem.setGoodsPrice("¥ " + goods_price);//设置商品价格
                    dealItem.setSellDate(deal_time);//设置交易时间
                    dealItem.setGoodsId(goods_id);//设置商品ID
                    dealItem.setSellerName(seller_id);
                    cache_deal.add(dealItem);
                }
                Common_msg_cache.set_deal_Cache(getContext(), cache_deal);//将商品信息存入缓存
                Common_msg_cache.set_goods_cache_status(getContext(), ConstantUtil.Goods_Piece);//第一次将数据添加到缓存中的时候，将加载状态设置为0
                for (int i = 0; i < cache_deal.size(); i++) {
                    mAdapter.getList().add(cache_deal.get(i));//第一次进来加载5条
                }
                //mAdapter.getList().addAll(goodsList);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "存入缓存成功", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_list);

        ig = (ImageView)findViewById(R.id.toolbarBack);
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv = (TextView)findViewById(R.id.toolbarTitle);
        tv.setText("订单历史");

        mDealList = (ListView) findViewById(R.id.deal_list);
        mDates = new ArrayList<>();
        mAdapter = new DealAdapter(this, mDates);
        mDealList.setAdapter(mAdapter);

        //初始化列表数据
        initData();
        //设置item监听
        setOnItemClickListener();
    }

    public void setOnItemClickListener() {
        mDealList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void getData() {
        if (Login_cache.get_login_status(getContext()).equals("true")) {
            String uid = Login_cache.get_login_username(getContext());
            RequestBody requestBody = new FormBody.Builder()
                    .add("uid", uid)
                    .build();
            HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH + "query_deal_item.php", requestBody, new Callback() {
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(DealListActivity.this, "网络好像出问题了...", Toast.LENGTH_SHORT).show();
                }

                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    Message msg = new Message();
                    String result = str;
                    Log.d("DealListAty", result);
                    msg.obj = str;
                    handler.sendMessage(msg);
                }
            });
        } else {
            Toast.makeText(this, "请登录账号", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void initData() {
        if (Common_msg_cache.get_deal_Cache(getContext()) != null) {//判断缓存中是否存在旧货信息
            ArrayList<Deal> cache_deal = Common_msg_cache.get_deal_Cache(getContext());
            int index = Common_msg_cache.get_goods_cache_status(getContext());//得到上次读取到第几条数据
            mAdapter.getList().clear();//加载之前将原来的list删除
            for (int i = 0; i < index; i++) {
                mAdapter.addDealItem(cache_deal.get(i));
            }
            mAdapter.notifyDataSetChanged();
        } else {
            getData();//从服务器获取数据，并存入缓存中
        }
    }
}
