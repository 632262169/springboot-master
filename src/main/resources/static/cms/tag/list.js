/**
 * Created by Administrator on 2017/10/9.
 */
var prefix = "/admin/cms/tag";
$(function() {
    loadData();

    $("#sortForm").validate({
        errorElement: "span",
        errorClass: "help-block",
        focusInvalid: !1,
        invalidHandler: function(e, r) {
            $(".alert-danger", $(".login-form")).show();
        },
        highlight: function(e) {
            $(e).closest(".form-group").addClass("has-error");
        },
        success: function(e) {
            e.closest(".form-group").removeClass("has-error");
            e.remove();
        },
        submitHandler: function(form){
            //loading('正在提交，请稍等...');
            //form.submit();
            saveSort();
        },
        errorContainer: "#messageBox",
        errorPlacement: function(error, element) {
            error.insertAfter(element.closest(".input-icon"));
            $("#messageBox").text("输入有误，请先更正。");
            if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
                error.appendTo(element.parent().parent().parent());
            } else {
                if (element.parent().hasClass("input-group")) {
                    error.appendTo(element.parent().parent());
                } else {
                    error.insertAfter(element);
                }

            }
        }
    });
});

function loadData() {
    $('#listTable')
        .bootstrapTable(
            {
                method : 'get', // 服务器数据的请求方式 get or post
                url : prefix + "/list", // 服务器数据的加载地址
                toolbar : '#toolbar',
                cache: false,
                striped : true, // 设置为true会有隔行变色效果
                dataType : "json", // 服务器返回的数据类型
                pagination : false, // 设置为true会在底部显示分页条
                // queryParamsType : "limit",
                // //设置为limit则会发送符合RESTFull格式的参数
                singleSelect : false, // 设置为true将禁止多选
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                pageSize : 10, // 如果设置了分页，每页数据条数
                pageNumber : 1, // 如果设置了分布，首页页码
                search : true, // 是否显示搜索框
                showColumns : false, // 是否显示内容下拉框（选择显示的列）
                uniqueId: "id", //每一行的唯一标识，一般为主键列
                sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParams : function(params) {
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        // pageSize: params.limit,
                        // pageNo:params.offset,
                        search:params.search
                    };
                },
                columns : [
                    {
                        field : 'name',
                        title : '名称'
                    },
                    {
                        title : '序号',
                        field : 'id',
                        align : 'center',
                        formatter : function(value, row, index) {
                            var sort = row.sort;
                            if (!sort || sort == "null") {
                                sort = 0;
                            }
                            var ipt = '<input type="hidden" name="ids" value="' + row.id + '">' +
                                '<input type="text" name="sorts" class="form-control required digits" ' +
                                'style="width: 100px" maxlength="6" ' +
                                'value="' + sort +'">';
                            return ipt;
                        }
                    },
                    {
                        field : 'isRecommend',
                        title : '是否推荐',
                        align : 'center',
                        formatter : function(value, row, index) {
                            var s = '', t = '';
                            if (value == '0') {
                                s = '<span class="label label-danger">不推荐</span>';
                            } else if (value == '1') {
                                s = '<span class="label label-primary">已推荐</span>';
                            }
                            return s + t;
                        }
                    },
                    {
                        title : '操作',
                        field : 'id',
                        align : 'center',
                        formatter : function(value, row, index) {

                            var f = "";
                            if (row.isRecommend == '0') {//如果是未推荐状态，则显示推荐按钮
                                f = '<a class="btn btn-success btn-sm" href="#" title="推荐"  mce_href="#" onclick="recommend(\''
                                    + row.id
                                    + '\')"><i class="fa fa-heart"></i></a> ';
                            } else {
                                f = '<a class="btn btn-default btn-sm" href="#" title="取消推荐"  mce_href="#" onclick="cancelRecommend(\''
                                    + row.id
                                    + '\')"><i class="fa fa-heart-o"></i></a> ';
                            }
                            var d = '<a class="btn btn-warning btn-sm" href="#" title="删除"  mce_href="#" onclick="remove(\''
                                + row.id
                                + '\')"><i class="fa fa-remove"></i></a> ';

                            return f + d;
                        }
                    } ]
            });
}
function reLoad() {
    $('#listTable').bootstrapTable('refresh');
}
function remove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn : [ '确定', '取消' ]
    }, function() {
        $.ajax({
            url : prefix + "/delete",
            type : "post",
            data : {
                'id' : id
            },
            success : function(data) {
                if (data.code==0) {
                    layer.msg("删除成功");
                    reLoad();
                }else{
                    if (data.msg) {
                        layer.msg(data.msg);
                    } else {
                        layer.msg("没有权限");
                    }
                }
            }
        });
    })
}
function recommend(id) {
    layer.confirm('确定要推荐当前标签吗？', {
        btn : [ '确定', '取消' ]
    }, function() {
        $.ajax({
            url : prefix + "/recommend",
            type : "post",
            data : {
                'id' : id
            },
            success : function(data) {
                if (data.code==0) {
                    layer.msg("推荐成功");
                    reLoad();
                }else{
                    if (data.msg) {
                        layer.msg(data.msg);
                    } else {
                        layer.msg("没有权限");
                    }
                }
            }
        });
    })
}

function cancelRecommend(id) {
    layer.confirm('确定要取消推荐当前标签吗？', {
        btn : [ '确定', '取消' ]
    }, function() {
        $.ajax({
            url : prefix + "/cancelRecommend",
            type : "post",
            data : {
                'id' : id
            },
            success : function(data) {
                if (data.code==0) {
                    layer.msg("取消推荐成功");
                    reLoad();
                }else{
                    if (data.msg) {
                        layer.msg(data.msg);
                    } else {
                        layer.msg("没有权限");
                    }
                }
            }
        });
    })
}

function saveSort() {
    var index = layer.load(0, {shade: false});
    $.ajax({
        type : "POST",
        url : prefix + "/saveSort",
        data : $('#sortForm').serialize(),
        async : true,
        error : function(request) {
            layer.alert("网络异常");
            layer.close(index);
        },
        success : function(data) {
            if (data.code == 0) {
                layer.msg("操作成功");
                reLoad();
            } else {
                if (data.msg) {
                    layer.msg(data.msg);
                } else {
                    layer.msg("没有权限");
                }
            }
            layer.close(index);
        }
    });
}
