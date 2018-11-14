package com.exception;


/**
 * 缓存异常
 * 
 * @author Joe
 */
public class CacheException extends ApplicationException{

	private static final long serialVersionUID = -2678203134198782909L;
	
	public static final String MESSAGE = "缓存异常";

	public CacheException() {
		super(MESSAGE);
	}

	public CacheException(String message) {
		super(message);
		this.code = 000;
	}
	
	public CacheException(int code, String message) {
		super(message);
		this.code = code;
	}

	public CacheException(String message, Throwable cause) {
		super(message, cause);
		this.code = 000;
	}
	
	public CacheException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public CacheException(Throwable cause) {
		super(cause);
		this.code = 999;
	}
}
