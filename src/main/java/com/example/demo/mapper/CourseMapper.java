package com.example.demo.mapper;

import com.example.demo.domain.CourseInfo;
import com.example.demo.domain.TimeSlot;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseMapper {

    @Select({"select * from v_courseinfo;"})
    List<CourseInfo> selectCourse();

    @Select({"select * from timeslot"})
    List<TimeSlot> selectTimeslot();

}
