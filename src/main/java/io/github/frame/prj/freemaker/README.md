# FreeMarker说明

## 对象包装（Object Swapping）
> 这些类型都实现了`freemarker.template.TemplateModel`接口

| 类型 | FreeMarker接口 | FreeMarker实现 |
| :---: | :---: | :---: |
| 字符串 | TemplateScalarModel | SimpleScalar |
| 数值 | TemplateNumberModel | SimpleNumber |
| 日期 | TemplateDateModel | SimpleDate |
| 布尔 | TemplateBooleanModel | TemplateBooleanModel.TRUE |
| 哈希 | TemplateHashModel | SimpleHash |
| 序列 | TemplateSequenceModel | SimpleSequence |
| 集合 | TemplateCollectionModel | SimpleCollection |
| 节点 | TemplateNodeModel | NodeModel |
