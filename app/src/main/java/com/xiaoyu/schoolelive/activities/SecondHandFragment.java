package com.xiaoyu.schoolelive.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.adapter.WaterFallAdapter;
import com.xiaoyu.schoolelive.data.Goods;
import com.xiaoyu.schoolelive.data.ImageBean;
import com.xiaoyu.schoolelive.util.Common_msg_cache;
import com.xiaoyu.schoolelive.util.ConstantUtil;
import com.xiaoyu.schoolelive.util.HttpUtil;
import com.xiaoyu.schoolelive.util.WidgetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/15.
 */
public class SecondHandFragment extends Fragment {
    @Bind(R.id.goods_recycler_view)
    RecyclerView mRecyclerView;
    public ArrayList<Goods> goodsList = new ArrayList();
    private WaterFallAdapter mAdapter;
    private RefreshLayout refreshLayout;
    public static Context mcontext;
    public static int ONSELL = 0;//未销售
    public static int SELLED = 1;//已销售
    LocalBroadcastManager broadcastManager;
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String goods_data = (String) msg.obj;
            ArrayList<Goods> cache_goods = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(goods_data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String uid = jsonObject.getString("uid");
                    String goods_id = jsonObject.getString("goods_id");
                    String top_image = jsonObject.getString("top_image");
                    String goods_name = jsonObject.getString("goods_name");
                    int goods_price = jsonObject.getInt("goods_price");
                    String goods_description = jsonObject.getString("goods_description");
                    int goods_type = jsonObject.getInt("goods_type");
                    String post_time = jsonObject.getString("post_time").substring(2,16);
                    int isSell = jsonObject.getInt("isSell");
                    int minPrice = jsonObject.getInt("bids");//每次加价不小于
                    Goods goods = new Goods();
                    final String str = ConstantUtil.SERVICE_PATH + WidgetUtil.str_trim(top_image);
                    ImageBean bean = new ImageBean();
                    bean.setImgsrc(str);
                    goods.setGoods_id(goods_id);
                    goods.setUid(uid);
                    goods.setTopImage(bean);//设置封面图片
                    goods.setPost_time(post_time);
                    goods.setPageViews(goods_price);  //设置商品浏览量
                    goods.setGoodsName(goods_name);//设置商品名称
                    goods.setGoodsIntro(goods_description);//设置商品介绍
                    goods.setGoodsStyle(ConstantUtil.Goods_New); //设置顶热新商品属性
                    goods.setGoodsType(goods_type); //设置商品出售方式
                    goods.setIsSell(isSell);
                    goods.setMinPrice(minPrice);
                    goods.setPost_time(post_time);
                    setPrice(goods, goods.getGoodsType(), goods_price,minPrice);//设置商品价格
                    // goodsList.add(goods);
                    if (goods.getIsSell() == ONSELL) {
                        cache_goods.add(goods);
                    }
                }
                Common_msg_cache.set_goods_Cache(getContext(), cache_goods);//将商品信息存入缓存
                if(cache_goods.size() < ConstantUtil.Goods_Piece){
                    for (int i = 0; i < cache_goods.size(); i++) {
                        mAdapter.getList().add(cache_goods.get(i));
                        Common_msg_cache.set_goods_cache_status(getContext(), cache_goods.size());
                    }
                }else{
                    for (int i = 0; i < ConstantUtil.Goods_Piece; i++) {
                        mAdapter.getList().add(cache_goods.get(i));
                        Common_msg_cache.set_goods_cache_status(getContext(), ConstantUtil.Goods_Piece);
                    }
                }
                //mAdapter.getList().addAll(goodsList);
                mAdapter.getRandomHeight(cache_goods);
                mAdapter.notifyDataSetChanged();
                //Toast.makeText(getContext(), "存入缓存成功", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.activity_main_menu_secondhand, container, false);
        initView(view, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        //refresh_goods_cache();//初始化之后更新缓存
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("refreshGoods");
        broadcastManager.registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }
    /**
     * 注销广播
     */
    @Override
    public void onDetach() {
        super.onDetach();
        broadcastManager.unregisterReceiver(mRefreshBroadcastReceiver);
    }

    // broadcast receiver
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String change = intent.getStringExtra("change");
            if ("yes".equals(change)) {
                mRecyclerView.smoothScrollToPosition(0);
                refreshLayout.autoRefresh();
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
    }


//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_main_menu_secondhand;
//    }


    //@Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {//设置上拉刷新监听器

            public void onRefresh(RefreshLayout refreshlayout) {
                //这一段话是小黑写的
                Common_msg_cache.set_goods_Cache(getContext(), null);
                mAdapter.getList().clear();
                HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH + "query_goods.php", new Callback() {
                    public void onFailure(Call call, IOException e) {
                    }

                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Message msg = new Message();
                        msg.obj = str;
                        handler.sendMessage(msg);
                    }
                });
                //mAdapter.notifyDataSetChanged();
                refreshlayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {//设置下滑加载监听器

            public void onLoadmore(RefreshLayout refreshlayout) {
                add_goods(refreshlayout);
                refreshlayout.finishLoadmore();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    //@Override

    protected void initData() {
        StaggeredGridLayoutManager sgl = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sgl);
        mAdapter = new WaterFallAdapter(getActivity(), goodsList);
        mRecyclerView.setAdapter(mAdapter);
        if (Common_msg_cache.get_goods_Cache(getContext()) != null) {//判断缓存中是否存在旧货信息
            ArrayList<Goods> cache_goods = Common_msg_cache.get_goods_Cache(getContext());
            int index = Common_msg_cache.get_goods_cache_status(getContext());//得到上次读取到第几条数据
            mAdapter.getList().clear();//加载之前将原来的list删除
            for (int i = 0; i < Common_msg_cache.get_goods_Cache(getActivity()).size(); i++) {
                mAdapter.getList().add(cache_goods.get(i));
            }
            mAdapter.getRandomHeight(cache_goods);
            mAdapter.notifyDataSetChanged();
        } else {
            getGoodsData();//从服务器获取数据，并存入缓存中
        }

        mAdapter.setOnItemClickListener(new WaterFallAdapter.OnItemClickListener() {
            public void onItemClick(View view, int position) {

                Goods goods;
                ArrayList<Goods> cache_goods = Common_msg_cache.get_goods_Cache(getContext());
                // goods = goodsList.get(position);
                goods = cache_goods.get(position);
                Intent intent = new Intent(getActivity(), GoodsInfoActivity.class);
                intent.putExtra("tmp_goodsid", goods.getGoods_id());//传递商品id
                intent.putExtra("tmp_goodsname", goods.getGoodsName());//传递商品名称
                intent.putExtra("tmp_intro", goods.getGoodsIntro());//传递商品介绍
                intent.putExtra("tmp_pageViews", String.valueOf(goods.getPageViews()));
                intent.putExtra("tmp_goodsStyle", goods.getGoodsStyle());
                intent.putExtra("tmp_goodsType", goods.getGoodsType());
                intent.putExtra("tmp_isSell", goods.getIsSell());
                intent.putExtra("tmp_position", position);
                intent.putExtra("tmp_minPrice", goods.getMinPrice());
                intent.putExtra("tmp_uid",goods.getUid());
                toIntentPrice(intent, goods);
                startActivity(intent);
            }
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getContext(), position + " long click",
                        Toast.LENGTH_SHORT).show();
                //mAdapter.removeData(position);
            }

        });
    }

    private void toIntentPrice(Intent intent, Goods goods) {
        if (goods.getGoodsType() == ConstantUtil.Goods_Type_ykj) {
            intent.putExtra("tmp_ykjPrice", String.valueOf(goods.getPrice()));
        } else if (goods.getGoodsType() == ConstantUtil.Goods_Type_yj) {
            // intent.putExtra("tmp_yjPrice", String.valueOf(goods.getRefPrice()));
            intent.putExtra("tmp_yjPrice", String.valueOf(goods.getPrice()));
        } else if (goods.getGoodsType() == ConstantUtil.Goods_Type_pai) {
            intent.putExtra("tmp_nowPrice", String.valueOf(goods.getNowPrice()));
            intent.putExtra("tmp_minPrice", String.valueOf(goods.getMinPrice()));
        }
    }

    public static void setPrice(Goods goods, int type, int pirce,int minPrice) {
        if (type == ConstantUtil.Goods_Type_ykj) {
            goods.setPrice(pirce);
        } else if (type == ConstantUtil.Goods_Type_yj) {
            goods.setRefPrice(pirce);
        } else if (type == ConstantUtil.Goods_Type_pai) {
            //goods.setBasePrice(pirce);
            goods.setNowPrice(pirce);
            goods.setMinPrice(minPrice);
        }
    }

    private void getGoodsData() {//向服务器请求数据
        HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH + "query_goods.php", new Callback() {
            public void onFailure(Call call, IOException e) {
                Log.i("iii", e.getMessage());
            }

            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Message msg = new Message();
                msg.obj = str;
                handler.sendMessage(msg);
            }
        });

        mAdapter.getList().addAll(goodsList);
        mAdapter.getRandomHeight(goodsList);
        mAdapter.notifyDataSetChanged();
    }

    public void add_goods(RefreshLayout refreshlayout) {//从缓存中添加商品
        if (Common_msg_cache.get_goods_Cache(getContext()) != null) {//判断缓存中是否存在旧货信息
            ArrayList<Goods> cache_goods = Common_msg_cache.get_goods_Cache(getContext());
            int index = Common_msg_cache.get_goods_cache_status(getContext());//得到上次读取到第几条数据
            int length = Common_msg_cache.get_goods_Cache(getContext()).size();
            if(length > ConstantUtil.Goods_Piece){//如果读取的条数>6条
                int number = Math.abs(index - cache_goods.size());
                if (number < ConstantUtil.Goods_Piece) {//如果缓存中的信息少于5条
                    if (number != 0) {//有多少条，加多少条
                        for (int i = index; i < index + number; i++) {
                            mAdapter.getList().add(cache_goods.get(i));
                            Common_msg_cache.add_goods_cache_status(getContext(), index + number);
                        }
                    } else if (number == 0) {
                        Toast.makeText(getContext(), "当前没有更多商品", Toast.LENGTH_SHORT).show();
                        refreshlayout.finishLoadmore();
                        return;
                    }
                } else {//缓存中信息的商品数多于5条
                    for (int i = index; i < index + ConstantUtil.Goods_Piece; i++) {//缓存中数据充足，加载4条

                        mAdapter.getList().add(cache_goods.get(i));

                    }
                    Common_msg_cache.add_goods_cache_status(getContext(), index + ConstantUtil.Goods_Piece);
                }
                mAdapter.getRandomHeight(cache_goods);
                mAdapter.notifyDataSetChanged();
               // Toast.makeText(getContext(), "缓存中读取", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "当前没有更多商品!", Toast.LENGTH_SHORT).show();
            }
        }

    }


}


