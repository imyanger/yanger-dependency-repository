package com.yanger.tools.web.entity;

import com.yanger.tools.general.constant.StringPool;
import com.yanger.tools.web.exception.BasicException;
import com.yanger.tools.web.support.IResultCode;

/**
 * 异常调用类
 * @Author yanger
 * @Date 2020/12/21 18:23
 */
public class E {

    /**
     * 设置异常信息
     * @param message 报错信息
     * @return the exception when
     */
    public static ExceptionWhen message(String message) {
        return new E().new ExceptionWhen(BasicException.DEFAULT_ERROR_CODE, message);
    }

    /**
     * 设置异常信息
     * @param code 错误码
     * @param message 报错信息
     * @return the exception when
     */
    public static ExceptionWhen message(Integer code, String message) {
        return new E().new ExceptionWhen(IResultCode.DEFAULT_SERVER_NAME.concat(StringPool.DASH) + code, message);
    }

    /**
     * 设置异常信息
     * @param resultCode resultCode 对象
     * @return the exception when
     */
    public static ExceptionWhen message(IResultCode resultCode) {
        return new E().new ExceptionWhen(resultCode.generateCode(), resultCode.generateMessage());
    }

    /**
     * 设置异常信息
     * @param resultCode resultCode 对象
     * @param message 报错信息
     * @return the exception when
     */
    public static ExceptionWhen message(IResultCode resultCode, Object... message) {
        return new E().new ExceptionWhen(resultCode.generateCode(), resultCode.generateMessage(message));
    }

    /**
     * 抛出异常的条件
     * @param condition 判断条件
     * @return the exception when . inner exception
     */
    public static ExceptionWhen.InnerException when(boolean condition) {
        return new E().new ExceptionWhen().new InnerException(BasicException.DEFAULT_ERROR_CODE, BasicException.DEFAULT_MESSAGE, condition);
    }

    public class ExceptionWhen {

        private String code;

        private String message;

        public ExceptionWhen() {
            super();
        }

        public ExceptionWhen(String code, String message) {
            super();
            this.code = code;
            this.message = message;
        }

        /**
         * 抛出异常的条件
         * @param condition 判断条件
         * @return the inner exception
         */
        public InnerException when(boolean condition) {
            return new InnerException(code, message, condition);
        }

        public class InnerException {

            private String code;

            private String message;

            private boolean condition;

            public InnerException(String code, String message, boolean condition) {
                super();
                this.code = code;
                this.message = message;
                this.condition = condition;
            }
            
            public void exception(Class<? extends BasicException> c) {
                if (condition) {
                    Object o = null;
                    String canonicalName = c.getCanonicalName();
                    try {
                        o = Class.forName(canonicalName).getConstructor(String.class, String.class).newInstance(code, message);
                    } catch (Exception e) {
                        // 异常时获取父类
                        Class<? extends BasicException> superclass = null;
                        try {
                            superclass = (Class<? extends BasicException>) Class.forName(canonicalName).getSuperclass();
                            exception(superclass);
                        } catch (Exception exception) {
                            if (exception.getClass().equals(superclass)) {
                                throw (BasicException)exception;
                            }
                            throw BasicException.of(code, message);
                        }
                    }
                    if (o instanceof BasicException) {
                        throw (BasicException) o;
                    }
                }
            }

        }

    }

}
