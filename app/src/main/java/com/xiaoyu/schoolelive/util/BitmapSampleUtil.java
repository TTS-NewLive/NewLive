package com.xiaoyu.schoolelive.util;

import java.util.Random;

import com.xiaoyu.schoolelive.R;

/**
 * Created by Administrator on 2017/7/11.
 */
public class BitmapSampleUtil {

    public static String[] IMAGES = new String[]{
            "drawable://" + R.drawable.dw_1,
            "drawable://" + R.drawable.dw_2,
            "drawable://" + R.drawable.dw_3,
            "drawable://" + R.drawable.dw_4,
            "drawable://" + R.drawable.dw_5,
            "drawable://" + R.drawable.dw_6,
            "drawable://" + R.drawable.dw_7,
            "drawable://" + R.drawable.dw_8,
            "drawable://" + R.drawable.dw_9};
    /**
     * 随机产生的一个图片Url
     *
     * @return
     */
    public static String getBmpUrl() {
        int index = new Random().nextInt(IMAGES.length);
        return IMAGES[index];
    }
}
