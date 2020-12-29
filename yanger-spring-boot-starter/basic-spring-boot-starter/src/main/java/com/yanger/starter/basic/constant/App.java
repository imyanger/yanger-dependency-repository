package com.yanger.starter.basic.constant;

import com.yanger.tools.general.constant.StringPool;
import com.yanger.tools.general.tools.OnceLogger;
import com.yanger.tools.general.tools.SystemUtils;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description 应用参数
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class App {

    /** 基础包 */
    public static final String BASE_PACKAGES = ConfigDefaultValue.BASE_PACKAGES;

    /** SERVICE_VERSION */
    public static final String SERVICE_VERSION = "yanger.service.version";

    /** 启动标识, 所有项目只能使用 BasicApplication 启动 */
    public static final String YANGER_BASIC_APPLICATION_STARTER = "yanger_basic_application_starter";

    /** WINDOWS_DEFAULT_USER_NAME */
    private static final String WINDOWS_DEFAULT_USER_NAME = "Administrator";

    /** 启动方式, 用于区分是否通过 server.sh 脚本启动 */
    public static final String START_TYPE = "start.type";

    /** 通过 server.sh 启动的类型, 用于区分本地开发和服务端运行 */
    public static final String START_SHELL = "shell";

    /** server.sh 中的启动参数, 用于获取配置文件路径 */
    public static final String APP_CONFIG_PATH = "config.path";

    /** 本地开发时 idea 启动 */
    public static final String START_IDEA = "idea";

    /** 用于打印 bean 信息 */
    public static final String DEBUG_MODEL = "debug";

    /**
     * 系统自定义应用类型, 比  {@link org.springframework.boot.WebApplicationType}
     * 多了 SERVICE 类型 (com.fkhwl.starter.launcher.enums.ApplicationType)
     */
    public static final String APPLICATION_TYPE = "APPLICATION_TYPE";

    /**
     * 本地开发时, 优先从 JVM 环境变量中读取 user.namespace, 然后是 user.name,
     * 如果 {@link SystemUtils#USER_NAME} 是 {@link App#WINDOWS_DEFAULT_USER_NAME}, 则读取系统环境变量 USER_NAMESPACE).
     * 如果通过 shell 启动, 会自动设置 USER_NAMESPACE 为传入的 env 参数, 最终找到 namespace 对应的的 nacos namespace.
     */
    public static final String FKH_NAME_SPACE;

    static {
        // JVM 参数 user.namespace 优先级最高, 可通过 System.setProperty(SystemUtils.USER_NAMESPACE, "xxxx") 或 JVM 参数设置
        String currentEnv = SystemUtils.USER_NAMESPACE_VALUE;

        // 第二优先级是 FKH_NAME_SPACE
        if (StringUtils.isBlank(currentEnv)) {
            currentEnv = StringUtils.isBlank(SystemUtils.getProperty("FKH_NAME_SPACE"))
                         ? StringPool.EMPTY
                         : SystemUtils.getProperty("FKH_NAME_SPACE");
        }

        // 第三优先级是 user.name, 如果在 windows 未设置计算机名, 此名称将是 Administrator
        currentEnv = StringUtils.isBlank(currentEnv) ? SystemUtils.USER_NAME : currentEnv;

        // 如果是 Administrator 则获取 FKH_NAME_SPACE
        if (WINDOWS_DEFAULT_USER_NAME.equalsIgnoreCase(currentEnv)) {
            OnceLogger.printOnce(App.class.getName(), "请配置环境变量 「FKH_NAME_SPACE」 或修改本地的计算机名. "
                                                      + "如果需要在本地开发时修改 namespace, 请设置 JVM 参数 「user.namespace」"
                                                      + "或直接使用 System.setProperty(SystemUtils.USER_NAMESPACE, \"xxxx\"))");
            currentEnv = "public";
        }

        FKH_NAME_SPACE = currentEnv;
    }


}
