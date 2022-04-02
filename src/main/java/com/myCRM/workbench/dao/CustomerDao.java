package com.myCRM.workbench.dao;

import com.myCRM.workbench.domain.Customer;

import java.util.ArrayList;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int add(Customer customer);

    ArrayList<String> getCustomerName(String name);
}
