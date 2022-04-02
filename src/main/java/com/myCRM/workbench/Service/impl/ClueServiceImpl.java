package com.myCRM.workbench.Service.impl;

import com.myCRM.Settings.domain.User;
import com.myCRM.util.DateTimeUtil;
import com.myCRM.util.SqlSessionUtil;
import com.myCRM.util.UUIDUtil;
import com.myCRM.workbench.Service.ClueService;
import com.myCRM.workbench.dao.*;
import com.myCRM.workbench.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    //private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);

    public ArrayList<User> getUserList() {

        ArrayList<User> arrayList = clueDao.getUserList();
        return arrayList;
    }

    public Boolean savaClue(Clue clue) {
        Boolean flag = false;
       int count =  clueDao.savaClue(clue);
       if(count == 1){
           flag = true;
       }
        return flag;
    }

    public Map<String,Object> getPageList(Map<String,Object> map) {

       ArrayList<Clue> arrayList =  clueDao.getPageList(map);

       int count = clueDao.getTotal();

        Map<String, Object> m = new HashMap<String, Object>();
        m.put("arrayList",arrayList);
        m.put("total",count);
        return m;
    }

    public Clue detail(String id) {
       Clue clue =  clueDao.detail(id);
        return clue;
    }

    public ArrayList<Activity> getActivity_clue(String id) {

        //获取关系表数据
        ArrayList<ClueActivityRelation> array = clueActivityRelationDao.getActivity(id);
        System.out.println("数组长度："+array.size());


        ArrayList<Activity> arrayList = new ArrayList<Activity>();
        if (array.size() >0){


            for (ClueActivityRelation c:array){
                String activityId = c.getActivityId();
                Activity activity = activityDao.getRelation(activityId);
                arrayList.add(activity);

                System.out.println("获取的活动信息名："+activity.getName());
            }
            return arrayList;
        }else {
            System.out.println("暂无关联活动");
        }
        return null;




    }


    //解除关联操作
    public Boolean deleteRelation(String relationId) {
        Boolean flag = false;
       int count =  clueActivityRelationDao.deleteRelation(relationId);

       if (count ==1){
           flag = true;
       }
        return flag;
    }

    public Boolean delete_activity_clue(String id) {
        Boolean flag = false;

        int count = clueActivityRelationDao.delete_activity_clue(id);
        if (count == 1){
            flag = true;
        }
        return flag;
    }

    public Boolean bund(String cid, String[] aids) {
        boolean flag = false;

        for (String aid:aids){
           ClueActivityRelation c = new ClueActivityRelation();
           c.setClueId(cid);
           c.setActivityId(aid);
           c.setId(UUIDUtil.getUUID());

            System.out.println("新增关联关系："+c);

           int count = clueActivityRelationDao.bund(c);
           if (count > 0){
               flag = true;
           }

        }

        return flag;
    }


    //线索转换
    public boolean convert(String clueId, Tran t, String createBy) {
        boolean flag = false;
        String createTime = DateTimeUtil.getSysTime();

        Clue c = clueDao.getById(clueId);

        //获取公司信息
        String company = c.getCompany();

        //通过公司名字精准查询数据
       Customer customer =  customerDao.getCustomerByName(company);

       //新客户
       if (customer == null){
           customer = new Customer();
           customer.setId(UUIDUtil.getUUID());
           customer.setAddress(c.getAddress());
           customer.setCreateBy(createBy);
           customer.setCreateTime(createTime);
           customer.setName(company);
           customer.setContactSummary(c.getContactSummary());
           customer.setDescription(c.getDescription());
           customer.setNextContactTime(c.getNextContactTime());
           customer.setOwner(c.getOwner());
           customer.setPhone(c.getPhone());
           customer.setWebsite(c.getWebsite());

           //添加新客户
           int count = customerDao.add(customer);
           if (count != 0){
               flag = true;
               System.out.println("新客户添加成功");
           }

       }else {
           //老客户，说明数据库已经有数据了
       }

       //保存联系人
        Contacts contacts = new Contacts();
       contacts.setId(UUIDUtil.getUUID());
       contacts.setOwner(c.getOwner());
       contacts.setAddress(c.getAddress());
       contacts.setSource(c.getSource());
       contacts.setAppellation(c.getAppellation());
       contacts.setContactSummary(c.getContactSummary());
       contacts.setCreateBy(createBy);
       contacts.setCreateTime(createTime);
       contacts.setCustomerId(customer.getId());
       contacts.setDescription(c.getDescription());
       contacts.setEmail(c.getEmail());
       contacts.setFullname(c.getFullname());
       contacts.setJob(c.getJob());
       contacts.setMphone(c.getMphone());
       contacts.setNextContactTime(c.getNextContactTime());

       int count = contactsDao.sava(contacts);
       if (count == 1){
           flag = true;
           System.out.println("添加联系人成功");
       }

       //删除线索的记录(这里没删备注、活动和线索相关联的记录)
       int count2 =  clueDao.delete(c.getId());
       if (count2 == 1){
           flag = true;
           System.out.println("线索已转换。记录已删除");
       }

        return flag;
    }


}
