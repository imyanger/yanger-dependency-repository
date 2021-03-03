package com.yanger.starter.test.dao;

import com.yanger.starter.mybatis.base.BaseDao;
import com.yanger.starter.test.po.User;

import org.apache.ibatis.annotations.Mapper;

// @Mapper
public interface UserDao extends BaseDao<User> {

    User getDelete();

}
