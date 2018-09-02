package org.daigc.sharding.mybatis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.daigc.sharding.Crossing;
import org.daigc.sharding.Sharding;
import org.daigc.sharding.UsrIdx;

import java.util.List;

@Mapper
public interface UserIdxRepositoryMybatis {

    @Sharding(key = "#a0.getMobile()")
    @Insert("insert into usr_idx(mobile, id, created) values(#{mobile}, #{id}, #{created})")
    Integer insert(UsrIdx usrIdx);

    @Sharding(value = "#p0", writing = false)
    @Select("select * from usr_idx where mobile = #{mobile}")
    UsrIdx select(Long mobile);

    @Crossing(writing = false)
    @Select("select count(mobile) from usr_idx")
    List<Long> count();

}
