package com.example.demo.controller;

import com.example.demo.domain.User;

import com.example.demo.mapper.StudentMapper;
import com.example.demo.mapper.TeacherMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @GetMapping("/admin/user.html")
    public String listUser(Model model) {
        Collection<User> users = userMapper.selectAllUsers();
        model.addAttribute("users", users);
        return "manageUser";
    }

    @GetMapping("/admin/user/del_{id}.html")
    public String delUser(@PathVariable("id") String username,  Model model) {
        userMapper.deleteUser(username);
        return "redirect:/admin/user.html";
    }

    @GetMapping("/admin/user/edit_{id}.html")
    public String toUpdateUser(@PathVariable("id") String username, Model model) {
        User user = userMapper.selectAnUser(username);
        model.addAttribute("user", user);
        return "updateUser";
    }

    @PostMapping("/admin/user/edit_{id}.html")
    public String updateUser(
            @RequestParam("username")String username,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            Model model) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);

        model.addAttribute("user", user);
        int status = userMapper.updateUser(user);

        if(username.length() == 8) {
            teacherMapper.resolveTeacher(username, role);
        }
        else if(username.length() == 11){
            studentMapper.resolveStudent(username, role);
        }

        return "redirect:/admin/user.html";
    }


}
