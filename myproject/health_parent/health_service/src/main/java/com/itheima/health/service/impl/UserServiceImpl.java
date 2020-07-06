package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.github.pagehelper.Page;

/**
 * Description: No Description
 * User: Eric
 */
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 根据登陆用户名称查询用户权限信息
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * 分页查询用户
     */
    @Override
    public PageResult<User> findUserByPage(QueryPageBean queryPageBean) {

        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 查询条件
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 模糊查询 %
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 条件查询，这个查询语句会被分页
        Page<User> page = userDao.findUserByCondition(queryPageBean.getQueryString());
        return new PageResult<User>((long)page.getResult().size(), page.getResult());
        
    }

    /**
     * 根据用户ID删除用户
     */
    @Override
    @Transactional
    public void deleteUserById(int id) {
        
        //删除用户角色表中的对应用户信息
        userDao.deleteUserRoleById(id);
        
        //删除用户表中的对应用户
        userDao.deleteUserById(id);
    }

    /**
     * 添加用户，同时添加角色信息
     */
    @Override
    @Transactional
    public void addUser(User user, Integer[] roleIds) {
        
        //添加无角色信息的新用户
        userDao.addUser(user);
        
        //获取用户id
        Integer userId = user.getId();
        //添加用户与角色的关系
        if (null != roleIds){
            
            for (Integer roleId : roleIds){
                
                userDao.addUserRole(userId,roleId);
            }
        }
    }
}
