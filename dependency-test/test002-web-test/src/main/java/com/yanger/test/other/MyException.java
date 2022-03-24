package com.yanger.test.other;

import com.yanger.tools.web.exception.BasicException;
import com.yanger.tools.web.support.IResultCode;

/**
 * @Author yanger
 * @Date 2022/3/20/020 22:43
 */
public class MyException extends BasicException {

    public MyException(String message) {
        super(message);
    }

    public MyException(Integer code, String message) {
        super(code, message);
    }

    public MyException(Integer code, String message, Object... args) {
        super(code, message, args);
    }

    public MyException(IResultCode resultCode) {
        super(resultCode);
    }

    public MyException(IResultCode resultCode, Object... args) {
        super(resultCode, args);
    }

    public MyException(Throwable cause) {
        super(cause);
    }

    public MyException(Throwable cause, String message) {
        super(cause, message);
    }

    public MyException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

    public MyException(Throwable cause, Integer code, String message, Object... args) {
        super(cause, code, message, args);
    }

    public MyException(Throwable cause, IResultCode resultCode) {
        super(cause, resultCode);
    }

    public MyException(Throwable cause, IResultCode resultCode, Object... args) {
        super(cause, resultCode, args);
    }

}
