package com.yanger.starter.basic.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 支持普通枚举类字段, 只用在enum类的字段上
 * <p>当实体类的属性是普通枚举, 且是其中一个字段, 使用该注解来标注枚举类里的那个属性对应字段</p>
 * {@code
 *  TableName("student")
 *  class Student {
 *      private Integer id;
 *      private String name;
 *      private GradeEnum grade;//数据库grade字段类型为int
 *  }
 *
 *  public enum GradeEnum {
 *      PRIMARY(1,"小学"),
 *      SECONDORY("2", "中学"),
 *      HIGH(3, "高中");
 *
 *      @SerializeValue
 *      private final int code;
 *      private final String desc;
 *  }
 * }*
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SerializeValue {

}
