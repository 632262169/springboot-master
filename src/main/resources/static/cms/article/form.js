/**
 * Created by Administrator on 2017/10/10.
 */
var prefix = "/admin/cms/article";
$(document).ready(function() {
    if ($("#status").attr("value") && $("#status").attr("value") != "") {
        $("#status").val($("#status").attr("value"));
    }
    if ($("#allowComment").attr("value") && $("#allowComment").attr("value") != "") {
        $("#allowComment").val($("#allowComment").attr("value"));
    }

    <!-- 实例化编辑器 -->
    var ue = UE.getEditor('container');

    $("#selectCategoryBtn").click(function(){
        $(this).singleTreeSelect({
            title: '选择上级菜单',// 弹出框标题
            inputName : 'categoryId',// 隐藏域输入框名称
            inputValue : $("#categoryId").val(),// 隐藏域输入框的值
            labelName : 'categoryName',// 显示输入框名称
            labelValue: $("#categoryName").val(),//显示输入框的值
            url : "/admin/cms/category/treeData", // 请求数据的ajax的url
            allowSelectParent: 'N' //是否允许选择父节点，默认为Y
        });
    });

    // Tags Input
    $('#tags').tagsInput({
        width: '100%',
        height: '35px',
        defaultText: '输入标签'
    });


    validateForm();
});

function save() {
    var idx = parent.layer.load(1, {shade: false});
    $.ajax({
        type : "POST",
        url : prefix + "/save",
        data : $('#inputForm').serialize(),
        // async : false,
        error : function(request) {
            parent.layer.alert("网络异常");
        },
        success : function(data) {
            parent.layer.close(idx);
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
            shortTitle: {remote: "/admin/cms/article/checkShortTitleAjax?oldShortTitle=" + encodeURIComponent($("#oldShortTitle").val())}
        },
        messages: {
            shortTitle: {remote: "短标题已存在"}
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

//当AJAX上传图片操作
function ajaxFileUpload(inputId) {
    var index = layer.load(1, {
        shade: [0.1,'#fff'] //0.1透明度的白色背景
    });
    $.ajaxFileUpload
    (
        {
            url: '/admin/cms/article/ajaxImageUpload?imageType=' + inputId, //用于文件上传的服务器端请求地址up参数标记此次是上传操作还是裁剪操作
            secureuri: false, //一般设置为false，是否安全上传
            fileElementId: inputId + "File", //文件上传控件的id属性  <input type="file" id="file" name="file" />
            dataType: 'json', //返回值类型 一般设置为json 期望服务器传回的数据类型
            success: function (data, status)  //服务器成功响应处理函数
            {
                var result = data.result;
                if (result && result == "success") {
                    var imagePath = data.img_path;
                    var imageUrl = data.img_url;
                    $("#show_" + inputId).html("<p><img style='max-height: 200px;max-width: 200px;' src='"+imageUrl+"'>" +
                        "<a href='javascript:void(0)' class='deleteImage' onclick='deleteImage(this)'>删除</a>" +
                        "<input type='hidden' name='"+inputId+"' value='"+imagePath+"'></p>"
                        );
                } else {
                    if (typeof (data.error) != 'undefined') {
                        if (data.error != '') {
                            layer.msg(data.error);
                        } else {
                            layer.msg(data.msg);
                        }
                    }
                }
                $("#" + inputId + "File").val("");
                layer.close(index);
            },
            error: function (data, status, e)//服务器响应失败处理函数
            {
                top.$.jBox.tip("系统错误：请检查图片尺寸及大小是否符合要求", 'error');
                $("#" + inputId + "File").val("");
                layer.close(index);
            }
        }
    );
    return false;
}

function deleteImage(obj) {
    $(obj).parent().remove();
}