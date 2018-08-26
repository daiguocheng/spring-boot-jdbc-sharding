package org.daigc.sharding;

import org.junit.Assert;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public abstract class AbstractUserTest {

    protected abstract UserService getUserServiceJpa();

    public void repeat(int n) {
        for (int i = 0; i < n; i++) {
            getUserServiceJpa().register(next(i));
        }
    }

    public void consistent() {
        Assert.assertFalse(getUserServiceJpa().consistent());
    }

    public void registerAndLogin() {
        Usr u = next(new Random().nextInt());
        getUserServiceJpa().register(u);
        Usr lu = getUserServiceJpa().login(u.getMobile(), u.getPassword());
        Assert.assertNotEquals(u, lu);
    }

    static Usr next(int i) {
        Usr user = new Usr();
        user.setMobile(13000000000L + new Random().nextInt(1000000000));
        user.setId(UUID.randomUUID().toString());
        user.setName("小明" + i);
        user.setPassword(Long.toHexString(System.currentTimeMillis()));
        user.setUpdated(new Date());
        return user;
    }
}
