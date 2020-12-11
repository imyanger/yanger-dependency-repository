package com.yanger.generator.exception;

/**
 * @author yanger 2020-05-02 21:10:28
 */
public class CodeGenerateException extends RuntimeException {

    private static final long serialVersionUID = 1024L;

    public CodeGenerateException() {
        super();
    }

    public CodeGenerateException(String msg) {
        super(msg);
    }

}
