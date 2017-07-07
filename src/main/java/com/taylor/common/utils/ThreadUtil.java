package com.taylor.common.utils;

import java.util.concurrent.TimeUnit;

/**
 * @author Michael.Wang
 * @date 2017/5/2
 */
public class ThreadUtil {
    public static void sleep(int millSeconds) {
        try {
            Thread.sleep(millSeconds);
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    public static void sleepSeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    public static void sleepMinutes(int minutes) {
        try {
           TimeUnit.MINUTES.sleep(minutes);
        } catch (InterruptedException e) {
            //do nothing
        }
    }
}
