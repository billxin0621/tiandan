package com.sinosoft.fragins.framework.exception;

import lombok.Getter;

/**
 * 业务异常
 *
 * @author panyu
 */
@SuppressWarnings("serial")
@Getter
public class BusinessException extends RuntimeException {

	private Integer status;

	/**
	 * 从异常cause中找到BusinessException
	 *
	 * @param t 任何异常对象
	 * @return 如果没有找到返回null
	 */
	public static BusinessException getBusinessExceptionFromCause(Throwable t) {
		Throwable t1 = t;
		while (t1 != null) {
			if (t1 instanceof BusinessException) {
				return (BusinessException) t1;
			}
			if (t1 == t1.getCause()) { // 防止自己cause死循环
				break;
			}
			t1 = t1.getCause();
		}
		return null;
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message, Integer status) {
		super(message);
		this.status = status;
	}

}
