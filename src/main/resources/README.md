# Actuator健康监控说明
> 配置项 `management.context-path`

| 端点名(URI) | 描述 | 鉴权 |
| :---: | :---: | :---: |
| conditions | 所有自动配置信息 | true |
| auditevents| 审计事件| true |
| beans| 所有Bean的信息| true |
| configprops| 所有自动化配置属性| true |
| threaddump| 线程状态信息| true |
| env| 当前环境信息| true |
| health| 应用健康状况| true |
| info| 当前应用信息| true |
| metrics| 应用的各项指标| true |
| mappings| 应用@RequestMapping映射路径| true |
| shutdown| 关闭当前应用（默认关闭）| true |
| httptrace| 追踪信息（最新的http请求）| true |
| trace| 基本追踪信息 | true |

# 代理配置说明
[SpringBoot中修改proxyTargetClass，但事务代理始终为CGLIB](https://blog.csdn.net/laoxilaoxi_/article/details/99896738)
##### 基于CGLIB的代理与基于JDK的动态代理实现的声明式事务的区别
- CGLIB基于继承实现，JDK动态代理基于实现接口实现
- CGLIB的代理类需要事务注解@Transactional标注在类上（或方法）;而JDK动态代理类事务注解@Transactional可以标注在接口上（或方法），也可以标注在实现类上（或方法）

> 配置项 `spring.aop.auto`&`spring.aop.proxy-target-class`&`@EnableTransactionManagement(proxyTargetClass = true)`

| auto | proxy-target-class | proxyTargetClass | 代理技术 | 备注 |
| :---: | :---: | :---: | :---: | :---: |
| true | false | false | JDK动态代理 |  |
| true | true | false | CGLIB | 默认值 |
| true | false | true | CGLIB |  |
| true | true | true | CGLIB |  |
| false | false | false | JDK动态代理 |  |
| false | true | false | JDK动态代理 |  |
| false | false | true | CGLIB |  |
| false | true | true | CGLIB |  |