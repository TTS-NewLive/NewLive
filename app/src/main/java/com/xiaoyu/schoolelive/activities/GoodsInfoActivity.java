package com.xiaoyu.schoolelive.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.adapter.GoodsJPAdapter;
import com.xiaoyu.schoolelive.custom.CustomDialog;
import com.xiaoyu.schoolelive.custom.MyListView;
import com.xiaoyu.schoolelive.data.Goods;
import com.xiaoyu.schoolelive.data.JPUser;
import com.xiaoyu.schoolelive.util.ACache;
import com.xiaoyu.schoolelive.util.Common_msg_cache;
import com.xiaoyu.schoolelive.util.ConstantUtil;
import com.xiaoyu.schoolelive.util.HttpUtil;
import com.xiaoyu.schoolelive.util.Login_cache;
import com.xiaoyu.schoolelive.util.WidgetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.xiaoyu.schoolelive.R.id.goods_name;

/**
 * Created by NeekChaw on 2017-07-29.
 */


public class GoodsInfoActivity extends AppCompatActivity implements View.OnClickListener {
    public static int ONSELL = 0;//未销售
    public static int SELLED = 1;//已销售
    public static int ITEM_NUMBER = 5;//加载数目
    //竞拍
    //private TextView mBasePrice;
    private TextView mNowPrice;
    private TextView mMinPrice;
    //竞拍列表控件
    private MyListView mListView;
    private GoodsJPAdapter mAdapter;
    private List<JPUser> mData;
    //一口价
    private TextView mPrice;
    //可议价
    private TextView mRefPrice;

    private String goods_id;
    private String seller_id;
    private ImageView btn_more;
    private ImageView btn_back;
    private Button btn_pai;
    private Button btn_ykj;
    private Button btn_yj_mai;
    private Button btn_yj_chat;
    private Goods goods;
    private TextView mGoodsName;//名称
    private TextView mGoodsPageViews;
    private TextView mGoodsHot;
    private TextView mGoodsNew;
    private TextView mGoodsTop;
    private TextView mGoodsIntro;//商品介绍
    private View mGoodsPai;
    private View mGoodsYKJ;
    private View mGoodsYJ;
    private View mGoodIntro_view;
    private BGABanner mGoodsImages;
    private ImageView callSeller;
    final String[] mItems = new String[]{"卖家详情", "举报", "联系卖家"};
    final String[] mAgainstItems = new String[]{"泄露隐私", "人身攻击", "淫秽色情", "垃圾广告", "敏感信息", "其他"};
    private Handler handler4 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mAdapter.getList().clear();
            String bid_data = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(bid_data);
                for (int i = 0; i < (jsonArray.length() <= ITEM_NUMBER ? jsonArray.length() : ITEM_NUMBER); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String bider_id = jsonObject.getString("uid");//竞拍者id
                    String bid_time = jsonObject.getString("date");//交易时间
                    String bid_price = jsonObject.getString("bid_price");//商品名称
                    String user_head = jsonObject.getString("photo");//头像
                    String user_name = jsonObject.getString("name");//昵称
                    JPUser jpUser = new JPUser();
                    final String str = ConstantUtil.SERVICE_PATH + WidgetUtil.str_trim(user_head);
                    jpUser.setImage(str);
                    jpUser.setName(user_name);
                    jpUser.setPrice(bid_price);
                    jpUser.setDate(bid_time);
                    jpUser.setUid(bider_id);
                    mData.add(jpUser);
                }
                //直接更新页面当前价格
                mNowPrice.setText(mData.get(0).getPrice());
                mAdapter.notifyDataSetChanged();
                // 广播通知
                Intent intent = new Intent("refreshGoods");
                intent.putExtra("change", "yes");
                LocalBroadcastManager.getInstance(GoodsInfoActivity.this).sendBroadcast(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private Handler handler3 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //判断交易是否成功
            String result = (String) msg.obj;
            if (String.valueOf(1).equals(result)) {
                Toast.makeText(GoodsInfoActivity.this, "竞价发布成功", Toast.LENGTH_SHORT).show();
            } else if (String.valueOf(2).equals(result)) {
                Toast.makeText(GoodsInfoActivity.this, "竞价发布失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Handler handler5 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //判断交易是否成功
            String result = (String) msg.obj;
            if (String.valueOf(1).equals(result)) {
                Toast.makeText(GoodsInfoActivity.this, "添加数据库成功", Toast.LENGTH_SHORT).show();
            } else if (String.valueOf(2).equals(result)) {
                Toast.makeText(GoodsInfoActivity.this, "添加数据库失败", Toast.LENGTH_SHORT).show();
            }else if(String.valueOf(3).equals(result)){
                Toast.makeText(GoodsInfoActivity.this, "更新数据库成功", Toast.LENGTH_SHORT).show();
                //清除缓存
            }
        }
    };
    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //判断交易是否成功
            String result = (String) msg.obj;
            if (String.valueOf(1).equals(result)) {
                btn_ykj.setEnabled(false);
                //btn_ykj.setBackground(getResources().getDrawable(R.drawable.btn_selled_bg));
                btn_ykj.setBackgroundResource(R.drawable.btn_selled_bg);
                btn_ykj.setText("已售");
                //这句话是小黑写的
                Common_msg_cache.get_goods_Cache(getApplicationContext()).get(getIntent().getIntExtra("tmp_position", SELLED)).setIsSell(SELLED);
                Toast.makeText(GoodsInfoActivity.this, "交易成功", Toast.LENGTH_SHORT).show();

                //交易成功后向卖家发送通知
                push_message(get_sell_id());

            } else if (String.valueOf(2).equals(result)) {
                Toast.makeText(GoodsInfoActivity.this, "交易失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ACache aCache = ACache.get(getApplicationContext());
            Bundle bundle = msg.getData();
            String goods_path = bundle.getString("goods_path");
            String goods_id = bundle.getString("goods_id");
            List<String> Image_List = new ArrayList<>();
            List<String> wordsList = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(goods_path);
                aCache.put(goods_id + "", jsonArray, 3 * ACache.TIME_DAY);//将数据放到缓存中
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Image_List.add(ConstantUtil.SERVICE_PATH + WidgetUtil.str_trim(jsonObject.getString("goods_path")));
                    wordsList.add("小雨科技");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mGoodsImages.setData(Image_List, wordsList);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_goods_info);
        goods = new Goods();
        initView();
        footerDataGet();
    }
    public void footerDataGet(){
        if (Login_cache.get_login_status(getApplicationContext()).equals("true")){
            String buyerId = Login_cache.get_login_username(getApplicationContext());
            String goodsId = goods_id;
            String time=String.valueOf(System.currentTimeMillis());
            RequestBody requestBody = new FormBody.Builder()
                    .add("buyer_id",buyerId)
                    .add("goods_id", goodsId)
                    .add("systime",time)
                    .build();
            HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH + "goods_footer.php", requestBody, new okhttp3.Callback() {
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(GoodsInfoActivity.this, "网络好像出问题了...", Toast.LENGTH_SHORT).show();
                }

                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    String result = responseData;
                    Log.d("GoodsInfo", result);
                    Message msg = Message.obtain();
                    msg.obj = result;
                    handler5.sendMessage(msg);
                }
            });
        }else {
            Toast.makeText(GoodsInfoActivity.this,"没登录，不存历史记录",Toast.LENGTH_SHORT).show();
        }
    }
    public void initView() {
        Intent intent = getIntent();

        findById();
        //  Toast.makeText(getApplicationContext(),intent.getStringExtra("tmp_goodsname"),Toast.LENGTH_SHORT).show();
        //setGoodsName(intent.getStringExtra("tmp_goodsname"));
        //设置商品类型
        setGoodsType(intent, intent.getIntExtra("tmp_goodsType", 0));
        init_callsell(intent.getIntExtra("tmp_goodsType", 0));

        //设置商品名称
        setGoodsName(intent.getIntExtra("tmp_goodsType", 0), intent.getStringExtra("tmp_goodsname"));

        //设置商品介绍
        setGoodsIntro(intent.getIntExtra("tmp_goodsType", 0), intent.getStringExtra("tmp_intro"));
        //设置浏览量
        //mGoodsPageViews.setText(intent.getStringExtra("tmp_pageViews"));
        //设置商品标签
        //setGoodsStyle(intent.getIntExtra("tmp_goodsStyle", 0));
        //设置商品价格
        setGoodsPrice(intent, intent.getIntExtra("tmp_goodsType", 0));

        goods_id = intent.getStringExtra("tmp_goodsid");
        seller_id = intent.getStringExtra("tmp_uid");
        setGoodsImages(goods_id);

        // 初始化数据
        mData = new ArrayList<>();
        // 初始化适配器
        mAdapter = new GoodsJPAdapter(this, mData);
        // 为评论列表设置适配器
        mListView.setAdapter(mAdapter);
        //初始化列表数据
        initData();
        btn_more.setOnClickListener(this);
        btn_pai.setOnClickListener(this);
        btn_ykj.setOnClickListener(this);
        btn_yj_mai.setOnClickListener(this);
        btn_yj_chat.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    public void initData() {
        //String uid = Login_cache.get_login_username(getApplicationContext());

        RequestBody requestBody = new FormBody.Builder()
                .add("goods_id", goods_id)
                .build();
        HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH + "query_bid.php", requestBody, new Callback() {
            public void onFailure(Call call, IOException e) {
                Toast.makeText(GoodsInfoActivity.this, "网络好像出问题了...", Toast.LENGTH_SHORT).show();
            }

            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Message msg = new Message();
                String result = str;
                msg.obj = str;
                handler4.sendMessage(msg);
            }
        });
    }

    public void getData() {

    }

    public void findById() {
        btn_more = (ImageView) findViewById(R.id.goods_more);
        btn_pai = (Button) findViewById(R.id.btn_pai);
        btn_ykj = (Button) findViewById(R.id.btn_ykj);
        btn_yj_mai = (Button) findViewById(R.id.btn_yj_mai);
        btn_yj_chat = (Button) findViewById(R.id.btn_yj_chat);
        btn_back = (ImageView) findViewById(R.id.goods_back);

        mGoodsImages = (BGABanner) findViewById(R.id.goods_images_banner);
        mGoodsPageViews = (TextView) findViewById(R.id.goods_attention_count);
        mGoodsHot = (TextView) findViewById(R.id.goods_hot);
        mGoodsNew = (TextView) findViewById(R.id.goods_new);
        mGoodsTop = (TextView) findViewById(R.id.goods_top);
        mGoodsPai = findViewById(R.id.goodsType_pai);
        mGoodsYKJ = findViewById(R.id.goodsType_ykj);
        mGoodsYJ = findViewById(R.id.goodsType_yj);

        //竞拍价格
        //mBasePrice = (TextView) findViewById(R.id.pai_base_price);
        mNowPrice = (TextView) findViewById(R.id.pai_now_price);
        mMinPrice = (TextView) findViewById(R.id.pai_min_price);

        //竞拍列表控件
        mListView = (MyListView) findViewById(R.id.jp_list);

        //一口价价格
        mPrice = (TextView) findViewById(R.id.yikoujia_price);
        //可议价价格
        mRefPrice = (TextView) findViewById(R.id.yj_price);



    }

    public void setGoodsImages(final String goods_id) {
        mGoodsImages.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(GoodsInfoActivity.this)
                        .load(model)
                        .placeholder(R.drawable.gif_ad_holder)
                        .error(R.mipmap.ic_launcher)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });
        ACache aCache = ACache.get(getApplicationContext());
        if (aCache.getAsJSONArray("" + goods_id) != null) {//如果缓存中存在,直接在缓存中读取
            List<String> Image_List = new ArrayList<>();
            List<String> wordsList = new ArrayList<>();
            try {
                JSONArray jsonArray = aCache.getAsJSONArray("" + goods_id);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Image_List.add(ConstantUtil.SERVICE_PATH + WidgetUtil.str_trim(jsonObject.getString("goods_path")));
                    wordsList.add("小雨科技");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mGoodsImages.setData(Image_List, wordsList);
        } else {//缓存中不存在，向服务器发送请求
            final RequestBody requestBody = new FormBody.Builder()
                    .add("goods_id", goods_id)
                    .build();
            HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH + "query_goods_image.php", requestBody, new Callback() {
                public void onFailure(Call call, IOException e) {

                }

                public void onResponse(Call call, Response response) throws IOException {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("goods_id", goods_id);
                    bundle.putString("goods_path", response.body().string());
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            });
//            HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH + "query_one_goods.php", requestBody, new Callback() {
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                public void onResponse(Call call, Response response) throws IOException {
//                    final String responseData = response.body().string();
//                    String result = responseData;
//                    Message msg = Message.obtain();
//                    msg.obj = result;
//                    handler3.sendMessage(msg);
//                }
//            });
        }


    }

    public void setGoodsName(int type, String goodsName) {
        if (type == ConstantUtil.Goods_Type_pai) {
            mGoodsName = (TextView) mGoodsPai.findViewById(goods_name);
            mGoodsName.setText(goodsName);
        } else if (type == ConstantUtil.Goods_Type_yj) {
            mGoodsName = (TextView) mGoodsYJ.findViewById(goods_name);
            mGoodsName.setText(goodsName);
        } else if (type == ConstantUtil.Goods_Type_ykj) {
            mGoodsName = (TextView) mGoodsYKJ.findViewById(goods_name);
            mGoodsName.setText(goodsName);
        }

    }


    public void setGoodsIntro(int type, String goodsName) {
        if (type == ConstantUtil.Goods_Type_pai) {
            mGoodIntro_view = mGoodsPai.findViewById(R.id.inc_goods_intro);
            mGoodsIntro = (TextView) mGoodIntro_view.findViewById(R.id.goods_intro);
            mGoodsIntro.setText(goodsName);
        } else if (type == ConstantUtil.Goods_Type_yj) {
            mGoodIntro_view = mGoodsYJ.findViewById(R.id.inc_goods_intro);
            mGoodsIntro = (TextView) mGoodIntro_view.findViewById(R.id.goods_intro);
            mGoodsIntro.setText(goodsName);
        } else if (type == ConstantUtil.Goods_Type_ykj) {
            mGoodIntro_view = mGoodsYKJ.findViewById(R.id.inc_goods_intro);
            mGoodsIntro = (TextView) mGoodIntro_view.findViewById(R.id.goods_intro);
            mGoodsIntro.setText(goodsName);
        }

    }

    public void setGoodsStyle(int type) {
        if (type == ConstantUtil.Goods_All) {
            mGoodsHot.setVisibility(View.VISIBLE);
            mGoodsNew.setVisibility(View.VISIBLE);
            mGoodsTop.setVisibility(View.VISIBLE);
        } else if (type == ConstantUtil.Goods_Hot) {
            mGoodsHot.setVisibility(View.VISIBLE);
        } else if (type == ConstantUtil.Goods_New) {
            mGoodsNew.setVisibility(View.VISIBLE);
        } else if (type == ConstantUtil.Goods_Top) {
            mGoodsTop.setVisibility(View.VISIBLE);
        } else if (type == ConstantUtil.Goods_Top_Hot) {
            mGoodsTop.setVisibility(View.VISIBLE);
            mGoodsHot.setVisibility(View.VISIBLE);
        } else if (type == ConstantUtil.Goods_Hot_New) {
            mGoodsHot.setVisibility(View.VISIBLE);
            mGoodsNew.setVisibility(View.VISIBLE);
        } else if (type == ConstantUtil.Goods_Top_New) {
            mGoodsTop.setVisibility(View.VISIBLE);
            mGoodsNew.setVisibility(View.VISIBLE);
        }
    }

    public void setGoodsType(Intent intent, int type) {
        if (type == ConstantUtil.Goods_Type_pai) {
            mGoodsName = (TextView) mGoodsPai.findViewById(goods_name);
            mGoodsPai.setVisibility(View.VISIBLE);
            if (intent.getIntExtra("tmp_isSell", ONSELL) == SELLED) {
                //btn_pai.setClickable(false);
                btn_pai.setEnabled(false);
                //btn_pai.setBackground(getResources().getDrawable(R.drawable.btn_selled_bg));
                btn_ykj.setBackgroundResource(R.drawable.btn_selled_bg);
                btn_pai.setText("已售");
            }
        } else if (type == ConstantUtil.Goods_Type_yj) {
            mGoodsName = (TextView) mGoodsYJ.findViewById(goods_name);
            mGoodsYJ.setVisibility(View.VISIBLE);
            if (intent.getIntExtra("tmp_isSell", ONSELL) == SELLED) {
                // btn_yj_mai.setClickable(false);
                btn_yj_mai.setEnabled(false);
                //btn_yj_mai.setBackground(getResources().getDrawable(R.drawable.btn_selled_bg));
                btn_ykj.setBackgroundResource(R.drawable.btn_selled_bg);
                btn_yj_mai.setText("已售");
            }
        } else if (type == ConstantUtil.Goods_Type_ykj) {
            mGoodsName = (TextView) mGoodsYKJ.findViewById(goods_name);
            mGoodsYKJ.setVisibility(View.VISIBLE);
            if (intent.getIntExtra("tmp_isSell", ONSELL) == SELLED) {
                //btn_ykj.setClickable(false);
                btn_ykj.setEnabled(false);
                //btn_ykj.setBackground(getResources().getDrawable(R.drawable.btn_selled_bg));
                btn_ykj.setBackgroundResource(R.drawable.btn_selled_bg);
                btn_ykj.setText("已售");
            }
        }
    }
    public void init_callsell(int type){
        if (type == ConstantUtil.Goods_Type_pai) {
            callSeller =(ImageView) mGoodsPai.findViewById(R.id.callSeller);
        } else if (type == ConstantUtil.Goods_Type_ykj) {
            callSeller =(ImageView) mGoodsYKJ.findViewById(R.id.callSeller);
        }
        callSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Login_cache.get_login_status(getApplicationContext()).equals("true")) {
                    Toast.makeText(GoodsInfoActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if(get_sell_id().equals(Login_cache.get_login_username(getApplicationContext()))){
                        Toast.makeText(GoodsInfoActivity.this, "您正是该商品的主人", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (RongIM.getInstance() != null) {
                        //不让卖家的电话号码全部显示,中间4位用*表示
                        // String encryption_sell_id = get_sell_id().substring(0,3)+"****"+get_sell_id().substring(7,11);//加密
                        //String sell_id = get_sell_id();
                        // RongIM.getInstance().startPrivateChat(GoodsInfoActivity.this,now_id.equals(sell_id)?now_id:sell_id,now_id.equals(sell_id)?now_id:sell_id);
                        String encryption_sell_id = get_sell_id().substring(0, 3) + "****" + get_sell_id().substring(7, 11);
                        RongIM.getInstance().startPrivateChat(GoodsInfoActivity.this, get_sell_id(), encryption_sell_id);
                    }

                }

            }


        });
    }

    public void setGoodsPrice(Intent intent, int type) {
        if (type == ConstantUtil.Goods_Type_pai) {
            mMinPrice.setText(intent.getStringExtra("tmp_minPrice"));
            //mBasePrice.setText(intent.getStringExtra("tmp_basePrice"));
            mNowPrice.setText(intent.getStringExtra("tmp_nowPrice"));
        } else if (type == ConstantUtil.Goods_Type_yj) {
            mRefPrice.setText(intent.getStringExtra("tmp_yjPrice"));
        } else if (type == ConstantUtil.Goods_Type_ykj) {
            mPrice.setText(intent.getStringExtra("tmp_ykjPrice"));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_more:
                showDialog(goods);
                break;
            case R.id.btn_pai:
                showPaiDialog();
                break;
            case R.id.btn_ykj:
                showYKJDialog();
                break;
            case R.id.btn_yj_chat:
                showYJDialog();
                break;
            case R.id.btn_yj_mai:
                break;
            case R.id.goods_back:
                finish();
                break;
        }
    }
    //

    private void showDialog(final Goods goods) {
        LayoutInflater layoutInflater = LayoutInflater.from(GoodsInfoActivity.this);
        //自定义对话框标题栏
        final View mTitleView = layoutInflater.inflate(R.layout.custom_user_center_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(GoodsInfoActivity.this);
        builder.setItems(mItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
//                        Intent intent = new Intent(GoodsInfoActivity.this, UserCenterActivity.class);
//                        startActivity(intent);
                        break;
//                    case 1:
//                        if (goods.isFocus()) {
//                            Toast.makeText(GoodsInfoActivity.this, "您已关注该用户", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(GoodsInfoActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
//                            goods.setFocus(true);
//                        }
//                        break;
//                    case 2:
//                        Toast.makeText(GoodsInfoActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
//                        break;
//                    case 3:
//                        ShowShareUtil.showShare(GoodsInfoActivity.this);
//                        break;
                    case 1:
                        AlertDialog.Builder builder = new AlertDialog.Builder(GoodsInfoActivity.this);
                        builder.setItems(mAgainstItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0://泄露隐私
                                        dealClick(goods, which);
                                        break;
                                    case 1://人身攻击
                                        dealClick(goods, which);
                                        break;
                                    case 2://淫秽色情
                                        dealClick(goods, which);
                                        break;
                                    case 3://垃圾广告
                                        dealClick(goods, which);
                                        break;
                                    case 4://敏感信息
                                        dealClick(goods, which);
                                        break;
                                    case 5://其他
                                        dealClick(goods, which);
                                        break;
                                }
                            }
                        });
                        AlertDialog against = builder.show();
                        //设置宽度和高度
                        WindowManager.LayoutParams params =
                                against.getWindow().getAttributes();
                        params.width = 600;
                        params.y = -200;
                        params.alpha = 0.9f;
                        against.getWindow().setAttributes(params);
                        break;
                    case 2:
                        if (!Login_cache.get_login_status(getApplicationContext()).equals("true")) {
                            Toast.makeText(GoodsInfoActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            if(get_sell_id().equals(Login_cache.get_login_username(getApplicationContext()))){
                                Toast.makeText(GoodsInfoActivity.this, "您正是该商品的主人", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (RongIM.getInstance() != null) {
                                //不让卖家的电话号码全部显示,中间4位用*表示
                                // String encryption_sell_id = get_sell_id().substring(0,3)+"****"+get_sell_id().substring(7,11);//加密
                                //String sell_id = get_sell_id();
                                // RongIM.getInstance().startPrivateChat(GoodsInfoActivity.this,now_id.equals(sell_id)?now_id:sell_id,now_id.equals(sell_id)?now_id:sell_id);
                                String encryption_sell_id = get_sell_id().substring(0, 3) + "****" + get_sell_id().substring(7, 11);
                                RongIM.getInstance().startPrivateChat(GoodsInfoActivity.this, get_sell_id(), encryption_sell_id);
                            }

                        }

                }
            }
        });
        builder.setCustomTitle(mTitleView);
        AlertDialog dialog = builder.show();
        //设置宽度和高度
        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        params.width = 600;
        params.alpha = 0.9f;
        params.y = -200;
        dialog.getWindow().setAttributes(params);
    }

    //判断是否重复举报
    public boolean isAgainst(Goods goods) {
        if (goods.isAgainst()) {
            goods.setAgainst(false);
            return true;
        } else {
            return false;
        }
    }

    //对举报信息进行处理
    public void dealClick(Goods goods, int dealCode) {
        if (isAgainst(goods)) {
            Toast.makeText(GoodsInfoActivity.this, "您已经举报过了！管理员正在审核处理", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(GoodsInfoActivity.this, "举报成功",
                    Toast.LENGTH_SHORT).show();
            goods.setAgainst(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showPaiDialog() {
        View view = View.inflate(this, R.layout.custom_dialog_pai, null);
        //TextView basePrice = (TextView) view.findViewById(R.id.basePrice);
        TextView nowPrice = (TextView) view.findViewById(R.id.nowPrice);
        TextView addPrice = (TextView) view.findViewById(R.id.addPrice);
        final EditText yourPrice = (EditText) view.findViewById(R.id.pai_yoursPrice);

        //basePrice.setText(mBasePrice.getText().toString());
        addPrice.setText(mMinPrice.getText().toString());
        nowPrice.setText(mNowPrice.getText().toString());
        final CustomDialog.Builder builder = new CustomDialog.Builder(GoodsInfoActivity.this);
        builder.setTitle("竞拍").
                setContentView(view).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Login_cache.get_login_status(getApplicationContext()).equals("true")) {
                    sendPrice(yourPrice);
                    dialog.dismiss();
                } else {
                    Toast.makeText(GoodsInfoActivity.this, "请登录账号", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        }).create().show();
    }

    public void showYKJDialog() {
        View view = View.inflate(this, R.layout.custom_dialog_ykj, null);
        final CustomDialog.Builder builder = new CustomDialog.Builder(GoodsInfoActivity.this);
        builder.setTitle("一口价").
                setContentView(view).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String buyerId = Login_cache.get_login_username(getApplicationContext());
                String sellerId = seller_id;
                String goodsId = goods_id;

                RequestBody requestBody = new FormBody.Builder()
                        .add("buyer_id", String.valueOf(buyerId))
                        //.add("seller_id", sellerId)
                        .add("goods_id", goodsId)
                        .build();
                HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH + "goods_deal.php", requestBody, new Callback() {
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(GoodsInfoActivity.this, "网络好像出问题了...", Toast.LENGTH_SHORT).show();
                    }

                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        String result = responseData;
                        Log.d("GoodsInfo", result);
                        Message msg = Message.obtain();
                        msg.obj = result;
                        handler2.sendMessage(msg);
                    }
                });
                dialog.dismiss();
            }
        }).create().show();
    }

    public void showYJDialog() {
        View view = View.inflate(this, R.layout.custom_dialog_yj, null);
        TextView yj_chat = (TextView) view.findViewById(R.id.refPrice);
        yj_chat.setText(mRefPrice.getText().toString());
        final CustomDialog.Builder builder = new CustomDialog.Builder(GoodsInfoActivity.this);
        builder.setTitle("可议价").
                setContentView(view).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        }).create().show();
    }

    /**
     * 竞拍发送报价
     */
    public void sendPrice(EditText myPrcie) {
        View view = View.inflate(this, R.layout.custom_dialog_pai, null);
        int yoursPrice = Integer.parseInt(myPrcie.getText().toString());
        int bids = Integer.parseInt(mMinPrice.getText().toString());
        int nowPrice = Integer.parseInt(mNowPrice.getText().toString());
        if (myPrcie.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "报价不能为空！", Toast.LENGTH_SHORT).show();
        } else if (yoursPrice < nowPrice + bids) {
            Toast.makeText(getApplicationContext(), "不符合报价规则！", Toast.LENGTH_SHORT).show();
            myPrcie.setText("");
        } else {
            // 生成报价数据
            JPUser jpUser = new JPUser();
            jpUser.setPrice(myPrcie.getText().toString());

            //获取当前时间
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
            Date date = new Date(System.currentTimeMillis());
            String str = format.format(date);
            jpUser.setDate(str);
            jpUser.setUid(Login_cache.get_login_username(getApplicationContext()));
            jpUser.setGoods_id(goods_id);
            SimpleDateFormat sDateFormat = new SimpleDateFormat("00yyyyMMddhhmmss");
            //得到系统当前时间作为竞拍的id
            String bid_id = sDateFormat.format(new java.util.Date());


            RequestBody requestBody = new FormBody.Builder()
                    .add("bid_id", bid_id)
                    .add("goods_id", jpUser.getGoods_id())
                    .add("uid", jpUser.getUid())
                    .add("bid_price", jpUser.getPrice())
                    .build();
            HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH + "insert_bid.php", requestBody, new Callback() {
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(GoodsInfoActivity.this, "网络好像出问题了...", Toast.LENGTH_SHORT).show();
                }

                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    String result = responseData;
                    Message msg = Message.obtain();
                    msg.obj = result;
                    handler3.sendMessage(msg);
                    initData();
                }
            });
            // 发送完，清空输入框
            myPrcie.setText("");

            // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
//            InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            im.hideSoftInputFromWindow(myPrcie.getWindowToken(), 0);
        }
    }

    //得到卖家的id
    private String get_sell_id() {
        return getIntent().getStringExtra("tmp_uid");
    }

    public void push_message(String sell_id){
        RequestBody requestBody = new FormBody.Builder()
                .add("sell_id",get_sell_id())
                .build();
        HttpUtil.sendHttpRequest("http://39.106.31.44/jpush/push_message.php",requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("GoodsInfoActivity",response.body().string());
            }
        });


    }

}