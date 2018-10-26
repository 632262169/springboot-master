/**
 * Created by Administrator on 2017/10/9.
 */
var prefix = "/admin/sys/user";
$(function() {
    loadData();

    $("#addBtn").click(function(){
        add();
    });
});

function loadData() {
    $('#listTable')
        .bootstrapTable(
            {
                method : 'get', // 服务器数据的请求方式 get or post
                url : prefix + "/list", // 服务器数据的加载地址
                toolbar : '#exampleToolbar',
                striped : true, // 设置为true会有隔行变色效果
                dataType : "json", // 服务器返回的数据类型
                pagination : true, // 设置为true会在底部显示分页条
                // queryParamsType : "limit",
                // //设置为limit则会发送符合RESTFull格式的参数
                singleSelect : false, // 设置为true将禁止多选
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                pageSize : 10, // 如果设置了分页，每页数据条数
                pageNumber : 1, // 如果设置了分布，首页页码
                //search : true, // 是否显示搜索框
                showColumns : false, // 是否显示内容下拉框（选择显示的列）
                sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParams : function(params) {
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        pageSize: params.limit,
                        pageNo:params.offset,
                        name:$('#name').val(),
                        loginName:$('#loginName').val()
                    };
                },
                columns : [
                    {
                        field : 'name',
                        title : '姓名'
                    },
                    {
                        field : 'loginName',
                        title : '登录名'
                    },
                    {
                        field : 'email',
                        title : '邮箱'
                    },
                    {
                        field : 'status',
                        title : '状态',
                        align : 'center',
                        formatter : function(value, row, index) {
                            if (value == '0') {
                                return '<span class="label label-danger">禁用</span>';
                            } else if (value == '1') {
                                return '<span class="label label-primary">正常</span>';
                            }
                        }
                    },
                    {
                        title : '操作',
                        field : 'id',
                        align : 'center',
                        formatter : function(value, row, index) {
                            var e = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                + row.id
                                + '\')"><i class="fa fa-edit"></i></a> ';
                            var d = '<a class="btn btn-warning btn-sm" href="#" title="删除"  mce_href="#" onclick="remove(\''
                                + row.id
                                + '\')"><i class="fa fa-remove"></i></a> ';
                            var f = '<a class="btn btn-success btn-sm" href="#" title="重置密码"  mce_href="#" onclick="resetPwd(\''
                                + row.id
                                + '\')"><i class="fa fa-key"></i></a> ';
                            return e + d + f;
                        }
                    } ]
            });
}
function reLoad() {
    $('#listTable').bootstrapTable('refresh');
}
function add() {
    // iframe层
    layer.open({
        type : 2,
        title : '增加用户',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '1200px', '800px' ],
        content : prefix + '/add'
    });
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
function edit(id) {
    layer.open({
        type : 2,
        title : '用户修改',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '400px', '500px' ],
        content : prefix + '/edit?id=' + id
    });
}
function resetPwd(id) {
    layer.confirm('确定要重置当前用户的密码吗？', {
        btn : [ '确定', '取消' ]
    }, function() {
        $.ajax({
            url : prefix + "/resetPwd",
            type : "post",
            data : {
                'id' : id
            },
            success : function(data) {
                if (data.code==0) {
                    layer.msg("密码重置成功");
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