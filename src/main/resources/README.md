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
