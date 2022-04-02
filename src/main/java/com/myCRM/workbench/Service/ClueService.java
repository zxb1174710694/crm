package com.myCRM.workbench.Service;

import com.myCRM.Settings.domain.User;
import com.myCRM.workbench.domain.Activity;
import com.myCRM.workbench.domain.Clue;
import com.myCRM.workbench.domain.Tran;

import java.util.ArrayList;
import java.util.Map;

public interface ClueService {
    ArrayList<User> getUserList();

    Boolean savaClue(Clue clue);

    Map<String,Object> getPageList(Map<String,Object> map);

    Clue detail(String id);

    ArrayList<Activity> getActivity_clue(String id);

    Boolean deleteRelation(String relationId);

    Boolean delete_activity_clue(String id);

    Boolean bund(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);
}
