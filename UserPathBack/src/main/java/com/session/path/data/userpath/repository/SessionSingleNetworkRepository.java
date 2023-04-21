/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.repository;


import com.session.path.data.userpath.entity.SessionSingleNetworkEntity;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * describe: session single network
 *
 * @author author
 * @date 2023/03/19
 */
public interface SessionSingleNetworkRepository extends JpaRepository<SessionSingleNetworkEntity, Integer> {

    /**
     * 查看数据挖掘中心性网络计算数据
     *
     * @return
     */
    @Transactional
    @Query(value =
            "select id,f_event_name as name,f_event_value as value "
                    + " from t_session_single_networkx "
                    + " where f_user_scene_version_id=?1 "
                    + " and f_make_version_id=?2", nativeQuery = true)
    List<Map<String, Object>> getSessionSingleNetwork(String fUserSceneVersionId, int fMakeVersionId);


}
