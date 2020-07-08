package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDao {

    /**
     * 查询所有角色
     */
    List<Role> findAllRole();

    /**
     * 条件查询
     */
    Page<Role> findRoleByCondition(String queryString);
    

    /**
     * 删除用户表中的对应用户
     */
    void deleteRoleById(int id);

    /**
     * 根据名称查询
     */
    Role findRoleByRoleName(String roleName);
    
    /**
     * 添加空用户
     */
    void addRole(Role role);

    /**
     * 通过ID查询角色
     */
    Role findRoleById(int id);

    /**
     * 更新用户
     */
    void updateRole(Role role);
}
