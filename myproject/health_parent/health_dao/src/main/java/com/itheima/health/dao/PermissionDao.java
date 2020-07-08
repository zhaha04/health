package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionDao {

    /**
     * 条件查询菜单
     */
    Page<Permission> findPermissionByCondition(String queryString);

    /**
     * 添加空菜单
     */
    void addPermission(Permission permission);

    /**
     * 绑定菜单与角色的关系
     */
    void addPermissionRole(@Param("permissionId")Integer permissionId, @Param("roleId")Integer roleId);

    /**
     * 删除菜单与角色的关系
     */
    void deletePermissionRoleById(int id);

    /**
     * 删除菜单
     */
    void deletePermissionById(int id);

    /**
     * 根据ID查询菜单
     */
    Permission findPermissionById(int id);

    /**
     * 根据ID查询菜单拥有的角色ID
     */
    List<Integer> findRoleIdsByPermissionId(int id);

    /**
     * 根据名称查询
     */
    Permission findPermissionByPermissionName(String permissionName);
    
    /**
     * 更新用户
     */
    void updatePermission(Permission permission);
}
