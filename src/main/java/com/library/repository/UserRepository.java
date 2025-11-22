package com.library.repository;

import com.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // 用于登录查找用户
    User findByUsername(String username);
}
