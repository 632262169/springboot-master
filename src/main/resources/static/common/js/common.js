/**
 * 公共方法
 * Created by Administrator on 2017/10/11.
 */

// 获取URL地址参数
function getQueryString(name, url) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    if (!url || url == ""){
        url = window.location.search;
    }else{
        url = url.substring(url.indexOf("?"));
    }
    r = url.substr(1).match(reg)
    if (r != null) return unescape(r[2]); return null;
}

function openIconSelectPage(inputName, labelName) {
    layer.open({
        type : 2,
        title : '选择图标',
        maxmin : false,
        shadeClose : true,
        area : [ ($(window).width()-20) + 'px', ($(window).height()-20) + 'px' ],
        content : '/admin/treeData/iconSelect?inputName=' + inputName + "&labelName=" + labelName
    });
}

// 保存cookie操作
function setCookie(name, value, options) {
    if (typeof value !== 'undefined') { // name and value given, set cookie
        options = options || {};
        if (value === null) {
            value = '';
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires === 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires === 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
        }
        var path = options.path ? '; path=' + options.path : '';
        var domain = options.domain ? '; domain=' + options.domain : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    }
}

// 读取cookie操作
function getCookie(name) {
    var cookieValue = null;
    if (document.cookie && document.cookie != '') {
        var cookies = document.cookie.split(';');
        for (var i = 0; i < cookies.length; i++) {
            var cookie = jQuery.trim(cookies[i]);
            // Does this cookie string begin with the name we want?
            if (cookie.substring(0, name.length + 1) == (name + '=')) {
                cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }
    return cookieValue;
}