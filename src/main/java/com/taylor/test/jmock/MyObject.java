package com.taylor.test.jmock;

import com.taylor.service.TestService;
import com.taylor.service.UserService;

import lombok.Data;

@Data
public class MyObject {

    private UserService userService;
    
    private TestService testService;

    public String hello(String name) {
        return "Hello " + name;
    }

    public String Hi(String message) {
        return "hi " + message;
    }

    public String getTestMsg(Integer id) {
        return testService.getByPrimaryKey(id).getName();
    }
}
