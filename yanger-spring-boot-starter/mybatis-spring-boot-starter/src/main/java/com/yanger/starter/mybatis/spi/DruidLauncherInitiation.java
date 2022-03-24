package com.yanger.starter.mybatis.spi;

import com.yanger.starter.basic.annotation.AutoService;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.basic.spi.LauncherInitiation;
import com.yanger.tools.web.support.ChainMap;
import com.yanger.tools.web.tools.NetUtils;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * 通过 SPI 加载 druid 默认配置
 * @Author yanger
 * @Date 2021/1/29 9:40
 */
@AutoService(LauncherInitiation.class)
public class DruidLauncherInitiation implements LauncherInitiation {

    /**
     * 加载默认配置
     * @param env               系统变量 Environment
     * @param appName           服务名
     * @return the chain map
     */
    @Override
    public Map<String, Object> launcher(Environment env, String appName) {
        return ChainMap.build(36)
            .put(ConfigKey.DruidConfigKey.DRIVER_CLASS, "com.mysql.cj.jdbc.Driver")
            .put(ConfigKey.DruidConfigKey.TYPE, "com.alibaba.druid.pool.DruidDataSource")
            .put(ConfigKey.DruidConfigKey.INITIAL_SIZE, 5)
            .put(ConfigKey.DruidConfigKey.MIN_IDLE, 5)
            .put(ConfigKey.DruidConfigKey.MAX_ACTIVE, 20)
            // 配置获取连接等待超时的时间
            .put(ConfigKey.DruidConfigKey.MAX_WAIT, 60000)
            // 配置间隔多久才进行一次检测,检测需要关闭的空闲连接,单位是毫秒
            .put(ConfigKey.DruidConfigKey.TIME_BETWEEN_EVICTION_RUNS_MILLIS, 60000)
            // 配置一个连接在池中最小生存的时间,单位是毫秒
            .put(ConfigKey.DruidConfigKey.MIN_EVICTABLE_IDLE_TIME_MILLIS, 300000)
            // 连接可用性检查，官方存在bug
            .put(ConfigKey.DruidConfigKey.TEST_WHILE_IDLE, Boolean.FALSE)
            .put(ConfigKey.DruidConfigKey.TEST_ON_BORROW, Boolean.FALSE)
            .put(ConfigKey.DruidConfigKey.TEST_ON_RETURN, Boolean.FALSE)
            // 打开PSCache,并且指定每个连接上PSCache的大小
            .put(ConfigKey.DruidConfigKey.POOL_PREPARED_STATEMENTS, Boolean.TRUE)
            .put(ConfigKey.DruidConfigKey.MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE, 20)
            // 配置监控统计拦截的filters,去掉后监控界面sql无法统计,'wall'用于防火墙
            .put(ConfigKey.DruidConfigKey.FILTERS, "stat,wall,slf4j")
            // 通过 connectProperties 属性来打开mergeSql功能, 慢SQL记录
            .put(ConfigKey.DruidConfigKey.CONNECTION_PROPERTIES, "druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000")
            .put(ConfigKey.DruidConfigKey.USE_GLOBAL_DATA_SOURCE_STAT, Boolean.TRUE)
            .put(ConfigKey.DruidConfigKey.WEB_FILTER, Boolean.TRUE)
            .put(ConfigKey.DruidConfigKey.WEB_FILTER_URL_PATTERN, "/*")
            .put(ConfigKey.DruidConfigKey.WEB_FILTER_EXCLUSIONS, "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")
            .put(ConfigKey.DruidConfigKey.STAT, Boolean.TRUE)
            .put(ConfigKey.DruidConfigKey.STAT_URL_PATTERN, "/druid/*")
            .put(ConfigKey.DruidConfigKey.STAT_ALLOW, NetUtils.getLocalHost())
            .put(ConfigKey.DruidConfigKey.STAT_DENY, "")
            // 禁用HTML页面上的“Reset All”功能
            .put(ConfigKey.DruidConfigKey.STAT_RESET, Boolean.FALSE)
            .put(ConfigKey.DruidConfigKey.STAT_USERNAME, "yanger")
            .put(ConfigKey.DruidConfigKey.STAT_PASSWORD, "yanger@druid");
    }

    /**
     * Gets order
     * @return the order
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }

    /**
     * Gets name
     * @return the name
     */
    @Override
    public String getName() {
        return "mybatis-spring-boot-starter:DruidLauncherInitiation";
    }

}
