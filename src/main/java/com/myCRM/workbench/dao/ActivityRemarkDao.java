package com.myCRM.workbench.dao;

import com.myCRM.workbench.domain.ActivityRemark;

import java.util.ArrayList;
import java.util.Map;

public interface ActivityRemarkDao {
    int shouldTotal(String[] ids);

    int getTotal(String[] ids);

    ArrayList<ActivityRemark> getList(String id);

    Boolean deleteRemark(String id);

    int savaRemark(ActivityRemark a);

    int insertRemark(ActivityRemark a);
}
