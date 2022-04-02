package com.myCRM.workbench.Service.impl;

import com.myCRM.Settings.dao.UserDao;
import com.myCRM.Settings.domain.User;
import com.myCRM.util.SqlSessionUtil;
import com.myCRM.vo.paginationVo;
import com.myCRM.workbench.Service.ActivityService;
import com.myCRM.workbench.dao.ActivityDao;
import com.myCRM.workbench.dao.ActivityRemarkDao;
import com.myCRM.workbench.domain.Activity;
import com.myCRM.workbench.domain.ActivityRemark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    //dao层
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public Boolean insert(Activity a) {
        Boolean flag = false;
       int count =  activityDao.insert(a);    //返回受影响的条数
        if (count == 1){
            flag = true;
            System.out.println("添加市场活动成功");
        }else {
            System.out.println("添加市场活动失败");
        }
        return flag;
    }

    public paginationVo<Activity> getList(Map<String, Object> map) {

        //获取total和Activity集合

        //获取total
        int total = activityDao.getTotal();

        //获取集合
        ArrayList<Activity> list = activityDao.getList(map);

        paginationVo<Activity> vo = new paginationVo<Activity>();
        vo.setList(list);
        vo.setTotal(total);

        return vo;
    }

    //删除活动
    public Boolean delete(String[] ids) {

        Boolean flag = false;
        //获取应该删除的条数
        int count1 = activityRemarkDao.shouldTotal(ids);
        System.out.println("班级表里有："+count1+"个学生");

        //获取受影响的条数
        int count2 = activityRemarkDao.getTotal(ids);
        System.out.println("删除了"+count2+"个学生");

        //执行删除操作
        if (count1 == count2){
            flag = true;
        }
        int count3 = activityDao.delete(ids);
        for(String s:ids){
            System.out.println(s);
            System.out.println("__________________");
        }
        System.out.println("成功删除的学生："+count3);
        if (count3 == ids.length){
            flag = true;
        }
        return flag;
    }

    //修改操作
    public paginationVo<User> edit(String id) {
        //获取所有用户列表
        List<User> userList = userDao.getUserList();
        //获取对应的activity表数据
        Activity activity = activityDao.getActivity(id);
        System.out.println("查询到的id："+activity.getId());

        paginationVo<User> vo = new paginationVo<User>();
        vo.setList(userList);
        vo.setActivity(activity);
        return vo;
    }

    public Boolean update(Activity a) {
        Boolean flag = activityDao.update(a);
        return flag;
    }

    //详细信息页
    public Activity detail(String id) {
        Activity activity = activityDao.detail(id);
        return activity;
    }

    //备注页
    public ArrayList<ActivityRemark> getRemarkList(String activityId) {
        ArrayList<ActivityRemark> arrayList = activityRemarkDao.getList(activityId);
        return arrayList;
    }

    //删除备注
    public Boolean deleteRemark(String id) {
        Boolean flag = activityRemarkDao.deleteRemark(id);
        return flag;
    }

    //通过id获取activity
    public Activity getActivity(String id) {
        Activity activity = activityDao.getActivity(id);
        return activity;
    }

    //保存新增备注
    public Boolean savaRemark(ActivityRemark a) {
        Boolean flag = false;
        int count  = activityRemarkDao.insertRemark(a);
        if (count==1){
            flag = true;
        }
        return flag;
    }

    //查询线索模态框数据
    public ArrayList<Activity> getRelationList(Map<String, String> map) {
       ArrayList<Activity> arrayList =  activityDao.getRelationList(map);

        return arrayList;
    }


    //交易搜索功能
    public ArrayList<Activity> SearchText(String searchText) {

       ArrayList<Activity> arrayList =  activityDao.SearchText(searchText);
        return arrayList;
    }


    //下拉框
    public List<User> getUserNameList() {
       List<User> list =  userDao.getUserNameList();

        return list;
    }

    //自己复习用到的
    public paginationVo<Activity> PageList(Map<String, Object> map) {
        int total = activityDao.getotal();
        ArrayList<Activity> list = activityDao.pageList(map);
        paginationVo<Activity> vo = new paginationVo();
        vo.setTotal(total);
        vo.setList(list);
        return vo;
    }


}
