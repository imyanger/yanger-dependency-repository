package com.yanger.starter.log.dynamic;

import com.yanger.starter.log.config.LogbackProperties;
import com.yanger.tools.web.exception.BasicException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 动态修改日志等级
 * @Author yanger
 * @Date 2021/2/26 14:59
 */
@Api(tags={"LevelApi：更改日志等级接口"})
@Slf4j
@RestController
public class LevelApi {

    @Resource
    private LogbackProperties LogbackProperties;

    @GetMapping(value = "logLevel/{logLevel}")
    @ApiOperation(value="动态修改日志等级", tags={"LevelApi：更改日志等级接口"}, notes="动态修改日志等级，logLevel 参数传入日志级别")
    public void changeLogLevel(@PathVariable("logLevel") String logLevel){
        if(!LogbackProperties.isDynamicLogEnabled()) {
            throw new BasicException("未允许动态修改日志等级，请设置 yanger.logger.dynamic-log-enabled=true 开启");
        }
        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            String dynamicLogPackage = LogbackProperties.getDynamicLogPackage();
            if (StringUtils.isNotBlank(dynamicLogPackage)) {
                String[] packages = dynamicLogPackage.trim().split(",");
                for (String logPackage : packages) {
                    if (StringUtils.isNotBlank(dynamicLogPackage)) {
                        loggerContext.getLogger(logPackage.trim()).setLevel(Level.valueOf(logLevel));
                    }
                }
            } else {
                loggerContext.getLogger("com.yanger").setLevel(Level.valueOf(logLevel));
            }
        } catch (Exception e) {
            log.error("动态修改日志级别出错", e);
            throw new BasicException("动态修改日志级别出错");
        }
    }

}
