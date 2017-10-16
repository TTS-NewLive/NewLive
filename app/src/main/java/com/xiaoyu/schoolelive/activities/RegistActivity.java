package com.xiaoyu.schoolelive.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.util.ConstantUtil;
import com.xiaoyu.schoolelive.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistActivity extends AppCompatActivity {
    class MyHandler extends Handler {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (String.valueOf(1).equals(result)) {
                Toast.makeText(RegistActivity.this, "该用户已经存在", Toast.LENGTH_SHORT).show();
            } else if (String.valueOf(2).equals(result)) {
                new AlertDialog.Builder(RegistActivity.this)
                        .setTitle("恭喜您注册成功")
                        .setMessage("请牢记您的帐号与密码，点击确定将进入登录界面")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            } else if (String.valueOf(3).equals(result)) {
                Toast.makeText(RegistActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private EditText rg_uid;//帐号
    private EditText rg_pwd;//密码
    private TextView getVerify;//获取验证码
    private Button btn_regist;//注册按钮
    private long rg_uid_content;//rg_uid中的内容
    private String rg_pwd_content;//rg_pwd中的内容
    private Handler handler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        //标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setTitle("注册");
        getVerify = (TextView) findViewById(R.id.getVerify);
        rg_uid = (EditText) findViewById(R.id.rg_uid);
        rg_pwd = (EditText) findViewById(R.id.rg_pwd);

        handler = new MyHandler();
        getVerify.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        btn_regist = (Button) findViewById(R.id.btn_regist);
        btn_regist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {

        if (rg_uid.getText().toString().isEmpty() || rg_pwd.getText().toString().isEmpty()) {
            Toast.makeText(RegistActivity.this, "你还没有输入帐号或密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rg_uid.getText().toString().length() < 6 || rg_pwd.getText().toString().length() < 6) {
            Toast.makeText(RegistActivity.this, "注册的帐号或密码不能小于6位", Toast.LENGTH_SHORT).show();
            return;
        }
        rg_uid_content = Long.parseLong(rg_uid.getText().toString());
        rg_pwd_content = rg_pwd.getText().toString();
        RequestBody requestBody = new FormBody.Builder()
                .add("uid", String.valueOf(rg_uid_content))
                .add("password", rg_pwd_content)
                .build();
        HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH + "register.php", requestBody, new okhttp3.Callback() {
            public void onFailure(Call call, IOException e) {
            }

            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                String result = responseData;
                Message msg = Message.obtain();
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    //标题栏菜单点击逻辑
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



