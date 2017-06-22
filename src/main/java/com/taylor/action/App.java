package com.taylor.action;

import org.springframework.util.StopWatch;

/**
 * @author xiaolu.zhang
 * @desc:
 * @date: 2017/6/22 17:48
 */
public class App {
    public static void main(String... args) {
        try {
            StopWatch stopWatch = new StopWatch();


            stopWatch.start("睡眠时间");
            Thread.sleep(1000);
            stopWatch.stop();

            stopWatch.start("吃饭时间");
            Thread.sleep(1000);
            stopWatch.stop();
            stopWatch.start("看电影时间");
            Thread.sleep(1000);
            stopWatch.stop();


            System.out.println("=== 请求结束，共计耗时为：" + stopWatch.getLastTaskTimeMillis() + "ms");
            System.out.println(stopWatch.prettyPrint());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
