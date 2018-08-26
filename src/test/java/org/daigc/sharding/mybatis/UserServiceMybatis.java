package org.daigc.sharding.mybatis;

import org.daigc.sharding.Usr;
import org.daigc.sharding.UsrIdx;
import org.daigc.sharding.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceMybatis extends UserService {

    @Autowired
    private UserIdxRepositoryMybatis userIdxRepository;
    @Autowired
    private UserRepositoryMybatis userRepository;

    @Override
    protected UsrIdx create(UsrIdx userIdx) {
        userIdxRepository.insert(userIdx);
        return userIdx;
    }

    @Override
    protected Usr create(Usr user) {
        userRepository.insert(user);
        return user;
    }

    @Override
    protected UsrIdx get(Long mobile) {
        return userIdxRepository.select(mobile);
    }

    @Override
    protected Usr get(String id) {
        return userRepository.select(id);
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
