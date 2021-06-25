package com.example.demo.mapper;

import com.example.demo.domain.Event;
import com.example.demo.domain.Labor;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import javax.naming.event.EventContext;
import java.util.Collection;
import java.util.List;

/*
 *
 * 实验室信息管理
 *
 * */

@Repository
public interface LaborMapper {

    @Select({"select * from v_AllLaborInfo;"})
    Collection<Labor> selectAllLabor();

    @Select({"select * from v_AllLaborInfo where l_id=#{l_id}"})
    Labor selectALabor(int l_id);

    @Insert({"insert into laboratory" +
            "(l_id, position, capa, os, cpu, gpu, mainboard) " +
            "values " +
            "(null, #{l.position}, #{l.capa}, #{l.os}, #{l.cpu}, #{l.gpu}, #{l.mainboard});"})
    int insertLabor(@Param("l") Labor l);

    @Delete({"delete from laboratory where l_id=#{l_id};"})
    int deleteLabor(int l_id);

    @Update({"update laboratory " +
            "set position=#{l.position}, capa=#{l.capa}, os=#{l.os}, cpu=#{l.cpu}, gpu=#{l.gpu}, mainboard=#{l.mainboard}" +
            "where l_id=#{l_id};"})
    int updateLabor(@Param("l") Labor l);


    @Select({"select course_name,day,start_time,end_time,date,type from v_AllLabTime where l_id=#{l_id};"})
    List<Event> getLaborTime(int l_id);



}
