package com.myCRM.workbench.dao;

import com.myCRM.workbench.domain.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ActivityDao {

    int insert(Activity a);

    ArrayList<Activity> getList(Map<String, Object> map);

    int getTotal();

    ArrayList<Activity> pageList(Map<String, Object> map);

    int getotal();

    int delete(String[] ids);

    Activity getActivity(String id);

    Boolean update(Activity a);

    Activity detail(String id);

    Activity getRelation(String activityId);

    ArrayList<Activity> getRelationList(Map<String, String> map);

    ArrayList<Activity> SearchText(String searchText);

}
