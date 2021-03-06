package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuDao {

    /**
     * 条件查询菜单
     */
    Page<Menu> findMenuByCondition(String queryString);

    /**
     * 查询子菜单
     */
    List<Menu> findChildMenu();

    /**
     * 添加空菜单
     */
    void addMenu(Menu menu);

    /**
     * 绑定菜单与角色的关系
     */

    /**
     * 删除菜单与角色的关系
     */
    void deleteMenuRoleById(int id);

    /**
     * 删除菜单
     */
    void deleteMenuById(int id);
     /**
     * 查询菜单和子菜单
*/
	Menu findMenuById(int id);

    /**
     * 根据ID查询菜单拥有的角色ID
     */
    List<Integer> findRoleIdsByMenuId(int id);

    /**
     * 根据名称查询
     */
    Menu findMenuByMenuName(String menuName);
    
    /**
     * 更新用户
     */
    void updateMenu(Menu menu);
    
    List<Menu> findByMenu(String loginUsername);

    void addMenuRole(@Param("menuId") Integer menuId, @Param("roleId") Integer roleId);
}
