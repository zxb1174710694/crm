package com.myCRM.Settings.Service.impl;

import com.myCRM.Settings.Service.DicService;
import com.myCRM.Settings.dao.DicTypeDao;
import com.myCRM.Settings.dao.DicValueDao;
import com.myCRM.Settings.domain.DicType;
import com.myCRM.Settings.domain.DicValue;
import com.myCRM.util.SqlSessionUtil;

import java.util.*;

public class DicServiceImpl implements DicService {
   private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
   private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

   public Map<String, List<DicValue>> getAll() {
      Map<String,List<DicValue>> map = new HashMap<String, List<DicValue>>();

      List<DicValue> appellationList = dicValueDao.getAppellationList();
      List<DicValue> clueStateList = dicValueDao.getClueStateList();
      List<DicValue> returnPriorityList = dicValueDao.getReturnPriorityList();
      List<DicValue> returnStateList = dicValueDao.getReturnStateList();
      List<DicValue> sourceList = dicValueDao.getSourceList();
      List<DicValue> stageList = dicValueDao.getStageList();
      List<DicValue> transactionTypeList = dicValueDao.getTransactionTypeList();


      map.put("appellationList",appellationList);
      map.put("clueStateList",clueStateList);
      map.put("returnPriorityList",returnPriorityList);
      map.put("returnStateList",returnStateList);
      map.put("sourceList",sourceList);
      map.put("stageList",stageList);
      map.put("transactionTypeList",transactionTypeList);
      return map;
   }
}
