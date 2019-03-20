package com.arvin.la.common.exception;


import com.arvin.la.common.constant.ResultCodeEnum;
import com.arvin.la.common.rest.ResultCode;

/**
 * 权限异常<p/>
 * 默认code 401
 *
 * @author arvin.
 * @date 2019-03-20 14:27.
 */
public class PermissionException extends BaseException {
	private static final long serialVersionUID = 8620044635365090977L;

	public PermissionException() {
		super(ResultCodeEnum.UNAUTHORIZED);
	}

	public PermissionException(ResultCode resultCode) {
		super(resultCode);
	}

	public PermissionException(ResultCode resultCode, Throwable cause) {
		super(resultCode, cause);
	}


	public PermissionException(Throwable cause) {
		super(cause);
	}
}