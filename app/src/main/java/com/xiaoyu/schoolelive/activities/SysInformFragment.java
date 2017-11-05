package com.xiaoyu.schoolelive.activities;

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

    private List<SysInform> mData;
    private SysInformAdapter mAdapter;
    private View view;
    private TabHost tabHost;
    private RecyclerView general_rcview;
    private static final int No_1 = 0x1;


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

            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }
    private void getSysInformData() {
        for (int i = 0; i < 10; i++) {
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


