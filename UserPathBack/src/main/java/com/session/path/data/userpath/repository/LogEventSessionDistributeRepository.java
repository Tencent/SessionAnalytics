/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.repository;


import com.session.path.data.userpath.entity.LogEventPVDistributeEntity;
import com.session.path.data.userpath.entity.LogEventSessionDistributeEntity;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * describe: session分布计算
 *
 * @author author
 * @date 2023/02/15
 */
public interface LogEventSessionDistributeRepository extends JpaRepository<LogEventSessionDistributeEntity, Integer> {


    /**
     * 插入数据（session分布计算）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "insert ignore into t_user_log_event_session_distribute (f_type,"
            + "f_is_distinct,f_session_avg,f_session_max,"
            + "f_session_25p,f_session_50p,f_session_75p,"
            + "f_session_status,f_make_version_id,f_user_scene_version_id) "
            + "values(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10)", nativeQuery = true)
    int insertUserLogEventSessionDistribute(String fType, int fIsDistinct, int fSessionAvg, int fSessionMax,
            int fSession25p, int fSession50p, int fSession75p, int fSessionStatus,
            int fMakeVersionId, String fUserSceneVersionId);



    /**
     * 插入数据(分维度session分布计算)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "insert ignore into t_user_log_event_dim_session_distribute (f_type,"
            + "f_is_distinct,f_dim,f_session_num,f_user_num,f_session_avg,"
            + "f_session_max,f_session_min,f_session_50p,"
            + "f_session_status,f_make_version_id,f_user_scene_version_id) "
            + "values(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12)", nativeQuery = true)
    int insertLogEventDimDistribute(
            String fType, int fIsDistinct, String fDim, int fSessionNum, int fUserNum,
            int fSessionAvg, int fSessionMax, int fSessionMin, int fSession50p, int fSessionStatus,
            int fMakeVersionId, String fUserSceneVersionId);


    /**
     * 获取session分布结果
     *
     * @return
     */
    @Transactional
    @Query(value = "select * from t_user_log_event_session_distribute where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and f_type=?3 ", nativeQuery = true)
    List<Map<String, Object>> getLogSessionDistribute(String fUserSceneVersionId,int fMakeVersionId,String fType);


    /**
     * 获取分维度session分布结果
     *
     * @return
     */
    @Transactional
    @Query(value = "select * from t_user_log_event_dim_session_distribute where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and f_type=?3 order by f_session_num desc", nativeQuery = true)
    List<Map<String, Object>> getLogDimSessionDistribute(String fUserSceneVersionId,int fMakeVersionId,String fType);



    /**
     * 获取分维度session分布结果总数
     *
     * @return
     */
    @Transactional
    @Query(value = "select count(*) from t_user_log_event_dim_session_distribute where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and f_type=?3 order by f_session_num desc", nativeQuery = true)
    List<Map<String, Object>> getLogDimSessionDistributeCount(
            String fUserSceneVersionId,int fMakeVersionId,String fType);


}
