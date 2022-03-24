package com.yanger.starter.web.api;

import com.yanger.starter.basic.constant.EndpointConst;
import com.yanger.starter.web.entity.LoginData;
import com.yanger.tools.web.entity.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author yanger
 * @Date 2022/3/23/023 17:39
 */
@Api(tags={"HealthApi：health check 接口"})
@RestController
public class HealthApi {

    @GetMapping(value = EndpointConst.HEALTH_CHECK_URL)
    @ApiOperation(value="health check", tags={"HealthApi：health check 接口"}, notes="health check")
    public R<Void> login(@RequestBody LoginData loginData) {
       return R.succeed();
    }

}
