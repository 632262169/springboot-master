/**文章详情js*/
$(function(){
    if (window.localStorage) {
        if (typeof(window.localStorage.name) !== 'undefined' && window.localStorage.name !== ''){
            $("#name").val(window.localStorage.name);
        }
        if (typeof(window.localStorage.email) !== 'undefined' && window.localStorage.email !== ''){
            $("#email").val(window.localStorage.email);
        }
        if (typeof(window.localStorage.webUrl) !== 'undefined' && window.localStorage.webUrl !== ''){
            $("#webUrl").val(window.localStorage.webUrl);
        }
    }


});
function submitComment() {

    if (window.localStorage) {
        window.localStorage.name = $("#name").val();
        window.localStorage.email = $("#email").val();
        window.localStorage.webUrl = $("#webUrl").val();
    }
    var idx = layer.load(1, {shade: false});
    $.ajax({
        type: 'post',
        url: '/submitComment',
        data: $('#commentForm').serialize(),
        // async: false,
        dataType: 'json',
        success: function (result) {
            layer.close(idx);
            if (result && result.code == 0) {
                var newComment = '<div class="am-u-sm-12 am-u-md-12 am-u-lg-12">' +
                    '                    <div>' +
                    '                        <span class="blog-color">'+$("#name").val()+'</span>' +
                    '                        <span class="am-fr">刚刚</span>' +
                    '                    </div>' +
                    '                    <p>'+$("#content").val()+'</p>' +
                    '                    <hr>' +
                    '                </div>';
                $("#commentList").prepend(newComment);
                $('#content').val("");
                layer.msg("操作成功");
            } else {
                if (result.msg) {
                    layer.msg(result.msg, {icon: 5});
                } else {
                    layer.msg("网络异常", {icon: 5});
                }
            }
        }
    });
    return false;
}