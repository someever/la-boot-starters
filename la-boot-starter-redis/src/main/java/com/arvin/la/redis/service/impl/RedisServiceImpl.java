package com.arvin.la.redis.service.impl;

import com.arvin.la.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis 操作类
 *
 * @author arvin
 * Date: 2018/3/15
 */
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void set(String key, Object object) {
        redisTemplate.opsForValue().set(key,object);
    }

    @Override
    public void set(String key, Object object, long timeout) {
        redisTemplate.opsForValue().set(key,object,timeout, TimeUnit.SECONDS);
    }

    @Override
    public Object get(String key) {
        return  redisTemplate.opsForValue().get(key);
    }

    @Override
    public void multiSet(Map<String, Object> paramMap) {
        redisTemplate.opsForValue().multiSet(paramMap);
    }

    @Override
    public List<Object> multiGet(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void hSet(String key, String field, Object object) {
        redisTemplate.opsForHash().put(key,field,object);
    }

    @Override
    public Object hGet(String key, String field) {
        return  redisTemplate.opsForHash().get(key,field);
    }

    @Override
    public void hMultiSet(String key, Map<String, Object> paramMap) {
        redisTemplate.opsForHash().putAll(key,paramMap);
    }

    @Override
    public List<Object> hMultiGet(String key, List<String> fields) {
        HashOperations<String,String,Object> hashOperations = redisTemplate.opsForHash();
        return  hashOperations.multiGet(key,fields);
    }

    @Override
    public Map<String, Object> hGetAll(String key) {
        HashOperations<String,String,Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    @Override
    public void hDel(String key, String... fileds) {
        redisTemplate.opsForHash().delete(key,fileds);
    }

    @Override
    public boolean expire(String key, long timeout) {
        return redisTemplate.expire(key,timeout,TimeUnit.SECONDS);
    }

    @Override
    public boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key,date);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void deleteAll(List<String> keys) {
        redisTemplate.delete(keys);
    }

    @Override
    public void lpush(String key, Object object) {
        redisTemplate.opsForList().leftPush(key,object);
    }

    @Override
    public Object rpop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    @Override
    public void rpush(String key, Object object) {
        redisTemplate.opsForList().rightPush(key,object);
    }

    @Override
    public Object lpop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    @Override
    public long size(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public void trim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key,start,end);
    }
}
