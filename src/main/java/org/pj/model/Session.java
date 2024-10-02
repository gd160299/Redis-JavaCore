package org.pj.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Session implements Serializable {
    private String sessionId;
    private User user;
    private Timestamp createTime;
    private Timestamp lastAccessTime;

    public Session() {
    }

    public Session(String sessionId, User user) {
        this.sessionId = sessionId;
        this.user = user;
        this.createTime = new Timestamp(System.currentTimeMillis());
        this.lastAccessTime = new Timestamp(System.currentTimeMillis());
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public Timestamp getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Timestamp lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
}
