package com.yanger.starter.web.property;

import com.yanger.starter.basic.constant.ConfigDefaultValue;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author YangHao
 * jwt 常量类
 * @date 2018年9月23日-下午4:28:57
 */
@Data
@Component
@ConfigurationProperties(prefix = EnumProperties.PREFIX)
public class EnumProperties {

    /** PREFIX */
    public static final String PREFIX = "yanger.serialize-enum";

    /** 是否开启 SerializeEnumContainer */
    private Boolean enable = ConfigDefaultValue.EnumConfigValue.ENABLE;

}
