package com.library.service;

import com.library.entity.User;
import java.util.List;

public interface UserService {

    User findByUsername(String username);

    User findById(Long id);

    User register(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    List<User> findAll();
}
