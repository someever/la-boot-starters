package com.arvin.la.common.exception;


import com.arvin.la.common.constant.ResultCodeEnum;
import com.arvin.la.common.rest.ResultCode;

/**
 * 异常父类，所有异常均可继承该类
 *
 * @author arvin.
 * @date 2019-03-20 14:27.
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * 错误码
     */
    private int errorCode;

    public BaseException() {
        this(ResultCodeEnum.SYSTEM_EXCEPTION);
    }

    public BaseException(String message){
        super(message);
    }

    public BaseException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.errorCode = resultCode.getCode();
    }

    public BaseException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMsg(), cause);
        this.errorCode = resultCode.getCode();
    }

    public BaseException(Throwable cause) {
        this(ResultCodeEnum.SYSTEM_EXCEPTION,cause);
    }

    public BaseException(ResultCode resultCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(resultCode.getMsg(), cause, enableSuppression, writableStackTrace);
        this.errorCode = resultCode.getCode();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
