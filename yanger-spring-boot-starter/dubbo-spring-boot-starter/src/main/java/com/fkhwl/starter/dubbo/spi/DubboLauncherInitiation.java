package com.fkhwl.starter.dubbo.spi;

import com.yanger.starter.basic.annotation.AutoService;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.basic.spi.LauncherInitiation;
import com.yanger.tools.web.support.ChainMap;
import com.yanger.tools.web.tools.NetUtils;

import org.apache.dubbo.metadata.integration.MetadataReportService;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 通过 SPI 加载 dubbo 默认配置</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.11.20 09:29
 * @since 1.0.0
 */
@Slf4j
@AutoService(LauncherInitiation.class)
public class DubboLauncherInitiation implements LauncherInitiation {

    /**
     * 加载默认配置
     *
     * @param env               系统变量 Environment
     * @param appName           服务名
     * @return the chain map
     */
    @Override
    @SuppressWarnings("PMD.RemoveCommentedCodeRule")
    public Map<String, Object> launcher(Environment env, String appName) {
        ChainMap chainMap = ChainMap.build(2);

        String localIp = NetUtils.getLocalHost();
        log.info("自动设置 [dubbo.protocol.host = {}] (默认使用优先级最高的网卡), "
                 + "避免注册成本地虚拟机网卡地址导致 dubbo 服务调用失败", localIp);
        chainMap.put(ConfigKey.DubboConfigKey.DUBBO_HOST, localIp);

        // dubbo 服务全部注册到 nacos
        chainMap.put(ConfigKey.DubboConfigKey.REGISTRY_ADDRESS, "spring-cloud://localhost");
        // 通信协议默认 dubbo
        chainMap.put(ConfigKey.DubboConfigKey.PROTOCOL_NAME, "dubbo");
        // 默认不检查 consumer
        chainMap.put(ConfigKey.DubboConfigKey.CONSUMER_CHECK, "false");
        // 设置默认的超时时间
        chainMap.put(ConfigKey.DubboConfigKey.PROVIDER_TIMEOUT, 10000);
        // chainMap.put(ConfigKey.DubboConfigKey.APPLICATION_LOGGER, "log4j2");
        // 开启Consumer参数校验
        chainMap.put(ConfigKey.DubboConfigKey.CONSUMER_VALIDATION, "true");

        // 将 dubbo 的 metadata 数据写入到对应的 namespace, 而不是全部写入到 public 中
        // [应用启动一次就会将全部的元数据写入 his_config_info, 会造成 The table 'his_config_info' is full]
        // if (!isLocalLaunch) {
            chainMap.put("dubbo.metadata-report.group", "DUBBO_METADATA");
            // TODO nacos地址和命名空间
            // chainMap.put(ConfigKey.DubboConfigKey.METADATA_REPORT_ADDRESS, "nacos://" + ConfigDefaultValue.NACOS_SERVER + "?namespace=" + App.FKH_NAME_SPACE);
            chainMap.put(ConfigKey.DubboConfigKey.METADATA_REPORT_ADDRESS, "");
        // }

        chainMap.put(ConfigKey.DubboConfigKey.DUBBO_CONSUMER_FILTER,
                     "crossJvmParameterPassingFilter");
        chainMap.put(ConfigKey.DubboConfigKey.DUBBO_PROVIDER_FILTER,
                     "-exception,dubboExceptionFilter,crossJvmParameterPassingFilter");
        // 如果存在 START_FKH_APPLICATION 环境变量, 则表示使用了 fkh-launcher 依赖
        Object port = 20880;
        // TODO 端口问题
        // if (StringUtils.isNotBlank(System.getProperty(App.START_FKH_APPLICATION)) && !ConfigKit.isStartedByJunit()) {
        //     port = "${range.random.int(28000, 28200)}";
        // }
        chainMap.put(ConfigKey.DubboConfigKey.PROTOCOL_PORT, port);
        // 设置随机端口
        return chainMap;
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
        return "fkh-starter-dubbo";
    }

}
