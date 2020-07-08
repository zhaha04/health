package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Menu;

import java.util.List;

public interface MenuService {

    /**
     * 根据名称查询
     */
    Menu findMenuByMenuName(String MenuName);

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
     * 通过id查询菜单
     */
    Menu findMenuById(int id);

    /**
     * 通过菜单ID查询所拥有的角色ID
     */
    List<Integer> findRoleIdsByMenuId(int id);

    /**
     * 修改菜单
     */
    void updateMenu(Menu menu,Integer[] roleIds);
}
