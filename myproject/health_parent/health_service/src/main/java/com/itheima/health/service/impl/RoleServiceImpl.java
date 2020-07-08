package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {
    
    @Autowired
    private RoleDao roleDao;

    /**
     * 查询所有角色
     */
    @Override
    public List<Role> findAllRole() {
        return roleDao.findAllRole();
    }

    /**
     * 分页查询用户
     */
    @Override
    public PageResult<Role> findRoleByPage(QueryPageBean queryPageBean) {

        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 查询条件
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 模糊查询 %
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 条件查询，这个查询语句会被分页
        Page<Role> page = roleDao.findRoleByCondition(queryPageBean.getQueryString());
        return new PageResult<Role>(page.getTotal(), page.getResult());

    }

    /**
     * 根据ID删除角色
     */
    @Override
    @Transactional
    public void deleteRoleById(int id) {

        //看角色是否被使用
/*        Role roleIfUsed = roleDao.findRoleIfUsed(id);*/
        
        //删除用户表中的对应用户
        roleDao.deleteRoleById(id);
    }

    /**
     * 添加角色
     */
    @Override
    @Transactional
    public void addRole(Role role, Integer[] roleIds) {

        //添加角色
        roleDao.addRole(role);
    }

    /**
     * 通过ID查询角色
     */
    @Override
    public Role findRoleById(int id) {
        return roleDao.findRoleById(id);
    }

    /**
     * 修改角色
     */
    @Override
    @Transactional
    public void updateRole(Role role, Integer[] roleIds) {
        
        roleDao.updateRole(role);
    }

    /**
     * 根据名称查询
     */
    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleDao.findRoleByRoleName(roleName);
    }
}
