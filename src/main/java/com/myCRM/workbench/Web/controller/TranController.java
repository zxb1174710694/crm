package com.myCRM.workbench.Web.controller;

import com.myCRM.Settings.Service.UserService;
import com.myCRM.Settings.Service.impl.UserServiceImpl;
import com.myCRM.Settings.domain.User;
import com.myCRM.util.DateTimeUtil;
import com.myCRM.util.PrintJson;
import com.myCRM.util.ServiceFactory;
import com.myCRM.util.UUIDUtil;
import com.myCRM.workbench.Service.ActivityService;
import com.myCRM.workbench.Service.CustomerService;
import com.myCRM.workbench.Service.TranService;
import com.myCRM.workbench.Service.impl.ActivityServiceImpl;
import com.myCRM.workbench.Service.impl.CustomerServiceImpl;
import com.myCRM.workbench.Service.impl.TranServiceImpl;
import com.myCRM.workbench.domain.Activity;
import com.myCRM.workbench.domain.Customer;
import com.myCRM.workbench.domain.Tran;
import com.myCRM.workbench.domain.TranHistory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();//  path = /setting/user/login.do
        if ("/workbench/transaction/add.do".equals(path)){
           add(request,response);

        }else if ("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request,response);
        }else if ("/workbench/transaction/createSate.do".equals(path)){
            createSate(request,response);
        }else if ("/workbench/transaction/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/transaction/PageList.do".equals(path)){
            PageList(request,response);
        }else if ("/workbench/transaction/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/transaction/getHistoryByTranId.do".equals(path)){
            getHistoryByTranId(request,response);
        }else if ("/workbench/transaction/getChars.do".equals(path)){
            getChars(request,response);
        }
    }

    //??????????????????
    private void getChars(HttpServletRequest request, HttpServletResponse response) {

        TranService target = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> map = target.getChars();

        PrintJson.printJsonObj(response,map);
    }

    //?????????????????????
    private void getHistoryByTranId(HttpServletRequest request, HttpServletResponse response) {
        //?????????????????????tranId
        String tranId = request.getParameter("tranId");

        TranService target = (TranService) ServiceFactory.getService(new TranServiceImpl());

       ArrayList<TranHistory> arrayList =  target.getHistoryList(request,tranId);

        System.out.println("?????????????????????"+arrayList.size());

        PrintJson.printJsonObj(response,arrayList);


    }

    //???????????????
    private void detail(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("?????????????????????");
        String id = request.getParameter("id");
        System.out.println(id);



        TranService target = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran t = target.getTranById(id);

        //???????????????
        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        String possible = pMap.get(t.getStage());

        t.setPossible(possible);

        System.out.println(t);

        //???????????????
        request.setAttribute("t",t);
        try {
            request.getRequestDispatcher("detail.jsp").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //????????????
    private void PageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("??????tranList????????????");

        String pageNo = request.getParameter("pageNo");
        String pageSize = request.getParameter("pageSize");

        int PageNo = Integer.parseInt(pageNo);
        int PageSize = Integer.parseInt(pageSize);
        int skipNo = (PageNo - 1)*PageSize;

        Map<String,Object> map = new HashMap<String, Object>();

        map.put("skipNo",skipNo);
        map.put("PageSize",PageSize);

        TranService target = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> m =  target.PageList(map);

        System.out.println("???????????????????????????"+m.size());


       PrintJson.printJsonObj(response,m);
    }


    //??????????????????
    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("????????????????????????");
        //????????????????????????
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");//????????????????????????
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        String createBy = ((User)request.getSession().getAttribute("user")).getCreateBy();
        String createTime = DateTimeUtil.getSysTime();

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        //t.setCustomerId(customerId);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);

        System.out.println(t);

        TranService target = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = target.save(t,customerName);

        if (flag){
            try {
                response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //??????????????????
    private void createSate(HttpServletRequest request, HttpServletResponse response) {
        //??????????????????
        String state = request.getParameter("state");

        ServletContext application = request.getServletContext();
        HashMap<String,String> pMap = (HashMap<String, String>) application.getAttribute("pMap");

        String possible = pMap.get(state);

        System.out.println("?????????"+state+",????????????"+possible);

        PrintJson.printJsonObj(response,possible);
    }


    //????????????
    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("????????????????????????????????????");

        String name = request.getParameter("name");

        System.out.println("??????????????????+"+name);

        CustomerService target = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
       ArrayList<String> arrayList =  target.getCustomerName(name);

        PrintJson.printJsonObj(response,arrayList);
    }

    //?????????????????????????????????????????????
    private void add(HttpServletRequest request,HttpServletResponse response){
        System.out.println("????????????????????????");

        //????????????????????????
        ActivityService target = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<User> list =  target.getUserNameList();

        System.out.println(list.size());

        try {
            request.setAttribute("uList",list);
            request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);

        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
