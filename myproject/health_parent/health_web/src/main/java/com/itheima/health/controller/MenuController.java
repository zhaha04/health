package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.User;
import com.itheima.health.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    
    @Reference
    private MenuService menuService;

    /**
     * 分页查询菜单
     */
    @RequestMapping("/findMenuByPage")
    public Result findMenuByPage(@RequestBody QueryPageBean queryPageBean){

        PageResult<Menu> pageResult = menuService.findMenuByPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_MENU_SUCCESS, pageResult);
    }

    /**
     * 查询子菜单
     * 未完成，暂停开发
     */
    @GetMapping("/findChildMenu")
    public Result findChildMenu(){
        
        List<Menu> childMenuList =  menuService.findChildMenu();
        return new Result(true,MessageConstant.QUERY_MENU_SUCCESS,childMenuList);
    }

    /**
     * 通过id不分页获取菜单信息
     */
    @GetMapping("/findMenuById")
    public Result findMenuById(int id){

        //调用服务
        Menu menu = menuService.findMenuById(id);

        //返回
        return new Result(true,MessageConstant.QUERY_MENU_SUCCESS,menu);
    }

    /**
     * 通过菜单ID查询所有的角色ID
     */
    @GetMapping("/findRoleIdsByMenuId")
    public Result findRoleIdsByMenuId(int id){

        List<Integer> roleIds = menuService.findRoleIdsByMenuId(id);
        return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,roleIds);
    }

    /**
     * 添加菜单，同时添加角色信息
     */
    @PostMapping("/addMenu")
    public Result addMenu(@RequestBody Menu menu,Integer[] roleIds){

        //查询
        Menu menuInDB = menuService.findMenuByMenuName(menu.getName());

        if (null == menuInDB){
            //调用服务
            menuService.addMenu(menu,roleIds);

            //返回结果
            return new Result(true,MessageConstant.ADD_MENU_SUCCESS);
        } else {
            return new Result(false,"该名称已被占用！");
        }
    }

    /**
     * 删除菜单
     */
    @PostMapping("/deleteMenuById")
    public Result deleteMenuById(int id){
        
        //调用服务
        menuService.deleteMenuById(id);
        
        //返回
        return new Result(true,MessageConstant.DELETE_MENU_SUCCESS);
    }

    /**
     * 修改用户
     */
    @RequestMapping("/updateMenu")
    public Result updateMenu(@RequestBody Menu menu,Integer[] roleIds){

        //调用业务修改
        menuService.updateMenu(menu,roleIds);

        return new Result(true,MessageConstant.UPDATE_MENU_SUCCESS);
    }
}
