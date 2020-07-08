package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.User;
import com.itheima.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {
    
    @Autowired
    private MenuDao menuDao;

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Menu> findMenuByPage(QueryPageBean queryPageBean) {

        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 查询条件
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 模糊查询 %
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 条件查询，这个查询语句会被分页
        Page<Menu> page = menuDao.findMenuByCondition(queryPageBean.getQueryString());
        return new PageResult<Menu>(page.getTotal(), page.getResult());
    }

    /**
     * 查询子菜单
     * @return
     */
    @Override
    public List<Menu> findChildMenu() {
        
        return menuDao.findChildMenu();
    }

    /**
     * 根据名称查询
     */
    @Override
    public Menu findMenuByMenuName(String menuName) {
        return menuDao.findMenuByMenuName(menuName);
    }
    
    /**
     * 添加菜单
     */
    @Override
    @Transactional
    public void addMenu(Menu menu, Integer[] roleIds) {
        
        //添加无角色关系的菜单
        menuDao.addMenu(menu);
        //获取菜单ID
        Integer menuId = menu.getId();
        //添加菜单与角色的关系
        if (null != roleIds){

            for (Integer roleId : roleIds){

                menuDao.addMenuRole(menuId,roleId);
            }
        }
    }

    /**
     * 删除菜单
     */
    @Override
    @Transactional
    public void deleteMenuById(int id) {

        //删除用户角色表中的对应用户信息
        menuDao.deleteMenuRoleById(id);

        //删除用户表中的对应用户
        menuDao.deleteMenuById(id);
    }

    /**
     * 通过ID查询菜单
     * @param id
     * @return
     */
    @Override
    public Menu findMenuById(int id) {
        return menuDao.findMenuById(id);
    }

    /**
     * 通过菜单ID查询拥有的角色ID
     * @param id
     * @return
     */
    @Override
    public List<Integer> findRoleIdsByMenuId(int id) {
        return menuDao.findRoleIdsByMenuId(id);
    }

    /**
     * 修改菜单
     */
    @Override
    public void updateMenu(Menu menu, Integer[] roleIds) {

        //更新用户基本信息
        menuDao.updateMenu(menu);

        //删除旧的用户角色信息
        menuDao.deleteMenuRoleById(menu.getId());
        //添加新的用户角色信息
        for (Integer roleId : roleIds){

            menuDao.addMenuRole(menu.getId(),roleId);
        }
    }
}
