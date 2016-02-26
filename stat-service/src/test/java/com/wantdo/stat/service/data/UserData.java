package com.wantdo.stat.service.data;

import com.wantdo.stat.data.RandomData;
import com.wantdo.stat.entity.account.User;

/**
 * @ Date : 15/11/16
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class UserData {

    public static User randomUser(){
        User user = new User();
        user.setLoginName(RandomData.randomName("user"));
        user.setName(RandomData.randomName("User"));
        user.setPlainPassword(RandomData.randomName("password"));

        return user;
    }
}
