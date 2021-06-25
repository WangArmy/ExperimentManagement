package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.mapper.LogMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class LogController {

    @Autowired
    private LogMapper logMapper;


   /* @GetMapping ("/manage")
    public String list(Model model) {
        return "redirect:/admin/main.html";
    }*/

    @PostMapping("/admin/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        User user = logMapper.selectUser(username, password);

        if(user!=null && user.getRole().equals("A")) {
            session.setAttribute("loginUser", username);
            return "redirect:/admin/main.html";
        }

        model.addAttribute("msg", "用户名或密码错误");
        return "login";

    }

    @PostMapping("/login")
    public String auth(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        User user = logMapper.selectUser(username, password);


        if (user != null) {
            /*
            * 教师登录入口
            * */
            if(user.getRole().equals("T")) {
                model.addAttribute("id", username);
                session.setAttribute("loginUser", username);
                session.setAttribute("loginRole", "T");
                return "redirect:/teacher/main.html";
            }

            if(user.getRole().equals("S")) {
                model.addAttribute("id", username);
                session.setAttribute("loginUser", username);
                session.setAttribute("loginRole", "T");
                return "redirect:/student/main.html";
            }

        }

        model.addAttribute("msg", "用户名或密码错误");
        return "auth";

    }

}
