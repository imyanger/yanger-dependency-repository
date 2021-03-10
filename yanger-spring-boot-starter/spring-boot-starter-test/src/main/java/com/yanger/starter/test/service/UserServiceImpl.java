package com.yanger.starter.test.service;

import com.yanger.starter.mybatis.base.BaseServiceImpl;
import com.yanger.starter.test.dao.UserDao;
import com.yanger.starter.test.po.User;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserDao, User> implements UserService {

}
