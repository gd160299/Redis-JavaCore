package org.pj.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.JedisPoolConfig;

public class RedisClient {
    private static final Logger logger = LogManager.getLogger(RedisClient.class);

    private final JedisPool jedisPool;

    public RedisClient(String host, int port) {
        logger.info("Initializing RedisClient with connection pool");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(25);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(5);
        poolConfig.setMaxWaitMillis(2000);
        jedisPool = new JedisPool(poolConfig, host, port);
    }

    public Jedis getJedis() {
        logger.info("Getting Jedis resource from pool");
        return jedisPool.getResource();
    }

    public void close() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }
}
