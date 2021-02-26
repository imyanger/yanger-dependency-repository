package com.yanger.starter.test.po;

import com.yanger.starter.mybatis.annotation.SensitiveField;
import com.yanger.starter.mybatis.entity.BaseTimeLogicPO;
import com.yanger.starter.test.enums.FileType;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * @Description TODO
 * @Author yanger
 * @Date 2021/2/3 16:54
 */
@Data
public class User extends BaseTimeLogicPO<Long, User> {

    @NotEmpty(message = "用户名不能为空")
    private String userName;

    private Integer age;

    private FileType fileType;

    @SensitiveField
    private String mobile;

}
