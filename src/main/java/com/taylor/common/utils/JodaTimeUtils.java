package com.taylor.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author carl.zhao
 * @version 1.0.0
 * @see JodaTimeUtils
 * @since 1.0.0 2017-04-25
 */
public abstract class JodaTimeUtils {

    /**
     * 获取传入时间当天的开始时间
     * in 2017-04-25 16:06:11
     * out 2017-04-25 00:00:00
     * @param currentTime
     * @return
     */
    public static Date getCurrentBeginTime(Date currentTime){
        DateTime dateTime = new DateTime(currentTime);
        int year = dateTime.year().get();
        int month = dateTime.monthOfYear().get();
        int day = dateTime.dayOfMonth().get();
        return new DateTime(year, month, day, 0, 0).toDate();
    }

    /**
     * 获取传入时间当天的最后时间
     * in 2017-04-25 16:06:11
     * out 2017-04-25 23:59:59
     * @param currentTime
     * @return
     */
    public static Date getCurrentEndTime(Date currentTime){
        DateTime dateTime = new DateTime(currentTime);
        int year = dateTime.year().get();
        int month = dateTime.monthOfYear().get();
        int day = dateTime.dayOfMonth().get();
        return new DateTime(year, month, day, 23, 59, 59).toDate();
    }

    public static boolean judgeDateFormat(String input, String target){
        if(StringUtils.isBlank(input) || StringUtils.isBlank(target)){
            return false;
        }
        SimpleDateFormat format = new SimpleDateFormat(target);
        format.setLenient(false);
        try {
            format.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
