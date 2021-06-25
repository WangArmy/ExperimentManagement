package com.example.demo.mapper;

import com.example.demo.domain.Event;
import com.example.demo.domain.StuTake;
import com.example.demo.domain.Student;
import jdk.nashorn.internal.ir.CallNode;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/*
 *
 * 学生信息管理
 *
 * */


@Repository
public interface StudentMapper {

    /*
     * 获取全部学生的个人信息
     * */
    @Select({
            "select * from v_AllStudentsInfo where role='S'"
    })
    Collection<Student> selectAllStudents();

    /*
     * 获取指定学生的个人信息
     * */
    @Select({
            "select * from v_AllStudentsInfo where role='S' and s_id=#{username}"
    })
    Student selectAStudent(String username);

    /*
     * 插入新学生
     * */
    @Insert({
            "insert into student" +
                    "(s_id, year, dept_id, class_id, situation, degree, name, birthday, gender, mail, role) " +
                    "values " +
                    "(null, #{s.year}, #{s.dept_name}, #{s.class_id}, #{s.situation}, #{s.degree}, #{s.name}, #{s.birthday}, #{s.gender}, null, 'S')"
    })
    int insertStudent(@Param("s") Student s);

    @Update({"update student set role=#{role} where s_id=#{username}"})
    int resolveStudent(String username, String role);

    /*
     * 更新学生信息
     * */
    @Update({
            "update student set name=#{s.name}, gender=#{s.gender}, birthday=#{s.birthday}, dept_id=#{s.dept_name}, " +
                    "class_id=#{s.class_id}, situation=#{s.situation}, degree=#{s.degree} " +
                    "where s_id=#{s.s_id}"
    })
    int updateStudent(@Param("s") Student s);

    /*
     * 删除学生信息
     * */
    @Update({
            "update student set role = 'Z' where s_id=#{username};"
    })
    int deleteStudent(String username);

    @Select({"select s_id from v_AllStudentsInfo where role='S'"})
    List<String> getAllStudentID();

    @Select({"select name, s_id, take_id, teach_id, seat_id, attend, nums, regular, final, total from v_stuSection where s_id=#{s_id} "})
    List<StuTake> getAllSection(String s_id);

    @Select({"select sec_id, concat(course_name, ' ', position) as course_name, day, start_time, end_time, date, type from v_teachersection where teach_id=#{teach_id}"})
    Event StuCourse(int teach_id);

}
