package com.xiaoyu.schoolelive.data;

import java.io.Serializable;

/**
 * Created by NeekChaw on 2017-10-23.
 */

public class Footer implements Serializable{
    String topImage;
    String goodsName;
    String goodsPrice;
    String goodsInfo;
    String goodsId;
    public int pageViews;//浏览量
    public int goodsType;
    public int goodsStyle;

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public String getTopImage() {
        return topImage;
    }

    public void setTopImage(String topImage) {
        this.topImage = topImage;
    }


    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(String goodsId){
        ;this.goodsId=goodsId;
    }


    public int getPageViews() {
        return pageViews;
    }

    public void setPageViews(int pageViews) {
        this.pageViews = pageViews;
    }

    public int getGoodsStyle() {
        return goodsStyle;
    }

    public void setGoodsStyle(int goodsStyle) {
        this.goodsStyle = goodsStyle;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }
}
