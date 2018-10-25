/**
 * Created by Administrator on 2017/10/10.
 */
$(document).ready(function() {
    if ($("#status").attr("value") && $("#status").attr("value") != "") {
        $("#status").val($("#status").attr("value"));
    }

    validateForm();
});

function getCheckedRoles() {
    var roleIds = "";
    $("input:checkbox[name=role]:checked").each(function(i) {
        roleIds += $(this).val() + ",";
    });
    return roleIds;
}
function save() {
    $("#roleIds").val(getCheckedRoles());
    $.ajax({
        type : "POST",
        url : "/admin/sys/user/save",
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
            loginName: {remote: "/admin/sys/user/checkLoginNameAjax?oldLoginName=" + encodeURIComponent($("#oldLoginName").val())}
        },
        messages: {
            loginName: {remote: "用户登录名已存在"},
            confirmNewPassword: {equalTo: "输入与上面相同的密码"}
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