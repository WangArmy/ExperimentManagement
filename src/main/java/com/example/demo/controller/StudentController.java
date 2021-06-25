package com.example.demo.controller;

import com.example.demo.domain.Student;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.Collection;

@Controller
public class StudentController {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private UserMapper userMapper;

    /* 学生管理主页 */
    @GetMapping("/admin/stu.html")
    public String listStu(Model model) {
        Collection<Student> students = studentMapper.selectAllStudents();
        model.addAttribute("stus", students);
        return "manageStu";
    }

    /* 添加学生 */
    @GetMapping("/admin/stu/add.html")
    public String toAddStu(Model model) {
        return "addStu";
    }

    /* 增添学生信息 */
    @PostMapping("/admin/stu/add.html")
    public String addStu(
            @RequestParam("name") String name,
            @RequestParam("gender") String gender,
            @RequestParam("birthday") Date birthday,
            @RequestParam("year") String year,
            @RequestParam("dept") String dept,
            @RequestParam("class_id") String class_id,
            @RequestParam("situation") String situation,
            @RequestParam("degree") String degree,
            Model model
    ) {
        Student student = new Student();
        student.setName(name);
        student.setGender(gender);
        student.setBirthday(birthday);
        student.setYear(year.substring(0,4));
        student.setDept_name(dept);
        student.setClass_id(class_id);
        student.setSituation(situation);
        student.setDegree(degree);

        int status = studentMapper.insertStudent(student);
        return "redirect:/admin/stu.html";
    }

    @GetMapping("/admin/stu/del_{id}.html")
    public String delStu(@PathVariable("id") String username, Model model) {
        studentMapper.deleteStudent(username);
        userMapper.deleteUser(username);
        return "redirect:/admin/stu.html";
    }

    @GetMapping("/admin/stu/edit_{id}.html")
    public String toUpdate(@PathVariable("id") String username, Model model) {
        Student stu = studentMapper.selectAStudent(username);
        model.addAttribute("stu", stu);
        return "updateStu";
    }

    @PostMapping("/admin/stu/edit_{id}.html")
    public String updateStu(
            @RequestParam("s_id") String s_id,
            @RequestParam("name") String name,
            @RequestParam("gender") String gender,
            @RequestParam("birthday") Date birthday,
            @RequestParam("year") String year,
            @RequestParam("dept_name") String dept_name,
            @RequestParam("class_id") String class_id,
            @RequestParam("situation") String situation,
            @RequestParam("degree") String degree,
            Model model
    ){
        Student student = new Student();
        student.setS_id(s_id);
        student.setName(name);
        student.setGender(gender);
        student.setBirthday(birthday);
        student.setYear(year.substring(0,4));
        student.setDept_name(dept_name);
        student.setClass_id(class_id);
        student.setSituation(situation);
        student.setDegree(degree);

        int status = studentMapper.updateStudent(student);
        return "redirect:/admin/stu.html";
    }

}
