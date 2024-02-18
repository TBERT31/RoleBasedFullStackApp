package com.tbert31.admin.dao;

import com.tbert31.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
