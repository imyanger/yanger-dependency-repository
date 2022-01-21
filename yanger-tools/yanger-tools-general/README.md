## General tools
包含一些常用的工具类，可作用于简单的 java 工程（不包含 web 依赖）。


### 1. constant
基础的常量池，消除硬编码。
* CharPool：一些字节常量
* Charsets：常用编码集
* DateConstant：日期时间格式的常量
* StringPool：字符串常量


### 2. format
一些转换工具。

##### ConcurrentDateFormat
安全的时间格式化，参考 tomcat8 中的并发 DateFormat, SimpleDateFormat的线程安全包装器. 不使用ThreadLocal,创建足够的SimpleDateFormat对象来满足并发性要求。
```java
    @Test
    public void testConcurrentDateFormat(){
        String now = ConcurrentDateFormat.of().format(new Date());
        System.out.println(now);
        String now2 = ConcurrentDateFormat.of("yyyy-MM").format(new Date());
        System.out.println(now2);
    }
```
> 2021-11-28 17:25:52
<br/>
> 2021-11

##### DateFormat
对于 Date 对象的 format 和 parse 工具类。
```java
    @Test
    public void testDateFormat() {
        String dateString = DateFormat.format(new Date(), "yyyy-MM");
        System.out.println(dateString);
    }
```

##### DateTimeFormat
DateTime 工具类, 提供 TemporalAccessor 实现的 format 和 parse。
```java
    @Test
    public void testDateTimeFormat() {
        String formatString = DateTimeFormat.format(LocalDate.now(), "yyyy-MM");
        System.out.println(formatString);
    }
```

##### StringFormat
替换String中的占位符，避免 “+” 进行字符串的拼接。
```java
    @Test
    public void testStringFormat() {
        String stringFormat = StringFormat.format("This is {} by {}", "StringFormat", "yanger");
        System.out.println(stringFormat);
        String mergeFormat = StringFormat.mergeFormat("This is {0} by {1}", "mergeFormat", "yanger");
        System.out.println(mergeFormat);
    }
```
> This is StringFormat by yanger
<br/>
>This is mergeFormat by yanger


### 3. tools

##### ClassScanner
类扫描器, 扫描给定包及其子包中的所有类。
```java
    @Test
    public void test(){
        Set<Class<?>> classes = ClassScanner.getClasses("com.yanger.tools.general.format");
        classes.forEach(s -> System.out.println(s.getSimpleName()));
    }
```
> ConcurrentDateFormat
<br/>
> DateTimeFormat
<br/>
> StringFormat

##### DateUtils
提供对时间操作的方法集合，包括一些时间对象的转换，时间的增加减少等。
```java
    @Test
    public void test() {
        // convertLocal 类方法，对象间转换
        long l = DateUtils.convertLocalDate2Long(LocalDate.now());

        // plus,minus 对时间进行加减操作
        Date plusDate = DateUtils.plusDays(DateUtils.now(), 2);
        Date minusDate = DateUtils.minusYears(DateUtils.now(), 2);
        
        // 间隔天数
        int daysCount = DateUtils.getDaysCount(new Date(), plusDate);
        System.out.println(daysCount);
    }
```

##### EncryptUtils
一些常用的加密解密方法。
* MD5
* AES 加解密
* Base64编码解码

##### EnumUtils
枚举工具类， 用于获取枚举对象。

##### ImageUtils
图像处理工具类， 包含图片的缩放和裁剪等。


##### LRUCache
LRU cache 单机实现，LinkedHashMap 的继承增强。

##### OnceLogger
只打印一次的 log 类实现，通过 ConcurrentHashMap 来对 log 内容进行缓存，保证对应 log 仅仅输出一次。

##### RuntimeUtils
* getPid：获得当前进程的PID
* getUpTime：返回应用启动到现在的时间
* getJvmArguments：返回输入的JVM参数列表
* getCpuNum：获取CPU核数

##### SnowflakeBuilder
提供基于 hutool 工具的 Snowflake Builder 类。

##### StringTools
String 工具类集合，常用的处理字符串的工具类。

##### SystemUtils
lang3.SystemUtils 扩展，增加对操作系统的判断。

