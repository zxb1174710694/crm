package com.myCRM.workbench.dao;


import com.myCRM.Settings.domain.User;
import com.myCRM.workbench.domain.Clue;

import java.util.ArrayList;
import java.util.Map;

public interface ClueDao {


    ArrayList<User> getUserList();

    int savaClue(Clue clue);

    ArrayList<Clue> getPageList(Map<String,Object> map);

    int getTotal();

    Clue detail(String id);


    Clue getById(String clueId);

    int delete(String id);
}
