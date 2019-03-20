package com.arvin.la.common.rest;

/**
 * 返回编码抽象接口，用于返回统一处理
 * 所有的返回编码枚举都有实现该接口
 * @author arvin.
 * @date 2019-03-20 14:26.
 */
public interface ResultCode {
    /**
     * code 码值
     * @return 码值
     */
    int getCode();

    /**
     * msg 话术
     * @return 话术
     */
    String getMsg();
}
