package com.fkhwl.starter.mybatis.spi;

import com.fkhwl.starter.basic.constant.ConfigDefaultValue;
import com.fkhwl.starter.basic.constant.ConfigKey;
import com.fkhwl.starter.common.start.LauncherInitiation;
import com.fkhwl.starter.common.util.ConfigKit;
import com.fkhwl.starter.core.support.ChainMap;
import com.fkhwl.starter.core.util.NetUtils;
import com.fkhwl.starter.processor.annotation.AutoService;

import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 通过 SPI 加载 druid 默认配置</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.27 11:19
 * @since 1.0.0
 */
@AutoService(LauncherInitiation.class)
public class DruidLauncherInitiation implements LauncherInitiation {
    /**
     * Launcher *
     *
     * @param env           env
     * @param appName       app name
     * @param isLocalLaunch is local launch
     * @return the map
     * @since 1.0.0
     */
    @Override
    public Map<String, Object> launcher(Environment env,
                                        String appName,
                                        boolean isLocalLaunch) {
        if (ConfigKit.isStartedByJunit()) {
            return ChainMap.build(0);
        }
        return ChainMap.build(36)
            .put(ConfigKey.DruidConfigKey.DRIVER_CLASS, "com.mysql.cj.jdbc.Driver")
            .put(ConfigKey.DruidConfigKey.TYPE, "com.alibaba.druid.pool.DruidDataSource")
            .put(ConfigKey.DruidConfigKey.INITIALSIZE, 5)
            .put(ConfigKey.DruidConfigKey.MINIDLE, 5)
            .put(ConfigKey.DruidConfigKey.MAXACTIVE, 20)
            // 配置获取连接等待超时的时间
            .put(ConfigKey.DruidConfigKey.MAXWAIT, 60000)
            // 配置间隔多久才进行一次检测,检测需要关闭的空闲连接,单位是毫秒
            .put(ConfigKey.DruidConfigKey.TIMEBETWEENEVICTIONRUNSMILLIS, 60000)
            // 配置一个连接在池中最小生存的时间,单位是毫秒
            .put(ConfigKey.DruidConfigKey.MINEVICTABLEIDLETIMEMILLIS, 300000)
            // todo-dong4j : (2020.10.15 11:53) [待官方修复后开启连接可用性检查]
            .put(ConfigKey.DruidConfigKey.TESTWHILEIDLE, ConfigDefaultValue.FALSE)
            .put(ConfigKey.DruidConfigKey.TESTONBORROW, ConfigDefaultValue.FALSE)
            .put(ConfigKey.DruidConfigKey.TESTONRETURN, ConfigDefaultValue.FALSE)
            // 打开PSCache,并且指定每个连接上PSCache的大小
            .put(ConfigKey.DruidConfigKey.POOLPREPAREDSTATEMENTS, ConfigDefaultValue.TRUE)
            .put(ConfigKey.DruidConfigKey.MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE, 20)
            // 配置监控统计拦截的filters,去掉后监控界面sql无法统计,'wall'用于防火墙
            .put(ConfigKey.DruidConfigKey.FILTERS, "stat,wall,slf4j")
            // 通过 connectProperties 属性来打开mergeSql功能, 慢SQL记录
            .put(ConfigKey.DruidConfigKey.CONNECTIONPROPERTIES, "druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000")
            .put(ConfigKey.DruidConfigKey.USUEGLOBALDATASOURCESTAT, ConfigDefaultValue.TRUE)
            .put(ConfigKey.DruidConfigKey.WEB_FILTER, ConfigDefaultValue.TRUE)
            .put(ConfigKey.DruidConfigKey.WEB_FILTER_URL_PATTERN, "/*")
            .put(ConfigKey.DruidConfigKey.WEB_FILTER_EXCLUSIONS, "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")
            .put(ConfigKey.DruidConfigKey.STAT, ConfigDefaultValue.TRUE)
            .put(ConfigKey.DruidConfigKey.STAT_URL_PATTERN, "/druid/*")
            .put(ConfigKey.DruidConfigKey.STAT_ALLOW, NetUtils.getLocalHost())
            .put(ConfigKey.DruidConfigKey.STAT_DENY, "")
            // 禁用HTML页面上的“Reset All”功能
            .put(ConfigKey.DruidConfigKey.STAT_RESET, ConfigDefaultValue.FALSE)
            .put(ConfigKey.DruidConfigKey.STAT_USERNAME, "fkh")
            .put(ConfigKey.DruidConfigKey.STAT_PASSWORD, "fkh@0819");

    }

    /**
     * Gets order *
     *
     * @return the order
     * @since 1.0.0
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 200;
    }

    /**
     * Gets name *
     *
     * @return the name
     * @since 1.0.0
     */
    @Override
    public String getName() {
        return "fkh-starter-mybatis/druid";
    }
}
