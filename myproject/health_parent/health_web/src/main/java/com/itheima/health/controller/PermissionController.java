package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.User;
import com.itheima.health.service.PermissionService;
import com.itheima.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    /**
     * 通过id不分页获取权限信息
     */
    @GetMapping("/findPermissionById")
    public Result findPermissionById(int id){

        //调用服务
        Permission permission = permissionService.findPermissionById(id);

        //返回
        return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
    }

    /**
     * 通过id分页获取权限信息
     */
    @PostMapping("/findPermissionByPage")
    public Result findPermissionByPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult<Permission> pageResult = permissionService.findPermissionByPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, pageResult);
    }

    /**
     * 通过ID删除权限
     */
    @RequestMapping("/deletePermissionById")
    public Result deletePermissionById(int id){

        //调用服务删除用户
        permissionService.deletePermissionById(id);

        //返回结果
        return new Result(true,MessageConstant.DELETE_PERMISSION_SUCCESS);
    }

    /**
     * 添加权限，同时添加角色信息
     */
    @PostMapping("/addPermission")
    public Result addPermission(@RequestBody Permission permission,Integer[] roleIds){

        //查询
        Permission permissionInDB = permissionService.findPermissionByPermissionName(permission.getName());

        if (null == permissionInDB){
            ///调用服务
            permissionService.addPermission(permission,roleIds);

            //返回结果
            return new Result(true,MessageConstant.ADD_PERMISSION_SUCCESS);
        } else {
            return new Result(false,"该名称已被占用！");
        }
        
    }

    /**
     * 通过ID查询所有的角色ID
     */
    @GetMapping("/findRoleIdsByPermissionId")
    public Result findRoleIdsByPermissionId(int id){

        List<Integer> roleIds = permissionService.findRoleIdsByPermissionId(id);
        return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,roleIds);
    }

    /**
     * 修改用户
     */
    @PostMapping("/updatePermission")
    public Result updateUser(@RequestBody Permission permission,Integer[] roleIds){

        //调用业务修改
        permissionService.updatePermission(permission,roleIds);

        return new Result(true,MessageConstant.UPDATE_PERMISSION_SUCCESS);
    }
}
