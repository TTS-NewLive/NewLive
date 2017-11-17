package com.xiaoyu.schoolelive.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.adapter.DealAdapter;
import com.xiaoyu.schoolelive.base.BaseSlideBack;
import com.xiaoyu.schoolelive.custom.CustomFindHistory;
import com.xiaoyu.schoolelive.data.Deal;
import com.xiaoyu.schoolelive.util.ConstantUtil;
import com.xiaoyu.schoolelive.util.HttpUtil;
import com.xiaoyu.schoolelive.util.RecordSQLiteOpenHelper;
import com.xiaoyu.schoolelive.util.WidgetUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
*  搜索详情界面:
* */
public class FindActivity extends BaseSlideBack {

    private EditText find_et;
    private TextView find_history_tv;
    private CustomFindHistory fint_history_list;
    private TextView find_history_clear;
    private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);
    private SQLiteDatabase db;
    private List<Deal> mDatas = new ArrayList<>();
    private BaseAdapter adapter;
    private ImageView search_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        // 初始化控件
        initView();
        // 清空搜索历史
        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        find_history_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
                queryData("");
            }
        });

        // 搜索框的键盘搜索键点击回调
        find_et.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                    if (find_et.getText().toString().equals("")) {
                        Toast.makeText(FindActivity.this, "不能为空！", Toast.LENGTH_SHORT).show();
                    }else{
                        boolean hasData = hasData(find_et.getText().toString().trim());
                        if (!hasData) {
                            insertData(find_et.getText().toString().trim());
                            queryData("");
                        }
                        // TODO 根据输入的内容模糊查询商品，并跳转到另一个界面，由你自己去实现
                        //Toast.makeText(FindActivity.this, find_et.getText().toString(), Toast.LENGTH_SHORT).show();
                       // find_et.setText("");

                        //跳转到搜索详情界面

                        String key = find_et.getText().toString();
                        //去除关键词中的空格和引号
                        search(key);

                    }
                }
                find_et.setText("");
                return false;
            }
        });
        // 搜索框的文本变化实时监听
        find_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    find_history_tv.setText("搜索历史");
                }
                String tempName = find_et.getText().toString();
                // 根据tempName去模糊查询数据库中有没有数据
                queryData(tempName);
            }
        });

        fint_history_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                find_et.setText(name);
                Toast.makeText(FindActivity.this, name, Toast.LENGTH_SHORT).show();
                // TODO 获取到item上面的文字，根据该关键字跳转到另一个页面查询，由你自己去实现
            }
        });

        // 第一次进入查询所有的历史记录
        queryData("");
    }
    /**
     * 插入数据
     */
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    /**
     * 模糊查询数据
     */
    private void queryData(String tempName) {
       Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        // 创建adapter适配器对象

        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[] { "name" },
                new int[] { android.R.id.text1 }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        fint_history_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        fint_history_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                search(tv.getText().toString());
            }
        });
    }
    /**
     * 检查数据库中是否已经有该条记录
     */
    private boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery("select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 清空数据
     */
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }

    private void initView() {
        find_et = (EditText) findViewById(R.id.et_search);
        find_history_tv = (TextView) findViewById(R.id.find_history_tv);
        fint_history_list = (CustomFindHistory) findViewById(R.id.find_history_list);
        find_history_clear = (TextView) findViewById(R.id.find_history_clear);
        search_back = (ImageView)findViewById(R.id.search_back);
        // 调整EditText左边的搜索按钮的大小
        Drawable drawable = getResources().getDrawable(R.drawable.find);
        drawable.setBounds(0, 0, 60, 60);// 第一0是距左边距离，第二0是距上边距离，60分别是长宽
        find_et.setCompoundDrawables(drawable, null, null, null);// 只放左边
    }
    private void search(String key){
        String final_key =WidgetUtil.str_yinhao(WidgetUtil.str_trim(key));
        RequestBody requestBody = new FormBody.Builder()
                .add("key",final_key)//关键字
                .build();
        HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH + "search_goods.php", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //将搜索到的结果发送到SearchDetail_Activity中去
                Intent intent = new Intent(FindActivity.this,SearchDetail_Activity.class);
                intent.putExtra("search_result",response.body().string());
                startActivity(intent);
            }
        });
    }
}

