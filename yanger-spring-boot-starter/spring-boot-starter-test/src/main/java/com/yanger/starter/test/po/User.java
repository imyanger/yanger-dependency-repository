package com.yanger.starter.test.po;

import com.yanger.starter.mybatis.entity.BaseLogicPO;
import com.yanger.starter.mybatis.entity.BasePO;
import com.yanger.starter.mybatis.entity.BaseTimeLogicPO;
import com.yanger.starter.mybatis.entity.BaseTimePO;
import com.yanger.starter.test.enums.FileType;

import lombok.Data;

/**
 * @Description TODO
 * @Author yanger
 * @Date 2021/2/3 16:54
 */
@Data
public class User extends BaseTimeLogicPO<Long, User> {

    private String userName;

    private Integer age;

    private FileType fileType;

}
