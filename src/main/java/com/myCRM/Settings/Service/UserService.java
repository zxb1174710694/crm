package com.myCRM.Settings.Service;

import com.myCRM.Settings.domain.User;
import com.myCRM.exception.LoginException;

import java.util.List;

public interface UserService {
    User login(String loginAct, String password,String ip) throws LoginException;

    List<User> getUserList();
}
