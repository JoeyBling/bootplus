<!DOCTYPE html>
<html>
<head>
    <title>${frameModuleName}列表</title>
    [#include "../header.ftl"]
    <link rel="stylesheet" href="${ctxstatic}/common/icheck/flat/green.css?version=${t_version}"/>
    <link rel="stylesheet"
          href="${ctxstatic}/common/bootstrap-switch/css/bootstrap-switch.min.css?version=${t_version}"/>
    <link rel="stylesheet" href="${ctxstatic}/common/bootstrap-table/bootstrap-table.min.css?version=${t_version}"/>
    <script>
        const action = "${ctx}${path}";
        const frameModule = "${frameModule}";
        const frameModuleName = "${frameModuleName}";
    </script>
</head>
<body class="gray-bg" style="display:none;">
<div class="wrapper wrapper-content">
    <div id="row" class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>定时任务列表</h5>
                    <div class="ibox-tools">
                        <a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                        <a class="close-link"><i class="fa fa-times"></i></a>
                    </div>
                </div>
                <div class="ibox-content form-horizontal">
                    <div class="row row-lg">
                        <form id="searchForm">
                            <div class="col-sm-3">
                                <div class="input-group">
                                    <div class="input-group-btn">
                                        <button data-toggle="dropdown" class="btn btn-default dropdown-toggle"
                                                type="button" aria-expanded="false">任务名称
                                        </button>
                                    </div>
                                    <input type="text" class="form-control" name="jobName" placeholder="">
                                </div>
                            </div>
                            <div class="col-sm-3">
                                <button type="button" class="btn btn-primary" onclick="javascript:search()">
                                    <i class="fa fa-search"></i>&nbsp;搜索
                                </button>
                            </div>
                        </form>
                        <div class="col-sm-12">
                            <div class="example-wrap">
                                <div class="example">
                                    <div id="toolbar" class="btn-group m-t-sm">
                                        [@shiro.hasPermission name="sys:task:save"]
                                            <button type="button" style="margin-right:10px;" class="btn btn-primary"
                                                    title="创建定时任务" onclick="javascript:add($('#showHandle'))">
                                                <i class="glyphicon glyphicon-plus"></i> 创建定时任务
                                            </button>
                                        [/@shiro.hasPermission]
                                        [@shiro.hasPermission name="sys:task:delete"]
                                            <button type="button" style="margin-right:10px;" class="btn btn-danger"
                                                    title="批量删除" onclick="javascript:multiDel($('#table'))">
                                                <i class="glyphicon glyphicon-remove"></i> 批量删除
                                            </button>
                                        [/@shiro.hasPermission]
                                    </div>
                                    <table id="table"
                                           data-toggle="table"
                                           data-height="600"
                                           data-search="false"
                                           data-search-on-enter-key="false"
                                           data-show-refresh="true"
                                           data-show-toggle="true"
                                           data-show-export="true"
                                           data-show-columns="true"
                                           data-url="${ctx}/admin/sys/task/list" [#-- 服务器数据URL --]
                                           data-pagination="true"
                                           data-page-size="20"
                                           data-page-list="[20, 50, 100, 200]"
                                           data-side-pagination="server"
                                           data-striped="true"
                                           data-pagination="true"
                                           data-sort-name="createTime" [#-- 默认排序字段 --]
                                           data-sort-order="desc" [#-- 默认排序顺序 可选asc desc --]
                                           data-toolbar="#toolbar" [#-- 指定工具类元素 --]
                                           data-click-to-select="true" [#-- 设置true 将在点击行时，自动选择rediobox 和 checkbox --]
                                           data-single-select="false" [#-- 设置True 将禁止多选 --]
                                           data-unique-id="id" [#-- 填写主键ID即可 --][#-- 官方文档:http://bootstrap-table.wenzhixin.net.cn/zh-cn/documentation/ --]
                                           data-response-handler="defaultBootstrapTableHandler">
                                        <thead>
                                        <tr>
                                            <th data-checkbox="true"></th>
                                            <th data-field="jobName" data-halign="center" data-align="center"
                                                data-sortable="true">任务名称
                                            </th>
                                            <th data-field="cronExpression" data-halign="center" data-align="center"
                                                data-sortable="true">执行表达式
                                            </th>
                                            <th data-field="bizModule" data-halign="center" data-align="center"
                                                data-sortable="true">业务模块
                                            </th>
                                            <th data-field="bizTag" data-halign="center" data-align="center"
                                                data-sortable="true">业务标识
                                            </th>
                                            <th data-field="enabled" data-formatter="formatStatus"
                                                data-sortable="true" data-halign="center" data-align="center">状态
                                            </th>
                                            <th data-field="callbackType" data-formatter="formatCallbackType"
                                                data-halign="center" data-align="center" data-sortable="true">回调类型
                                            </th>
                                            <th data-field="createTime" data-halign="center" data-align="center"
                                                data-sortable="true">创建时间
                                            </th>
                                            <th data-formatter="actionFormatter" data-events="actionEvents"
                                                data-halign="center" data-align="center">操作
                                            </th>
                                        </tr>
                                        </thead>
                                    </table>
                                    <input type='hidden' id="handle"
                                            [@shiro.hasPermission name="sys:task:update"] data-update="true" [/@shiro.hasPermission]
                                            [@shiro.hasPermission name="sys:task:delete"] data-delete="true"[/@shiro.hasPermission]/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- add or update -->
<div id="showHandle" style="display:none;">
    <div class="col-sm-12">
        <div class="ibox float-e-margins">
            <div class="ibox-title">
                <h5><span id="title">创建定时任务</span><small></small></h5>
                <div class="ibox-tools">
                    <a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                    <a class="close-link"><i class="fa fa-times"></i></a>
                </div>
            </div>
            <div class="ibox-content form-horizontal">
                <form id="form">
                    <input type='hidden' name="id"/>

                    <div class="form-group m-t">
                        <label class="col-sm-2 col-xs-offset-1 control-label">任务名称：</label>
                        <div class="col-sm-6">
                            <input type="text" maxlength="15" class="form-control" name="jobName">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-2 col-xs-offset-1 control-label">执行表达式：</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" name="cronExpression">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-2 col-xs-offset-1 control-label">回调内容：</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" name="callbackData">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-2 col-xs-offset-1 control-label">回调地址：</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" name="callbackUrl">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <div class="col-sm-12 text-center">
                            [#--<button type="button" class="btn btn-success" onclick="saveOrUpdate(this);">提交
                            </button>--]
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- bootstrapvalidator-master前端验证框架 -->
<script src="${ctxstatic}/common/bootstrapvalidator/js/bootstrapValidator.min.js"></script>
<!-- Bootstrap table -->
<script src="${ctxstatic}/common/bootstrap-table/bootstrap-table.min.js"></script>
<script src="${ctxstatic}/common/bootstrap-table/extensions/export/bootstrap-table-export.js"></script>
<script src="${ctxstatic}/common/bootstrap-table/tableExport.js"></script>
<script src="${ctxstatic}/common/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<!-- 自定义js -->
<script src="${ctxstatic}/js/admin/sys/taskList.js"></script>
<!-- iCheck -->
<script src="${ctxstatic}/common/icheck/icheck.min.js"></script>
<script src="${ctxstatic}/common/bootstrap-switch/js/bootstrap-switch.min.js"></script>
[#-- 页面加载进度条 --]
[#assign parentName="#row"][#-- 默认为Body --]
[#include "../nprogress.ftl"]
</body>
</html>