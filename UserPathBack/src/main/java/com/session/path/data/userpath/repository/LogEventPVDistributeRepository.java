/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.repository;


import com.session.path.data.userpath.entity.LogEventPVDistributeEntity;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * describe: pv分布计算
 *
 * @author author
 * @date 2023/02/06
 */
public interface LogEventPVDistributeRepository extends JpaRepository<LogEventPVDistributeEntity, Integer> {


    /**
     * 插入数据（pv分布数据）
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "insert ignore into t_user_log_event_pv_distribute (f_type,"
            + "f_is_distinct,f_session_pv_avg,f_session_pv_max,"
            + "f_session_pv_25p,f_session_pv_50p,f_session_pv_75p,"
            + "f_pv_status,f_make_version_id,f_user_scene_version_id) "
            + "values(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10)", nativeQuery = true)
    int insertUserLogEventPVDistribute(String fType, int fIsDistinct, int fSessionPvAvg, int fSessionPvMax,
            int fSessionPv25p, int fSessionPv50p, int fSessionPv75p, int fPvStatus,
            int fMakeVersionId, String fUserSceneVersionId);



    /**
     * 插入数据(分维度pv分布数据)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "insert ignore into t_user_log_event_dim_pv_distribute (f_type,"
            + "f_is_distinct,f_dim,f_session_pv_num,f_user_pv_num,f_session_pv_avg,"
            + "f_session_pv_max,f_session_pv_min,f_session_pv_50p,"
            + "f_pv_status,f_make_version_id,f_user_scene_version_id) "
            + "values(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12)", nativeQuery = true)
    int insertUserLogEventDimPVDistribute(String fType, int fIsDistinct, String fDim, int fSessionPvNum, int fUserPvNum,
            int fSessionPvAvg, int fSessionPvMax, int fSessionPvMin, int fSessionPv50p, int fPvStatus,
            int fMakeVersionId, String fUserSceneVersionId);


    /**
     * 获取分类型分布(session-pv、user-pv、平均session-pv、最大session-pv、最小session-pv、50分位session-pv)
     *
     * @return
     */
    @Transactional
    @Query(value = "select * from t_user_log_event_pv_distribute where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and f_type=?3 ", nativeQuery = true)
    List<Map<String, Object>> getLogPVDistribute(String fUserSceneVersionId,int fMakeVersionId,String fType);


    /**
     * 获取分类型+维度分布(session-pv、user-pv、平均session-pv、最大session-pv、最小session-pv、50分位session-pv)
     *
     * @return
     */
    @Transactional
    @Query(value = "select * from t_user_log_event_dim_pv_distribute where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and f_type=?3 "
            + " order by f_session_pv_num desc limit ?4 offset ?5", nativeQuery = true)
    List<Map<String, Object>> getLogDimPVDistribute(String fUserSceneVersionId,
            int fMakeVersionId,String fType,Integer pageSize,Integer limit);




    /**
     * 获取分类型维度分布总数
     *
     * @return
     */
    @Transactional
    @Query(value = "select count(*) from t_user_log_event_dim_pv_distribute where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and f_type=?3 ", nativeQuery = true)
    int getLogDimPVDistributeCount(String fUserSceneVersionId,int fMakeVersionId,String fType);

}
