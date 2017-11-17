package com.xiaoyu.schoolelive.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.adapter.SysInformAdapter;
import com.xiaoyu.schoolelive.data.Footer;
import com.xiaoyu.schoolelive.data.SysInform;
import com.xiaoyu.schoolelive.util.Common_msg_cache;
import com.xiaoyu.schoolelive.util.ConstantUtil;
import com.xiaoyu.schoolelive.util.HttpUtil;
import com.xiaoyu.schoolelive.util.Login_cache;
import com.xiaoyu.schoolelive.util.WidgetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mob.MobSDK.getContext;

/**
 * Created by Administrator on 2017/7/15.
 */
public class SysInformFragment extends Fragment {

    private List<SysInform> mData;
    private SysInformAdapter mAdapter;
    private View view;
    private RecyclerView general_rcview;
    private static final int No_1 = 0x1;
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String inform_data = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(inform_data);
                //从服务器端得到通知
                for (int i = (jsonArray.length()-1); i >= 0 ; i--) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String inform_name =jsonObject.getString("name");//通知发布者
                    String inform_date = jsonObject.getString("date");//发布时间
                    String inform_content = jsonObject.getString("content");//商品价格

                    SysInform inform = new SysInform();
                    inform.setSysInform_image(R.drawable.ic_home32);
                    inform.setSysInform_name(inform_name);
                    inform.setSysInform_data(inform_date);
                    inform.setSysInform_content(inform_content);
                    mData.add(inform);
                }
                mAdapter.notifyDataSetChanged();
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
        view = inflater.inflate(R.layout.activity_main_menu_sysinform, container,false);
        ButterKnife.bind(this, view);

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }
    private void initData() {
        general_rcview = (RecyclerView) view.findViewById(R.id.general_rcview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        general_rcview.setLayoutManager(layoutManager);
        general_rcview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mData = new ArrayList<>();

        getData();

        mAdapter = new SysInformAdapter(getActivity(), mData);
        general_rcview.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new SysInformAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }
    public void getData() {
        if (Login_cache.get_login_status(getContext()).equals("true")) {
            HttpUtil.sendHttpRequest(ConstantUtil.SERVICE_PATH + "query_inform.php",new Callback() {
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(getContext(), "网络好像出问题了...", Toast.LENGTH_SHORT).show();
                }

                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    Message msg = new Message();
                    msg.obj = str;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}


