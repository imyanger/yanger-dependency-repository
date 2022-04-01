## yanger-tools-web
提供 web 开发一些便捷工具，基于 spring-web 依赖。
pom 引入：
````java
    <dependency>
        <groupId>com.yanger</groupId>
        <artifactId>tools-web</artifactId>
        <version>${yanger-tools.version}</version>
    </dependency>
````
目前 1.0.0.RELEASE 版本，<yanger-tools.version>1.0.0.RELEASE</yanger-tools.version>。


### 1. IResultCode 接口
该接口是一个返回状态码的定义接口，实现该接口，服务可实现一套自己的状态码。而在通用接口返回，异常处理上，都可以使用该接口的实现类进行方便调用。
```java
    default String getModuleMarker() {
        return DEFAULT_SERVER_NAME;
    }
```
moduleMarker 设计寓意为模块标识，期望每个模块（服务）拥有其唯一的标识，方便在接口返回和异常处理上进行区分。

#### DefaultResultCode
IResultCode 的 实现类，定义了一些常用的状态码，如果一个服务需要自己特定的状态码，可按此来定义。
其中状态码 message 字段中允许使用 {} 进行占位。
eg:
```java
    AUTHORITY_INVALID_PERCH(-102, "权限不足，{}"),
```
具体什么权限不足，描述信息我们可以在使用时动态传入。


### 2. 通用泛型返回体 Result
```java
public abstract class Result<T>
```
通用返回体包含字段：
* Integer code 自定义响应码
* String message 响应信息
* T data 数据
* boolean success 是否成功，true表示成功
* String traceId 每次请求生成的唯一标识
* String moduleMarker 模块标识

Result 是一个抽象类，无法被构造，实例化 Result，使用其子类 R，就是 R，多一个字母都不想写。
* bulid 方法，接受全部参数的实例化方法
* 一系列 succeed 方法，boolean success 参数默认为 true 的情况，供成功响应时调用
* 一系列 failed 方法，boolean success 参数默认为 false 的情况，供失败响应时调用
* 一系列 of 方法，主要方便传入 condition 判断

在 R 的构造参数中，可以接受 IResultCode 接口参数。
```java
    R.failed(DefaultResultCode.PARAMETER_ERROR_PERCH, "请传入数字")
    // Result(code=602, message=参数错误，请传入数字, data=null, success=false, traceId=N/A, moduleMarker=BASIC)
``` 
#### Trace
traceId context 全局对象，方便存储和获取 traceId

### 3. E
E 是一个 Exception 处理对象
* when(boolean condition) 定义什么条件下抛出异常，condition 为 true 则抛出异常
* throwExp(Class<? extends BasicException> clz) 抛出异常的类型，需要提供一个 BasicException 类型或者其子类
* message 异常时的展示信息，可搭配 IResultCode 接口使用
```java
    E.when(str == null).throwExp(MyException.class).message(MyResultCode.PARAMETER_ERROR_PERCH, "参数不能为 null");
    // com.yanger.tools.web.exception.BasicException: test-module-602, 参数错误，参数不能为 null
```

#### BasicException
通用异常类， 同时也是 IResultCode 接口实现类，以便方便的与 IResultCode 实现类结合使用。

#### Asserts 与 AssertUtils
Asserts 提供了一些常见需要进行判断可能出现异常的处理情形
AssertUtils 是 Asserts 与 IResultCode 结合的产物，方法参数均接受 IResultCode 对象

#### Exceptions
Exceptions 提供处理 Exception 的通用方法，主要将受检查异常转为 unchecked Exception

#### Unchecked
包装常用的 FunctionalInterface，内部处理掉异常，使其使用不在需要做异常检查


### 4. Tools
web 一些常用工具类

#### AesKit
AES 加密工具，兼容微信、小程序的 AES 加密

#### AopTargetUtils
方便获取目标对象的代理对象，包括 cglib 和 jdk 动态代理

#### Base64Utils
Base64 处理工具类

#### BeanUtils
提供更为灵活的 Bean 处理工具类，支持 FunctionalInterface

#### ClassUtils 与 ReflectionUtils
反射工具类，更便捷的使用反射

#### GPSUtils
GPS 工具类，主要用于世界坐标、国测局坐标、百度坐标间的转换

#### INetUtils 与 NetUtils
Network工具类，提供处理一些网络有关的方法，NetUtils 为最终实现类

#### IoUtils
IO 工具类

#### JwtUtils
Jwt token 生成和解析工具类

#### KaptchaGenerator
用于生成验证码

#### NumberUtils
处理数字的工具类

#### ObjectUtils
提供一些对象判断的方法

#### PathUtils 
用来获取各种目录 

#### RandomUtils
用于随机数生成

#### ResourceUtils
用于获取资源

#### TimeoutUtil
简单延时任务工具类

#### UrlUtils
url 处理工具类
