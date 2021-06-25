package com.example.demo.mapper;

import com.example.demo.domain.Labor;
import com.example.demo.domain.Student;
import com.example.demo.domain.Teacher;
import com.example.demo.domain.User;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/*
 *
 * 登录管理
 *
 * */

@Repository
public interface LogMapper {
    @Select("select username,password,role from user where username=#{username} and password=#{password}")
    User selectUser(@Param("username") String username, @Param("password") String password);


}
