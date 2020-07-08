package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.PermissionDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 根据名称查询
     */
    @Override
    public Permission findPermissionByPermissionName(String permissionName) {
        return permissionDao.findPermissionByPermissionName(permissionName);
    }

    /**
     * 通过用户ID查询拥有的角色ID
     */
    @Override
    public List<Integer> findRoleIdsByPermissionId(int id) {
        return permissionDao.findRoleIdsByPermissionId(id);
    }

    /**
     * 分页查询用户
     */
    @Override
    public PageResult<Permission> findPermissionByPage(QueryPageBean queryPageBean) {

        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 查询条件
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 模糊查询 %
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 条件查询，这个查询语句会被分页
        Page<Permission> page = permissionDao.findPermissionByCondition(queryPageBean.getQueryString());
        return new PageResult<Permission>(page.getTotal(), page.getResult());

    }

    /**
     * 根据用户ID删除用户
     */
    @Override
    @Transactional
    public void deletePermissionById(int id) {

        //删除用户角色表中的对应用户信息
        permissionDao.deletePermissionRoleById(id);

        //删除用户表中的对应用户
        permissionDao.deletePermissionById(id);
    }

    /**
     * 添加用户，同时添加角色信息
     */
    @Override
    @Transactional
    public void addPermission(Permission permission, Integer[] roleIds) {

        //添加无角色信息的新用户
        permissionDao.addPermission(permission);

        //获取用户id
        Integer permissionId = permission.getId();
        //添加用户与角色的关系
        if (null != roleIds){

            for (Integer roleId : roleIds){

                permissionDao.addPermissionRole(permissionId,roleId);
            }
        }
    }

    /**
     * 通过ID查询用户
     */
    @Override
    public Permission findPermissionById(int id) {
        return permissionDao.findPermissionById(id);
    }

    /**
     * 修改用户
     */
    @Override
    @Transactional
    public void updatePermission(Permission permission, Integer[] roleIds) {

        //更新用户基本信息
        permissionDao.updatePermission(permission);

        //删除旧的用户角色信息
        permissionDao.deletePermissionRoleById(permission.getId());
        //添加新的用户角色信息
        for (Integer roleId : roleIds){

            permissionDao.addPermissionRole(permission.getId(),roleId);
        }
    }
}
