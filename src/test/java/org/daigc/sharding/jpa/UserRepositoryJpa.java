package org.daigc.sharding.jpa;

import org.daigc.sharding.Crossing;
import org.daigc.sharding.Sharding;
import org.daigc.sharding.Usr;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryJpa extends Repository<Usr, String> {

    @Sharding(key = "#p0.getId()",writing = true)
    Usr save(Usr user);

    @Sharding(key = "#a0")
    Optional<Usr> findById(String id);

    @Crossing
    List<Long> count();

}
