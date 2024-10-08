package org.pj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pj.model.Session;
import org.pj.redis.RedisClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.sql.Timestamp;

public class SessionManagerImpl implements ISessionManager {
    private static final Logger logger = LogManager.getLogger(SessionManagerImpl.class);

    private final ObjectMapper objectMapper;
    private static final int SESSION_TIMEOUT = 1800; // 30 phút
    private final RedisClient redisClient;
    private static final String SESSION_PREFIX = "session:";
    private static final Integer DELETE_SUCCESSFUL = 1;

    public SessionManagerImpl(RedisClient redisClient) {
        logger.info("Initializing RedisSessionManager");
        this.redisClient = redisClient;
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Xử lý cho phần LocalDateTime của class Session
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void createSession(String sessionId, Session session) {
        logger.info("Begin createSession");

        try (Jedis jedis = redisClient.getJedis()) {
            String key = new StringBuilder(SESSION_PREFIX).append(sessionId).toString();
            String value = objectMapper.writeValueAsString(session);

            // Sử dụng SET với tùy chọn NX để đảm bảo không ghi đè nếu key đã tồn tại
            SetParams params = new SetParams();
            params.nx();  // Chỉ set nếu key chưa tồn tại
            params.ex(SESSION_TIMEOUT); // Thiết lập timeout cho session

            String result = jedis.set(key, value, params);

            if ("OK".equals(result)) {
                logger.info("Session created successfully for sessionId: {}", sessionId);
            } else {
                logger.info("Session creation failed for sessionId: {}", sessionId);
            }
            logger.info("End createSession");
        } catch (JsonProcessingException e) {
            logger.error("JsonProcessingException in createSession:", e);
        }
    }

    @Override
    public Session getSession(String sessionId) {
        logger.info("Begin getSession");
        Session session = null;
        try (Jedis jedis = redisClient.getJedis()) {
            String key = new StringBuilder(SESSION_PREFIX).append(sessionId).toString();
            String value = jedis.get(key);
            if (value != null) {
                jedis.expire(key, SESSION_TIMEOUT); // Cập nhật thời gian hết hạn cho session
                session = objectMapper.readValue(value, Session.class);
                session.setLastAccessTime(new Timestamp(System.currentTimeMillis()));
                updateSession(sessionId, session); // Cập nhật session
                logger.info("Session retrieved successfully for sessionId: {}", sessionId);
            } else {
                logger.info("Session not found for sessionId: {}", sessionId);
            }
            logger.info("End getSession");
        } catch (Exception e) {
            logger.error("Exception in getSession:", e);
        }
        return session;
    }

    @Override
    public void updateSession(String sessionId, Session session) {
        logger.info("Begin updateSession");
        try (Jedis jedis = redisClient.getJedis()) {
            String key = new StringBuilder(SESSION_PREFIX).append(sessionId).toString();
            String value = objectMapper.writeValueAsString(session);

            SetParams params = new SetParams().xx().ex(SESSION_TIMEOUT); // xx: chỉ update nếu key tồn tại
            String result = jedis.set(key, value, params);

            if ("OK".equals(result)) {
                logger.info("Session updated successfully for sessionId: {}", sessionId);
            } else {
                logger.info("Session update failed for sessionId: {}", sessionId);
            }
            logger.info("End updateSession");
        } catch (JsonProcessingException e) {
            logger.error("Exception in updateSession:", e);
        }
    }

    @Override
    public void deleteSession(String sessionId) {
        logger.info("Begin deleteSession");
        Jedis jedis = null;
        try {
            jedis = redisClient.getJedis();
            String key = new StringBuilder(SESSION_PREFIX).append(sessionId).toString();
            long result = jedis.del(key);

            if (DELETE_SUCCESSFUL == result) {
                logger.info("Session deleted successfully for sessionId: {}", sessionId);
            } else {
                logger.info("Session deletion failed for sessionId: {}", sessionId);
            }
            logger.info("End deleteSession");
        }
        catch (Exception e) {
            logger.error("Exception in deleteSession:", e);
        }
    }
}
