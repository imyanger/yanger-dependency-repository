## web-spring-boot-starter
web spring starter，提供 web 应用的一些扩展。
```java
        <dependency>
            <groupId>com.yanger</groupId>
            <artifactId>web-spring-boot-starter</artifactId>
            <version>${web-spring-boot-starter.version}</version>
        </dependency>
```

### 1.api
##### BaseApi
注入 request && response。
```java
    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;
```
##### EnumApi
暴露`/yanger/enums`接口，调用该接口可放回所有`SerializeEnum`接口实现类枚举集合。
##### HealthApi
暴露`/health`接口，仅用于判断服务是否正常。
##### IndexApi
实现登录登出先关接口的简单实现，若需要完善其功能，实现`ILoginService`类，完善对应功能的方法。
* /login：登录
* /wxMiniLogin：微信登录
* /wxAppLogin：微信小程序登录
* /randomCode：获取验证码，base64 字符串
* /randomCode/cal：获取计算型验证码，base64 字符串


### 2.SerializeEnum
可序列化为 json 的枚举接口，`SerializeEnum`接口标注的类，在返回给前端时，可转化为json格式。
```json
{
  "value": 1,
  "desc": "one",
  "name": "A",
  "ordinal": 0
}
```
而在`SerializeEnum`接口作为 api 参数时：
* 可以传入 json 格式
* 传入`SerializeEnum`的 value
* 传入`SerializeEnum`的 name
* 传入`SerializeEnum`的 ordinal

都可以转化为`SerializeEnum`对象
>都为数字时，注意区分 value 与 ordinal，此时 value 具有更高的优先级


### 3.LoginAuth && @IgnoreLoginAuth
开启登录校验后，可通过 @LoginAuth 与 @IgnoreLoginAuth 声明 api （controller 方法）是否需要进行登录校验。
>  @LoginAuth 与 @IgnoreLoginAuth 同时作用时，@LoginAuth 生效，依然需要验证

### 4.@LoginUser 
开启登录校验后，可通过 @LoginUser AuthUser authUser 的方法参数来获取登录用户信息。
```java
public class AuthUser {

    /** 用户id */
    private Long userId;
    /** 用户姓名 */
    private String name;
    /** 用户账号 */
    private String username;
    /** 手机号码 */
    private String mobile;

}
```

### 5.GlobalResponseHandler && GlobalExceptionHandler
若无 @IgnoreResponseAdvice 注解标识的 api 方法，默认返回对象都将是 Result 对象，如果不是，则会被封装为 Result。
GlobalExceptionHandler 中定义了几种常见的异常处理方式，并携带了异常的通用描述信息，可配合 IResultCode 接口使用。
> GlobalExceptionHandler 与 validation 作用时，会提示遇到的第一个校验失败项提示语。



