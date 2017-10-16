package com.xiaoyu.schoolelive.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.base.BaseSlideBack;
import com.xiaoyu.schoolelive.util.ConstantUtil;
import com.xiaoyu.schoolelive.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends BaseSlideBack implements LoaderManager.LoaderCallbacks<Cursor> {
    class MyHandler extends Handler {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            String data = b.getString("data");
            long uid = b.getLong("uid");
            if(String.valueOf(-1).equals(data)){//如果返回-1 说明该用户不存在
                Toast.makeText(LoginActivity.this,"该用户不存在", Toast.LENGTH_SHORT).show();
            }else{
               if(password.equals(data)){
                   Toast.makeText(LoginActivity.this,"登陆成功", Toast.LENGTH_SHORT).show();
                   MainActivity.boo = true;
                   if(remember_pass.isChecked()){//如果用户点击了记住密码
                       editor.putLong("uid",uid);//将用户的实际帐号密码存到shareperference中，
                       editor.putString("Password",data);//用来设置记录的帐号和密码
                       editor.putBoolean("isChecked",true);//将isChecked常量放到sharedperference中
                   }else{//没有点击记住密码，直接进入主界面
                       editor.putBoolean("isChecked",false);
                   }
                   editor.apply();//提交
                   Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                   intent.putExtra("uid",uid);
                   startActivity(intent);//跳转到主界面并传递id
               }else{
                   Toast.makeText(LoginActivity.this,"密码不正确", Toast.LENGTH_SHORT).show();
               }

            }
        }
    }
    public String getPwd = null;//判断用户是否存在，得到用户的真正密码
    private Button login;
    private Button register;
    private CheckBox remember_pass;//记住密码按钮
    private RequestBody requestBody;//发送请求对象
    private SharedPreferences login_sp;
    private SharedPreferences.Editor editor;
    private static long UsernameValue;//将要存到sharedpreference中的帐号
    private static String PasswordValue;//将要存到sharedpreference中的密码
    private boolean choseRemember;//是否选择记住密码、login_status登录状态
    private Handler handler;
    private String password;


    private EditText mUserwordView,mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    //账号最短长度
    private boolean isEmailValid(String userword) {
        //TODO: Replace this with your own logic
        return userword.length() >= 6;
    }
    //密码最短长度
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    }
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserwordView = (EditText) findViewById(R.id.userword);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    login();
                    return true;
                }
                return false;
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        //标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setTitle("登录");

        register = (Button) findViewById(R.id.btn_regist);
        login_sp = getSharedPreferences("User_info", MODE_APPEND);
        choseRemember = login_sp.getBoolean("isChecked", false);
        editor = login_sp.edit();//实例化Editor对象
        handler = new MyHandler();
        remember_pass = (CheckBox) findViewById(R.id.mRememberCheck);
        if (choseRemember) {
            UsernameValue = login_sp.getLong("uid", 0);
            PasswordValue = login_sp.getString("Password", null);
            mUserwordView.setText(String.valueOf(UsernameValue));
            mPasswordView.setText(PasswordValue);
            mUserwordView.setSelection(String.valueOf(UsernameValue).length());//将帐号输入框的光标移到最后
            remember_pass.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(){
        // 重置错误
        mUserwordView.setError(null);
        mPasswordView.setError(null);
        // 获取用户名和密码
        final String userword = mUserwordView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(userword)) {
            mUserwordView.setError(getString(R.string.error_field_required));
            focusView = mUserwordView;
            cancel = true;
        } else if (!isEmailValid(userword)) {
            mUserwordView.setError(getString(R.string.error_invalid_userword));
            focusView = mUserwordView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            //showProgress(true);
            requestBody = new FormBody.Builder()
                    .add("uid", String.valueOf(userword))
                    .add("password",password)
                    .build();
            HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH+"login.php", requestBody, new okhttp3.Callback() {
                public void onFailure(Call call, IOException e) {
                    Log.e("error",e.getMessage());
                }
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    getPwd = responseData;
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("data",getPwd);
                    bundle.putLong("uid", Long.parseLong(userword));
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            });
        }
    }
    //标题栏菜单点击逻辑
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

