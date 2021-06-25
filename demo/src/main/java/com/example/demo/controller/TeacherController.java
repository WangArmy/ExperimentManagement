package com.example.demo.controller;

import com.example.demo.domain.DetailedEvent;
import com.example.demo.domain.Event;
import com.example.demo.domain.Teacher;
import com.example.demo.mapper.TeacherMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.Helper;
import com.sun.org.apache.xpath.internal.objects.XString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class TeacherController {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private UserMapper userMapper;

    /*
     * 教师管理主页
     * */
    @GetMapping("/admin/tch.html")
    public String listTeacher(Model model) {
        Collection<Teacher> teachers = teacherMapper.selectAllTeachers();
        model.addAttribute("tchs", teachers);
        return "manageTch";
    }

    @GetMapping("/admin/tch/add.html")
    public String toAddTeacher(Model model) {
        return "addTch";
    }

    @PostMapping("/admin/tch/add.html")
    public String addTeacher(
            @RequestParam("name") String name,
            @RequestParam("gender") String gender,
            @RequestParam("birthday") Date birthday,
            @RequestParam("title") String title,
            @RequestParam("dept") String dept,
            @RequestParam("major") String major,
            Model model){
        Teacher tch = new Teacher();
        tch.setName(name);
        tch.setGender(gender);
        tch.setBirthday(birthday);
        tch.setTitle(title);
        tch.setDept_name(dept);
        if (major == null || major.length() == 0) {
            tch.setMajor("");
        }else{
            tch.setMajor(major);
        }
        teacherMapper.InsertTeacher(tch);
        return "redirect:/admin/tch.html";
    }

    @GetMapping("/admin/tch/del_{id}.html")
    public String delTch(@PathVariable("id") String username, Model model) {
        teacherMapper.deleteTeacher(username);
        userMapper.deleteUser(username);
        return "redirect:/admin/tch.html";
    }

    @GetMapping("/admin/tch/edit_{id}.html")
    public String toUpdateTch(@PathVariable("id") String username, Model model) {
        Teacher tch = teacherMapper.selectATeacher(username);
        model.addAttribute("tch", tch);
        return "updateTch";
    }

    @PostMapping("/admin/tch/edit_{id}.html")
    public String updateTch(
            @RequestParam("t_id") String t_id,
            @RequestParam("name") String name,
            @RequestParam("gender") String gender,
            @RequestParam("birthday") Date birthday,
            @RequestParam("title") String title,
            @RequestParam("dept_name") String dept_name,
            @RequestParam("major") String major,
            Model model){
        Teacher teacher = new Teacher();
        teacher.setT_id(t_id);
        teacher.setName(name);
        teacher.setTitle(title);
        teacher.setDept_name(dept_name);
        teacher.setGender(gender);
        teacher.setBirthday(birthday);
        if (major == null || major.length() == 0) {
            teacher.setMajor("");
        }else{
            teacher.setMajor(major);
        }

        int status = teacherMapper.updateTeacher(teacher);
        return "redirect:/admin/tch.html";
    }


}
