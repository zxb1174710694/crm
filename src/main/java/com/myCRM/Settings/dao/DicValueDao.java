package com.myCRM.Settings.dao;

import com.myCRM.Settings.domain.DicType;
import com.myCRM.Settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getAppellationList();

    List<DicValue> getClueStateList();

    List<DicValue> getReturnPriorityList();

    List<DicValue> getReturnStateList();

    List<DicValue> getSourceList();

    List<DicValue> getStageList();

    List<DicValue> getTransactionTypeList();
}
