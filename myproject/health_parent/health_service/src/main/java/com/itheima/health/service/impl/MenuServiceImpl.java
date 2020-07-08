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
}
