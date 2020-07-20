# bootplus

```
  _                 _         _
 | |               | |       | |
 | |__   ___   ___ | |_ _ __ | |
 | '_ \ / _ \ / _ \| __| '_ \| | | | / __|
 | |_) | (_) | (_) | |_| |_) | | |_| \__ \
 |_.__/ \___/ \___/ \__| .__/|_|\__,_|___/
                       | |
                       |_|
```

![star](https://img.shields.io/github/stars/JoeyBling/bootplus "star")
![fork](https://img.shields.io/github/forks/JoeyBling/bootplus "fork")
![GitHub Last Commit](https://img.shields.io/github/last-commit/JoeyBling/bootplus.svg?label=commits "GitHub Last Commit")
![issues](https://img.shields.io/github/issues/JoeyBling/bootplus "issues")
[![Author](https://img.shields.io/badge/Author-JoeyBling-red.svg "Author")](https://zhousiwei.gitee.io "Author")

------------------

> 欢迎使用和Star支持，如使用过程中碰到问题，可以提出[Issue](https://github.com/JoeyBling/bootplus/issues)，我会尽力完善

## 介绍
&emsp;&emsp;`bootplus`是基于`SpringBoot + Shiro + MyBatisPlus`的权限管理框架
- 功能还很少，欢迎各位给我提意见和建议~

> `SpringBoot`1.5.1集成例子请参考[bootplus_1.5.1](https://github.com/JoeyBling/bootplus/tree/1.5)（老项目只进行Bug修复，不再添加新功能实现）

## 线上预览

> **预览 ➡️[http://bootplus.diandianys.com/](http://bootplus.diandianys.com/)**

## 项目结构

```lua
bootplus
├── sql  -- 项目SQL语句
│
├── App -- 项目启动类
│
├── common -- 公用模块
|    ├── enums -- 枚举工具类
|    ├── hessian -- Hessian自定义配置
|    ├── serializer -- 自定义序列化实现
|    ├── typehandler -- 自定义MyBatis类型转换器
│
├── config -- 配置信息
|    ├── aop -- Spring AOP深入实现
|    ├── filter -- 过滤器
|    ├── interceptor -- 拦截器
|    ├── listener -- 监听器
│
├── controller -- 控制器
|    ├── admin -- 后台管理员控制器
|    ├── api -- Api接口开放层
│
├── dao -- 数据访问接口及对应的XML文件
│
├── entity -- 数据持久化实体类
|    ├── enums -- 实体枚举类型
│
├── frame -- 框架公用模块
|    ├── cache -- 缓存模块
|    ├── controller -- 控制器模块
|    ├── log -- 日志模块
|    ├── spring -- spring模块
│
├── service -- 业务逻辑接口
|    ├── impl -- 业务逻辑接口实现类
│
├── shiro -- Shiro验证框架
│
├── task -- 定时任务
│
├── util -- 工具类
|    ├── db -- 数据库模块
|    ├── encry -- 加解密模块
|    ├── exception -- 自定义异常
|    ├── file -- 文件工具类
|    ├── freemaker -- 自定义FreeMarker标签
|    ├── http -- http模块实现
|    ├── sketch -- 字体、素描、图像
|    ├── spring -- spring公用模块
│
├── resources
|    ├── conf -- 不同环境配置
|    ├── file -- 模板文件
|    ├── jdk_fonts -- JDK字体
|    ├── META-INF -- SpringBoot配置
|    ├── statics -- 静态资源(css、js...)
|    ├── templates -- 页面FreeMarker模版
|    ├── upload -- 上传文件
```

## 技术选型：
- 核心框架：`Spring Boot 2.3.1`
- 安全框架：`Apache Shiro`
- 视图框架：`Spring MVC`
- 持久层框架：`MyBatis`、`MyBatisPlus`
- 缓存技术：`EhCache`、`Redis`
- 定时器：`Quartz`
- 数据库连接池：`Druid`
- 日志实现：`SLF4J`
- 模版技术：`FreeMarker`
- 页面交互：`BootStrap`、`Layer`等

## 效果截图
![](./examples/images/login.png "登录页面")
![](./examples/images/personal_data.png "个人资料")
![](./examples/images/new_menu.png "新建菜单")
![](./examples/images/menu.png "菜单管理")
![](./examples/images/role.png "角色管理")

## 本地部署：
- mysql执行[sql/bootplus.sql](./sql/bootplus.sql)文件，初始化数据
- 修改`application.yml`，更新`MySQL`连接信息,更新`Redis`连接信息（待实现）
- 项目访问路径：[http://localhost/admin](http://bootplus.diandianys.com/)
- 管理员账号密码：`admin/admin`

## 捐赠
&emsp;&emsp;**如果感觉对您有帮助，请作者喝杯咖啡吧，请注明您的名字或者昵称，方便作者感谢o(*￣︶￣*)o**

| 微信 | 支付宝 |
| :---: | :---: |
| ![](./examples/images/weixin.png) | ![](./examples/images/alipay.jpeg) |

## LICENSE
[![LICENSE](https://img.shields.io/github/license/JoeyBling/bootplus "LICENSE")](./LICENSE "LICENSE")