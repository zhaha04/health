package com.itheima.health.service;

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
}
