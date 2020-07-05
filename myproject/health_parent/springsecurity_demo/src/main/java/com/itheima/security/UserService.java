package com.itheima.security;

import com.google.gson.internal.$Gson$Preconditions;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description: 将来要进入spring容器, 服务也可以注入进来
 * User: Eric
 */
public class UserService implements UserDetailsService {

    /**
     * 通过用户名加载用户信息 User登陆用
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //提供用户的名称、密码、权限集合
        // 通过用户名来查询数据库, 查询角色及权限
        com.itheima.health.pojo.User userInDB = findByUsername(username);
        // String username,
        // String password, 数据库中的密码, 密码校验(security帮我们做了)
        // Collection<? extends GrantedAuthority> authorities 权限集合
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        // 遍历用户身上的角色
        /*Set<Role> roles = userInDB.getRoles();
        if(null != roles){
            for (Role role : roles) {
                // 授予的权限
                // 构造方法要的是一个角色名
                GrantedAuthority ga = new SimpleGrantedAuthority(role.getName());
                authorities.add(ga);
                for (Permission permission : role.getPermissions()) {
                    // 权限
                    ga = new SimpleGrantedAuthority(permission.getName());
                    authorities.add(ga);
                }
            }
        }*/

        //测试其它方式的认证
        GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_ADMIN");
        authorities.add(ga);
        ga = new SimpleGrantedAuthority("add");
        authorities.add(ga);

        // 登陆用户的认证信息,名称、密码、权限集合
        //User user = new User(username,"{noop}" + userInDB.getPassword(),authorities);
        // 使用加密的密码后，去除{noop}
        User user = new User(username,userInDB.getPassword(),authorities);

        return user;
    }

    /**
     * 假设从数据库查询
     * @param username
     * @return
     */
    private com.itheima.health.pojo.User findByUsername (String username){
        if("admin".equals(username)) {
            com.itheima.health.pojo.User user = new com.itheima.health.pojo.User();
            user.setUsername("admin");
            //user.setPassword("admin");
            // 使用密码加密器encoder, 加密后的密码
            user.setPassword("$2a$10$P7Qx8eKUPX5lngz9UEstUOaDRldEWrj9Rbyox/ShyeoxvPbEHTvni");

            Role role = new Role();
            role.setName("ROLE_ADMIN");
            Permission permission = new Permission();
            permission.setName("ADD_CHECKITEM");
            role.getPermissions().add(permission);

            Set<Role> roleList = new HashSet<Role>();
            roleList.add(role);

            user.setRoles(roleList);
            return user;
        }
        return null;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 加密密码
        //System.out.println(encoder.encode("1234"));

        // 验证密码
        // 原密码
        // 加密后的密码
        System.out.println(encoder.matches("1234", "$2a$10$P7Qx8eKUPX5lngz9UEstUOaDRldEWrj9Rbyox/ShyeoxvPbEHTvni"));
        System.out.println(encoder.matches("1234", "$2a$10$5q.0a0F0hRix8TBJxQ4DB.ekwGzPs3e47hoQVNR7cihi/Rob.G3T6"));
        System.out.println(encoder.matches("1234", "$2a$10$voh.1PJRXQazoijK72sIoOslpmLYPyB.6LtT7aUrXqUO5G8Aw43we"));

        System.out.println(encoder.matches("1234", "$2a$10$u/BcsUUqZNWUxdmDhbnoeeobJy6IBsL1Gn/S0dMxI2RbSgnMKJ.4a"));
    }
}
