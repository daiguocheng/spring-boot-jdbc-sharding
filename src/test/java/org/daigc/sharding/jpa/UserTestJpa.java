package org.daigc.sharding.jpa;

import org.daigc.sharding.AbstractUserTest;
import org.daigc.sharding.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTestJpa extends AbstractUserTest {


    @Autowired
    private UserServiceJpa userServiceJpa;

    @Override
    protected UserService getUserServiceJpa() {
        return userServiceJpa;
    }

    @Test
    public void testSharding() {
        repeat(400);
    }

    @Test
    public void testCrossing() {
        consistent();
    }

    @Test
    public void test2p() {
        registerAndLogin();
    }

}
