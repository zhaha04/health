package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
public interface UserDao {
    /**
     * 通过用户ID查询拥有的角色ID
     */
    List<Integer> findRoleIdsByUserId(int id);

    /**
     * 根据名称查询
     * @param username
     * @return
     */
    User findByUsername(String username);
    /**
     * 条件查询
     */
    Page<User> findUserByCondition(String queryString);

    /**
     * 删除用户角色表中的对应用户
     */
    void deleteUserRoleById(int id);

    /**
     * 删除用户表中的对应用户
     */
    void deleteUserById(int id);

    /**
     * 添加空用户
     */
    void addUser(User user);

    /**
     * 绑定用户与角色的关系
     */
    void addUserRole(@Param("userId") Integer userId,@Param("roleId") Integer roleId);

    /**
     * 通过ID查询用户
     */
    User findUserById(int id);

    /**
     * 更新用户
     */
    void updateUser(User user);
}
