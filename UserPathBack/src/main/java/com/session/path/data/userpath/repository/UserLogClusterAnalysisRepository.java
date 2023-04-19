/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.repository;

import com.session.path.data.userpath.entity.UserLogClusterAnalysisEntity;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * describe:
 *
 * @author author
 * @date 2023/02/25
 */
public interface UserLogClusterAnalysisRepository extends JpaRepository<UserLogClusterAnalysisEntity, Integer> {

    /**
     * 获取用户聚类分析日志
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select * from t_user_log_cluster_analysis "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 and "
            + " f_cluster_status=1 and f_make_version_id=?2 "
            + " and f_cluster_type=?3 and if(IFNULL(?4,'') !='',f_label=?4,1=1)", nativeQuery = true)
    List<Map<String, Object>> getUserLogClusterAnalysis(String fUserSceneVersionId,int fMakeVersionId,
            String fClusterType, String fLabel);


    /**
     * 获取用户聚类占比
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_label,count(distinct f_user_id) as user_num from t_user_log_cluster_analysis "
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + "and f_cluster_status=1 and f_cluster_type=?3 group by f_label",
            nativeQuery = true)
    List<Map<String, Object>> getUserLogClusterDistribute(String fUserSceneVersionId,int fMakeVersionId,
            String fClusterType);


    /**
     * 获取用户聚类详情统计
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_label,count(distinct f_user_id) as user_num,max(pv) as max_pv,"
            + "min(pv) as min_pv,round(avg(pv),2) as avg_pv from \n"
            + "(\n"
            + "select f_label,t1.f_user_id,pv from \n"
            + "(select f_user_id,count(*) as pv from t_session_log_event "
            + "where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + "group by f_user_id)t1 \n"
            + "join\n"
            + "(select distinct f_label,f_user_id from t_user_log_cluster_analysis where f_user_scene_version_id=?1 "
            + " and f_make_version_id=?2 and if(IFNULL(?3,'') !='',f_label=?3,1=1)"
            + "and f_cluster_type='kmeans' and f_user_id is not null and f_user_id!='')t2 \n"
            + "on t1.f_user_id=t2.f_user_id)t\n"
            + "group by f_label", nativeQuery = true)
    List<Map<String, Object>> getUserLogClusterDetail(
            String fUserSceneVersionId,int fMakeVersionId,String fLabel);


    /**
     * 获取用户dtw聚类示例数据
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select distinct a.f_user_id,f_age,f_sex,f_province,f_city,f_channel from "
                + "t_user_log_cluster_analysis a join t_session_log_event b on "
                + "a.f_user_scene_version_id=b.f_user_scene_version_id and a.f_user_id=b.f_user_id "
                + " where a.f_user_id!='' and b.f_user_scene_version_id=?1 and b.f_make_version_id=?2 "
                + " and f_cluster_type=?3 and "
                + "f_cluster_status=1 and f_label=?4  order by f_age limit 10", nativeQuery = true)
    List<Map<String, Object>> getUserLogClusterDetailSample(String fUserSceneVersionId,int fMakeVersionId,
            String fClusterType, String fLabel);


    /**
     * 获取用户kmeans聚类示例数据(场景pv)
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_category,count(*) as pv from "
            + " t_user_log_cluster_analysis a join t_session_log_event b on "
            + " a.f_user_scene_version_id=b.f_user_scene_version_id "
            + " and a.f_user_id=b.f_user_id and a.f_make_version_id=b.f_make_version_id"
            + " where a.f_user_id!='' and b.f_user_scene_version_id=?1 and b.f_make_version_id=?2 "
            + " and f_cluster_type=?3 and "
            + " f_cluster_status=1 and f_label=?4  group by f_category limit 10", nativeQuery = true)
    List<Map<String, Object>> getUserLogClusterDetailCategory(String fUserSceneVersionId,int fMakeVersionId,
            String fClusterType, String fLabel);

}
