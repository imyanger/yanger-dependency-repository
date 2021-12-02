package com.yanger.test.format;

import com.yanger.tools.general.format.ConcurrentDateFormat;
import com.yanger.tools.general.format.StringFormat;
import org.junit.Test;

import java.util.Date;

public class TestFormatTools {

    @Test
    public void testStringFormat() {
        String stringFormat = StringFormat.format("This is {} by {}", "StringFormat", "yanger");
        System.out.println(stringFormat);
        String mergeFormat = StringFormat.mergeFormat("This is {0} by {1}", "mergeFormat", "yanger");
        System.out.println(mergeFormat);
    }

    @Test
    public void testConcurrentDateFormat(){
        String now = ConcurrentDateFormat.of().format(new Date());
        System.out.println(now);
        String now2 = ConcurrentDateFormat.of("yyyy-MM").format(new Date());
        System.out.println(now2);
    }

}
