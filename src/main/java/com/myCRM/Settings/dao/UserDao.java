package com.myCRM.Settings.dao;

import com.myCRM.Settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    User login(Map<String, Object> map);
    List<User> getUserList();

    List<User> getUserNameList();
}
