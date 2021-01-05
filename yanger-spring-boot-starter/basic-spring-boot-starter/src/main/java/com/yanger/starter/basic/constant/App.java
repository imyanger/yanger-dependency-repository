package com.yanger.starter.basic.constant;

/**
 * @Description 应用参数
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class App {

    /** 基础包 */
    public static final String BASE_PACKAGES = ConfigDefaultValue.BASE_PACKAGES;

    /** 系统自定义应用类型, 比  {@link org.springframework.boot.WebApplicationType} 多了 SERVICE 类型 */
    public static final String APPLICATION_TYPE = "APPLICATION_TYPE";

    /** SERVICE_VERSION */
    public static final String SERVICE_VERSION = "yanger.boot.service.version";

    /** 启动标识, 所有项目只能使用 BasicApplication 启动 */
    public static final String YANGER_BASIC_APPLICATION_STARTER = "yanger_basic_application_starter";

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

    /** 单元测试启动 */
    public static final String START_JUNIT = "junit";

    /** 依赖的 library */
    public static final String LIBRARY_NAME = "used.librarys";

    /** 应用类型 {@link org.springframework.boot.WebApplicationType} */
    public static final String APP_TYPE = "app.type";

}
