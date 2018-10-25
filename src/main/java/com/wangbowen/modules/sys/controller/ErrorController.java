package com.wangbowen.modules.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.wangbowen.common.base.SuperController;

@Controller
public class ErrorController extends SuperController {
	@GetMapping("/400")
	String error400() {
		return "error/400";
	}

	@GetMapping("/404")
	String error404() {
		return "error/404";
	}

	@GetMapping("/500")
	String error500() {
		return "error/500";
	}

    @GetMapping("/error")
    String error() {
        return "error/error";
    }


}
