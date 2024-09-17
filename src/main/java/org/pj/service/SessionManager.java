package org.pj.service;

import org.pj.model.Session;

public interface SessionManager {
    Session createSession(String sessionId, Session session);
    Session getSession(String sessionId);
    void updateSession(String sessionId, Session session);
    void deleteSession(String sessionId);
}
