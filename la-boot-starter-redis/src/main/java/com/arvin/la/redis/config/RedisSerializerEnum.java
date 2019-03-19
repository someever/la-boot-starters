package com.arvin.la.redis.config;

/**
 * redis 序列化方式枚举
 * <p/>
 *
 * @author arvin.
 * @date 2019-03-19 13:36.
 */
public enum RedisSerializerEnum {
    /**
     * jdk 序列化
     */
    JDK,
    /**
     * kryo 序列化
     */
    KRYO,
    /**
     * jackson 序列化
     */
    JACKSON
}
