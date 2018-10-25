var default_config = {
    errorElement: "span",
    errorClass: "help-block",
    focusInvalid: !1,
    highlight: function(e) {
        $(e).closest(".form-group").addClass("has-error");
    },
    success: function(e) {
        $(e).closest(".form-group").removeClass("has-error");
        $(e).remove()
    },
    submitHandler: function(form){
        formSubmit(form);
    },
    errorPlacement: function(error, element) {
        error.insertAfter(element.closest(".input-icon"));
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
};