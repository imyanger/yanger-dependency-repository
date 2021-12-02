## General tools
包含一些常用的工具类，可作用于简单的 java 工程（不包含 web 依赖）。

### 1. constant
基础的常量池，消除硬编码。
* CharPool：一些字节常量
* Charsets：常用编码集
* StringPool：字符串常量

### 2. format
一些转换工具。

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

#### ConcurrentDateFormat
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

#### DateTimeFormat
DateTime 工具类。

### 3. tools


