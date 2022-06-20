## base-spring-boot-starter
base spring starter，其他增强 starter 都扩展于此。
```java
    <dependency>
        <groupId>com.yanger</groupId>
        <artifactId>base-spring-boot-starter</artifactId>
        <version>${base-spring-boot-starter.version}</version>
    </dependency>
```

### 0.BasicApplication
spring boot 启动类，自定义应用继承该类即可，无需再写入main方法。
```java
@SpringBootApplication
public class WebTestApplication extends BasicApplication {

}
```
如果仍有逻辑增强，需要在启动前后进行，可重写 before() 和 after(ApplicationContext context)。
```java
    /**
     * 在 spring 容器启动之前执行自定义逻辑.
     */
    protected void before() {}

    /**
     * 在 spring 容器启动完成后执行自定义逻辑.
     */
    protected void after(ApplicationContext context) {}
```

### 1.BaseAutoConfiguration
基础配置接口类，其他扩展配置都应该实现该接口，以便在加载时识别并打印出来。

### 2.LauncherInitiation
SPI 实现，launcher 扩展，用于一些组件发现。

### 3.constant
##### App
App类包含一些应用相关的常量。
##### ConfigKey && ConfigDefaultValue
在 ConfigKey 内，定义了所有扩展组件可以自定义配置的参数（当然这并不是必须的），但我希望这个可以保持，以便方便查看有哪些配置。
而 ConfigDefaultValue 则是 ConfigKey 配置对应的一些默认值。
##### EndpointConst
应用启动暴露出去的一些端口。
##### FullyQualifiedName
一些类全限定名常量。
##### OrderConstant
加载顺序常量。

### 4.EarlySpringContext
以静态变量保存 Spring ApplicationContext, 可在任何代码任何地方任何时候取出 ApplicaitonContext，以前使用 ApplicationContextAware 接口注入, 但是有时需要在 bean 中使用, 不能确保在使用之前已经将 ApplicaitonContext 注入，可能会出现 null 异常, 因此修改为使用 ApplicationContextInitializer,
 * 会在 ConfigurableApplicationContext 类型（或者子类型）的 ApplicationContext 做 refresh 之前
 * 允许我们对 ConfigurableApplicationContext 的实例做进一步的设置或者处理, 能有效解决上述问题
 * 此类在 Spring Cloud 中会被多次刷新

### 5.annotation
一些扩展注解。
##### AutoService
自动装配标志，该注解无任何实际作用，仅仅用于标识该类可自动装配，用于 LauncherInitiation 接口的实现类。
##### RunningType
Application 启动类上注明启动类型 ApplicationType：
* NONE：非 web 应用, 启动完成后将自动退出
* SERVICE：非 web 应用, 但是启动后不会退出, 比如 dubbo 服务提供者, 只提供 RPC 服务
* SERVLET：web 应用
* REACTIVE：webflux 应用
```java
@RunningType(ApplicationType.REACTIVE)
@SpringBootApplication
public class WebTestApplication extends BasicApplication {

}
```

### 6.util
##### ConfigKit
全局配置工具类, 用于获取整个应用的配置。
##### ConvertUtils
基于 spring ConversionService 类型转换
##### DataTypeUtils
类型转换工具。
##### FileUtils
文件处理工具类。
##### JsonUtils
Jackson工具类。
##### YmlUtils
yaml文件工具，用于获取yaml文件配置。

### 7.JsonProperties
通过配置 yanger.json.date-format 和 yanger.json.time-zone 来设置 json 格式化的格式

### 8.SerializeEnum
SerializeEnum 接口实现类，使其获得可序列化为 json 的功能。

