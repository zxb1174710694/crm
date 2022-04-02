package com.myCRM.Settings.web.controller;

import com.myCRM.Settings.Service.UserService;
import com.myCRM.Settings.Service.impl.UserServiceImpl;
import com.myCRM.Settings.domain.User;
import com.myCRM.util.MD5Util;
import com.myCRM.util.PrintJson;
import com.myCRM.util.ServiceFactory;
import sun.security.provider.MD5;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String path = request.getServletPath();//  path = /setting/user/login.do
        if ("/setting/user/login.do".equals(path)){
            //说明是登录页面进来的
                login(request,response);
        }else if ("/setting/user/xxx.do".equals(path)){

        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        //验证用户登录
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //将用户密码加密
        String password = MD5Util.getMD5(loginPwd);

        //获取浏览器ip地址
        String ip = request.getRemoteAddr();

        //创建动态代理
        UserService us = new UserServiceImpl(); //厂家
        UserService target = (UserService) ServiceFactory.getService(us); //代理对象

        try{
            //调用业务层login方法（业务层会抛异常）
            User user = target.login(loginAct,password,ip);

            //注意：这里是代理对象来调用login方法，不是厂家调用的，他先进入invoke方法，
            //      然后通过method调用厂家的方法，这时候后台会抛出异常在控制台当中打印，同时抛到invoke方法当中,
            //      然后继续回到invoke方法向下执行，直到invoke结束。
            //      重点: 这里invoke只是接受了异常，但是并没有继续往上抛出，所以这里收不到异常
            //            try语句块就会以为没有异常，就不会执行catch语句块内容。
            //      因为这是代理对象调用的方法，所以只要invoke不报异常，这里就不会跳到catch语句块当中。
            //      所以要异常继续上抛：throw e.getCause();

            //将user数据放入Session域
            HttpSession session = request.getSession();
            session.setAttribute("user",user);

            //将成功的结果返回给前端
            PrintJson.printJsonFlag(response,true);


        }catch (Exception e){
            e.printStackTrace();

            //解决响应乱码
            //response.setContentType("application/json;charset=UTF-8");

            //将失败结果返回给前端
            String msg = e.getMessage();

            Map<String,Object> map = new HashMap<String,Object>();
            map.put("success",false);
            map.put("msg",msg);

            PrintJson.printJsonObj(response,map);
        }



    }
}
