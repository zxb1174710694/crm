package com.myCRM.workbench.Service.impl;

import com.myCRM.util.SqlSessionUtil;
import com.myCRM.workbench.Service.CustomerService;
import com.myCRM.workbench.dao.CustomerDao;
import com.myCRM.workbench.domain.Customer;

import java.util.ArrayList;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    public ArrayList<String> getCustomerName(String name) {
       ArrayList<String> arrayList =  customerDao.getCustomerName(name);
        return arrayList;
    }




















}
