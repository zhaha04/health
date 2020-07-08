package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;

import java.util.List;

public interface RoleService {

    /**
     * 根据名称查询
     */
    Role findRoleByRoleName(String roleName);
    
    /**
     * 查询所有角色
     */
    List<Role> findAllRole();

    /**
     * 分页查询菜单
     */
    PageResult<Role> findRoleByPage(QueryPageBean queryPageBean);

    /**
     * 根据ID删除菜单
     */
    void deleteRoleById(int id);

    /**
     * 添加菜单，同时添加角色信息
     */
    void addRole(Role role,Integer[] roleIds);

    /**
     * 通过id查询菜单
     */
    Role findRoleById(int id);

    /**
     * 修改菜单
     */
    void updateRole(Role role,Integer[] roleIds);
}
