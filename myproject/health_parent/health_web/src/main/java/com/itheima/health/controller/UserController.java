package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Reference
    private UserService userService;


    /**
     * 获取登陆用户名
     */
    @GetMapping("/getLoginUsername")
    public Result getLoginUsername() {
        // 获取登陆用户的认证信息
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 登陆用户名
        String username = loginUser.getUsername();
        // 返回给前端
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
    }

    /**
     * 通过id不分页获取用户信息
     */
    @GetMapping("/findUserById")
    public Result findUserById(int id){
        
        //调用服务
        User user = userService.findUserById(id);
        
        //返回
        return new Result(true,MessageConstant.QUERY_USER_SUCCESS,user);
    }
    
    /**
     * 通过id分页获取用户信息
     */
    @PostMapping("/findUserByPage")
    public Result findUserByPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult<User> pageResult = userService.findUserByPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_USER_BY_PAGE_SUCCESS, pageResult);
    }

    /**
     * 通过用户ID删除用户
     */
    @RequestMapping("/deleteUserById")
    public Result deleteUserById(int id){
        
        //调用服务删除用户
        userService.deleteUserById(id);
        
        //返回结果
        return new Result(true,MessageConstant.DELETE_USER_SUCCESS);
    }

    /**
     * 添加用户，同时添加角色信息
     */
    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user,Integer[] roleIds){
        
        //调用服务
        userService.addUser(user,roleIds);
        
        //返回结果
        return new Result(true,MessageConstant.ADD_USER_SUCCESS);
    }

    /**
     * 通过用户ID查询所有的角色ID
     */
    @GetMapping("/findRoleIdsByUserId")
    public Result findRoleIdsByUserId(int id){

        List<Integer> roleIds = userService.findRoleIdsByUserId(id);
        return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,roleIds);
    }

    /**
     * 修改用户
     */
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody User user,Integer[] roleIds){
        
        //调用业务修改
        userService.updateUser(user,roleIds);
        
        return new Result(true,MessageConstant.UPDATE_USER_SUCCESS);
    }
}
