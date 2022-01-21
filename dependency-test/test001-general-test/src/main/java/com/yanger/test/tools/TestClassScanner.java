package com.yanger.test.tools;

import com.yanger.tools.general.tools.ClassScanner;
import org.junit.Test;

import java.util.Set;

/**
 * @Author yanger
 * @Date 2021/12/4/004 15:52
 */
public class TestClassScanner {

    @Test
    public void test(){
        Set<Class<?>> classes = ClassScanner.getClasses("com.yanger.tools.general.format");
        classes.forEach(s -> System.out.println(s.getSimpleName()));
    }

}
