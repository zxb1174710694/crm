package com.myCRM.workbench.Service.impl;

import com.myCRM.util.SqlSessionUtil;
import com.myCRM.util.UUIDUtil;
import com.myCRM.workbench.Service.TranService;
import com.myCRM.workbench.dao.CustomerDao;
import com.myCRM.workbench.dao.TranDao;
import com.myCRM.workbench.dao.TranHistoryDao;
import com.myCRM.workbench.domain.Customer;
import com.myCRM.workbench.domain.Tran;
import com.myCRM.workbench.domain.TranHistory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public boolean save(Tran t, String customerName) {
        //判断有没有这个客户，有直接拿id，没有创建一个
        //这里需要添加三张表：交易、客户、交易历史
        boolean flag = false;
        Customer customer = customerDao.getCustomerByName(customerName);
        if (customer == null){
            System.out.println("没有这个客户");
            //创建新客户
            Customer c = new Customer();
            c.setId(UUIDUtil.getUUID());
            c.setOwner(t.getOwner());
            c.setContactSummary(t.getContactSummary());
            c.setNextContactTime(t.getNextContactTime());
            c.setDescription(t.getDescription());
            c.setCreateTime(t.getCreateTime());
            c.setCreateBy(t.getCreateBy());
            c.setName(customerName);
            //添加到数据库
            int count = customerDao.add(c);
            if (count == 1){
                System.out.println("添加新客户成功");

                t.setCustomerId(c.getId());

                //添加交易表
                int count2  = tranDao.save(t);
                if (count2 == 1){
                    System.out.println("添加新交易成功");

                    //创建交易记录表
                    TranHistory th = new TranHistory();
                    th.setId(UUIDUtil.getUUID());
                    th.setCreateBy(t.getCreateBy());
                    th.setCreateTime(t.getCreateTime());
                    th.setExpectedDate(t.getExpectedDate());
                    th.setMoney(t.getMoney());
                    th.setStage(t.getStage());
                    th.setTranId(t.getId());


                    int count3 = tranHistoryDao.save(th);
                    if (count3 == 1){
                        System.out.println("交易历史添加成功");
                        flag = true;
                    }

                }else {
                    System.out.println("添加交易表失败");
                }
            }
        }else {
            t.setCustomerId(customer.getId());
            System.out.println("客户已存在："+t.getCustomerId());

            //添加交易表
           int count3= tranDao.save(t);
           if (count3 == 1){
               System.out.println("添加新交易成功");

               //创建交易记录表
               TranHistory th = new TranHistory();
               th.setId(UUIDUtil.getUUID());
               th.setCreateBy(t.getCreateBy());
               th.setCreateTime(t.getCreateTime());
               th.setExpectedDate(t.getExpectedDate());
               th.setMoney(t.getMoney());
               th.setStage(t.getStage());
               th.setTranId(t.getId());


               int count4 = tranHistoryDao.save(th);
               if (count4 == 1){
                   System.out.println("交易历史添加成功");
                   flag = true;
               }
           }
        }


        return flag;
    }

    public Map<String,Object> PageList(Map<String, Object> map) {
       ArrayList<Tran> arrayList =  tranDao.PageList(map);
       int total = tranDao.getTotal();

       Map<String,Object> m = new HashMap<String, Object>();
       m.put("total",total);
       m.put("arrayList",arrayList);
       return m;
    }

    public Tran getTranById(String id) {
       Tran  t = tranDao.getTranById(id);
        return t;
    }

    public ArrayList<TranHistory> getHistoryList(HttpServletRequest request, String tranId) {

        TranHistory t  = tranHistoryDao.getHistoryList(tranId);
        ArrayList<TranHistory> arrayList = new ArrayList<TranHistory>();

        //存入可能性
        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        t.setPossible(pMap.get(t.getStage()));

        arrayList.add(t);
        return arrayList;
    }

    public Map<String, Object> getChars() {

        int total = tranDao.getTotal();

        ArrayList<Map<String,Object>> arrayList = tranDao.getChars();

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total",total);
        map.put("arrayList",arrayList);

        return map;
    }


}
