package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
