package com.example.demo.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class Teacher {

    public String t_id;
    public String title;
    public String dept_name;
    public String major;
    public String mail;
    public String name;
    public Date   birthday;
    public String gender;
    public String role;

}
