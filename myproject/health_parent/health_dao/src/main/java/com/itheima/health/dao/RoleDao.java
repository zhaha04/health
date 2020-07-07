package com.itheima.health.dao;

import com.itheima.health.pojo.Role;

import java.util.List;

public interface RoleDao {

    /**
     * 查询所有角色
     */
    List<Role> findAllRole();
}
