package com.example.demo.controller;

import com.example.demo.domain.*;
import com.example.demo.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@Controller
public class SectionController {

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private LaborMapper laborMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private TeachMapper teachMapper;

    @GetMapping("/admin/section.html")
    public String listCourse(Model model) {
        List<Section> sections = sectionMapper.listAllCourseInfo();
        //System.out.println(sections);
        model.addAttribute("sections", sections);
        return "manageSection";
    }

    @GetMapping("/admin/section/add.html")
    public String toaddCourse(Model model) {

        // 获得课程信息
        List<CourseInfo> courseInfos = courseMapper.selectCourse();
        model.addAttribute("courseInfos", courseInfos);

        // 获得开课地点
        Collection<Labor> labors = laborMapper.selectAllLabor();
        model.addAttribute("labors", labors);

        // 获得开课时间
        Collection<TimeSlot> timeSlots = courseMapper.selectTimeslot();
        model.addAttribute("timeslots", timeSlots);

        // 获得教师
        Collection<Teacher> teachers = teacherMapper.selectAllTeachers();
        model.addAttribute("teachers", teachers);

        return "addSection";
    }

    @PostMapping("/admin/section/add.html")
    public String addCourse(
            @RequestParam("type") String type,
            @RequestParam("course_id") int course_id,
            @RequestParam("t_id") String t_id,
            @RequestParam("l_id") int l_id,
            @RequestParam("date") String date,
            @RequestParam("day") int day,
            @RequestParam("start_slot") int start_slot,
            @RequestParam("end_slot") int end_slot,
            Model model) {

        SeCourse seCourse = new SeCourse();
        seCourse.setCourse_id(course_id);
        seCourse.setYear("2021");
        seCourse.setSemester("Autumn");
        sectionMapper.insertSectionInfo(seCourse);
        int sec_id = seCourse.getSec_id();
        //System.out.println(sec_id);

        if(type.equals('P')) {
            int status = sectionMapper.insertSection(sec_id, day, l_id, start_slot, end_slot, "P", null);
            int teach_id = teachMapper.insertTeach(sec_id, t_id);
        }
        else {
            int status = sectionMapper.insertSection(sec_id, 0, l_id, start_slot, end_slot, "S", date);
            int teach_id = teachMapper.insertTeach(sec_id, t_id);
        }

        return "redirect:/admin/section.html";
    }

}
