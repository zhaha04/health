package com.itheima.health.service;

import com.itheima.health.pojo.Role;

import java.util.List;

public interface RoleService {

    /**
     * 查询所有角色
     */
    List<Role> findAllRole();
}
