package com.arvin.la.common.constant;

import com.arvin.la.common.rest.ResultCode;

/**
 * 常用返回编码枚举
 * @author arvin.
 * @date 2019-03-20 14:26.
 */
public enum ResultCodeEnum implements ResultCode {

    /**
     *操作成功
     */
    SUCCESS(200,"操作成功！"),
    /**
     *系统异常，请稍后在试
     */
    SYSTEM_EXCEPTION(500,"系统异常，请稍后再试！"),
    /**
     *系统超时
     */
    SYSTEM_TIMEOUT(504,"系统超时，请重试！"),
    /**
     *非法参数
     */
    INVALID_REQUEST(400,"非法参数！"),
    /**
     *非法访问
     */
    UNAUTHORIZED(401,"非法访问！"),
    /**
     *记录不存在
     */
    NOT_FOUND(404,"记录不存在！")
    ;

    private int code;
    private String msg;

    ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
