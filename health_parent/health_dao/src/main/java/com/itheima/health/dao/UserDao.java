package com.itheima.health.dao;

import com.itheima.health.pojo.User;

/**
 * Description: No Description
 * User: Eric
 */
public interface UserDao {
    /**
     * 根据登陆用户名称查询用户权限信息
     * @param username
     * @return
     */
    User findByUsername(String username);
}
