package com.xiaoyu.schoolelive.data;


import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by NeekChaw on 2017-07-16.
 */
public class Publish implements Serializable {
    public ArrayList<String> photos;
    public String photo_path = null;
    public String name; //评论者
    public String content; //发布内容
    public String ymd;//年月日
    public String date;//小时分钟
    public boolean PUB_LIKE_FLAG = false;//初始状态，还未点赞
    public boolean IS_AGAINST = false;//初始状态，还未举报
    public boolean IS_FOCUS = false;//初始状态，还未关注


    public long uid = 2015404;//发帖者的uid

    public int like_count, comment_count, share_count;//点赞评论数量

    public Publish(String nickname,String ymd,String date,String content, ArrayList<String> photos) {
        this.name=nickname;
        this.ymd=ymd;
        this.date=date;
        this.content = content;
        this.photos = photos;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public boolean isPUB_LIKE_FLAG() {
        return PUB_LIKE_FLAG;
    }

    public void setPUB_LIKE_FLAG(boolean PUB_LIKE_FLAG) {
        this.PUB_LIKE_FLAG = PUB_LIKE_FLAG;
    }

    public boolean IS_AGAINST() {
        return IS_AGAINST;
    }

    public void setIS_AGAINST(boolean IS_AGAINST) {
        this.IS_AGAINST = IS_AGAINST;
    }

    public boolean IS_FOCUS() {
        return IS_FOCUS;
    }

    public void setIS_FOCUS(boolean IS_FOCUS) {
        this.IS_FOCUS = IS_FOCUS;
    }
    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
