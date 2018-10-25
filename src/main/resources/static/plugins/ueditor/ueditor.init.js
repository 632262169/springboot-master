UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
UE.Editor.prototype.getActionUrl = function(action) {
    if (action == "uploadimage") {
        return "/admin/sys/ueditor/uploadImage";
    } else if (action == "catchimage") {
        return "/admin/sys/ueditor/catchImage";
    } else if (action == "uploadvideo") {
        return "/admin/sys/ueditor/uploadvideo";
    } else if (action == "uploadfile") {
        return "/admin/sys/ueditor/uploadFile";
    } else if (action == "listfile") {
        return "/admin/sys/ueditor/listFile";
    } else {
        return this._bkGetActionUrl.call(this, action);
    }
}