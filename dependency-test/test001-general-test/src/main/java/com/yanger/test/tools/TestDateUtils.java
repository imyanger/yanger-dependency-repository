package com.yanger.test.tools;

import com.yanger.tools.general.tools.DateUtils;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Date;

/**
 * @Author yanger
 * @Date 2021/12/8/008 23:29
 */
public class TestDateUtils {

    @Test
    public void test() {
        // convertLocal 类方法，对象间转换
        long l = DateUtils.convertLocalDate2Long(LocalDate.now());

        // plus,minus 对时间进行加减操作
        Date plusDate = DateUtils.plusDays(DateUtils.now(), 2);
        Date minusDate = DateUtils.minusYears(DateUtils.now(), 2);

        int daysCount = DateUtils.getDaysCount(new Date(), plusDate);
        System.out.println(daysCount);
    }

}
