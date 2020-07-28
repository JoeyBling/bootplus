# bootplus
&emsp;&emsp;基于SpringBoot + Shiro + MyBatisPlus的权限管理框架

## git分支说明
&emsp;&emsp;目前开发使用`dev`分支，开发完成或者发布版本需要合并`master`分支

> Tips:如果是第一次克隆仓库。需要从远程分支检出`dev`分支
```bash
git checkout -b dev origin/dev
```

```bash
# 检出master分支
git checkout master
# 合并dev分支
git merge dev
# 推送
git push origin/master
```

##### Bug修复说明
&emsp;&emsp;如果是修复线上的Bug、则从`master`分支检出一个`hotfix-0728`Bug修复分支，改完后分别合并`master`和`dev`分支并删除hotfix分支
```bash
git checkout master
git checkout -b hotfix-demo
// ... fix bugs & commit ....

# 检出master分支
git checkout master
# 合并dev分支
git merge hotfix-demo
# 推送
git push origin/master

// 删除本地分支
git branch -D hotfix-demo
```


## mvn乱码问题
```bash
mvn -v
// 默认编码：platform encoding: GBK
```
> 设置windows环境变量：设置后需要重启IDEA才能生效
- Key: `MAVEN_OPTS`
- Value: `-Xms256m -Xmx512m -Dfile.encoding=UTF-8`

## Git标签管理
> 查看远程标签
```bash
git tag
```
> 建新标签
```bash
git tag v1.0
```
> 删除远程标签
```bash
git tag -d 标签名
```
> 从远程仓库中移除这个标签
```bash
git push origin :refs/tags/标签名
```
> 推送标签到远程仓库
```bash
git push --tags
```

## 表结构修改记录
> 2020/3/12

## api接口测试示例
### curl
```bash
curl --location --request POST 'http://dev-saas.diandianys.com/api/app' \
--header 'Content-Type: application/javascript' \
--header 'sign: test' \
--data '{
    "channel": "12",
    "format": "JSON",
    "hosId": "00000",
    "oper": "127.0.0.1",
    "random": "5680",
    "spid": "1001",
    "version": "1",
    "service": "smarthos.system.area.list.version"
}'
```

## `Lombok`使用说明
- `@NoArgsConstructor`: 自动生成无参数构造函数
- `@Builder`: 一步步创建一个对象（.builder()）
- `@AllArgsConstructor`: 自动生成全参数构造函数
- `@Data`: 自动为所有字段添加`@ToString`, `@EqualsAndHashCode`, `@Getter`方法，为非final字段添加`@Setter`,和`@RequiredArgsConstructor`

## `FastJson`使用说明
1. `SerializerFeature`说明

| 名称 | 含义 | 备注 |
| :---: | :---: | :---: |
| QuoteFieldNames | 输出key时是否使用双引号，默认为true |  |
| UseSingleQuotes	 | 使用单引号而不是双引号，默认为false |  |
| WriteMapNullValue | 是否输出值为null的字段，默认为false |  |
| WriteEnumUsingToString | Enum输出name()或者original，默认为false |  |
| WriteEnumUsingName | enum值序列化为其Name，默认为true |  |
| UseISO8601DateFormat | Date使用ISO8601格式输出，默认为false |  |
| WriteNullListAsEmpty | List字段如果为null,输出为[],而非null |  |
| WriteNullStringAsEmpty | 字符类型字段如果为null,输出为”“,而非null |  |
| WriteNullNumberAsZero | 数值字段如果为null,输出为0,而非null |  |
| WriteNullBooleanAsFalse | Boolean字段如果为null,输出为false,而非null |  |
| SkipTransientField | 如果是true，类中的Get方法对应的Field是transient，序列化时将会被忽略。默认为true |  |
| SortField | 按字段名称排序后输出。默认为false |  |
| WriteTabAsSpecial | 把\t做转义输出，默认为false | 不推荐 |
| PrettyFormat | 结果是否格式化,默认为false | 不推荐 |
| WriteClassName | 序列化时写入类型信息，默认为false。反序列化是需用到 | 不推荐 |
| DisableCircularReferenceDetect | 消除对同一对象循环引用的问题，默认为false | 不推荐 |
| WriteSlashAsSpecial | 对斜杠’/’进行转义 | 不推荐 |
| BrowserCompatible | 将中文都会序列化为\uXXXX格式，字节数会多一些，但是能兼容IE 6，默认为false | 不推荐 |
| WriteDateUseDateFormat | 全局修改日期格式,默认为false。 | 不推荐 |
| DisableCheckSpecialChar | 一个对象的字符串属性中如果有特殊字符如双引号，将会在转成json时带有反斜杠转移符。如果不需要转义，可以使用这个属性。默认为false | 不推荐 |
| NotWriteRootClassName | 含义 | 不推荐 |
| BeanToArray | 将对象转为array输出 | 不推荐 |
| WriteNonStringKeyAsString | 将属性key写为String | 不推荐 |
| NotWriteDefaultValue | 不设默认值 | 不推荐 |
| BrowserSecure |  | 不推荐 |
| IgnoreNonFieldGetter | 忽略没有getter方法的属性 | 不推荐 |
| WriteNonStringValueAsString | 不是String的字段写为String |  |
| IgnoreErrorGetter | 忽略掉getter方法出错的属性 |  |
| WriteBigDecimalAsPlain | 大数字写成文本 |  |
| MapSortField | 字段按照TreeMap排序，默认false |  |
