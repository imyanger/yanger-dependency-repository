package com.yanger.starter.basic.constant;

/**
 * @Description Order注解顺序
 * @Author yanger
 * @Date 2021/1/26 19:18
 */
public interface OrderConstant {

    /** StarterInfoRunner */
    int ORDER_STARTER_INFO_RUNNER = 999;

    /** SwaggerInfoRunner */
    int ORDER_SWAGGER_INFO_RUNNER = ORDER_STARTER_INFO_RUNNER + 1;

    int getOrder();

}
