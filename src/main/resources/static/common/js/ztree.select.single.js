/**
 * 快捷打开树形单选页面
 */
(function($) {
    $.fn.singleTreeSelect = function(options, param) {
        layer.open({
            type : 2,
            title : options.title,
            maxmin : false,
            shadeClose : true,
            area : [ '300px', '450px' ],
            content : '/admin/treeData/singleSelect?url=' + encodeURIComponent(options.url) +
            "&inputName=" + options.inputName +
            "&inputValue=" + options.inputValue +
            "&labelName=" + options.labelName +
            "&labelValue=" + options.labelValue +
            "&allowSelectParent=" + options.allowSelectParent
        });
    };

    $.fn.singleTreeSelect.defaults = {
        title: '单选',// 弹出框标题
        inputName : 'inputId',// 隐藏域输入框名称
        inputValue : '',// 隐藏域输入框的值
        labelName : 'inputLabel',// 显示输入框名称
        labelValue: '',//显示输入框的值
        type : "GET", // 请求数据的ajax类型
        url : null, // 请求数据的ajax的url
        checked: "N",//是否是多选，默认为否
        allowSelectParent: "N" //是否允许选择父节点，默认为允许
    };
})(jQuery);