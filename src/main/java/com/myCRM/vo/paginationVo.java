package com.myCRM.vo;

import com.myCRM.workbench.domain.Activity;
import com.myCRM.workbench.domain.ActivityRemark;

import java.util.List;

public class paginationVo<T> {
    private int total;
    private List<T> list;
    private Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
