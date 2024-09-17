package org.pj.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Session implements Serializable {
    private String sessionId;
    private User user;
    private LocalDateTime createTime;
    private LocalDateTime lastAccessTime;

    public Session() {}

    public Session(String sessionId, User user) {
        this.sessionId = sessionId;
        this.user = user;
        this.createTime = LocalDateTime.now();
        this.lastAccessTime = LocalDateTime.now();
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(LocalDateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
}
