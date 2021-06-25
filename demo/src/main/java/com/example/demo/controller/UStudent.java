package com.example.demo.controller;

import com.example.demo.domain.*;
import com.example.demo.mapper.SectionMapper;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.service.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class UStudent {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private SectionMapper sectionMapper;

    @GetMapping("/student/main.html")
    public String UStudent(HttpSession session, Model model) throws Exception{
        String username = session.getAttribute("loginUser").toString();
        List<StuTake> stuTakes = studentMapper.getAllSection(username);
        model.addAttribute("stuTakes", stuTakes);

        List<Take> takes = new ArrayList<>();

        for(int i=0; i<stuTakes.size(); i++) {
            int teach_id = stuTakes.get(i).getTeach_id();
            Event event = studentMapper.StuCourse(teach_id);
            int sec_id = event.getSec_id();
            Year year = sectionMapper.getSectionInfo(sec_id);
            String[] course_name = event.getCourse_name().split(" ");
            Take take = new Take();
            take.setSec_id(event.getSec_id());
            take.setCourse_name(course_name[0]);
            take.setPosition(course_name[1]);
            take.setSeat_id(stuTakes.get(i).getSeat_id());
            if(event.getType().equals("P")) {
                take.setTime(event.getStart_time().toString()+"-"+event.getEnd_time().toString());
            }else{
                take.setTime(event.getDate()+" "+event.getStart_time().toString()+"-"+event.getEnd_time().toString());
            }
            take.setDay(event.getDay());
            take.setType(event.getType());
            take.setYear(year.getYear());
            take.setSemester(year.getSemester());
            takes.add(take);
        }
        model.addAttribute("takes", takes);

        return "student/index";
    }

    @GetMapping("/student/timetable.html")
    public String timetable() {
        return "student/timetable";
    }

    @PostMapping("/student_{id}.html")
    @ResponseBody
    public List<DetailedEvent> getSec(
            @PathVariable("id") String s_id,
            Model model) throws Exception{
        String start = "2021-03-01 00:00:00";
        String end = "2021-06-30 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start_time = sdf.parse(start);
        Date end_time = sdf.parse(end);

        List<StuTake> stuTakes = studentMapper.getAllSection(s_id);
        List<Event> events = new ArrayList<>();
        List<DetailedEvent> detailedEvents = new ArrayList<>();

        for(int i=0; i<stuTakes.size(); i++) {
            int teach_id = stuTakes.get(i).getTeach_id();
            Event event = studentMapper.StuCourse(teach_id);
            events.add(event);
        }

        for(int i=0; i<events.size(); i++) {
            String[] cour_position = events.get(i).getCourse_name().split(" ");
            if(events.get(i).getType().equals("P")) {
                Date from_date = Helper.addDays(events.get(i).getDay()-1, start_time);
                Date s_time = Helper.addHours(events.get(i).getStart_time().toString(), from_date);
                Date e_time = Helper.addHours(events.get(i).getEnd_time().toString(), from_date);
                while(s_time.getTime() <= end_time.getTime()) {
                    DetailedEvent my = new DetailedEvent();
                    my.setCourse_name(cour_position[0]+"\n"+cour_position[1]);
                    my.setStart_time(Helper.changeFormat(s_time.toString()));
                    my.setEnd_time(Helper.changeFormat(e_time.toString()));
                    detailedEvents.add(my);
                    s_time = Helper.addDays(7, s_time);
                    e_time = Helper.addDays(7, e_time);
                }
            }else{
                DetailedEvent my = new DetailedEvent();
                my.setCourse_name(cour_position[0]+"\n"+cour_position[1]);
                my.setStart_time(events.get(i).getDate().toString()+" "+events.get(i).getStart_time().toString());
                my.setEnd_time(events.get(i).getDate().toString()+" "+events.get(i).getEnd_time().toString());

                detailedEvents.add(my);
            }
        }
        return detailedEvents;
    }

    @GetMapping("/student/section_{id}.html")
    public String getSectionInfo(
            @PathVariable("id") String sec_id,
            HttpSession session,
            Model model
        ) {
        int teach_id = sectionMapper.getTeachID(Integer.parseInt(sec_id));
        String s_id = session.getAttribute("loginUser").toString();

        StuTake stuTake = sectionMapper.getAStuSection(teach_id, s_id);
        model.addAttribute("stu", stuTake);

        String str_attend = stuTake.getAttend();
        int counts = stuTake.getNums();
        int attend = Helper.countAttend(str_attend);

        if(counts == 0) {
            model.addAttribute("isToken", 0);
            model.addAttribute("attendP", 0);
            model.addAttribute("nattendP", 0);
        }
        else{
            model.addAttribute("isToken", 1);
            model.addAttribute("attendP", attend);
            model.addAttribute("nattendP", counts-attend);
        }

        return "student/grade";
    }


    @GetMapping("/student/personal.html")
    public String personal(HttpSession session, Model model) {
        String username = session.getAttribute("loginUser").toString();

        Student student = studentMapper.selectAStudent(username);

        model.addAttribute("student", student);
        return "student/personal";
    }
}
