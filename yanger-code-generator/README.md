# code-generator 代码生成工具

### 1.介绍

此工程是一款基于 freemarker 模板快捷生成 javaWeb 代码的工具，便于实现简单的 CRUD 功能开发，提高开发效率，解放双手。

生成的代码需基于 Spring 环境作用，也可以是 SpringBoot 工程，ORM 框架为 mybatis 或者 JPA。

生成代码范围：
 * 基于 SpringMVC 的 Controller 层
 * Service 层
 * DAO 层
    * mybatis 注解方式
    * mybatis xml 方式
    * mybatis-plus 方式
    * JPA
 * entity 模型对象
 * 不同模型对象的转换工具类
    
### 2.使用

暂时未提供私服 maven 地址，需要自行编译打包，首先克隆项目工程
````$xslt
git clone https://github.com/imyanger/code-generator.git
````

编译项目，可 maven 直接打包，也可以导入 IDE 中进行编译打包
```$xslt
mvn clean install
```

在项目中引入 code-generator 依赖 jar，建议作用范围选用 test，避免包污染
```pom
<dependency>
    <groupId>com.yanger</groupId>
    <artifactId>code-generator</artifactId>
    <version>1.0.0.RELEASE</version>
    <scope>test</scope>
</dependency>
```

编写测试方法，设置必要的参数，调用 Generator 类 generate 方法即可生成代码，以为为简单的生成设置
```java
    @Test
    public void generate() {
        // 代码生成对象
        Generator generator = new Generator();

        // 参数对象
        GeneratorConfig generatorConfig = new GeneratorConfig();

        // 通用参数
        GeneralConfig generalConfig = new GeneralConfig();
        generalConfig.setCodePath("D:\\code\\code-generator-test");
        generalConfig.setCodePackage("com.yanger.demo");
        generatorConfig.setGeneralConfig(generalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setSqlFile("generator.sql");
        generatorConfig.setDataSourceConfig(dataSourceConfig);
    
        // 调用生成
        generator.generate(generatorConfig);
    }
```

运行代码即可在指定项目中生成代码

### 3.参数说明

所有的参数均通过 GeneratorConfig 对象进行设置，主要分为 5 部分：
* GeneralConfig：通用参数配置
* ApiConfig： controller 层参数配置
* ServiceConfig：service 层参数配置
* DaoConfig：dao 层参数配置
* DataSourceConfig：数据源配置

#### 具体说明

|  参数对象   | 参数字段  |  参数说明  | 参数可选值   | 是否必须  | 默认值  |
|  :----:  | :----:  | :----:  | :----: | :----:  |:----:  |
| GeneralConfig  | authorName | 作者名，生成代码注释上用于标注作者名 | -- | 否 | yanger|
|   | codePath | 代码生成的目录 | -- | 是 | -- |
|   | codePackage | 生成代码文件的包名 | -- | 是  | -- |
|   | lombok | 是否使用lombok插件 | true（是）、false（否） | 否 | false |
|   | convertWay | 对象转换类型，不同对象间转换的方式 | beanUtils、get-set | 否 | beanUtils |
| ApiConfig | controllerSuffix | controller的命名的后缀 | -- | 否 | Controller |
|   | swagger | 是否使用swagger | true（是）、false（否） | 否 | false |
|   | saveType | 存储（save、update）方法对象类型 | vo、query、form、dto、bo、po | 否 | po |
|   | queryType | 查询（find）方法对象类型 | vo、query、form、dto、bo、po | 否 | po |
|   | returnType | controller返回对象类型 | vo、query、form、dto、bo、po | 否 | po |
|   | returnObjName | 自定义通用返回对象全限定名 | -- | 否 | -- |
| ServiceConfig | interfaceNeed | service是否需要接口 | true（是）、false（否） | 否 | true |
|   | interfacePrefix | service接口名前缀 | -- | 否 | -- |
|   | interfaceSuffix | service接口名后缀 | -- | 否 | Service |
|   | implementationSuffix | service实现类名后缀 | -- | 否 | ServiceImpl |
|   | objType | servie层对象类型 | vo、query、form、dto、bo、po | 否 | po |
|  DaoConfig | interfacePrefix | dao接口名前缀 | -- | 否 | -- |
|   | interfaceSuffix | dao接口名后缀 | -- | 否 | Dao |
|   | objType | dao层对象类型 |vo、query、form、dto、bo、po | 否 | po |
|   | daoUtilType | dao层ORM工具类型 | mybatis-xml、mybatis-annotation、mybatis-plus、jpa | 否 | mybatis-plus |
|   | nameCase | 数据库对象字段编码风格 | camel（驼峰）、underline（下划线） | 否 | camel |
|   | tinyintTransType | 数据库tinyintTransType类型对应java类型 | -- | 否 | Boolean |
| DataSourceConfig | sqlFilePath | insert语句的sql文件位置，磁盘路径，或者resources目录下 | -- | 是（与数据源二选一） | generator.sql |
|   | driverName | 数据库驱动类型 | -- | 是（与sqlFilePath二选一） | -- |
|   | url | 数据库连接地址 | -- | 是（与sqlFilePath二选一） | -- |
|   | userName | 数据库用户名 | -- | 是（与sqlFilePath二选一） | -- |
|   | password | 数据库密码 | -- | 是（与sqlFilePath二选一） | -- |
|   | schema | 数据库schema名称，便于区分多schema下存在相同表名 | -- | 否 | -- |
|   | tables | 需要生成的表名  | -- | 是（在使用数据源情况下） | -- |
| templateTypes | -- | 设置需要生成模板的类型，调用addTemplate方法进行设置，参数为TemplateType枚举 | -- | -- | -- |

#### 特别注意
* GeneralConfig 配置中的 codePath 与 codePackage 必须设置，否则无法生成代码
* DataSourceConfig 配置数据源有两种方式：
    * 通过配置创建表 insert 的 SQL 语句进行生成，通过设置 sqlFilePath属性，sql 文件可以放置在项目的 resources 目录下，文件路径以 resources 目录为根目录即可，可以放在其他磁盘位置，但此时 sql 文件的路径为完全路径
    * 也可以通过配置数据源的方式，需设置 driverName、url、userName、password 属性进行数据库；连接，同时通过设置 tables 进行声明需要生成的表
    * 当两者同时设置时，以数据源方式优先
    * 推荐使用数据源配置方式
    
#### 全量配置代码参考
```java
@Test
public void generate() {
    GeneratorConfig generatorConfig = new GeneratorConfig();

    // 通用配置
    GeneralConfig generalConfig = new GeneralConfig();
    generalConfig.setAuthorName("yanger");
    generalConfig.setCodePath("E:\\coding\\projects\\code-generator-test");
    generalConfig.setCodePackage("com.yanger.demo");
    generalConfig.setLombok(true);
    generalConfig.setConvertWay("beanUtils");
    generatorConfig.setGeneralConfig(generalConfig);

    // controller配置
    ApiConfig apiConfig = new ApiConfig();
    apiConfig.setControllerSuffix("Api");
    apiConfig.setSwagger(true);
    apiConfig.setSaveType("form");
    apiConfig.setQueryType("query");
    apiConfig.setReturnType("vo");
    apiConfig.setReturnObjName("com.yanger.generator.entity.ApiResponse");
    generatorConfig.setApiConfig(apiConfig);

    ServiceConfig serviceConfig = new ServiceConfig();
    serviceConfig.setInterfaceNeed(true);
    serviceConfig.setInterfacePrefix("");
    serviceConfig.setInterfaceSuffix("Service");
    serviceConfig.setImplementationSuffix("ServiceImpl");
    serviceConfig.setObjType("bo");
    generatorConfig.setServiceConfig(serviceConfig);

    DaoConfig daoConfig = new DaoConfig();
    daoConfig.setInterfacePrefix("");
    daoConfig.setInterfaceSuffix("Dao");
    daoConfig.setObjType("po");
    daoConfig.setDaoUtilType("mybatis-xml");
    daoConfig.setNameCase("camel");
    daoConfig.setTinyintTransType("Integer");
    generatorConfig.setDaoConfig(daoConfig);

    DataSourceConfig dataSourceConfig = new DataSourceConfig();
    // sql 文件与配置文件方式选取一种即可
    // dataSourceConfig.setSqlFilePath("generator.sql");
    dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
    dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/code-generator-test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
    dataSourceConfig.setUserName("root");
    dataSourceConfig.setPassword("1234");
    dataSourceConfig.setSchema("rt");
    dataSourceConfig.addTable("rt_count");
    dataSourceConfig.addTable("rt_goods");
    generatorConfig.setDataSourceConfig(dataSourceConfig);

    new Generator().generate(generatorConfig);
}
```

### 4.后记

感谢使用，如果使用中遇到任何问题，欢迎反馈。

