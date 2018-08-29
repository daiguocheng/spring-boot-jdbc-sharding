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

    @Sharding(key = "#p0.getId()", writing = true)
    @Insert("insert into usr(id, mobile, name, password, updated) values(#{id}, #{mobile}, #{name}, #{password}, #{updated})")
    Integer insert(Usr usr);

    @Sharding("#a0")
    @Select("select * from usr where id = #{id}")
    Usr select(String id);

    @Crossing
    @Select("select count(id) from usr")
    List<Long> count();
}
