package com.yanger.starter.web.annotation;

import java.lang.annotation.*;

/**
 * 表格数据抽象
 * @Author yanger
 * @Date 2021/1/27 18:52
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormDataBody {
}
