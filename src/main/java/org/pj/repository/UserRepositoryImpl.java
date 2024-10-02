package org.pj.repository;

import org.pj.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepositoryImpl implements IUserRepository {
    private final Map<String, User> userStore = new HashMap<>();

    public UserRepositoryImpl() {
        userStore.put("giangdh", new User("1", "giangdh", "G@123"));
        userStore.put("ngandt", new User("1", "ngandt", "N@456"));
        userStore.put("thanhnt", new User("1", "thanhnt", "T@789"));
    }

    @Override
    public User findByUsername(String username) {
        return userStore.get(username);
    }
}
