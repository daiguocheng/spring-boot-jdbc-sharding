package org.daigc.sharding.jpa;

import org.daigc.sharding.Crossing;
import org.daigc.sharding.Sharding;
import org.daigc.sharding.UsrIdx;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserIdxRepositoryJpa extends Repository<UsrIdx, Long> {

    @Sharding(key = "#a0.getMobile()")
    UsrIdx save(UsrIdx userIdx);

    @Sharding(key = "#p0", writing = false)
    Optional<UsrIdx> findById(Long mobile);

    @Crossing(writing = false)
    List<Long> count();

}
