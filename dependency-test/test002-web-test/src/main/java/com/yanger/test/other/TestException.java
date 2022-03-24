package com.yanger.test.other;

import com.yanger.test.result.MyResultCode;
import com.yanger.tools.web.entity.E;
import com.yanger.tools.web.exception.AssertUtils;
import com.yanger.tools.web.exception.BasicException;
import org.junit.Test;

/**
 * @Author yanger
 * @Date 2022/3/20/020 21:45
 */
public class TestException {

    @Test
    public void testException(){
        String str = null;
        E.when(str == null).throwExp().message("当str == null抛出异常");
    }

    @Test
    public void testException2(){
        String str = null;
        E.when(str == null).throwExp(BasicException.class).message("当str == null抛出异常");
    }

    @Test
    public void testException3(){
        String str = null;
        E.when(str == null).throwExp(MyException.class).message("当str == null 抛出异常，{} 为 {}", "str", "null");
    }

    @Test
    public void testException4(){
        String str = null;
        E.when(str == null).throwExp(MyException.class).message(MyResultCode.PARAMETER_ERROR_PERCH, "参数不能为 null");
    }

    @Test
    public void testException5(){
        String str = null;
        AssertUtils.notNull(str, "当str == null 抛出异常，{} 为 {}", "str", "null");
    }

    @Test
    public void testException6(){
        String str = null;
        AssertUtils.notNull(str, MyResultCode.PARAMETER_ERROR_PERCH, "参数不能为 null");
    }

}
