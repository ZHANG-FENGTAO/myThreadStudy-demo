package com.example;

import com.alibaba.dubbo.config.annotation.Service;

import java.util.Date;

@Service(version = "1.0.0")
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "Hello, " + name + ", " + new Date();
    }

}

