# bootplus
- ```bootplus```是基于```SpringBoot + Shiro + MyBatisPlus```的真正```restful URL```资源无状态认证权限管理框架

### **项目结构**
```
bootplus
├──sql  项目SQL语句
│
├──App 项目启动类
│
├──config 配置信息
│
├──controller 控制器
│	├─admin 后台管理员控制器
│
├──service 业务逻辑接口
│	├─impl 业务逻辑接口实现类
│
├──dao 数据访问接口
│
├──entity 数据持久化实体类
│
├──shiro Shiro验证框架
│
├──util 项目所用的的所有工具类
│	├─FreeMarker 自定义FreeMarker标签
│
├──resources
│	├─mapper SQL对应的XML文件
│
├──webapp
│	├─statics 静态资源
│	├─upload 上传文件
│	├─WEB-INF
│		├─templates 页面FreeMarker模版
```

### **技术选型：**
- 核心框架：Spring Boot 1.5.1
- 安全框架：Apache Shiro
- 视图框架：Spring MVC
- 持久层框架：MyBatis MyBatisPlus
- 缓存技术：EhCache
- 数据库连接池：Druid
- 日志管理：SLF4J、Log4j
- 模版技术：FreeMarker
- 页面交互：BootStrap、Layer等

### **本地部署**
- mysql执行sql/bootplus.sql文件，初始化数据

- 修改application.yml，更新MySQL连接信息

- 项目访问路径：http://localhost/admin
- 管理员账号密码：admin/admin

### 效果展示  
![演示效果图](/images/1.png "个人资料")
![演示效果图](/images/2.png "新建菜单")
![演示效果图](/images/3.png "菜单管理")
![演示效果图](/images/4.png "角色管理")