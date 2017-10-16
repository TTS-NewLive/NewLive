package com.xiaoyu.schoolelive.util;

/**
 * Created by NeekChaw on 2017-07-31.
 */

public class ConstantUtil {
    public static String SERVICE_PATH = "http://139.199.181.68/xiaoyu/";

    /**
     * 兼职类别
     */
    public static int PartJob_YANYUAN = 1;
    public static int PartJob_XIAOSHOU = 2;
    public static int PartJob_BAOAN = 3;
    public static int PartJob_FACHUANDAN = 4;
    public static int PartJob_WAIMAI = 5;
    public static int PartJob_KUAIDI = 6;
    public static int PartJob_TUIGUANG = 7;
    public static int PartJob_WANGLUO = 8;
    public static int PartJob_JIAJIAO = 9;
    public static int PartJob_GONGCHANG = 10;
    public static int PartJob_FUWUYUAN = 11;
    public static int PartJob_XIAOYUAN = 12;
    public static int PartJob_QITA = 13;

    /**
     * 工资计算方式
     */
    //每小时
    public static int WagesType_PERHOUR = 0;
    //每天
    public static int WagesType_PERDAY = 1;
    //每月
    public static int WagesType_PERMONTH = 2;
    //每件
    public static int WagesType_PERPIECE = 3;
    //每单
    public static int WagesType_PERITEM = 4;


    /**
     * 工资结算方式
     */
    //日结
    public static int WagesPay_DAY = 1;
    //周结
    public static int WagesPay_WEEK = 2;
    //月结
    public static int WagesPay_MONTH = 3;
    //完工结算
    public static int WagesPay_AFTERWORK = 4;

    /**
     * 商品：顶 热 新
     */
    //无
    public static int Goods_Null = 0;
    //顶
    public static int Goods_Top = 1;
    //热
    public static int Goods_Hot = 2;
    //新
    public static int Goods_New = 3;
    //顶_热
    public static int Goods_Top_Hot = 4;
    //顶_新
    public static int Goods_Top_New = 5;
    //热_新
    public static int Goods_Hot_New = 6;
    //全部
    public static int Goods_All = 7;

    /**
     * 商品类型 ：一口价  可议价  竞拍
     */
    //一口价
    public static int Goods_Type_ykj = 0;
    //可议价
    public static int Goods_Type_yj = 1;
    //竞拍
    public static int Goods_Type_pai = 2;

    /**
     * 性别
     */
    //男
    public static int MAN = 1;
    //女
    public static int WOMEN = 2;
    //不限
    public static int ALL_SEX = 3;

    /**
     * 兼职待遇
     */
    //餐补
    public static int ALLOWANCE_MEAL = 1;
    //住宿
    public static int ALLOWANCE_LIVE = 2;
    //交通补助
    public static int ALLOWANCE_TRAFFIC = 3;
    //其他
    public static int ALLOWANCE_OTHER = 4;

    /**
     * Activity标识
     */
    public static int MAIN_ACTIVITY = 1;
    public static int USERALBUM_ACTIVITY = 2;
    public static int USERCENTET_ACTIVITY = 3;

    public static int Goods_Piece = 5;//每次刷新加载商品件数
}

