package com.arvin.la.common.exception;


import com.arvin.la.common.constant.ResultCodeEnum;
import com.arvin.la.common.rest.ResultCode;

/**
 * 校验异常<p/>
 *
 * @author arvin.
 * @date 2019-03-20 14:27.
 */
public class InvalidException extends BaseException {
	private static final long serialVersionUID = 8624944628363090977L;

	public InvalidException() {
		super(ResultCodeEnum.INVALID_REQUEST);
	}

	public InvalidException(ResultCode resultCode) {
		super(resultCode);
	}

	public InvalidException(ResultCode resultCode, Throwable cause) {
		super(resultCode, cause);
	}


	public InvalidException(Throwable cause) {
		super(cause);
	}
}