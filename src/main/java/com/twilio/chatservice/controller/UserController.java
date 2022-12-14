package com.twilio.chatservice.controller;

import com.twilio.chatservice.model.User;
import com.twilio.chatservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value="/user/{userId}", method= RequestMethod.GET)
    public User getUser(@PathVariable(value = "userId") Long id) throws Exception {
        return userService.getUser(id);
    }

    @RequestMapping(value="/user", method=RequestMethod.POST)
    public void createUser(@RequestBody User user) {
        userService.createUser(user);
    }

    @RequestMapping(value="/user/{userId}", method=RequestMethod.DELETE)
    public void deleteEmployees(@PathVariable(value = "userId") Long id) throws Exception {
        userService.deleteUser(id);
    }

    @RequestMapping(value="/user", method=RequestMethod.GET)
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
