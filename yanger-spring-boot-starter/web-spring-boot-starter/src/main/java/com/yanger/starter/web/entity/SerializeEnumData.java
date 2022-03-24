package com.yanger.starter.web.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author yanger
 * @Date 2022/3/22/022 21:49
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "SerializeEnum 单个枚举数据")
public class SerializeEnumData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("枚举值")
    private Object value;

    @ApiModelProperty("枚举描述")
    private String desc;

    @ApiModelProperty("枚举名")
    private String name;

    @ApiModelProperty("枚举顺序")
    private Integer ordinal;

}
