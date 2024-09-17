package org.pj.repository;

import org.pj.model.User;

public interface UserRepository {
    User findByUsername(String username);
}
