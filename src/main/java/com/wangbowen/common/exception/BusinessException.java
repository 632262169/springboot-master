package com.wangbowen.common.exception;

public class BusinessException extends Exception {

	private static final long serialVersionUID = -5995434434196299378L;

	private String businessCode;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String code, String message) {
		super(message);
		businessCode = code;
	}

	public BusinessException(String message, Throwable t) {
		super(message, t);
	}

	public BusinessException(String code, String message, Throwable t) {
		super(message, t);
		businessCode = code;
	}

	public String getBusinessCode() {
		return businessCode;
	}
}
