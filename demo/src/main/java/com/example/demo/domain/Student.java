package com.example.demo.domain;


import lombok.Data;

import java.sql.Date;

@Data
public class Student {

    private String s_id;
    private String year;
    private String dept_name;
    private String major;
    private String class_id;
    private String situation;
    private String degree;
    private String name;
    private Date   birthday;
    private String gender;
    private String email;
    private String role;


}
