package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.User;

/**
 * Description: 用户服务(企业员工)
 * User: Eric
 */
public interface UserService {
    /**
     * 根据登陆用户名称查询用户权限信息
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 分页查询用户
     */
    PageResult<User> findUserByPage(QueryPageBean queryPageBean);

    /**
     * 根据用户ID删除用户
     */
    void deleteUserById(int id);

    /**
     * 添加用户，同时添加角色信息
     */
    void addUser(User user,Integer[] roleIds);
}
