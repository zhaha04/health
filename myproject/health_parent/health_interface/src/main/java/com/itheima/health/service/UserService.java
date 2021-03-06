package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.User;

import java.util.List;

/**
 * Description: 用户服务(企业员工)
 * User: Eric
 */
public interface UserService {
    /**
     * 通过ID查询所拥有的角色ID
     */
    List<Integer> findRoleIdsByUserId(int id);
    
    
    /**
     * 根据用户名查询用户
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

    /**
     * 通过id查询用户
     */
    User findUserById(int id);

    /**
     * 修改用户
     */
    void updateUser(User user,Integer[] roleIds);

}
