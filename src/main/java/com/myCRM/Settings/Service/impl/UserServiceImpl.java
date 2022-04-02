package com.myCRM.Settings.Service.impl;

import com.myCRM.Settings.Service.UserService;
import com.myCRM.Settings.dao.UserDao;
import com.myCRM.Settings.domain.User;
import com.myCRM.exception.LoginException;
import com.myCRM.util.DateTimeUtil;
import com.myCRM.util.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    //创建dao层接口实现类对象
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    public User login(String loginAct, String password,String ip) throws LoginException {
        //将账号密码传给dao层验证数据库
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",password);
        User user = userDao.login(map);
        System.out.println(user);
        if (user == null){
            throw new LoginException("账号密码错误");
        }

        //执行到这里说明账号密码没问题，接下来验证时间、账号状态、ip地址
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime) < 0){
            throw new LoginException("账号已失效");
        }

        if ("0".equals(user.getLockState())){
            throw new LoginException("账号已被封");
        }

        String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)){
            throw new LoginException("ip地址受限");
        }
        return user;
    }

    public List<User> getUserList() {
        List<User> userList = userDao.getUserList();
        return userList;
    }
}
