package com.myCRM.workbench.dao;

import com.myCRM.workbench.domain.TranHistory;

public interface TranHistoryDao {

    int save(TranHistory th);

    TranHistory getHistoryList(String tranId);
}
