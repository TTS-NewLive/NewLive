package com.xiaoyu.schoolelive.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2017/8/12.
 */

public class Goods implements Serializable{
    //竞拍
    public int basePrice;
    public int nowPrice;
    public int minPrice;
    //一口价
    public int price;
    //可议价
    public int refPrice;


    public List<ImageBean> images;
    public ImageBean topImage;
    public String goodsName;
    public String goodsIntro;
    public String goodsStartDate;
    public String goodsEndDate;
    public String goods_id;
    public int goodsType;
    public int goodsStyle;
    public int pageViews;//浏览量
    public boolean isFocus = false;
    public boolean isAgainst = false;
    public boolean isCollection = false;
    public int  status = 0;//加载状态

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public int getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(int nowPrice) {
        this.nowPrice = nowPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRefPrice() {
        return refPrice;
    }

    public void setRefPrice(int refPrice) {
        this.refPrice = refPrice;
    }

    public int getGoodsStyle() {
        return goodsStyle;
    }

    public void setGoodsStyle(int goodsStyle) {
        this.goodsStyle = goodsStyle;
    }

    public List<ImageBean> getImages() {
        return images;
    }

    public void setImages(List<ImageBean> images) {
        this.images = images;
    }

    public ImageBean getTopImages() {
        return topImage;
    }

    public void setTopImage(ImageBean image) {
        this.topImage = image;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsIntro() {
        return goodsIntro;
    }

    public void setGoodsIntro(String goodsIntro) {
        this.goodsIntro = goodsIntro;
    }

    public String getGoodsStartDate() {
        return goodsStartDate;
    }

    public void setGoodsStartDate(String goodsStartDate) {
        this.goodsStartDate = goodsStartDate;
    }

    public String getGoodsEndDate() {
        return goodsEndDate;
    }

    public void setGoodsEndDate(String goodsEndDate) {
        this.goodsEndDate = goodsEndDate;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public int getPageViews() {
        return pageViews;
    }

    public void setPageViews(int pageViews) {
        this.pageViews = pageViews;
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }

    public boolean isAgainst() {
        return isAgainst;
    }

    public void setAgainst(boolean against) {
        isAgainst = against;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
