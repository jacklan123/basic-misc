package com.lt.basic.util.lock;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * @Auther: yx
 * @Date: 2019-05-05 16:13
 * @Description: DistributedLocker
 */
@Slf4j
public class JedisDistributedLocker {
    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final String UNLOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    private static final int RETRY_TIME = 100;

    private Jedis jedisTemplate;

    public JedisDistributedLocker(Jedis jedisTemplate) {
        this.jedisTemplate = jedisTemplate;
    }

    /**
     * 尝试获取分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public boolean lock(String lockKey, String requestId, int expireTime) {

        String result = jedisTemplate.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }

        return false;
    }

    /**
     * 加锁
     * @param lockKey
     * @param waitTime    等待时间
     * @param expireTime 强制锁释放时间
     * @return
     */
    public boolean tryLock(String lockKey, String requestId, int waitTime, int expireTime) {
        int alreadyWaitTime = 0;
        while (waitTime > alreadyWaitTime) {
            if (lock(lockKey, requestId, expireTime)) {
                return true;
            } else {
                try {
                    Thread.sleep(RETRY_TIME);
                } catch (InterruptedException e) {
                    log.warn(e.getMessage(), e);

                    return false;
                }
            }

            alreadyWaitTime += RETRY_TIME;
        }

        return false;
    }

    /**
     * 释放分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean unlock(String lockKey, String requestId) {

        Object result = jedisTemplate.eval(UNLOCK_SCRIPT, Collections.singletonList(lockKey), Collections.singletonList(requestId));

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }
}