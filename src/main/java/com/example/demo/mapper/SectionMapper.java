package com.example.demo.mapper;

import com.example.demo.domain.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionMapper {

    @Select({"select * from v_allSection"})
    List<Section> listAllCourseInfo();

    @Insert({
            "insert into " +
            "sectioninfo(course_id, year, semester) " +
            "values " +
            "(#{sc.course_id}, #{sc.year}, #{sc.semester});"})
    @Options(useGeneratedKeys = true, keyProperty = "sc.sec_id")
    int insertSectionInfo(@Param("sc")SeCourse sc);

    @Insert({
            "insert into" +
            " section(sec_id, day, l_id, start_slot, end_slot, type, date) " +
            "values " +
            "(#{sec_id}, #{day}, #{l_id}, #{start_slot}, #{end_slot}, #{type}, #{date});"})
    int insertSection(int sec_id, int day, int l_id, int start_slot, int end_slot, String type, String date);

    //　获取一个指定教师的课程信息
    @Select({"select year, semester, sec_id, course_name, " +
            "        position, type, date, day, " +
            "        concat(start_time,'-',end_time) as time " +
            "from v_teachersection " +
            "where t_id=#{t_id}"})
    List<Teach> listATeacher(String t_id);

    @Select({"select course_name from v_allSection where sec_id=#{sec_id}"})
    String getSectioName(int sec_id);

    @Select({"select teach_id from teach where sec_id=#{sec_id}"})
    int getTeachID(int sec_id);

    @Select({"select name, s_id, take_id, teach_id, seat_id, attend, nums, regular, final as _final, total from v_stuSection where teach_id=#{teach_id}"})
    List<StuTake> getAllStuSection(int teach_id);

    @Select({"select name, s_id, take_id, teach_id, seat_id, attend, nums, regular, final as _final, total from v_stuSection where teach_id=#{teach_id} and s_id=#{s_id}"})
    StuTake getAStuSection(int teach_id, String s_id);

    @Update({"update take set regular=#{regular}, final=#{_final} where teach_id=#{teach_id} and s_id=#{s_id}"})
    int updateStuGrade(int teach_id, String s_id, int regular, int _final);

    @Update({"update take set attend=concat(attend,'0') where teach_id=#{teach_id}"})
    int signAllStu(int teach_id);

    @Update({"update take set nums = nums+1, attend=concat(substring(attend,1, #{cur}-1), '1', substring(attend, #{cur}+1)) where teach_id=#{teach_id} and s_id=#{s_id}"})
    int signAStu(int teach_id, String s_id, int cur);

    @Select({"select year, semester from sectioninfo where sec_id=#{sec_id}"})
    Year getSectionInfo(int sec_id);
}
