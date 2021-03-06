package org.daigc.sharding.mybatis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.daigc.sharding.Crossing;
import org.daigc.sharding.Sharding;
import org.daigc.sharding.Usr;

import java.util.List;

@Mapper
public interface UserRepositoryMybatis {

    @Sharding(key = "#p0.getId()")
    @Insert("insert into usr(id, mobile, name, password, updated) values(#{id}, #{mobile}, #{name}, #{password}, #{updated})")
    Integer insert(Usr usr);

    @Sharding(value = "#a0", writing = false)
    @Select("select * from usr where id = #{id}")
    Usr select(String id);

    @Crossing(writing = false)
    @Select("select count(id) from usr")
    List<Long> count();
}
