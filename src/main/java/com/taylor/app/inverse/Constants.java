package com.taylor.app.inverse;

public class Constants {
    /** cookie信息 **/
    public static final String cookie = "aliyungf_tc=AQAAAB+SojZJEgkAjrvndLI/QIi+ntuI; SESSION=3ccc08f6-cc17-4ecc-be95-2c58b43617e2";

    /** 加载因子-相当于下平均下来每次都赚 **/
    public static final int factor = new Integer(2);

    /** 最大承受倍数 **/
    public static final int maxMutiply = new Integer(75000);

    /** 投注单位 **/
    public static final String unit = new String("li");

    /** 增长因子-每次加注时在保本的情况下，附加上次的1/n筹码用于扩大收益 **/
    public static final int increaseFactor = new Integer(4);

    /** 重复单双多少次数后反转 **/
    public static final int inverseNum = new Integer(3);

    /** 到达最大容忍后从几分之当前投注开始从新投注 **/
    public static final int maxTerentFactor = new Integer(6);

    /** 睡眠时间 **/
    public static final int sleepTime = new Integer(5000);

    /** 反转次数时机---连续n次不中后改变为相反策略，一旦失败-从头开始 **/
    public static final int reversetimes = new Integer(5);
}
