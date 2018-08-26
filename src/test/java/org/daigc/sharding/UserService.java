package org.daigc.sharding;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public abstract class UserService {

    public void register(Usr user) {
        create(user);
        create(new UsrIdx(user.getMobile(), user.getId()));
    }

    public Usr login(Long mobile, String password) {
        UsrIdx userIdx = get(mobile);
        Usr user = get(userIdx.getId());
        if (user.getPassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("用户名或密码不正确！");
    }

    public boolean consistent() {
        AtomicLong count = new AtomicLong();
        countIdx().forEach(e -> count.addAndGet(e));
        count().forEach(e -> count.addAndGet(-e));
        return count.get() == 0;
    }

    protected abstract UsrIdx create(UsrIdx userIdx);

    protected abstract Usr create(Usr user);

    protected abstract UsrIdx get(Long mobile);

    protected abstract Usr get(String id);

    protected abstract List<Long> countIdx();

    protected abstract List<Long> count();

}
