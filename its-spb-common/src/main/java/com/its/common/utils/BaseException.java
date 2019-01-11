package com.its.common.utils;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = -7667374945272239037L;

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String message, Throwable ex) {
		super(message, ex);
	}

}
