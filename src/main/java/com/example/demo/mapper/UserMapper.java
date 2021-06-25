package com.example.demo.mapper;

import com.example.demo.domain.User;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/*
*
* 用户账户管理
*
* */

@Repository
public interface UserMapper {

    /*
     * 获取全部用户的账户信息
     * */
    @Select({
            "select * from v_AllUsersInfo"
    })
    Collection<User> selectAllUsers();

    /*
     * 获取指定用户的账户信息
     * */
    @Select({
            "select * from v_AllUsersInfo where username=#{username}"
    })
    User selectAnUser(String username);

    /*
     * 更新某个用户的账户信息
     * */
    @Update({
            "update user set password=#{u.password}, role=#{u.role} where username=#{u.username}"
    })
    int updateUser(@Param("u") User u);

    /*
     * 删除指定用户的账户
     * */
    @Update({
            "update user set role='Z' where username=#{username}"
    })
    int deleteUser(String username);



}
