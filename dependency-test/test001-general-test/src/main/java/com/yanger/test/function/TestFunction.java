package com.yanger.test.function;

import com.yanger.tools.general.function.CheckedCallable;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TestFunction {

    @Test
    public void testFunction() {
        Callable<String> retString = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Callable";
            }
        };
        CheckedCallable<String> stringCheckedCallable = new CheckedCallable<String>() {
            @Override
            public String call() throws Throwable {
                return null;
            }
        };
    }

}
