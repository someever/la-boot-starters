package com.arvin.la.redis.service;

/**
 * redis 分布式锁
 *
 * @author arvin
 * Date: 2018/4/8
 */
public interface RedisLockService {
    /**
     * 尝试获取锁，立即返回<p/>
     * 加锁时需要传入一个流水号requestId，用来确保不被其他节点、其他线程删除或者覆盖
     * @param lockKey 锁的key
     * @param requestId 流水号，建议使用uuid生成确保唯一性
     * @param expireTimeSeconds 过期时间，单位秒
     * @return boolean 是否成功获取锁
     */
    boolean tryLock(String lockKey, String requestId, long expireTimeSeconds);

    /**
     * 解锁<p/>
     * 解锁时校验流水号requestId，避免持有过期锁的用户误删
     * @param lockKey key
     * @param requestId 流水号
     * @return 是否成功解锁
     */
    boolean unLock(String lockKey, String requestId);

    /**
     * job加锁，expireTimeSeconds 秒后自动解锁
     * @param jobKey jobkey
     * @return 是否获取到锁
     */
    boolean canRunJob(String jobKey);
    /**
     * job加锁，默认5分钟自动解锁
     * @param jobKey jobkey
     * @param expireTimeSeconds 过期时间，单位：秒
     * @return 是否获取到锁
     */
    boolean canRunJob(String jobKey, long expireTimeSeconds);
}
