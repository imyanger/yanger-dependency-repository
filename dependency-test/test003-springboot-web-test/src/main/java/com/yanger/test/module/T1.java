package com.yanger.test.module;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yanger.starter.mybatis.entity.BaseLogicPO;
import com.yanger.starter.mybatis.entity.BasePO;
import com.yanger.starter.mybatis.entity.BaseTimeLogicPO;
import com.yanger.starter.mybatis.entity.BaseTimePO;
import lombok.Data;

import java.util.Date;

/**
 * @Author yanger
 * @Date 2022/3/25/025 17:28
 */
@Data
@TableName("t1")
public class T1  extends BaseTimeLogicPO<Long, T1> {

    //private Long id;

    private String name;

    private Integer age;

//    private Date createTime;

//    private Date updateTime;

//    private int deleted;

    private TestEnum enumVal;

}
