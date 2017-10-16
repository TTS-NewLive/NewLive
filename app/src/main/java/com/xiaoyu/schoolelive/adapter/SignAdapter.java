package com.xiaoyu.schoolelive.adapter;

import com.xiaoyu.schoolelive.custom.SignView;
import com.xiaoyu.schoolelive.data.SignEntity;

import java.util.List;

/**
 * SignAdapter
 * Created by E.M on 2016/4/21.
 */
public class SignAdapter extends CalendarAdapter {
    private List<SignEntity> data;

    public SignAdapter(List<SignEntity> data) {
        this.data = data;
    }

    @Override
    public SignView.DayType getType(int dayOfMonth) {
        return SignView.DayType.valueOf(data.get(dayOfMonth - 1).getDayType());
    }
}