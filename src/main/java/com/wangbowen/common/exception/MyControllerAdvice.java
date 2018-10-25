package com.wangbowen.common.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 统一异常请求处理
 * 如：shiro请求页面没有权限的处理等
 */
@ControllerAdvice
public class MyControllerAdvice {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@ExceptionHandler(UnauthenticatedException.class)
	public String handleUnauthenticatedException(UnauthenticatedException e) {
		logger.error("没有认证：{}", e.getMessage());
		return "admin/403";
	}

    @ExceptionHandler(AuthorizationException.class)
    public String handleAuthorizationException(AuthorizationException e) {
        logger.error("没有授权：{}", e.getMessage());
        return "admin/403";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return "error/500";
    }
}
