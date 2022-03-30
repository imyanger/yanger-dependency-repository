package com.yanger.starter.mybatis.property;

import lombok.Data;

/**
 * @Author yanger
 * @Date 2022/3/25/025 18:21
 */
@Data
public class DataSourceDetail {

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    private String effectPackage;

    private Boolean defaultEnable = false;

}
