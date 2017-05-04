package com.taylor.app.inverse;

public class Constants {
    /** cookie信息 **/
    public static final String cookie = "aliyungf_tc=AQAAAItpvWph/QQAb9hAcG0qzqxQGjtt; SESSION=dbfcd81b-745b-487d-b251-6ff838aea75f";

    /** 加载因子 **/
    public static final int factor = new Integer(2);

    /** 最大倍数 **/
    public static final int maxMutiply = new Integer(10000);

    /** 投注单位 **/
    public static final String unit = new String("fen");

    /** 增长因子 **/
    public static final int increaseFactor = new Integer(4);

    /** 重复单双多少次数后反转 **/
    public static final int inverseNum = new Integer(4);

    /** 到达最大容忍后从几分之当前投注开始从新投注 **/
    public static final int maxTerentFactor = new Integer(8);

    /** 睡眠时间 **/
    public static final int sleepTime = new Integer(5000);
}
