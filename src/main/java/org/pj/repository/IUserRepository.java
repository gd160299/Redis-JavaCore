package org.pj.repository;

import org.pj.model.User;

public interface IUserRepository {
    User findByUsername(String username);
}
