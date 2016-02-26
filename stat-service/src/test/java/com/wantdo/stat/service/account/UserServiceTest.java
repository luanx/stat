package com.wantdo.stat.service.account;

import com.wantdo.stat.dao.account.UserDao;
import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.service.data.UserData;
import com.wantdo.stat.utils.Clock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserService的测试用例,测试Service层的业务逻辑
 *
 * @ Date : 15/11/16
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserDao userDao;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveUser(){
        User user = UserData.randomUser();
        Date currentTime = new Date();
        userService.setClock(new Clock.MockClock(currentTime));

        userService.saveUser(user);

        System.out.println(user.getPassword());
        assertThat(user.getPassword()).isNotNull();
    }

}
