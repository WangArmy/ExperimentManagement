package com.example.demo.domain;

import lombok.Data;

import java.util.Date;

@Data
public class DetailedEvent {


    private int sec_id;
    private String course_name;
    private String start_time;
    private String end_time;

}
