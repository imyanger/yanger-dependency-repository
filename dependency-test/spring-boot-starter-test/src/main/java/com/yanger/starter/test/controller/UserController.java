package com.yanger.starter.test.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanger.starter.basic.entity.LQuery;
import com.yanger.starter.log.annotation.BusinessLog;
import com.yanger.starter.test.dao.UserDao;
import com.yanger.starter.test.dto.UserDTO;
import com.yanger.starter.test.po.User;
import com.yanger.starter.test.service.UserService;
import com.yanger.starter.web.annotation.IgnoreLoginAuth;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * User的接口Controller类
 * @Author yanger
 * @Date 2021-01-08 16:58:04
 */
@Slf4j
@Api(tags={"UserController接口"})
@RestController
@RequestMapping("user")
@IgnoreLoginAuth
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserDao userDao;

    /**
     * 根据id查找user
     * @Author yanger
     * @date 2021-01-08 16:58:04
     * @return 心情数据
     */
    @GetMapping("{id}")
    @BusinessLog(description = "根据id查找user")
    @ApiOperation(value="根据id查找user", tags={"UserController接口"}, notes="根据id查找user")
    public User find(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        User user = userDao.selectById(id);
        User del = userDao.getDelete();
        log.info("del user {}", del);
        long exeTimes = System.currentTimeMillis() - startTime;
        User byId = userService.getById(id);
        log.info("find耗时{}", exeTimes);
        return byId;
    }

    /**
     * 分页
     * @Author yanger
     * @date 2021-01-08 16:58:04
     * @return 心情数据
     */
    @GetMapping("page")
    @ApiOperation(value="分页", tags={"UserController接口"}, notes="分页")
    public IPage<UserDTO> page(User user, LQuery query) {
        IPage<User> page = userService.page(query);
        IPage<UserDTO> page2 = userService.page(query, UserDTO.class);
        IPage<User> page3 = userService.page(1, 10);
        IPage<UserDTO> page4 = userService.page(1, 10, UserDTO.class);
        return page4;
    }

    /**
     * 保存user
     * @Author yanger
     * @date 2021-01-08 18:02:38
     * @return void
     */
    @IgnoreLoginAuth
    @PostMapping
    @ApiOperation(value="保存user", tags={"UserController接口"}, notes="保存user")
    public User save(@RequestBody @Validated User user) {
        // userService.save(user);
        userDao.insert(user);
        return userService.getById(user.getId());
    }

    /**
     * 替换保存user
     * @Author yanger
     * @date 2021-01-08 18:02:38
     * @return void
     */
    @PostMapping("replace")
    @ApiOperation(value="替换保存user", tags={"UserController接口"}, notes="替换保存user")
    public void saveReplace(@RequestBody User user) {
        userService.saveReplace(user);
    }

    /**
     * 重复保存user
     * @Author yanger
     * @date 2021-01-08 18:02:38
     * @return void
     */
    @PostMapping("ignore")
    @ApiOperation(value="重复保存user", tags={"UserController接口"}, notes="重复保存user")
    public void saveIgnore(@RequestBody User user) {
        userService.saveIgnore(user);
    }

    /**
     * 强制使用id保存user
     * @Author yanger
     * @date 2021-01-08 18:02:38
     * @return void
     */
    @PostMapping("id")
    @ApiOperation(value="强制使用id保存user", tags={"UserController接口"}, notes="强制使用id保存user")
    public void insertUseId(@RequestBody User user) {
        userService.insertUseId(user);
    }

    /**
     * 删除user
     * @Author yanger
     * @date 2021-01-08 18:02:38
     * @return void
     */
    @DeleteMapping("{id}")
    @ApiOperation(value="删除user", tags={"UserController接口"}, notes="删除user")
    public void save(@PathVariable Long id) {
        userService.removeById(id);
    }

    /**
     * 更新user
     * @Author yanger
     * @date 2021-01-08 18:02:38
     * @return void
     */
    @PutMapping
    @ApiOperation(value="更新user", tags={"UserController接口"}, notes="更新user")
    public void update(@RequestBody User user) {
        userService.updateById(user);
    }

}