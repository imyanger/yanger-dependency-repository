package com.yanger.test.dao;

import com.yanger.starter.mybatis.base.BaseDao;
import com.yanger.test.module.T1;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author yanger
 * @Date 2022/3/25/025 17:25
 */
@Mapper
public interface DataDao extends BaseDao<T1> {
}
