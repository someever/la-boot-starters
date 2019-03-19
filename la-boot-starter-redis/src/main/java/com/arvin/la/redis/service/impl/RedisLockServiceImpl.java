package com.arvin.la.redis.service.impl;

import com.arvin.la.redis.service.RedisLockService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Collections;
import java.util.UUID;

/**
 * redis 分布式锁
 *
 * @author arvin
 * Date: 2018/4/8
 */
public class RedisLockServiceImpl implements RedisLockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLockServiceImpl.class);
    /**
     * 常量，加锁成功
     */
    private static final String LOCK_SUCCESS = "ok";
    /**
     * redis lock key 的前缀
     */
    private static final String REDIS_LOCK_KEY_PREFIX = "LOCK:";
    /**
     * 默认过期时间，300秒
     */
    private static final long DEFAULT_EXPIRE_TIME_SECONDS = 300;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean tryLock(String lockKey, String requestId, long expireTimeSeconds) {
        if(expireTimeSeconds < 1){
            expireTimeSeconds = DEFAULT_EXPIRE_TIME_SECONDS;
        }
        String result = set(lockKey,requestId,expireTimeSeconds);
        LOGGER.info("GET DISTRIBUTED LOCK : LOCKKEY={},REQUESTID={},EXPIRETIMESECONDS={},RUSULT={}",lockKey,requestId,expireTimeSeconds,result);
        return LOCK_SUCCESS.equals(result);
    }

    @Override
    public boolean unLock(String lockKey, String requestId) {
        boolean result = delByLua(lockKey,requestId);
        return result;
    }

    @Override
    public boolean canRunJob(String jobKey) {
        return canRunJob(jobKey,DEFAULT_EXPIRE_TIME_SECONDS);
    }

    @Override
    public boolean canRunJob(String jobKey, long expireTimeSeconds) {
        if(expireTimeSeconds < 1){
            expireTimeSeconds = DEFAULT_EXPIRE_TIME_SECONDS;
        }
        String requestId = UUID.randomUUID().toString();
        return tryLock(jobKey,requestId,expireTimeSeconds);
    }

    /**
     * redis set命令， 带nx、ex参数（一条指令保证原子性）
     * @param lockKey key
     * @param requestId 流水号
     * @param expireTimeSeconds 过期时间，单位：秒
     * @return 指令执行结果
     */
    private String set(final String lockKey, final String requestId, final long expireTimeSeconds){

        if(StringUtils.isBlank(lockKey) || StringUtils.isBlank(requestId)){
            LOGGER.info("GET DISTRIBUTED LOCK ILLEGAL ARGS : LOCKKEY={},REQUESTID={}",lockKey,requestId);
            return "FAILURE";
        }

        RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
        return redisTemplate.execute((RedisCallback<String>) connection -> redisSerializer.deserialize(
                (byte[]) connection.execute("set", (REDIS_LOCK_KEY_PREFIX+lockKey).getBytes(),
                        requestId.getBytes(),"NX".getBytes(),"EX".getBytes(),
                        String.valueOf(expireTimeSeconds).getBytes())
        ));
    }

    /**
     * 使用lua脚本安全删除，只有在value匹配的情况下会删除
     * @param lockKey key
     * @param requestId 流水号
     * @return boolean 执行指令结果
     */
    private boolean delByLua(String lockKey, String requestId){

        if(StringUtils.isBlank(lockKey) || StringUtils.isBlank(requestId)){
            LOGGER.info("GET DISTRIBUTED LOCK ILLEGAL ARGS : LOCKKEY={},REQUESTID={}",lockKey,requestId);
            return false;
        }

        String script = "if redis.call('get',KEYS[1]) == ARGV[1]" +
                " then " +
                "return redis.call('del',KEYS[1]) " +
                "else " +
                "return 0 " +
                "end";
        return redisTemplate.execute(new DefaultRedisScript<>(script,Boolean.class),
                Collections.singletonList(REDIS_LOCK_KEY_PREFIX + lockKey),requestId);
    }
}
