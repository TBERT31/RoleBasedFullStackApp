package com.tbert31.admin.dao;

import com.tbert31.admin.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
