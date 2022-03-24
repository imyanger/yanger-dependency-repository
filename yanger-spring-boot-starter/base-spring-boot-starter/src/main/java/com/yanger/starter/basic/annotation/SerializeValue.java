package com.yanger.starter.basic.annotation;

import java.lang.annotation.*;

/**
 * 支持普通枚举类字段, 只用在enum类的字段上
 * <p>当实体类的属性是普通枚举, 且是其中一个字段, 使用该注解来标注枚举类里的那个属性对应字段</p>
 * {@code
 *
 *  TableName("student")
 *  class Student {
 *      private Integer id;
 *      private String name;
 *      private GradeEnum grade;//数据库 grade 字段类型为 int
 *  }
 *
 *  public enum GradeEnum {
 *      PRIMARY(1,"小学"),
 *      SECONDORY("2", "中学"),
 *      HIGH(3, "高中");
 *
 *      @SerializeValue
 *      private final int code; // 数据库实际入库字段
 *      private final String desc;
 *  }
 *
 * }
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SerializeValue {

}
