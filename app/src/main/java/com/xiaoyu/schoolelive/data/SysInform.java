package com.xiaoyu.schoolelive.data;


/**
 * Created by Administrator on 2017/8/13.
 */

public class SysInform {
    int sysInform_image;

    String sysInform_name;
    String sysInform_data;
    String sysInform_content;

    public int getSysInform_image() {
        return sysInform_image;
    }

    public String getSysInform_content() {
        return sysInform_content;
    }

    public String getSysInform_data() {
        return sysInform_data;
    }
    public String getSysInform_name() {
        return sysInform_name;
    }

    public void setSysInform_content(String sysInform_content) {
        this.sysInform_content = sysInform_content;
    }

    public void setSysInform_data(String sysInform_data) {
        this.sysInform_data = sysInform_data;
    }

    public void setSysInform_image(int sysInform_image) {
        this.sysInform_image = sysInform_image;
    }

    public void setSysInform_name(String sysInform_name) {
        this.sysInform_name = sysInform_name;
    }
}
