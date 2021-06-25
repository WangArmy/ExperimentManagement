package com.example.demo.domain;

import lombok.Data;

import java.sql.Date;
import java.sql.Time;

@Data
public class Event {

    private int sec_id;
    private String course_name;
    private int day;
    private Time start_time;
    private Time end_time;
    private Date date;
    private String type;

}
