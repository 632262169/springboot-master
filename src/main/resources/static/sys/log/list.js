/**
 * Created by Administrator on 2017/10/9.
 */
var prefix = "/admin/sys/log";
$(function() {
    loadData();
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
                        title:$('#title').val()
                    };
                },
                columns : [
                    {
                        field : 'title',
                        title : '操作名称'
                    },
                    {
                        field : 'requestUri',
                        title : '链接'
                    },
                    {
                        field : 'createByName',
                        title : '操作人'
                    },
                    {
                        field : 'createDate',
                        title : '操作时间'
                    },
                    {
                        title : '操作',
                        field : 'id',
                        align : 'center',
                        formatter : function(value, row, index) {
                            var e = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="详情" onclick="viewDetail(\''
                                + row.id
                                + '\')"><i class="fa fa-info"></i></a> ';
                            var d = '<a class="btn btn-warning btn-sm" href="#" title="删除"  mce_href="#" onclick="remove(\''
                                + row.id
                                + '\')"><i class="fa fa-remove"></i></a> ';
                            return e + d;
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
function viewDetail(id) {
    layer.open({
        type : 2,
        title : '查看详情',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ ($(window).width()-20) + 'px', ($(window).height()-20) + 'px' ],
        content : prefix + '/view?id=' + id
    });
}