package com.yanger.test.controller;

import com.yanger.test.module.T1;
import com.yanger.test.service.DataService;
import com.yanger.tools.web.entity.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author yanger
 * @Date 2022/3/25/025 17:24
 */
@RestController
public class DataApi {

    @Autowired
    private DataService dataService;

    @GetMapping("dao")
    public R<T1> dao(){
        return R.succeed(dataService.save());
    }

    @GetMapping("mapper")
    public R<T1> mapper(){
        return R.succeed(dataService.saveMapper());
    }

    @GetMapping("del/{id}")
    public R<Void> del(@PathVariable Integer id){
        dataService.del(id);
        return R.succeed();
    }

}
