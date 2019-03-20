package com.arvin.la.common.exception;


import com.arvin.la.common.rest.ResultCode;

/**
 * 业务异常<p/>
 *
 * @author arvin.
 * @date 2019-03-20 14:27.
 */
public class BusinessException extends BaseException {
	private static final long serialVersionUID = 8624944628363400977L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message){
	    super(message);
    }

	public BusinessException(ResultCode resultCode) {
		super(resultCode);
	}

	public BusinessException(ResultCode resultCode, Throwable cause) {
		super(resultCode, cause);
	}


	public BusinessException(Throwable cause) {
		super(cause);
	}
}