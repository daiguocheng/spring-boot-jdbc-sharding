package org.daigc.sharding;

import org.junit.Assert;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public abstract class AbstractUserTest {

    protected abstract UserService getUserService();

    public void repeat(int n) {
        for (int i = 0; i < n; i++) {
            getUserService().register(next(i));
        }
    }

    public void consistent() {
        Assert.assertTrue(getUserService().consistent());
    }

    public void registerAndLogin() {
        Usr u = next(new Random().nextInt());
        getUserService().register(u);
        Usr lu = getUserService().login(u.getMobile(), u.getPassword());
        Assert.assertEquals(u, lu);
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
