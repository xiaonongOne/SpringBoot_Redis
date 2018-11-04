package com.example.redis.controller;

import com.example.redis.bean.User;
import com.example.redis.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**  
* <p>Title: UserController.java</p>  
* <p>Description:用户Controller类 </p>  
* <p>Copyright: Copyright (c) 2018</p>  
* @author Xiao Nong
* @date 2018年11月4日  
* @version 1.0  
*/  
@RestController
@RequestMapping("user")
public class UserController {
	
    @Autowired
    private UserService userService;
    /**
     * 通过id 查询User对象
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/findinfo/{id}")
    public User findUserById(@PathVariable("id") Long id) {
        return userService.findUserById(id);
    }
    /**
     * 保存用户信息
     * @param user
     */
    @RequestMapping(value = "/api/create")
    public void saveUser(User user) {
        userService.saveUser(user);

    }
    /**
     * 更改用户信息
     * @param user
     */
    @RequestMapping(value = "/api/modify")
    public void updateUser(User user) {
        userService.updateUser(user);
    }
    /**
     * 删除用户信息
     * @param id
     */
    @RequestMapping(value = "/api/delete/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }
}
