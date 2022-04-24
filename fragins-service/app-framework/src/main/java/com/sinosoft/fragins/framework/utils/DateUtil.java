package com.sinosoft.fragins.framework.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author yanzhaohu
 * @date 2022-01-04
 * 添加时间处理
 */
public class DateUtil {

    public static int getYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH)+1;
    }
}
