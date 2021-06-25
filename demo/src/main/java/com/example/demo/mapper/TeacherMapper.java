package com.example.demo.mapper;

import com.example.demo.domain.Event;
import com.example.demo.domain.Teacher;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/*
*
* 教师信息管理
*
* */

@Repository
public interface TeacherMapper {
    /*
     * 获取全部教师信息
     * */
    @Select({
            "select * from v_AllTeachersInfo where role='T'"
    })
    Collection<Teacher> selectAllTeachers();

    /*
     * 获取指定教师信息
     * */
    @Select({
            "select * from v_AllTeachersInfo where role='T' and t_id=#{username}"
    })
    Teacher selectATeacher(String username);

    /*
     * 插入新教师
     * */
    @Insert({"insert into teacher" +
            "(t_id, title, dept_id, major, mail, name, birthday, gender, role) " +
            "values " +
            "(null, #{t.title}, #{t.dept_name}, #{t.major}, null, #{t.name}, #{t.birthday}, #{t.gender}, 'T')"
    })
    int InsertTeacher(@Param("t") Teacher t);

    @Update({"update teacher set role='Z' where t_id=#{username}"})
    int deleteTeacher(String username);

    @Update({"update teacher set role=#{role} where t_id=#{username}"})
    int resolveTeacher(String username, String role);

    @Update({"update teacher " +
            "set name=#{t.name}, title=#{t.title}, dept_id=#{t.dept_name}, major=#{t.major}, birthday=#{t.birthday}, gender=#{t.gender} " +
            "where t_id=#{t.t_id}"})
    int updateTeacher(@Param("t") Teacher t);

    @Select({"select sec_id, concat(course_name,'\n',position) as course_name, day, start_time, end_time, type, date from v_teachersection where teach_id=#{id};"})
    List<Event> getTSection(String id);
}
