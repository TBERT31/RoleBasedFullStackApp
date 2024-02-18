package com.tbert31.admin.service;

import com.tbert31.admin.entity.User;

public interface UserService {
    User loadUserByEmail(String email);

    User createUser(String email, String password);

    void assignRoleToUser(String email, String roleName);


}
