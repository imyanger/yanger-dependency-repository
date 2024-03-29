package com.yanger.test.controller;

import com.yanger.test.module.TestEnum;
import com.yanger.test.module.TestVo;
import com.yanger.test.result.MyResultCode;
import com.yanger.tools.web.entity.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author yanger
 * @Date 2022/3/17/017 22:31
 */
@RestController
@RequestMapping("result")
public class RController {

    @GetMapping("message")
    public R<String> message(){
        return R.succeedDefault("test ok");
    }

    @GetMapping("vo")
    public R<TestVo> vo(){
        return R.succeed(TestVo.builder().name("test").age(12).build());
    }

    @GetMapping("resultCode")
    public R<TestVo> resultCode(){
        return R.failed(MyResultCode.PARAMETER_ERROR_PERCH, "请参数数字");
    }

    @GetMapping("enum")
    public R<TestVo> enumTest(TestEnum testEnum){
        System.out.println(testEnum);
        return R.succeed(TestVo.builder().name("test").age(12).testEnum(TestEnum.A).build());
    }

}
