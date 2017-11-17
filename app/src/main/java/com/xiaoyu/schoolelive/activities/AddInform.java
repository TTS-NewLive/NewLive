package com.xiaoyu.schoolelive.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.util.HttpUtil;
import com.xiaoyu.schoolelive.util.Login_cache;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddInform extends AppCompatActivity {

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String data = (String) msg.obj;
            if(String.valueOf(2).equals(data)){//如果返回-1 说明该用户不存在
                Toast.makeText(AddInform.this,"发布失败", Toast.LENGTH_SHORT).show();
            }else if(String.valueOf(1).equals(data)){
                add_inform.setText("");
                Toast.makeText(AddInform.this,"发布成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private ImageView ig;
    private TextView tv;
    private EditText add_inform;
    private Button send_inform;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inform);

        add_inform = (EditText)findViewById(R.id.add_inform);
        send_inform = (Button)findViewById(R.id.send_inform);
        ig = (ImageView)findViewById(R.id.toolbarBack);
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv = (TextView)findViewById(R.id.toolbarTitle);
        tv.setText("通知发布");

        send_inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login_cache.get_login_status(getApplicationContext()).equals("false")) {
                    Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    RequestBody requestBody = new FormBody.Builder()
                            .add("name", Login_cache.get_login_username(getApplicationContext()))
                            .add("content",add_inform.getText().toString())
                            .build();
                    HttpUtil.sendHttpRequest("http://39.106.31.44/jpush/push_messageall.php", requestBody, new okhttp3.Callback() {
                        public void onFailure(Call call, IOException e) {
                            Log.e("error",e.getMessage());
                        }
                        public void onResponse(Call call, Response response) throws IOException {
                            String data = response.body().string();
                            Message msg = Message.obtain();
                            msg.obj = data;
                            handler.sendMessage(msg);
                        }
                    });
                }

            }
        });
    }
}
