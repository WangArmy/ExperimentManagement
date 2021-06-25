package com.example.demo.controller;

import com.example.demo.domain.DetailedEvent;
import com.example.demo.domain.Event;
import com.example.demo.domain.Labor;
import com.example.demo.mapper.LaborMapper;
import com.example.demo.service.Helper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class LaborController {

    @Autowired
    private LaborMapper laborMapper;

    @GetMapping("/admin/lab.html")
    public String listLab(Model model) {
        Collection<Labor> labors = laborMapper.selectAllLabor();
        model.addAttribute("labors", labors);
        return "manageLab";
    }

    @GetMapping("/admin/lab/add.html")
    public String toAddLabor(Model model) {
        return "addLab";
    }

    @PostMapping("/admin/lab/add.html")
    public String addLabor(
            @RequestParam("position") String position,
            @RequestParam("capa") String capa,
            @RequestParam("os") String os,
            @RequestParam("cpu") String cpu,
            @RequestParam("gpu") String gpu,
            @RequestParam("mainboard") String mainboard,
            Model model) {
        Labor labor = new Labor();
        labor.setPosition(position);
        labor.setCapa(Integer.parseInt(capa));

        if(os == null || os.length() < 0) {
            labor.setOs("");
        }else{
            labor.setOs(os);
        }

        if(cpu == null || cpu.length() < 0) {
            labor.setCpu("");
        }else{
            labor.setCpu(cpu);
        }

        if(gpu == null || gpu.length() < 0) {
            labor.setGpu("");
        }else{
            labor.setGpu(gpu);
        }

        if(mainboard == null || mainboard.length() < 0) {
            labor.setMainboard("");
        }else{
            labor.setMainboard(mainboard);
        }

        laborMapper.insertLabor(labor);
        return "redirect:/admin/lab.html";
    }

    @GetMapping("/admin/lab/del_{id}.html")
    public String delLab(@PathVariable("id") String l_id, Model model) {
        laborMapper.deleteLabor(Integer.parseInt(l_id));
        return "redirect:/admin/lab.html";
    }

    @GetMapping("/admin/lab/edit_{id}.html")
    public String toUpdateLab(@PathVariable("id") String l_id, Model model) {
        Labor lab = laborMapper.selectALabor(Integer.parseInt(l_id));
        model.addAttribute("lab", lab);
        return "updateLab";
    }

    @PostMapping("/admin/lab/edit_{id}.html")
    public String updateLab() {
        return "redirect:/admin/lab.html";
    }

    @GetMapping("/admin/lab/manage_lab{id}.html")
    public String infoLab(@PathVariable("id") String l_id,Model model) {
        model.addAttribute("l_id", Integer.parseInt(l_id));
        return "labInfo";
    }

    @PostMapping("/admin/lab/manage_lab{id}.html")
    @ResponseBody
    public List<DetailedEvent> allTime (
            @PathVariable("id") String l_id,
            Model model)
            throws Exception {
        String start = "2021-03-01 00:00:00";
        String end = "2021-06-30 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start_time = sdf.parse(start);
        Date end_time = sdf.parse(end);
        List<Event> events = laborMapper.getLaborTime(Integer.parseInt(l_id));
        //System.out.println(events);
        List<DetailedEvent> detailedEvents = new ArrayList<>();
        for (int i=0; i< events.size(); i++) {

            if(events.get(i).getType().equals("P")){
                //System.out.println(events.get(i).getStart_time());
                Date from_date= Helper.addDays(events.get(i).getDay()-1, start_time);
                Date s_time = Helper.addHours(events.get(i).getStart_time().toString(), from_date);
                Date e_time = Helper.addHours(events.get(i).getEnd_time().toString(), from_date);
                while (s_time.getTime() <= end_time.getTime()) {
                    DetailedEvent my = new DetailedEvent();
                    my.setCourse_name(events.get(i).getCourse_name());
                    my.setStart_time(Helper.changeFormat(s_time.toString()));
                    my.setEnd_time(Helper.changeFormat(e_time.toString()));
                    detailedEvents.add(my);
                    s_time = Helper.addDays(7, s_time);
                    e_time = Helper.addDays(7, e_time);
                }
            }
            else {
                DetailedEvent my = new DetailedEvent();
                my.setCourse_name(events.get(i).getCourse_name());
                my.setStart_time(events.get(i).getDate().toString()+" "+events.get(i).getStart_time().toString());
                my.setEnd_time(events.get(i).getDate().toString()+" "+events.get(i).getEnd_time().toString());

                detailedEvents.add(my);
            }

        }
        //System.out.println(detailedEvents);
        return detailedEvents;
    }
}
