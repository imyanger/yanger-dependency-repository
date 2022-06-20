package com.yanger.starter.web.api;

import com.yanger.starter.basic.constant.EndpointConst;
import com.yanger.starter.web.context.SerializeEnumContainer;
import com.yanger.starter.web.entity.SerializeEnumData;
import com.yanger.starter.web.property.EnumProperties;
import com.yanger.tools.web.entity.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author yanger
 * @Date 2022/3/22/022 23:53
 */
@Api(tags={"EnumApi：SerializeEnum 枚举数据获取接口"})
@RestController
public class EnumApi {

    @Autowired
    private EnumProperties enumProperties;

    @GetMapping(value = EndpointConst.GET_SERIALIZE_ENUM_CONTAINER_URL)
    @ApiOperation(value="获取 SerializeEnum 枚举数据获取接口", tags={"EnumApi：SerializeEnum 枚举数据获取接口"}, notes="获取 SerializeEnum 枚举数据获取接口")
    public R<Map<String, List<SerializeEnumData>>> getEnums() {
        if (enumProperties.getEnable()) {
            return R.succeed(SerializeEnumContainer.context());
        } else {
            return R.failedDefault("未开启 SerializeEnum Api");
        }
    }

}
