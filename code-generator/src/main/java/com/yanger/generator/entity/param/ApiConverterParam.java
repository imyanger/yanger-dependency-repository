package com.yanger.generator.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @Description Api转换工具类参数
 * @Author yanger
 * @Date 2020/8/26 16:48
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ApiConverterParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 是否需要导入转换工具类 */
    private boolean needImprotUtil;

    /** 保存或者更新转换 */
    private String saveOrUpdateCp;

    /** 集合存储转换 */
    private String saveListCp;

    /** 返回对象转换 */
    private String returnCp;

    /** 查询对象转换 */
    private String queryCp;

    /** 返回集合转换 */
    private String returnListCp;

    /** 返回分页转换 */
    private String returnPageCp;

}