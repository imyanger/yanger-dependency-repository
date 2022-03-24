package com.yanger.starter.basic.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * id字段基础DTO对象
 * @Author yanger
 * @Date 2021/1/29 9:45
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BaseDTO<T extends Serializable> extends AbstractBaseEntity<T> {

    private static final long serialVersionUID = 4484918372546552703L;

}
