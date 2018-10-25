/**侧边栏js*/
$(document).ready(function(){
    initTag();

    initCategory();
});

/**
 * 初始化分类
 */
function initCategory() {
    try {
        $.ajax({
            type : "POST",
            url : "/getCategoryList",
            error : function(request) {
            },
            success : function(data) {
                if (data && data.length > 0) {
                    var cate_setting = {
                        view: {
                            showLine: false
                        },
                        data: {
                            simpleData: {
                                enable: true
                            }
                        }, callback: {
                            onClick:function(event, treeId, treeNode){
                                if (!treeNode.isParent) {
                                    window.location.href = "/article-list?categoryId=" + treeNode.id;
                                }
                            }
                        }
                    };
                    var cate_nodes = data;
                    $.fn.zTree.init($("#categoryList"), cate_setting, cate_nodes);
                    var treeObj = $.fn.zTree.getZTreeObj("categoryList");
                    treeObj.expandAll(true);
                }
            }
        });
    } catch (e) {
    }
}

/**
 * 初始化标签
 */
function initTag() {
    try {
        $.ajax({
            type : "POST",
            url : "/getTagList",
            error : function(request) {
            },
            success : function(data) {
                if (data && data.length > 0) {
                    for (var i = 0; i < data.length; i++) {
                        var tag = data[i];
                        $("#tagList").append('<a href="/article-list?tag='+tag.name+'" class="blog-tag">'+tag.name+'</a>');
                    }
                }
            }
        });
    } catch (e) {
    }
}