/**
 * Created by Administrator on 2017/10/10.
 */
var tree;
$(document).ready(function() {
    if ($("#status").attr("value") && $("#status").attr("value") != "") {
        $("#status").val($("#status").attr("value"));
    }

    validateForm();

    var setting = {check:{enable:true,nocheckInherit:true},view:{selectedMulti:false},
        data:{simpleData:{enable:true}},callback:{beforeClick:function(id, node){
            tree.checkNode(node, !node.checked, true, true);
            return false;
        }}};

    // 用户-菜单
    //var zNodes=menuList;
    // 初始化树结构
    tree = $.fn.zTree.init($("#menuTree"), setting, menuList);
    // 不选择父节点
    tree.setting.check.chkboxType = { "Y" : "ps", "N" : "s" };
    // 默认选择节点
    if (selectedIds && selectedIds != null) {
        var ids = selectedIds.split(",");
        for(var i=0; i<ids.length; i++) {
            var node = tree.getNodeByParam("id", ids[i]);
            try{tree.checkNode(node, true, false);}catch(e){}
        }
    }

    // 默认展开全部节点
    tree.expandAll(true);
});

function getCheckedMenus() {
    var ids = [], nodes = tree.getCheckedNodes(true);
    for(var i=0; i<nodes.length; i++) {
        ids.push(nodes[i].id);
    }
    return ids;
}
function save() {
    $("#menuIds").val(getCheckedMenus());
    $.ajax({
        type : "POST",
        url : "/admin/sys/role/save",
        data : $('#inputForm').serialize(),
        async : false,
        error : function(request) {
            parent.layer.alert("网络异常");
        },
        success : function(data) {
            if (data.code == 0) {
                parent.layer.msg("操作成功");
                parent.reLoad();
                var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                parent.layer.close(index);
            } else {
                if (data.msg) {
                    parent.layer.msg(data.msg);
                } else {
                    parent.layer.msg("没有权限");
                }
            }
        }
    });

}
function validateForm() {
    $("#inputForm").validate({
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
        rules: {
            name: {remote: "/admin/sys/role/checkLoginNameAjax?oldName=" + encodeURIComponent($("#oldName").val())}
        },
        messages: {
            name: {remote: "角色名已存在"},
        },
        submitHandler: function(form){
            //loading('正在提交，请稍等...');
            //form.submit();
            save();
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
}