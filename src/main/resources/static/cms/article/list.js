/**
 * Created by Administrator on 2017/10/9.
 */
var prefix = "/admin/cms/article";
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
                        title:$('#title').val(),
                        author:$('#author').val()
                    };
                },
                columns : [
                    {
                        field : 'title',
                        title : '标题'
                    },
                    {
                        field : 'author',
                        title : '作者'
                    },
                    {
                        field : 'readCount',
                        title : '浏览量'
                    },
                    {
                        field : 'createDate',
                        title : '创建时间'
                    },
                    {
                        field : 'status',
                        title : '状态',
                        align : 'center',
                        formatter : function(value, row, index) {
                            var s = '', t = '';
                            if (value == '0') {
                                s = '<span class="label label-danger">未发布</span>';
                            } else if (value == '1') {
                                s = '<span class="label label-primary">已发布</span>';
                            }

                            if (row.sort != null && row.sort != '' && row.sort != '0') {
                                t = '&nbsp;<span class="label label-danger">已置顶</span>';
                            }

                            return s + t;
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
                            var f = "";
                            if (row.status == '0') {//如果是未发布状态，则显示发布按钮
                                f = '<a class="btn btn-success btn-sm" href="#" title="发布"  mce_href="#" onclick="pub(\''
                                    + row.id
                                    + '\')"><i class="fa fa-heart"></i></a> ';
                            } else {
                                f = '<a class="btn btn-default btn-sm" href="#" title="取消发布"  mce_href="#" onclick="cancelPub(\''
                                    + row.id
                                    + '\')"><i class="fa fa-heart-o"></i></a> ';
                            }
                            var t = "";

                            if (row.sort != null && row.sort != '' && row.sort != '0') {//如果是置顶状态，则显示取消置顶按钮
                                t = '<a class="btn btn-success btn-sm" href="#" title="取消置顶"  mce_href="#" onclick="cancelTop(\''
                                    + row.id
                                    + '\')"><i class="fa fa-thumbs-o-down"></i></a> ';
                            } else {
                                t = '<a class="btn btn-default btn-sm" href="#" title="置顶"  mce_href="#" onclick="setTop(\''
                                    + row.id
                                    + '\')"><i class="fa fa-thumbs-up"></i></a> ';
                            }

                            return e + d + f + t;
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
        title : '发布文章',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ ($(window).width()-20) + 'px', ($(window).height()-20) + 'px' ],
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
        title : '文章修改',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ ($(window).width()-20) + 'px', ($(window).height()-20) + 'px' ],
        content : prefix + '/edit?id=' + id
    });
}
function pub(id) {
    layer.confirm('确定要发布当前文章吗？', {
        btn : [ '确定', '取消' ]
    }, function() {
        $.ajax({
            url : prefix + "/pub",
            type : "post",
            data : {
                'id' : id
            },
            success : function(data) {
                if (data.code==0) {
                    layer.msg("发布成功");
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

function cancelPub(id) {
    layer.confirm('确定要取消发布当前文章吗？', {
        btn : [ '确定', '取消' ]
    }, function() {
        $.ajax({
            url : prefix + "/cancelPub",
            type : "post",
            data : {
                'id' : id
            },
            success : function(data) {
                if (data.code==0) {
                    layer.msg("取消发布成功");
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

function setTop(id) {
    layer.confirm('确定要置顶当前文章吗？', {
        btn : [ '确定', '取消' ]
    }, function() {
        $.ajax({
            url : prefix + "/setTop",
            type : "post",
            data : {
                'id' : id
            },
            success : function(data) {
                if (data.code==0) {
                    layer.msg("置顶成功");
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

function cancelTop(id) {
    layer.confirm('确定要取消置顶当前文章吗？', {
        btn : [ '确定', '取消' ]
    }, function() {
        $.ajax({
            url : prefix + "/cancelTop",
            type : "post",
            data : {
                'id' : id
            },
            success : function(data) {
                if (data.code==0) {
                    layer.msg("取消置顶成功");
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