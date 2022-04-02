package com.myCRM.workbench.Service;

import com.myCRM.workbench.domain.Tran;
import com.myCRM.workbench.domain.TranHistory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

public interface TranService {

    boolean save(Tran t, String customerName);

    Map<String,Object> PageList(Map<String, Object> map);

    Tran getTranById(String id);


    ArrayList<TranHistory> getHistoryList(HttpServletRequest request, String tranId);

    Map<String, Object> getChars();
}
