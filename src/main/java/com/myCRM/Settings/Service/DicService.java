package com.myCRM.Settings.Service;

import com.myCRM.Settings.domain.DicType;
import com.myCRM.Settings.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {
    Map<String, List<DicValue>> getAll();
}
