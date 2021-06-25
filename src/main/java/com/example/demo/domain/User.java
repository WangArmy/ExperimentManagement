package com.example.demo.domain;

import lombok.Data;

/*
* 用户表接口
* */

@Data
public class User {

    private String role;
    private String username;
    private String password;
    private String addr;

}
