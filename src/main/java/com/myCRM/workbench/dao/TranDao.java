package com.myCRM.workbench.dao;

import com.myCRM.workbench.domain.Tran;

import java.util.ArrayList;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    ArrayList<Tran> PageList(Map<String, Object> map);

    int getTotal();

    Tran getTranById(String id);

    ArrayList<Tran> getHistoryList(String tranId);

    ArrayList<Map<String, Object>> getChars();
}
