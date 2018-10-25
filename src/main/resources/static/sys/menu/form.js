/**
 * Created by Administrator on 2017/10/10.
 */
var prefix = "/admin/sys/menu";
$(document).ready(function() {
    if ($("#status").attr("value") && $("#status").attr("value") != "") {
        $("#status").val($("#status").attr("value"));
    }
    if ($("#type").attr("value") && $("#type").attr("value") != "") {
        $("#type").val($("#type").attr("value"));
    }

    $("#selectParentBtn").click(function(){
        $(this).singleTreeSelect({
            title: '选择上级菜单',// 弹出框标题
            inputName : 'parentId',// 隐藏域输入框名称
            inputValue : $("#parentId").val(),// 隐藏域输入框的值
            labelName : 'parentName',// 显示输入框名称
            labelValue: $("#parentName").val(),//显示输入框的值
            url : "/admin/sys/menu/treeData", // 请求数据的ajax的url
            allowSelectParent: 'Y' //是否允许选择父节点，默认为true
        });
    });

    $("#selectIconBtn").click(function(){
        openIconSelectPage("icon", "iconLabel");
    });

    validateForm();
});

function save() {
    $.ajax({
        type : "POST",
        url : prefix + "/save",
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