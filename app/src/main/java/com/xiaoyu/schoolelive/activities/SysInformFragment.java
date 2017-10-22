package com.xiaoyu.schoolelive.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.adapter.SysInformAdapter;
import com.xiaoyu.schoolelive.data.SysInform;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/15.
 */
public class SysInformFragment extends Fragment {
    public static boolean SysInformIsDisplay = false;
    private static final int NOTIFICATION_FLAG = 1;
    private List<SysInform> mData;
    private SysInformAdapter mAdapter;
    private View view;
    private TabHost tabHost;
    private RecyclerView general_rcview;
    private static final int No_1 = 0x1;
    private NotificationManager manager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_menu_sysinform, container,false);
        ButterKnife.bind(this, view);
        setSysInformTab();
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
        mAdapter = new SysInformAdapter(getActivity(), mData);
        general_rcview.setAdapter(mAdapter);
        getSysInformData();

        mAdapter.setOnItemClickListener(new SysInformAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(),position+"",Toast.LENGTH_SHORT).show();

                manager = (NotificationManager)getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                PendingIntent pendingIntent2 = PendingIntent.getActivity(getContext(), 0,
                        new Intent(getContext(), MainActivity.class), 0);
                // 通过Notification.Builder来创建通知，注意API Level
                // API11之后才支持
                Notification notify2 = new Notification.Builder(getContext())
                        .setSmallIcon(mData.get(position).getSysInform_image()) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
                        // icon)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setTicker("TickerText:" + "您有新短消息，请注意查收！")// 设置在status
                        // bar上显示的提示文字
                        .setContentTitle(mData.get(position).getSysInform_name())// 设置在下拉status
                        // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                        .setContentText(mData.get(position).getSysInform_content())// TextView中显示的详细内容
                        .setContentIntent(pendingIntent2) // 关联PendingIntent
                        .getNotification(); // 需要注意build()是在API level
                // 16及之后增加的，在API11中可以使用getNotificatin()来代替
                notify2.flags |= Notification.FLAG_AUTO_CANCEL;// FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
                manager.notify(NOTIFICATION_FLAG, notify2);
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }
    private void getSysInformData() {
        for (int i = 0; i < 10; i++) {
            if (i == 1){
                SysInform inform = new SysInform();
                inform.setSysInform_image(R.drawable.ic_menu_gallery);
                inform.setSysInform_name("第二条");
                inform.setSysInform_data("2017-08-13  12:51:44");
                inform.setSysInform_content("第二条通知发布啦!!!");
                mData.add(inform);
            }
            SysInform inform = new SysInform();
            inform.setSysInform_image(R.drawable.ic_home_black_24dp);
            inform.setSysInform_name("校园E生活官方");
            inform.setSysInform_data("2017-08-13  12:51:44");
            inform.setSysInform_content("第一条通知发布啦!!!");
            mData.add(inform);
        }
//        mAdapter.getList().addAll(mData);
        mAdapter.notifyDataSetChanged();
    }

    public void setSysInformTab() {
        //找到TabHost的标签集合
        tabHost = (TabHost) view.findViewById(R.id.sysinform_tabhost);
        /*如果没有继承TabActivity时，通过下面这种方法加载启动tabHost.这一句在源代码中,
        会根据findviewbyId()找到对应的TabWidget,还需要根据findViewById()找到
        这个TabWidget下面对应的标签页的内容.也就是FrameLayout这个显示控件.*/
        tabHost.setup();

        //TabSpec这个是标签页对象.
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("page1");//新建一个标签页对象.
        tabSpec.setIndicator(("普通通知"));//设置这个标签页的标题和图片
        tabSpec.setContent(R.id.inform_page1);//指定标签页的内容页.
        tabHost.addTab(tabSpec);//把这个标签页,添加到标签对象tabHost中.

        tabSpec = tabHost.newTabSpec("page2");
        tabSpec.setIndicator("系统通知");
        tabSpec.setContent(R.id.inform_page2);
        tabHost.addTab(tabSpec);
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


