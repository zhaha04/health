package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Menu;

import java.util.List;

public interface MenuService {

    /**
     * 分页查询菜单
     */
    PageResult<Menu> findMenuByPage(QueryPageBean queryPageBean);

    /**
     * 查询子菜单
     */
    List<Menu> findChildMenu();

    /**
     * 添加菜单，同时添加角色信息
     */
    void addMenu(Menu menu,Integer[] roleIds);

    /**
     * 删除菜单
     */
    void deleteMenuById(int id);


    /**
     * 查询菜单和子菜单
     * @param loginUsername
     * @return
     */
    List<Menu> findByMenu(String loginUsername);
}
