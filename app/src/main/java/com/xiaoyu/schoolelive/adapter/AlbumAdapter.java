package com.xiaoyu.schoolelive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.data.Album;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * Created by lenovo on 2017/8/18.
 */

public class AlbumAdapter extends BGARecyclerViewAdapter<Album> {
    Context mContext;
    List<Album> mData;
    BGANinePhotoLayout.Delegate mDelegate;

    public AlbumAdapter(Context c, RecyclerView recyclerView) {
        super(recyclerView, R.layout.custom_user_album);
        this.mContext = c;
    }


    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, Album album) {
        helper.setText(R.id.photoDate, album.getPhotoDate());

        BGANinePhotoLayout ninePhotoLayout = helper.getView(R.id.album_Photos);
        ninePhotoLayout.setDelegate(mDelegate);
        ninePhotoLayout.setData(album.photos);
    }

    public BGANinePhotoLayout.Delegate setDelegate(BGANinePhotoLayout.Delegate delegate) {
        this.mDelegate = delegate;
        return delegate;
    }

}
