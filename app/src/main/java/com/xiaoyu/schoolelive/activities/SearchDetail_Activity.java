package com.xiaoyu.schoolelive.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.adapter.DealAdapter;
import com.xiaoyu.schoolelive.data.Deal;
import com.xiaoyu.schoolelive.util.ConstantUtil;
import com.xiaoyu.schoolelive.util.HttpUtil;
import com.xiaoyu.schoolelive.util.WidgetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SearchDetail_Activity extends AppCompatActivity {

    private ListView fList;
    private List<Deal> mDatas;
    private DealAdapter mAdapter;
    private Intent get_result;
    private Deal deal;
    String goods_description;
    int goods_type;
    String goods_price;
    String goods_name;
    String goods_id;
    int goods_isSell;
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // Toast.makeText(SearchDetail_Activity.this, "qqq", Toast.LENGTH_SHORT).show();
            String result = (String) msg.obj;
            Intent intent = new Intent(getApplicationContext(), GoodsInfoActivity.class);
            try {
                JSONArray array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    goods_description = data.getString("goods_description");
                    goods_price = data.getString("goods_price");
                    goods_type = data.getInt("goods_type");
                    goods_id = data.getString("goods_id");
                    goods_name = data.getString("goods_name");
                    goods_isSell = data.getInt("isSell");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            intent.putExtra("tmp_goodsid", goods_id);//传递商品id
            intent.putExtra("tmp_goodsname", goods_name);//传递商品名称
            intent.putExtra("tmp_intro", goods_description);//传递商品介绍
            intent.putExtra("tmp_goodsType", goods_type);
            intent.putExtra("tmp_ykjPrice", goods_price);
            intent.putExtra("tmp_isSell", goods_isSell);
            //toIntentPrice(intent, goods);
            startActivity(intent);
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_detail);
        fList = (ListView)findViewById(R.id.find_detail_list);
        mDatas = new ArrayList<>();
        get_result = getIntent();
        String json_result = get_result.getStringExtra("search_result");
        mDatas.clear();//将先前的界面删掉
        try {
            JSONArray data = new JSONArray(json_result);
            for (int i = 0;i < data.length();i++){
                JSONObject jsonobject = data.getJSONObject(i);
                String name = jsonobject.getString("goods_name");
                String price = jsonobject.getString("goods_price");
                String sell = jsonobject.getString("uid");
                String top_image = jsonobject.getString("top_image");
                String goods_id = jsonobject.getString("goods_id");//商品id
                //真实路径
                String  real_path = WidgetUtil.str_trim(ConstantUtil.SERVICE_PATH+top_image);
                Deal goods = new Deal();
                goods.setGoodsName(name);
                goods.setGoodsPrice("¥ " +price);
                goods.setSellerName(sell);
                goods.setTopImage(real_path);
                goods.setGoodsId(goods_id);
                mDatas.add(goods);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter = new DealAdapter(this, mDatas);
        fList.setAdapter(mAdapter);
        setClick();
    }
    public void setClick() {
        fList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                final String[] str = {""};
                deal = mDatas.get(position);
                RequestBody requestBody = new FormBody.Builder()
                        .add("goods_id", deal.getGoodsId())
                        .build();
                HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH + "query_one_goods.php", requestBody, new okhttp3.Callback() {
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(SearchDetail_Activity.this, "网络好像出问题了...", Toast.LENGTH_SHORT).show();
                    }

                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseData = response.body().string();
                        String result = responseData;
                        Message msg = Message.obtain();
                        msg.obj = result;
                        handler.sendMessage(msg);

                    }
                });

            }
        });
    }
}
