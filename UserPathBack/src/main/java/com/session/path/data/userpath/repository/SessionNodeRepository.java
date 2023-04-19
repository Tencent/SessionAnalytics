/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.repository;


import com.session.path.data.userpath.entity.LogEventPVDistributeEntity;
import com.session.path.data.userpath.entity.SessionNodeEntity;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * describe: session node
 *
 * @author author
 * @date 2023/03/18
 */
public interface SessionNodeRepository extends JpaRepository<SessionNodeEntity, Integer> {


    /**
     * 插入数据（node）-暂未使用
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "insert ignore into t_session_node (f_category,"
            + "f_event,f_weight_pv,f_make_version_id,f_user_scene_version_id) "
            + "values(?1,?2,?3,?4,?5)", nativeQuery = true)
    int insertSessionNode(String fCategory, String fEvent, int fWeightPv,
            int fMakeVersionId, String fUserSceneVersionId);





    /**
     * 查看node数据-暂未使用
     *
     * @return
     */
    @Transactional
    @Query(value = "select id as '~id', f_category as '~label', f_event as 'name:string',"
            + " f_weight_pv as 'pv:int' from t_session_node where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2", nativeQuery = true)
    List<Map<String, Object>> getSessionNode(String fUserSceneVersionId, int fMakeVersionId);


}
