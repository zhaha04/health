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
     * 通过用户id获取用户信息
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
}
