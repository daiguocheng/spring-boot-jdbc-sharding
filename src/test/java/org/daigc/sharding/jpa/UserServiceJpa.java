package org.daigc.sharding.jpa;

import org.daigc.sharding.Usr;
import org.daigc.sharding.UsrIdx;
import org.daigc.sharding.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceJpa extends UserService {

    @Autowired
    private UserIdxRepositoryJpa userIdxRepository;
    @Autowired
    private UserRepositoryJpa userRepository;

    @Override
    protected UsrIdx create(UsrIdx userIdx) {
        return userIdxRepository.save(userIdx);
    }

    @Override
    protected Usr create(Usr user) {
        return userRepository.save(user);
    }

    @Override
    protected UsrIdx get(Long mobile) {
        return userIdxRepository.findById(mobile).get();
    }

    @Override
    protected Usr get(String id) {
        return userRepository.findById(id).get();
    }

    @Override
    protected List<Long> countIdx() {
        return userIdxRepository.count();
    }

    @Override
    protected List<Long> count() {
        return userRepository.count();
    }
}
