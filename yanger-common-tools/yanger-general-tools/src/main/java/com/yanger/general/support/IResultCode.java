package com.yanger.general.support;

import com.yanger.general.constant.StringPool;
import com.yanger.general.format.StringFormatter;
import com.yanger.general.tools.StringTools;

import java.io.*;

/**
 * @Description 请求响应代码接口
 * @Author yanger
 * @Date 2020/12/18 11:31
 */
public interface IResultCode extends Serializable {

    /**
     * 获取返回消息, 可使用占位符
     *
     * @return String message
     */
    String getMessage();

    /**
     * 获取返回状态码
     *
     * @return String code
     */
    Integer getCode();

    /**
     * Name
     *
     * @return the string
     */
    default String serverName() {
        return "BASIC";
    }

    /**
     * 根据错误码生成一定规则的错误编码【项目标识-错误码】
     *
     * @return the string
     */
    default String generateCode() {
        String serverName = serverName();
        return serverName.concat(StringPool.DASH) + getCode();
    }

    /**
     * 根据错误码生成一定规则的错误编码【项目标识-错误码】
     *
     * @param customerMessage 自定义的返回消息
     * @return the string
     */
    default String generateMessage(Object... customerMessage) {
        String message = getMessage();
        if (customerMessage == null) {
            return message;
        }
        Object first = customerMessage[0];
        if (StringTools.contains(message, StringPool.EMPTY_JSON)) {
            return StringFormatter.format(message, customerMessage);
        } else if (first != null && StringTools.contains(first.toString(), StringPool.EMPTY_JSON)) {
            Object[] params = new String[customerMessage.length -1 ];
            System.arraycopy(customerMessage, 1, params, 0, customerMessage.length -1);
            return StringFormatter.format(first.toString(), params);
        }
        return first == null || StringTools.isBlank(first.toString()) ? message : first.toString();
    }

}
