package com.myCRM.workbench.Service;

import com.myCRM.Settings.domain.User;
import com.myCRM.vo.paginationVo;
import com.myCRM.workbench.domain.Activity;
import com.myCRM.workbench.domain.ActivityRemark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ActivityService {

    Boolean insert(Activity a);

    paginationVo<Activity> getList(Map<String, Object> map);

    paginationVo<Activity> PageList(Map<String, Object> map);

    Boolean delete(String[] ids);

    paginationVo<User> edit(String id);

    Boolean update(Activity a);

    Activity detail(String id);

    ArrayList<ActivityRemark> getRemarkList(String id);

    Boolean deleteRemark(String id);

    Activity getActivity(String id);

    Boolean savaRemark(ActivityRemark a);

    ArrayList<Activity> getRelationList(Map<String, String> map);

    ArrayList<Activity> SearchText(String searchText);

    List<User> getUserNameList();
}
