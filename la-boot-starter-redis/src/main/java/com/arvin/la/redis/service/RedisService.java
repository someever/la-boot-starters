package com.arvin.la.redis.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Redis 操作接口
 *
 * @author arvin
 * Date: 2018/3/15
 */
public interface RedisService {

    /**
     * 存储对象（采用redis的string存储）
     * @param key key
     * @param object 对象
     */
    void set(String key, Object object);

    /**
     * 存储对象，包含过期时间
     * @param key key
     * @param object 对象
     * @param timeout 过期时间，单位：秒
     */
    void set(String key, Object object, long timeout);

    /**
     * 根据key获取存储对象
     * @param key key
     * @return 对象
     */
    Object get(String key);

    /**
     * 设置多key的同一类型对象集合
     * @param paramMap 参数map
     */
    void multiSet(Map<String, Object> paramMap);

    /**
     * 根据keys获取同一对象列表
     * @param keys keys列表
     * @return List<Object> 对象列表
     */
    List<Object> multiGet(List<String> keys);

    /**
     *Hash 存储（可用于存储对象或者保存对象的单个field）
     * @param key key
     * @param field field
     * @param object 对象
     */
    void hSet(String key, String field, Object object);

    /**
     *根据Hash表中key和field获取对应的value（可用于获取对象或者对象的单个field）
     * @param key key
     * @param field fie
     * @return Object 对象
     */
    Object hGet(String key, String field);

    /**
     *Hash批量设置（可用于存储多个对象或者保存单个对象的多个fields）
     * @param key key
     * @param paramMap hash表的fields和value对应的map
     */
    void hMultiSet(String key, Map<String, Object> paramMap);

    /**
     *根据hash表中的key和fields列表获取对应的values（可用于获取多个对象值或对象的多个属性）
     * @param key key
     * @param fields fields
     * @return List<Object> hash表中的key和fields列表获取对应的values
     */
    List<Object> hMultiGet(String key, List<String> fields);

    /**
     *根据hash表中key获取所有对象（可用于获取多个对象）
     * @param key hash表中key
     * @return Map<String,Object> hash表中对应的map对象
     */
    Map<String,Object> hGetAll(String key);

    /**
     *根据hash表中key和fields进行删除
     * @param key key
     * @param fileds fields
     */
    void hDel(String key, String... fileds);

    /**
     *设置key在多少秒后过期
     * @param key key
     * @param timeout 过期时间
     * @return boolean 是否设置成功
     */
    boolean expire(String key, long timeout);

    /**
     *设置key在固定时间后过期
     * @param key key
     * @param date 某个固定过期时间
     * @return boolean 是否设置成功
     */
    boolean expireAt(String key, Date date);

    /**
     *根据key删除一个对象
     * @param key key
     */
    void delete(String key);

    /**
     *根据keys删除多个对象
     * @param keys keys
     */
    void deleteAll(List<String> keys);

    /**
     *队列：从左边插入一数据
     * @param key key
     * @param object 对象
     */
    void lpush(String key, Object object);

    /**
     *队列: 从队列末尾获取一个数据
     * @param key key
     * @return object 对象
     */
    Object rpop(String key);

    /**
     *队列：从右边插入一数据
     * @param key key
     * @param object 对象
     */
    void rpush(String key, Object object);

    /**
     *队列：从队列开始获取一个数据
     * @param key key
     * @return object 对象
     */
    Object lpop(String key);

    /**
     * 对列：获取队列某个key的数量
     * @param key key
     * @return long 队列数量
     */
    long size(String key);

    /**
     * 队列：保留key指定范围内的列表值，其余均删除
     * @param key key
     * @param start 起始位置
     * @param end 结束位置
     */
    void trim(String key, long start, long end);

}
