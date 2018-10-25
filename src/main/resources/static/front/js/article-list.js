/**文章列表js*/
function page(pageNo, pageSize) {
    if(pageNo) $("#pageNo").val(pageNo);
    if(pageSize) $("#pageSize").val(pageSize);
    $("#dataForm").submit();
    return false;
}