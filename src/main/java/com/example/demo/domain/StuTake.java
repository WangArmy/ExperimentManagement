package com.example.demo.domain;

import lombok.Data;

@Data
public class StuTake {

    private String name;
    private String s_id;
    private int take_id;
    private int teach_id;
    private int seat_id;
    private String attend;
    private int nums;
    private int regular;
    private int _final;
    private int total;

}
