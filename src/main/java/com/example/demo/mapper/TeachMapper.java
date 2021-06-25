package com.example.demo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachMapper {

    @Insert({
            "insert into " +
            "teach(teach_id, sec_id, t_id) " +
            "values " +
            "(null, #{sec_id}, #{t_id})"})
    int insertTeach(int sec_id, String t_id);

    @Select({"select count(s_id) from take group by teach_id having teach_id=#{teach_id}"})
    String countSeat(int teach_id);

    @Insert({"insert into " +
            "take " +
            "values " +
            "(null, #{teach_id}, #{s_id}, #{seat_id}, #{a}, #{b}, #{c}, #{d}, #{e})"})
    int insertTake(int teach_id, String s_id, int seat_id, String a, int b, int c, int d, int e);

}
