package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    
    @Reference
    private RoleService roleService;

    /**
     * 查询所有角色
     */
    @GetMapping("/findAllRole")
    public Result findAllRole(){

        List<Role> roleList = roleService.findAllRole();
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,roleList);
    }

    /**
     * 分页查询菜单
     */
    @RequestMapping("/findRoleByPage")
    public Result findRoleByPage(@RequestBody QueryPageBean queryPageBean){

        PageResult<Role> pageResult = roleService.findRoleByPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, pageResult);
    }

    /**
     * 通过id不分页获取菜单信息
     */
    @GetMapping("/findRoleById")
    public Result findRoleById(int id){

        //调用服务
        Role role = roleService.findRoleById(id);

        //返回
        return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,role);
    }

    /**
     * 删除菜单
     */
    @PostMapping("/deleteRoleById")
    public Result deleteRoleById(int id){

        //调用服务
        roleService.deleteRoleById(id);

        //返回
        return new Result(true,MessageConstant.DELETE_ROLE_SUCCESS);
    }

    /**
     * 修改角色
     */
    @RequestMapping("/updateRole")
    public Result updateRole(@RequestBody Role role,Integer[] roleIds){

        //调用业务修改
        roleService.updateRole(role,roleIds);

        return new Result(true,MessageConstant.UPDATE_ROLE_SUCCESS);
    }

    /**
     * 添加权限，同时添加角色信息
     */
    @PostMapping("/addRole")
    public Result addRole(@RequestBody Role role,Integer[] roleIds){

        //查询
        Role roleInDB = roleService.findRoleByRoleName(role.getName());

        if (null == roleInDB){
            //调用服务
            roleService.addRole(role,roleIds);

            //返回结果
            return new Result(true,MessageConstant.ADD_ROLE_SUCCESS);
        } else {
            return new Result(false,"该名称已被占用！");
        }
    }
}
