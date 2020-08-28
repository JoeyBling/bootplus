/**
 * init
 */
$(function () {
});

let handle = $("#handle");
let data_update = $(handle).attr("data-update");
let data_delete = $(handle).attr("data-delete");

// 筛选数据
function search() {
    let params = $('#table').bootstrapTable('getOptions');
    params.queryParams = function (params) {
        // 遍历form 组装json
        $.each($("#searchForm").serializeArray(), function (i, field) {
            // 可以添加提交验证
            // 判断不为空白字符串
            if (!!field.value && $.trim(field.value) != '') {
                params[field.name] = field.value;
            }
        });
        return params;
    }
    $('#table').bootstrapTable('refresh', params);
}

// 状态
function formatStatus(value, row, index) {
    if (value == null) {
        return "";
    }
    if (value === true) {
        return '<input type="checkbox" name="my-checkbox" data-id="'
            + row.id + '" checked>';
    } else {
        return '<input type="checkbox" name="my-checkbox" data-id="'
            + row.id + '" >';
    }
}

// 回调类型
function formatCallbackType(value, row, index) {
    switch (value) {
        case 'PRINT':
            return '打印';
            break;
        case 'CLASS':
            return '类回调';
            break;
        default:
            return value == null ? '' : value;
    }
}

/**
 * 开关切换(确保在Dom元素加载后渲染)
 * 绑定多个事件
 * @url {https://bootstrap-table.com/docs/api/}
 */
$("#table").on("toggle.bs.table load-success.bs.table", function (event) {
    $("input[name='my-checkbox']").bootstrapSwitch({
        onText: "启用",
        offText: "禁用",
        size: "mini"
    });
    // Dom元素加载后才能绑定触发事件
    $('input[name="my-checkbox"]').on('switchChange.bootstrapSwitch', function (event, state) {
        let index = layer.load(1, {
            shade: [0.5, '#fff'],
            // shade: [0.8, '#393D49'],
            time: 6 * 1000 //time设置最长等待时间
        });
        let id = $(this).attr("data-id");
        if (!id || !data_update) {
            // 无权限或其它
            layer.msg('无权限或非法操作!', {
                shade: 0.2,
                icon: 2,
                time: 1 * 1000 // 1秒关闭（如果不配置，默认是3秒）
            });
            // 取消停用切换开关状态为未改变之前
            $(this).bootstrapSwitch("toggleState", true);
            layer.close(index);
            return false;
        }
        $.ajax({
            type: "POST",
            url: action + '/updateEnable',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: {
                id: id,
                enable: state
            },
            success: function (r) {
                layer.close(index);
                if (r.code === 0) {
                    layer.msg('操作成功!', {
                        shade: 0.5,
                        icon: 1,
                        time: 0.3 * 1000 // ?秒关闭（如果不配置，默认是3秒）
                    });
                } else {
                    layer.alert(r.msg, {
                        icon: 2
                    });
                }
            }
        });
    });
});

// BootStrapTable自定义操作
function actionFormatter(e, value, row, index) {
    if (null == data_update && null == data_delete) {
        return "";
    } else if (null == data_update && null != data_delete) {
        return [
            '<a class="remove text-danger" href="javascript:void(0)" title="删除">',
            '<i class="glyphicon glyphicon-remove"></i>删除', '</a>']
            .join('');
    } else if (null != data_update && null == data_delete) {
        return [
            '<a class="edit text-warning" href="javascript:void(0)" title="编辑">',
            '<i class="glyphicon glyphicon-edit"></i>编辑', '</a>']
            .join('');
    } else {
        return [
            '<a class="edit m-r-sm text-warning" href="javascript:void(0)" title="编辑">',
            '<i class="glyphicon glyphicon-edit"></i>编辑',
            '</a>',
            '<a class="remove text-danger" href="javascript:void(0)" title="删除">',
            '<i class="glyphicon glyphicon-remove"></i>删除', '</a>']
            .join('');
    }
}

// Table操作
window.actionEvents = {
    // 编辑
    'click .edit': function (e, value, row, index) {
        update(index, row.id);
    },
    // 删除
    'click .remove': function (e, value, row, index) {
        del(index, row.id);
    }
};

/*
 * 删除
 */
function del(index, value) {
    let ids = new Array();
    ids[0] = value;
    layer.confirm('确认要删除该任务吗？', {
        btn: ['确定', '取消'],
        shade: 0.2,
        shadeClose: true
        // 按钮
    }, function () {
        $.ajax({
            type: 'delete',
            dataType: 'json',
            contentType: "application/x-www-form-urlencoded",
            data: {
                ids: JSON.stringify(ids)
            },
            url: action + '/delete',
            success: function (result) {
                if (result.code === 0) {
                    $('#table').bootstrapTable('hideRow', {
                        index: index
                    }); // 移除Table的一行
                    layer.msg('删除成功!', {
                        icon: 1,
                        time: 1000
                    });
                } else {
                    layer.alert(result.msg, {
                        icon: 2
                    });
                }
            }
        })
    });
}

/**
 * 批量删除
 */
function multiDel(tableName) {
    const selectArr = $(tableName).bootstrapTable('getSelections');
    if (selectArr.length < 1) {
        layer.msg('请至少选择一条数据', {
            shade: 0.2,
            icon: 2,
            time: 0.5 * 1000 // （如果不配置，默认是3秒）
        });
        return false;
    }
    let ids = new Array();
    for (let i = 0; i < selectArr.length; i++) {
        ids[i] = selectArr[i].id;
    }
    layer.confirm('确认要删除选中的数据吗？', {
        btn: ['确定', '取消'],
        shade: 0.2,
        shadeClose: true
        // 按钮
    }, function () {
        $.ajax({
            type: 'delete',
            dataType: 'json',
            contentType: "application/x-www-form-urlencoded",
            data: {
                ids: JSON.stringify(ids)
            },
            url: action + '/delete',
            success: function (result) {
                if (result.code === 0) {
                    layer.msg('操作成功!', {
                        icon: 1,
                        time: 1000
                    }, function () {
                        location.reload();
                    });
                } else {
                    layer.alert(result.msg, {
                        icon: 2
                    });
                }
            }
        })
    });
}

// 新建
function add(ele) {
    bootplus.progressBarStartUp();
    $("#title").text("新建定时任务");

    $("#form")[0].reset();

    layer_show("新建定时任务", $(ele), 800, 500);
    bootplus.progressBarShutDown();
}

// 修改
function update(index, value) {
    $("#title").text("修改定时任务");
    getInfo(value);
    layer_show("修改定时任务", $("#showHandle"), 800, 500);
}

// 详情
function getInfo(id) {
    $.get(action + "/select/" + id, function (result) {
        if (result.code === 0) {
            let record = result.record;
            $("#form input[name='id']").val(record.id);
            $("#form input[name='jobName']").val(record.jobName);
            $("#form input[name='cronExpression']").val(record.cronExpression);
            $("#form input[name='callbackData']").val(record.callbackData);
            $("#form input[name='callbackUrl']").val(record.callbackUrl);
        } else {
            layer.alert(result.msg, {
                icon: 2
            });
        }
    });
}

/**
 * 保存或更新
 */
function saveOrUpdate(e) {
    loadingButton($(e));
    if (!validateForm($("#form"))) {
        return false;
    }

    const id = $("input[name='id']").val();
    let params = "";
    $("#form input").each(function () {
        params += $(this).serialize() + "&";
    });

    const reqUrl = id == null ? "/save" : "/update";
    bootplus.progressBarStartUp();
    $.ajax({
        type: "POST",
        url: action + reqUrl,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: params,
        success: function (r) {
            bootplus.progressBarShutDown();
            if (r.code == 0) {
                layer.msg('操作成功!', {
                    icon: 1,
                    time: 1000
                }, function () {
                    location.reload();
                });
            } else {
                layer.alert(r.msg, {
                    icon: 2
                });
            }
        }
    });
}