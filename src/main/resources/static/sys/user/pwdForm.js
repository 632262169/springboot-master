/**
 * Created by Administrator on 2017/10/24.
 */
$(document).ready(function() {
    validateForm();
});

function save() {
    $.ajax({
        type : "POST",
        url : "/admin/sys/user/updatePwd",
        data : $('#inputForm').serialize(),
        async : false,
        error : function(request) {
            layer.alert("网络异常");
        },
        success : function(data) {
            if (data.code == 0) {
                layer.msg("操作成功");
                $("#oldPassword").val("");
                $("#newPassword").val("");
                $("#reNewPassword").val("");
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
        messages: {
            reNewPassword: {equalTo: "输入与上面相同的密码"}
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