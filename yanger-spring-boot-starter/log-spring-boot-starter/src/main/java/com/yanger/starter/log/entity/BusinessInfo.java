package com.yanger.starter.log.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 请求信息
 * @Author yanger
 * @Date 2021/3/10 15:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessInfo {

    /** 全限定类名 */
    private String className;

    /** 简单类名 */
    private String classSimpleName;

    /** 方法名 */
    private String methodName;

    /** BusinessLog 的注解描述 */
    private String description;

    /** 访问uri */
    private String uri;

}
