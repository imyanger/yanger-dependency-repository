package com.yanger.tools.web.entity;

import com.yanger.tools.web.exception.BasicException;
import com.yanger.tools.web.support.IResultCode;

/**
 * 异常调用类
 * @Author yanger
 * @Date 2020/12/21 18:23
 */
public class E {

    /**
     * 抛出异常的条件
     * @param condition 判断条件
     * @return the inner exception
     */
    public static WhenException when(boolean condition) {
        return new E().new WhenException(condition);
    }

    public class WhenException {

        private boolean condition;

        public WhenException(boolean condition) {
            super();
            this.condition = condition;
        }

        /**
         * 抛出的异常， 不指定默认抛出 BasicException
         * @return the inner exception
         */
        public ThrowException throwExp() {
            return new ThrowException(condition, BasicException.class);
        }

        /**
         * 抛出的异常
         * @return the inner exception
         */
        public ThrowException throwExp(Class<? extends BasicException> clz) {
            return new ThrowException(condition, clz);
        }

        public class ThrowException {

            private boolean condition;
            private Class<? extends BasicException> clz;

            public ThrowException(boolean condition, Class<? extends BasicException> clz) {
                super();
                this.condition = condition;
                this.clz = clz;
            }

            /**
             * 设置异常信息
             * @param message 报错信息
             * @return the exception when
             */
            public MessageException message(String message) {
                return new MessageException(condition, clz, BasicException.DEFAULT_ERROR_CODE, message);
            }

            /**
             * 设置异常信息
             * @param message 报错信息
             * @return the exception when
             */
            public MessageException message(String message, Object... args) {
                return new MessageException(condition, clz, BasicException.DEFAULT_ERROR_CODE, message, args);
            }

            /**
             * 设置异常信息
             * @param code 错误码
             * @param message 报错信息
             * @return the exception when
             */
            public MessageException message(Integer code, String message) {
                return new MessageException(condition, clz, code, message);
            }

            /**
             * 设置异常信息
             * @param code 错误码
             * @param message 报错信息
             * @return the exception when
             */
            public MessageException message(Integer code, String message, Object... args) {
                return new MessageException(condition, clz, code, message, args);
            }

            /**
             * 设置异常信息
             * @param resultCode resultCode 对象
             * @return the exception when
             */
            public MessageException message(IResultCode resultCode) {
                return new MessageException(condition, clz, resultCode);
            }

            /**
             * 设置异常信息
             * @param resultCode resultCode 对象
             * @param args 报错信息
             * @return the exception when
             */
            public MessageException message(IResultCode resultCode, Object... args) {
                return new MessageException(condition, clz, resultCode, args);
            }

            public class MessageException {

                private boolean condition;
                private Integer code;
                private String message;
                private IResultCode resultCode;

                public MessageException(boolean condition, Class<? extends BasicException> clz, Integer code, String message) {
                    super();
                    this.condition = condition;
                    this.code = code;
                    this.message = message;
                    exception(clz, null);
                }

                public MessageException(boolean condition, Class<? extends BasicException> clz, Integer code, String message, Object... args) {
                    super();
                    this.condition = condition;
                    this.code = code;
                    this.message = message;
                    exception(clz, args);
                }

                public MessageException(boolean condition, Class<? extends BasicException> clz, IResultCode resultCode) {
                    super();
                    this.condition = condition;
                    this.resultCode = resultCode;
                    exception(clz, null);
                }

                public MessageException(boolean condition, Class<? extends BasicException> clz, IResultCode resultCode, Object... args) {
                    super();
                    this.condition = condition;
                    this.resultCode = resultCode;
                    exception(clz, args);
                }

                /**
                 * 抛出异常
                 * @param c 异常类
                 * @param args 异常信息参数
                 * @return
                 * @Author yanger
                 * @Date 2022/03/21 12:41
                 */
                public void exception(Class<? extends BasicException> c, Object... args) {
                    if (condition) {
                        Throwable throwable = null;
                        String exceptionFrom = "";
                        try{
                            if(args != null && args.length > 0) {
                                if(resultCode != null) {
                                    throwable = (Throwable) Class.forName(c.getCanonicalName()).getConstructor(IResultCode.class, Object[].class).newInstance(resultCode, args);
                                } else {
                                    throwable = (Throwable) Class.forName(c.getCanonicalName()).getConstructor(Integer.class, String.class, Object[].class).newInstance(code, message, args);
                                }
                            } else {
                                if(resultCode != null) {
                                    throwable = (Throwable) Class.forName(c.getCanonicalName()).getConstructor(IResultCode.class).newInstance(resultCode);
                                } else {
                                    throwable = (Throwable) Class.forName(c.getCanonicalName()).getConstructor(Integer.class, String.class).newInstance(code, message);
                                }
                            }
                        }catch (Exception e) {
                            try {
                                throwable = c.newInstance();
                            } catch (Exception exception) {
                                // ignore empty param constructor error
                            }
                        }
                        if(resultCode != null) {
                            throw new BasicException(throwable, resultCode, args);
                        } else {
                            throw new BasicException(throwable, code, message, args);
                        }
                    }
                }
            }
        }
    }

}
