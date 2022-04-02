package com.myCRM.workbench.dao;


import com.myCRM.workbench.domain.ClueActivityRelation;

import java.util.ArrayList;

public interface ClueActivityRelationDao {


    ArrayList<ClueActivityRelation> getActivity(String id);

    int deleteRelation(String relationId);

    int delete_activity_clue(String id);

    int bund(ClueActivityRelation c);
}
