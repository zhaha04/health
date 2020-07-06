package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

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
}
