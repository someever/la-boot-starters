package com.arvin.la.common.exception;

import com.arvin.la.common.constant.ResultCodeEnum;
import com.arvin.la.common.rest.ResultCode;

import java.io.Serializable;

/**
 * 自定义参数校验model，
 * 该model节后hibernate valid进行校验
 *
 * @author arvin.
 * @date 2019-03-20 14:27.
 */
public class FieldValidError implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * 错误码
     */
    private int code;
    /**
     * 错误信息
     */
    private String message;
    /**
     * 错误列名称
     */
    private String field;

    public FieldValidError() {
        this(ResultCodeEnum.INVALID_REQUEST);
    }

    public FieldValidError(ResultCode resultCode) {
        this(resultCode.getCode(),resultCode.getMsg());
    }

    public FieldValidError(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public FieldValidError(ResultCode resultCode, String field) {
        this(resultCode.getCode(),resultCode.getMsg());
        this.field = field;
    }
    public FieldValidError(int code, String message, String field) {
        this.code = code;
        this.message = message;
        this.field = field;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
