package com.taylor.uuid.redis;

import com.taylor.common.PropertiesLoader;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class RedisLockInternals {

    private JedisPool jedisPool;

    /**
     * 重试等待时间
     */
    private int retryAwait = 300;

    private int lockTimeout = 2000;


    public RedisLockInternals(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public String tryRedisLock(String lockId, long time, TimeUnit unit) {
        final long startMillis = System.currentTimeMillis();
        final Long millisToWait = (unit != null) ? unit.toMillis(time) : null;
        String lockValue = null;
        while (lockValue == null) {
            lockValue = createRedisKey(lockId);
            if (lockValue != null) {
                break;
            }
            if (System.currentTimeMillis() - startMillis - retryAwait > millisToWait) {
                break;
            }
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(retryAwait));
        }
        return lockValue;
    }

    private String createRedisKey(String lockId) {
        Jedis jedis = null;
        try {
            String value = lockId + randomId(1);
            jedis = jedisPool.getResource();
            String password = PropertiesLoader.getResource().getPassword();
            if (password!=null&&!"".equals(password)){
                jedis.auth(password);
            }
            List<String> keys = new ArrayList<>();
            keys.add(lockId);
            List<String> args = new ArrayList<>();
            args.add(value);
            args.add(lockTimeout + "");
            Long ret = (Long) jedis.eval(RedisLockConstant.CREATE_KEY_LUA, keys, args);
            if (Long.valueOf(1).equals(ret)) {
                return value;
            }
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    void unlockRedisLock(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> keys = new ArrayList<>();
            keys.add(key);
            List<String> args = new ArrayList<>();
            args.add(value);
            jedis.eval(RedisLockConstant.UNLOCK_LUA, keys, args);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    private String randomId(int size) {
        char[] cs = new char[size];
        for (int i = 0; i < cs.length; i++) {
            cs[i] = RedisLockConstant.digits[ThreadLocalRandom.current().nextInt(RedisLockConstant.digits.length)];
        }
        return new String(cs);
    }

}
