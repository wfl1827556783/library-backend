package com.library.service;

import com.library.entity.User;

public interface UserService {

    User findByUsername(String username);

    User register(User user);
}
