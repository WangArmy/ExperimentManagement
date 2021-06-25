package com.example.demo.controller;

import com.example.demo.domain.*;
import com.example.demo.mapper.SectionMapper;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.mapper.TeachMapper;
import com.example.demo.mapper.TeacherMapper;
import com.example.demo.service.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class UTeacher {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeachMapper teachMapper;

    @GetMapping("/teacher/main.html")
    public String  TSection(HttpSession session, Model model) throws Exception{
        String username = session.getAttribute("loginUser").toString();
        List<Teach> teaches = sectionMapper.listATeacher(username);
        model.addAttribute("teaches", teaches);

        List<Event> events = teacherMapper.getTSection(username);
        List<DetailedEvent> detailedEvents = new ArrayList<>();
        String start = "2021-03-01 00:00:00";
        String end = "2021-06-30 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date start_time = sdf.parse(start);
        java.util.Date end_time = sdf.parse(end);
        // 获得当前学期该老师的所有课程
        for (int i=0; i< events.size(); i++) {
            if (events.get(i).getType().equals("P")) {
                java.util.Date from_date = Helper.addDays(events.get(i).getDay() - 1, start_time);
                java.util.Date s_time = Helper.addHours(events.get(i).getStart_time().toString(), from_date);
                java.util.Date e_time = Helper.addHours(events.get(i).getEnd_time().toString(), from_date);
                while (s_time.getTime() <= end_time.getTime()) {
                    DetailedEvent my = new DetailedEvent();
                    my.setCourse_name(events.get(i).getCourse_name());
                    my.setStart_time(Helper.changeFormat(s_time.toString()));
                    my.setEnd_time(Helper.changeFormat(e_time.toString()));
                    my.setSec_id(events.get(i).getSec_id());
                    detailedEvents.add(my);
                    s_time = Helper.addDays(7, s_time);
                    e_time = Helper.addDays(7, e_time);
                }
            } else {
                DetailedEvent my = new DetailedEvent();
                my.setCourse_name(events.get(i).getCourse_name());
                my.setStart_time(events.get(i).getDate().toString() + " " + events.get(i).getStart_time().toString());
                my.setEnd_time(events.get(i).getDate().toString() + " " + events.get(i).getEnd_time().toString());
                my.setSec_id(events.get(i).getSec_id());
                detailedEvents.add(my);
            }
        }
        model.addAttribute("events", detailedEvents);
        //System.out.println(detailedEvents);
        Date nowDate = new Date();
        String course_name = null;
        /*
        * 获得当前时间该老师的授课(最多只有一节课)
        * 并将　
        *   当前时间该教师　是否有课　课程名　地点　和 课程id　　
        * 放至model返回前端
        * */
        //System.out.println(detailedEvents);
        for(int i=0; i<detailedEvents.size(); i++) {
            String s_t = detailedEvents.get(i).getStart_time();
            String e_t = detailedEvents.get(i).getEnd_time();
            Date s_t1 = sdf.parse(s_t);
            Date e_t1 = sdf.parse(e_t);
            int com_s = nowDate.compareTo(s_t1);
            int com_e = e_t1.compareTo(nowDate);
            if(com_s == 1 && com_e == 1){
                course_name = detailedEvents.get(i).getCourse_name();
                String[] course_names = course_name.split("\n");
                model.addAttribute("cur_course", course_names[0]+course_names[1]);
                model.addAttribute("isCur", 1);
                model.addAttribute("cur_sec", detailedEvents.get(i).getSec_id());
                break;
            }
        }
        if(course_name == null) {
            model.addAttribute("isCur", 0);
            model.addAttribute("cur_course", "无课可上");
            model.addAttribute("cur_sec", -1);
        }

        return "teacher/index";
    }

    //当前时间课程id
    @GetMapping("/teacher/sign_{id}.html")
    public String sign(
            @PathVariable("id") String sec_id,
            HttpSession session ,Model model) {
        int teach_id = sectionMapper.getTeachID(Integer.parseInt(sec_id));
        List<StuTake> stuTakes = sectionMapper.getAllStuSection(teach_id);

        model.addAttribute("sec_id", sec_id);
        model.addAttribute("stuTakes", stuTakes);
        int cur;
        if(stuTakes == null) {
            cur = 0;
        }else{
            cur = 100;
            for(int i=0; i<stuTakes.size(); i++){
                if(cur > stuTakes.get(i).getNums()){
                    cur = stuTakes.get(i).getNums();
                }
            }
        }

        String course_name = sectionMapper.getSectioName(Integer.parseInt(sec_id));
        model.addAttribute("course_name", course_name);
        model.addAttribute("course_id", cur);
        return "teacher/sign";
    }

    //
    @GetMapping("/teacher/sign_{id}/{s_id}.html")
    public String toSign(
            @PathVariable("id") String sec_id,
            @PathVariable("s_id") String s_id,
            HttpSession session ,
            Model model) {
        int teach_id = sectionMapper.getTeachID(Integer.parseInt(sec_id));
        StuTake stuTake = sectionMapper.getAStuSection(teach_id, s_id);
        int cur = stuTake.getNums();
        int len = stuTake.getAttend().length();
        if(cur<len){
            sectionMapper.signAStu(teach_id, s_id, cur);
            model.addAttribute("signMsg", "签到成功");
        }else{
            model.addAttribute("signMsg", "已签到");
        }
        return "redirect:/teacher/sign_"+sec_id+".html";
    }

    //　管理课程
    @GetMapping("/teacher/section_{id}.html")
    public String manageCourse(
            @PathVariable("id") String sec_id,
            HttpSession session,
            Model model) {
        model.addAttribute("sec_id", sec_id);
        return "teacher/manageSec";
    }

    @GetMapping("/teacher/section_{id}/stu.html")
    public String toListStu(
            @PathVariable("id") String sec_id,
            HttpSession session,
            Model model) {
        int teach_id = sectionMapper.getTeachID(Integer.parseInt(sec_id));
        List<StuTake> stuTakes = sectionMapper.getAllStuSection(teach_id);
        model.addAttribute("takes", stuTakes);
        model.addAttribute("sec_id", sec_id);
        return "teacher/manageStu";
    }

    @GetMapping("/teacher/section_{id}/sign.html")
    public String toListSign(@PathVariable("id") String sec_id,Model model) {
        int teach_id = sectionMapper.getTeachID(Integer.parseInt(sec_id));
        List<StuTake> stuTakes = sectionMapper.getAllStuSection(teach_id);
        int counts = 0;
        int attend = 0;

        if(stuTakes == null || stuTakes.size() <= 0) {
            model.addAttribute("allTakes", 0);
            model.addAttribute("isToken", 0);
            model.addAttribute("sec_id", sec_id);
            model.addAttribute("attendP", 0);
            model.addAttribute("nattendP", 0);
            model.addAttribute("per", 0);
            return "/teacher/manageSign";
        }

        for(int i=0; i<stuTakes.size(); i++) {
            String cur = stuTakes.get(i).getAttend();
            if(cur==null || cur.length() == 0) {
                model.addAttribute("allTakes", 0);
                model.addAttribute("isToken", 0);
                model.addAttribute("sec_id", sec_id);
                model.addAttribute("attendP", 0);
                model.addAttribute("nattendP", 0);
                model.addAttribute("per", 0);
                return "/teacher/manageSign";
            }
            counts += cur.length();
            attend += Helper.countAttend(cur);
        }
        double per = (double) attend/counts;
        NumberFormat nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(0);



        model.addAttribute("isToken", 1);
        model.addAttribute("allTakes", stuTakes.get(0).getAttend().length());
        model.addAttribute("attendP", attend);
        model.addAttribute("nattendP", counts-attend);
        model.addAttribute("sec_id", sec_id);
        model.addAttribute("per", nt.format(per));
        return "/teacher/manageSign";
    }

    @GetMapping("/teacher/section_{id1}/manage_{id2}.html")
    public String manageStu(
            @PathVariable("id1") String sec_id,
            @PathVariable("id2") String s_id,
            Model model
        ){
        int teach_id = sectionMapper.getTeachID(Integer.parseInt(sec_id));
        StuTake stuTake = sectionMapper.getAStuSection(teach_id, s_id);
        int count = stuTake.getAttend().length();
        int attend = Helper.countAttend(stuTake.getAttend());
        if(count == 0){
            model.addAttribute("isToken", 0);
            model.addAttribute("attendP", 0);
            model.addAttribute("nattendP", 0);
        }
        else{
            model.addAttribute("isToken", 1);
            model.addAttribute("attendP", attend);
            model.addAttribute("nattendP", count-attend);
        }
        model.addAttribute("sec_id", sec_id);
        return "/teacher/toAStudent";
    }

    @GetMapping("/teacher/section_{id}/add.html")
    public String toAddStu(
            @PathVariable("id") String sec_id,
            HttpSession session,
            Model model) {
        List<String> stus = studentMapper.getAllStudentID();
        model.addAttribute("stus", stus);
        model.addAttribute("sec_id", sec_id);
        return "teacher/addStu";
    }

    @PostMapping("/teacher/section_{id}/add.html")
    public String addStu(
            @RequestParam("id") String s_id,
            @PathVariable("id") String sec_id,
            HttpSession session,
            Model model) {
        int seat_id = 0;
        int teach_id = sectionMapper.getTeachID(Integer.parseInt(sec_id));
        String counts = teachMapper.countSeat(teach_id);
        if(counts == null) {
            seat_id = 1;
        }else {
            seat_id = Integer.parseInt(counts)+1;
        }
        if(seat_id >= 50) {
            model.addAttribute("msg","班级人数过多，插入失败");
            return "redirect:/teacher/section_"+sec_id+"/add.html";
        }

        teachMapper.insertTake(teach_id, s_id, seat_id, "0", 1, 0, 0, 0);
        return "redirect:/teacher/section_"+sec_id+".html";
    }

    @PostMapping("/section_{id}.html")
    @ResponseBody
    public List<DetailedEvent> getSec(
            @PathVariable("id")String t_id,
            Model model) throws Exception{
        String start = "2021-03-01 00:00:00";
        String end = "2021-06-30 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start_time = sdf.parse(start);
        Date end_time = sdf.parse(end);
        List<Event> events = teacherMapper.getTSection(t_id);
        //System.out.println(events);
        List<DetailedEvent> detailedEvents = new ArrayList<>();
        for(int i=0; i<events.size(); i++) {
            if(events.get(i).getType().equals("P")) {
                Date from_date = Helper.addDays(events.get(i).getDay()-1, start_time);
                Date s_time = Helper.addHours(events.get(i).getStart_time().toString(), from_date);
                Date e_time = Helper.addHours(events.get(i).getEnd_time().toString(), from_date);
                while(s_time.getTime() <= end_time.getTime()) {
                    DetailedEvent my = new DetailedEvent();
                    my.setCourse_name(events.get(i).getCourse_name());
                    my.setStart_time(Helper.changeFormat(s_time.toString()));
                    my.setEnd_time(Helper.changeFormat(e_time.toString()));
                    detailedEvents.add(my);
                    s_time = Helper.addDays(7, s_time);
                    e_time = Helper.addDays(7, e_time);
                }
            }else{
                DetailedEvent my = new DetailedEvent();
                my.setCourse_name(events.get(i).getCourse_name());
                my.setStart_time(events.get(i).getDate().toString()+" "+events.get(i).getStart_time().toString());
                my.setEnd_time(events.get(i).getDate().toString()+" "+events.get(i).getEnd_time().toString());

                detailedEvents.add(my);
            }
        }
        return detailedEvents;
    }

    @GetMapping("/teacher/section_{id1}/grade_{id2}.html")
    public String grade(
            @PathVariable("id1") String sec_id,
            @PathVariable("id2") String s_id,
            Model model) {
        int teach_id = sectionMapper.getTeachID(Integer.parseInt(sec_id));
        StuTake stuTake = sectionMapper.getAStuSection(teach_id, s_id);
        model.addAttribute("sec_id", sec_id);
        model.addAttribute("s_id", s_id);
        model.addAttribute("sTake", stuTake);
        System.out.println(stuTake);
        return "teacher/updateGrade";
    }

    @PostMapping("/teacher/section_{id1}/grade_{id2}.html")
    public String updateGrade(
            @PathVariable("id1") String sec_id,
            @PathVariable("id2") String s_id,
            @RequestParam("regular") String regular,
            @RequestParam("_final") String _final,
            Model model ) {
        model.addAttribute("sec_id", sec_id);
        int teach_id = sectionMapper.getTeachID(Integer.parseInt(sec_id));
        int r = Integer.parseInt(regular);
        int f = Integer.parseInt(_final);
        if(r<0 || r>100 || f<0 || f>100) {
            model.addAttribute("error_msg", "请正确输入分数");
        }
        else{
            sectionMapper.updateStuGrade(teach_id, s_id, r, f);
        }

        return "redirect:/teacher/section_"+sec_id+"/stu.html";
    }

    @GetMapping("/teacher/timetable.html")
    public String timetable(HttpSession session, Model model) {
        return "teacher/timetable";
    }

    @GetMapping("/teacher/personal.html")
    public String personal(HttpSession session, Model model) {
        String username = session.getAttribute("loginUser").toString();
        Teacher teacher = teacherMapper.selectATeacher(username);
        model.addAttribute("teacher", teacher);
        return "teacher/personal";
    }
}
