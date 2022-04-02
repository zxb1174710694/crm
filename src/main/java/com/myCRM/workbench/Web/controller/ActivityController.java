package com.myCRM.workbench.Web.controller;

import com.myCRM.Settings.Service.UserService;
import com.myCRM.Settings.Service.impl.UserServiceImpl;
import com.myCRM.Settings.dao.UserDao;
import com.myCRM.Settings.domain.User;
import com.myCRM.util.*;
import com.myCRM.workbench.Service.ActivityService;
import com.myCRM.workbench.Service.impl.ActivityServiceImpl;
import com.myCRM.workbench.dao.ActivityRemarkDao;
import com.myCRM.workbench.domain.Activity;
import com.myCRM.vo.paginationVo;
import com.myCRM.workbench.domain.ActivityRemark;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.security.auth.login.CredentialException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*市场*/
        String path = request.getServletPath();//  path = /setting/user/login.do
        if ("/workbench/activity/getUserList.do".equals(path)){
            activity(request,response);

        }else if ("/workbench/activity/insert.do".equals(path)){
            insert(request,response);
        }else if ("/workbench/activity/getList.do".equals(path)){
            getList(request,response);
        }else if ("/workbench/activity/delete.do".equals(path)){
            delete(request,response);
        }else if ("/workbench/activity/edit.do".equals(path)){
            edit(request,response);
        }else if ("/workbench/activity/update.do".equals(path)){
            update(request,response);
        }else if ("/workbench/activity/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/activity/getMessage.do".equals(path)){
            getMessage(request,response);
        }else if ("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if ("/workbench/activity/savaRemark.do".equals(path)){
            savaRemark(request,response);
        }
    }

    //保存新增的备注信息
    private void savaRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入保存备注信息页");
        //接收前端数据
        String activityId = request.getParameter("activityId"); //活动id
        String context = request.getParameter("context");       //文本域

        ActivityService target = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        //Activity activity =  target.getActivity(activityId);

        ActivityRemark a = new ActivityRemark();

        a.setId(UUIDUtil.getUUID());
        a.setNoteContent(context);
        a.setActivityId(activityId);
        a.setEditFlag("0");
        a.setCreateTime(DateTimeUtil.getSysTime());


        a.setCreateBy(((User)(request.getSession().getAttribute("user"))).getName());

        System.out.println(a);

        Boolean flag = target.savaRemark(a);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("activityRemark",a);

        if (flag){
            PrintJson.printJsonObj(response,map);

        }else {
            System.out.println("添加备注失败");
        }


    }


    //删除备注
    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入删除备注操作");
        //获取前端信息
        String id = request.getParameter("id");
        ActivityService target = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Boolean flag = target.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);

    }

    //获取备注信息
    private void getMessage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入备注信息页");
        String activityId = request.getParameter("id");
        System.out.println(activityId);
        ActivityService target = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        ArrayList<ActivityRemark> arrayList = target.getRemarkList(activityId);

        PrintJson.printJsonObj(response,arrayList);
    }

    //获取信息并跳转到jsp页面
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入详细信息页");
        //获取前端id
        String id = request.getParameter("id");
        System.out.println(id);

        ActivityService target = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity a = target.detail(id);
        System.out.println(a);

        //因为是传统请求，所以需要将数据放入请求域当中
        request.setAttribute("a",a);

        request.getRequestDispatcher("detail.jsp").forward(request,response);



    }

    //更新模态框数据
    private void update(HttpServletRequest request, HttpServletResponse response) {
        //获取前端数据
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String editBy = request.getParameter("createBy");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String describe = request.getParameter("describe");

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(describe);
        a.setEditTime(DateTimeUtil.getSysTime());
        a.setEditBy(editBy);

        ActivityService target = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = target.update(a);

        PrintJson.printJsonFlag(response,flag);

        System.out.println(a);
    }

    //修改模态框的数据
    private void edit(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入修改操作");
        //获取前端数据
        String id = request.getParameter("id");
        System.out.println(id);
        //前端需要user集合、activity的记录
        ActivityService target = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        paginationVo<User> vo = target.edit(id);

        //将数据发送给前端
        PrintJson.printJsonObj(response,vo);
    }

    //删除操作
    private void delete(HttpServletRequest request, HttpServletResponse response) {
        //
        System.out.println("进入删除操作");
        //获取前端数据
        String ids[] = request.getParameterValues("id");//  这里的id是拼接的时候给的。
        System.out.println("前端数据长度："+ids.length);

        //动态代理
        ActivityService target = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = target.delete(ids);

        PrintJson.printJsonFlag(response,flag);
    }

    //查询数据库信息并展现
    private void getList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("开始访问数据库");

        //接收前端数据
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");

        int pageNo = Integer.parseInt(pageNoStr);
        int pageSize = Integer.parseInt(pageSizeStr);

        int skipNo = (pageNo - 1)*pageSize;
        System.out.println(skipNo+pageSize);

        //将数据传进业务层
        //因为不能用domain，所以只能用map
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipNo",skipNo);
        map.put("pageSize",pageSize);

        //创建·动态代理
        ActivityService target = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        paginationVo<Activity> vo = target.PageList(map);  //前端需要total和activity的集合，所以需要返回VO类来封装

        //将vo返回给前端
        PrintJson.printJsonObj(response,vo);

       /* //接收前端数据
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNo = request.getParameter("pageNo");
        String pageSize = request.getParameter("pageSize");

        int PageNo = Integer.parseInt(pageNo);
        int PageSize = Integer.parseInt(pageSize);

        int skipNo = (PageNo - 1)*PageSize;

        //将数据用map打包发送给业务层
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipNo",skipNo);
        map.put("pageSize",PageSize);



        //动态代理访问数据库
        ActivityService target = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        //因为返回的数据有total和Activity，所以需要创建VO类来接收
        paginationVo<Activity> vo  = target.getList(map);

        //将数据返给前端
        PrintJson.printJsonObj(response,vo);
*/

    }

    //添加市场操作
    private void insert(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("请求成功");

        //主键，用UUId创建
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");   //外键，关联用户表的id
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //调用当前的时间
        String createTime = DateTimeUtil.getSysTime();
        //session域获取当前用户的名字v
        //String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createBy = request.getParameter("createBy");
        System.out.println("创建人："+createBy);

        //封装到对象当中
        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);

        //将对象传入数据库(动态代理)
        ActivityService activityService = new ActivityServiceImpl();
        ActivityService target = (ActivityService) ServiceFactory.getService(activityService);

        Boolean flag = target.insert(a);

        PrintJson.printJsonFlag(response,flag);
    }

    //查询
    private void activity(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("市场响应");
        //创建厂家的代理对象(因为是查询用户信息，所以new UserService)
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        //获取数据
        List<User> list = us.getUserList();

        //打包json发送前端
        PrintJson.printJsonObj(response,list);

    }
}
