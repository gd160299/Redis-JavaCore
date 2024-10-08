package org.pj.service;

import org.pj.model.Session;

public interface ISessionManager {
    void createSession(String sessionId, Session session);
    Session getSession(String sessionId);
    void updateSession(String sessionId, Session session);
    void deleteSession(String sessionId);
}
