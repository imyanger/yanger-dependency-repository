package com.yanger.starter.basic.entity;

import java.io.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * @Description TODO
 * @Author yanger
 * @Date 2021/1/29 9:45
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
public abstract class AbstractBaseEntity<T extends Serializable> implements IBaseEntity<T> {

    private static final long serialVersionUID = -3550589993340031894L;

    /** 实体 Id */
    @ApiModelProperty(value = "实体ID", notes = "新增时可不填,修改时必填")
    private T id;

    @Override
    public T getId() {
        return this.id;
    }

}
