package com.sinosoft.fragins.framework.utils;


import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * @author yanzhaohu
 * @date 2022-01-04
 */
public class DateUtilTest {

    @Test
    public  void testYear(){
      int year =  DateUtil.getYear(new Date());
        Assert.assertEquals(year,2022);
    }

    @Test
    public  void testMonth(){
        int year =  DateUtil.getMonth(new Date());
        Assert.assertEquals(year,1);
    }
}