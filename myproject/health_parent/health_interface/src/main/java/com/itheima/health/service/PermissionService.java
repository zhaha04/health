package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Permission;

import java.util.List;

public interface PermissionService {

    /**
     * 根据名称查询
     */
    Permission findPermissionByPermissionName(String permissionName);

    /**
     * 通过ID查询所拥有的角色ID
     */
    List<Integer> findRoleIdsByPermissionId(int id);

    

    /**
     * 分页查询菜单
     */
    PageResult<Permission> findPermissionByPage(QueryPageBean queryPageBean);

    /**
     * 根据ID删除菜单
     */
    void deletePermissionById(int id);

    /**
     * 添加菜单，同时添加角色信息
     */
    void addPermission(Permission permission,Integer[] roleIds);

    /**
     * 通过id查询菜单
     */
    Permission findPermissionById(int id);

    /**
     * 修改菜单
     */
    void updatePermission(Permission permission,Integer[] roleIds);
}
