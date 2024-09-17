package org.pj.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RedisClient {
    private static final Logger logger = LogManager.getLogger(RedisClient.class);

    private JedisPool jedisPool;

    private RedisClient() {
        logger.info("Initializing RedisClient");
        jedisPool = new JedisPool("localhost", 6379);
    }

    // Lớp nội bộ tĩnh
    private static class RedisClientHelper {
        private static final RedisClient INSTANCE = new RedisClient();
    }

    public static RedisClient getInstance() {
        return RedisClientHelper.INSTANCE;
    }

    public Jedis getJedis() {
        logger.info("Getting Jedis resource from pool");
        return jedisPool.getResource();
    }
}
