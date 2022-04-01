package com.yanger.tools.web.support;

import com.yanger.tools.general.constant.StringPool;
import com.yanger.tools.general.format.StringFormat;
import com.yanger.tools.general.tools.StringTools;
import com.yanger.tools.web.context.Trace;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 请求响应代码接口
 * @Author yanger
 * @Date 2020/12/18 11:31
 */
public interface IResultCode extends Serializable {

    String DEFAULT_SERVER_NAME = "BASIC";

    /**
     * 获取返回状态码
     * @return String code
     */
    Integer getCode();

    /**
     * 获取返回消息, 可使用占位符
     * @return String message
     */
    String getMessage();

    /**
     * Name
     * @return the string
     */
    default String getModuleMarker() {
        return DEFAULT_SERVER_NAME;
    }

    /**
     * 根据错误码生成一定规则的错误编码【项目标识-错误码】
     * @return the string
     */
    default String generateCode() {
        String moduleMarker = getModuleMarker();
        return StringTools.isNotBlank(moduleMarker) ? moduleMarker.concat(StringPool.DASH) + getCode() : getCode().toString();
    }

    /**
     * 根据错误码生成一定规则的错误编码【项目标识-错误码】
     * @param customerMessage 自定义的返回消息
     * @return the string
     */
    default String generateMessage(Object... customerMessage) {
        String message = getMessage();
        if (customerMessage == null || customerMessage.length == 0) {
            return message;
        }
        Object first = customerMessage[0];
        if (StringUtils.isNotBlank(message)) {
            if (StringTools.contains(message, StringPool.EMPTY_JSON)) {
                return StringFormat.format(message, customerMessage);
            } else if (first != null && StringUtils.isNotBlank(first.toString())) {
                if (StringTools.contains(first.toString(), StringPool.EMPTY_JSON)) {
                    Object[] params = new String[customerMessage.length - 1];
                    System.arraycopy(customerMessage, 1, params, 0, customerMessage.length - 1);
                    return (message.endsWith("，") ? message : message + "，") + StringFormat.format(first.toString(), params);
                } else {
                    return (message.endsWith("，") ? message : message + "，") + first.toString();
                }
            }
            return message;
        } else if (first != null) {
            if (StringTools.contains(first.toString(), StringPool.EMPTY_JSON)) {
                Object[] params = new String[customerMessage.length - 1];
                System.arraycopy(customerMessage, 1, params, 0, customerMessage.length - 1);
                return StringFormat.format(first.toString(), params);
            }
            return first.toString();
        }
        return null;
    }

    default String generateFullMessage(String moduleMarker, Integer code, String message, Object... args){
        String fullMessage = StringFormat.format(message, args);
        String traceId = Trace.context().get();
        return StringTools.builder()
                .append(StringTools.isBlank(traceId) ? "" : traceId.concat(","))
                .append(StringTools.isBlank(moduleMarker) ? "" : moduleMarker.concat(StringPool.DASH))
                .append(code == null ? "" : code + ", ")
                .append(fullMessage)
                .toString();
    }

}
