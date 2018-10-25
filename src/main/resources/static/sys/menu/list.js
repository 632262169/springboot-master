/**
 * Created by Administrator on 2017/10/9.
 */
var prefix = "/admin/sys/menu";
$(function() {
    loadData();

    $("#addBtn").click(function(){
        add();
    });

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
        .bootstrapTreeTable(
            {
                id : 'id',// 选取记录返回的值
                code : 'id',// 用于设置父子关系
                parentColumn : 'parentId',
                type : "GET", // 请求数据的ajax类型
                url : prefix + '/list', // 请求数据的ajax的url
                ajaxParams : {}, // 请求数据的ajax的data属性
                expandColumn : '0',// 在哪一列上面显示展开按钮
                striped : true, // 是否各行渐变色
                bordered : true, // 是否显示边框
                expandAll : true, // 是否全部展开
                columns : [

                    {
                        title : '名称',
                        field : 'name'
                    },

                    {
                        title : '图标',
                        field : 'icon',
                        align : 'center',
                        valign : 'middle',
                        formatter : function(item, index) {
                            return item.icon == null ? ''
                                : '<i class="fa fa-fw ' + item.icon
                            + '"></i>';
                        }
                    },
                    {
                        title : '类型',
                        field : 'type',
                        align : 'center',
                        valign : 'middle',
                        formatter : function(item, index) {
                            if (item.type === 0) {
                                return '<span class="label label-primary">目录</span>';
                            }
                            if (item.type === 1) {
                                return '<span class="label label-success">菜单</span>';
                            }
                            if (item.type === 2) {
                                return '<span class="label label-warning">权限</span>';
                            }
                        }
                    },
                    {
                        title : '地址',
                        field : 'href'
                    },
                    {
                        title : '权限标识',
                        field : 'permission'
                    },
                    {
                        title : '序号',
                        field : 'orderNo',
                        align : 'center',
                        formatter : function(row, index) {
                            var orderNo = row.orderNo;
                            if (!orderNo || orderNo == "null") {
                                orderNo = 0;
                            }
                            var ipt = '<input type="hidden" name="ids" value="' + row.id + '">' +
                                '<input type="text" name="orderNos" class="form-control required digits" ' +
                                'style="width: 100px" maxlength="6" ' +
                                'value="' + orderNo +'">';
                            return ipt;
                        }
                    },
                    {
                        field : 'status',
                        title : '状态',
                        align : 'center',
                        valign : 'middle',
                        formatter : function(item, index) {
                            if (item.status == '0') {
                                return '<span class="label label-danger">隐藏</span>';
                            } else if (item.status == '1') {
                                return '<span class="label label-primary">显示</span>';
                            }
                        }
                    },
                    {
                        title : '操作',
                        field : 'id',
                        align : 'center',
                        formatter : function(row, index) {
                            var e = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                + row.id
                                + '\')"><i class="fa fa-edit"></i></a> ';
                            var p = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="添加下级" onclick="add(\''
                                + row.id
                                + '\')"><i class="fa fa-plus"></i></a> ';
                            var d = '<a class="btn btn-warning btn-sm" href="#" title="删除"  mce_href="#" onclick="remove(\''
                                + row.id
                                + '\')"><i class="fa fa-remove"></i></a> ';
                            return e + d + p;
                        }
                    } ]
            });
}
function reLoad() {
    loadData();
}
function add(parentId) {
    // iframe层
    layer.open({
        type : 2,
        title : '新增菜单',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '400px', '500px' ],
        content : prefix + '/add?parentId=' + (parentId ? parentId : "")
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
        title : '菜单修改',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '400px', '500px' ],
        content : prefix + '/edit?id=' + id
    });
}

function saveSort() {
    $.ajax({
        type : "POST",
        url : prefix + "/saveSort",
        data : $('#sortForm').serialize(),
        async : false,
        error : function(request) {
            layer.alert("网络异常");
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
        }
    });
}
