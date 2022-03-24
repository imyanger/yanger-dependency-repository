package com.yanger.starter.mybatis.config;

import com.alibaba.druid.wall.WallConfig;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.yanger.starter.basic.config.BaseAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid连接池配置
 * @Author yanger
 * @Date 2021/1/28 15:36
 */
@Slf4j
@Configuration
@ConditionalOnClass(MybatisPlusAutoConfiguration.class)
public class DruidAutoConfiguration implements BaseAutoConfiguration {

    @Bean
    public WallConfig wallConfig() {
        WallConfig wallConfig = new WallConfig();
        // 允许一次执行多条语句
        wallConfig.setMultiStatementAllow(true);
        // 允许一次执行多条语句
        wallConfig.setNoneBaseStatementAllow(true);
        return wallConfig;
    }

}
