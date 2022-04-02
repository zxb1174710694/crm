package com.myCRM.workbench.Web.controller;

import com.myCRM.Settings.Service.UserService;
import com.myCRM.Settings.Service.impl.UserServiceImpl;
import com.myCRM.Settings.domain.User;
import com.myCRM.util.DateTimeUtil;
import com.myCRM.util.PrintJson;
import com.myCRM.util.ServiceFactory;
import com.myCRM.util.UUIDUtil;
import com.myCRM.vo.paginationVo;
import com.myCRM.workbench.Service.ActivityService;
import com.myCRM.workbench.Service.ClueService;
import com.myCRM.workbench.Service.impl.ActivityServiceImpl;
import com.myCRM.workbench.Service.impl.ClueServiceImpl;
import com.myCRM.workbench.domain.Activity;
import com.myCRM.workbench.domain.ActivityRemark;
import com.myCRM.workbench.domain.Clue;
import com.myCRM.workbench.domain.Tran;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*市场*/
        String path = request.getServletPath();//  path = /setting/user/login.do
        if ("/workbench/clue/getUserList.do".equals(path)){

            getUserList(request,response);
        }else if ("/workbench/clue/savaClue.do".equals(path)){
            savaClue(request,response);
        }else if ("/workbench/clue/PageList.do".equals(path)){
            PageList(request,response);
        }else if ("/workbench/clue/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/clue/activity_clue.do".equals(path)){
            activity_clue(request,response);
        }else if ("/workbench/clue/delete_activity_clue.do".equals(path)){
            delete_activity_clue(request,response);
        }else if ("/workbench/clue/relationList.do".equals(path)){
            relationList(request,response);
        }else if ("/workbench/clue/bund.do".equals(path)){
            bund(request,response);
        }else if ("/workbench/clue/showSearchModal.do".equals(path)){
            showSearchModal(request,response);
        }else if ("/workbench/clue/form.do".equals(path)){
            form(request,response);
        }
        System.out.println("当前请求路径："+path);
    }

    //提交表单数据
    private void form(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入表单提交操作");

        String clueId = request.getParameter("clueId");
        //System.out.println(clueId);
        String createBy = ((User)request.getSession().getAttribute("user")).getCreateBy();

        Tran t = null;
        if ("a".equals(request.getParameter("flag"))){

            //打勾
            t = new Tran();
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String exceptionDate = request.getParameter("exceptionDate");
            String state = request.getParameter("state");
            String activityId = request.getParameter("activityId");

           // t.setId(UUIDUtil.getUUID());控制器只负责接收数据
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(exceptionDate);
            t.setStage(state);
            t.setActivityId(activityId);
        }

        //到这说明没打勾

        ClueService target = (ClueService) new ServiceFactory().getService(new ClueServiceImpl());
        boolean flag = target.convert(clueId,t,createBy);

        System.out.println("线索转换："+flag);

        //重定向跳转页面
        try {
            response.sendRedirect("index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //展开模态框之前将数据准备好便于搜索
    private void showSearchModal(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入搜索框操作");
        //获取前端数据
        String searchText = request.getParameter("SearchText");

        ActivityService target= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
       ArrayList<Activity> arrayList =  target.SearchText(searchText);

       PrintJson.printJsonObj(response,arrayList);
        System.out.println("搜索记录条数："+arrayList.size());



    }

    //关联线索
    private void bund(HttpServletRequest request, HttpServletResponse response) {
        //获取前端数据
        String cid = request.getParameter("cid");
        String[] aids = request.getParameterValues("aid");

       ClueService target = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
       Boolean flag = target.bund(cid,aids);

       PrintJson.printJsonFlag(response,flag);
    }

    //查询搜索框的内容
    private void relationList(HttpServletRequest request, HttpServletResponse response) {

        String aname = request.getParameter("aname");   //活动名称
        String clueId = request.getParameter("clueId");//线索id（代表当前人物）

        Map<String,String> map = new HashMap<String, String>();
        map.put("aname",aname);
        map.put("clueId",clueId);

        ActivityService target = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        ArrayList<Activity> arrayList = target.getRelationList(map);

        PrintJson.printJsonObj(response,arrayList);

    }

    //解除关联关系操作
    private void delete_activity_clue(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入解除关系操作");
        String id = request.getParameter("id");


        ClueService target = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        //Boolean flag = target.deleteRelation(relationId);
        Boolean flag = target.delete_activity_clue(id);

        PrintJson.printJsonFlag(response,flag);
    }

    //市场活动与线索关联关系表
    private void activity_clue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入市场活动与线索关联关系表");

        String id = request.getParameter("id");

        ClueService target = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
       ArrayList<Activity> arrayList =  target.getActivity_clue(id);



       PrintJson.printJsonObj(response,arrayList);
    }

    //详细详细页
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入线索详细信息页");
        String id = request.getParameter("id");

        ClueService target = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue = target.detail(id);

        //将数据转发到jsp页面，用转发比较容易取值
        request.setAttribute("c",clue);
        request.getRequestDispatcher("detail.jsp").forward(request,response);
    }


    //获取数据库数据展示分页
    private void PageList(HttpServletRequest request, HttpServletResponse response) {

        int PageNo = Integer.parseInt(request.getParameter("PageNo"));
        int PageSize = Integer.parseInt(request.getParameter("PageSize"));

        int skipNo = (PageNo - 1)*PageSize;
        System.out.println("获取数据库数据");
        ClueService target = (ClueService) new ServiceFactory().getService(new ClueServiceImpl());

        Map<String,Object> Map= new HashMap<String, Object>();
        Map.put("skipNo",skipNo);
        Map.put("pageSize",PageSize);

        Map<String,Object> map = target.getPageList(Map);


       //返回给前端
        PrintJson.printJsonObj(response,map);


    }

    //保存新增的数据
    private void savaClue(HttpServletRequest request, HttpServletResponse response) {

        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy =request.getParameter("createBy");
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue clue = new Clue();
        clue.setId(id);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);

        System.out.println(clue);

        ClueService target = (ClueService) new ServiceFactory().getService(new ClueServiceImpl());
        Boolean flag = target.savaClue(clue);

        PrintJson.printJsonFlag(response,flag);

    }

    //打开新增模态框
    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入线索查询页面");

        ClueService target = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        ArrayList<User> arrayList = target.getUserList();

        System.out.println("集合长度："+arrayList.size());
        PrintJson.printJsonObj(response,arrayList);
    }


}
