package com.edw.controller;

import com.edw.model.User;
import com.edw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 *  com.edw.controller.IndexController
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 15 Oct 2024 19:38
 */
@RestController
public class IndexController {

    private UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/")
    public HashMap index() {
        return new HashMap(){{
            put("hello", "world");
        }};
    }

    @GetMapping(path = "/user")
    public List<User> getAllUser() {
        return userService.getUsers();
    }

    @GetMapping(path = "/sync")
    public ResponseEntity synchronize() {
        userService.synchronize();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(path = "/user")
    public ResponseEntity save(@RequestBody User user) {
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
