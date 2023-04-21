/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.repository;

import com.session.path.data.userpath.entity.RecentAcessRecordEntity;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface RecentAcessRecordRepository extends JpaRepository<RecentAcessRecordEntity, Integer> {

    /**
     * 获取最近访问记录
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select a.f_analysis_name_first,a.f_analysis_name_second,a.f_upload_user,"
            + " a.f_admin_user,a.f_upload_name,a.f_user_scene_version_id,"
            + " a.f_scene_id,a.f_version_id,a.f_make_version_id,a.f_access_time from\n"
            + "(select id,f_analysis_name_first,f_analysis_name_second,f_upload_user,f_admin_user,"
            + " f_upload_name,f_user_scene_version_id,f_scene_id,f_version_id,f_make_version_id,f_access_time  "
            + " from t_recent_access_records "
            + " where if(IFNULL(?1,'') !='',f_admin_user=?1,1=1))a\n"
            + " join\n"
            + "(select f_analysis_name_first,f_analysis_name_second,f_upload_user,f_admin_user,"
            + " f_upload_name,f_user_scene_version_id,f_scene_id,f_version_id,f_make_version_id,"
            + " max(id) as id from t_recent_access_records "
            + " where if(IFNULL(?1,'') !='',f_admin_user=?1,1=1) "
            + " group by f_analysis_name_first,f_analysis_name_second,f_upload_user,f_admin_user,"
            + " f_upload_name,f_user_scene_version_id,f_scene_id,f_version_id,f_make_version_id "
            + " order by id desc limit 5)b\n"
            + "on a.id=b.id", nativeQuery = true)
    List<Map<String, Object>> getRecentAcessRecord(String fAdminUser);


    /**
     * 获取注册用户数
     *
     * @return
     */
    @Transactional
    @Query(value = "select count(distinct username) as uv  "
            + " from t_user "
            + " where if(IFNULL(?1,'') !='',substring(f_create_time,1,10)>=?1,1=1) "
            + " and if(IFNULL(?2,'') !='',substring(f_create_time,1,10)<=?2,1=1)", nativeQuery = true)
    List<Map<String, Object>> getRegisterUserNum(String fStartTime, String fEndTime);


    /**
     * 获取全部访问记录
     *
     * @return
     */
    @Transactional
    @Query(value = "select *  "
            + " from t_recent_access_records "
            + " where if(IFNULL(?1,'') !='',substring(f_access_time,1,10)>=?1,1=1) "
            + " and if(IFNULL(?2,'') !='',substring(f_access_time,1,10)<=?2,1=1)"
            + " and if(IFNULL(?3,'') !='',f_admin_user=?3,1=1)"
            + " order by id desc", nativeQuery = true)
    Page<RecentAcessRecordEntity> getRecentAcessRecordList(String fStartTime, String fEndTime,String fAdminUser,
            Pageable pageable);



    /**
     * 获取访问记录UV
     *
     * @return
     */
    @Transactional
    @Query(value = "select count(distinct f_admin_user) as uv  "
            + " from t_recent_access_records "
            + " where if(IFNULL(?1,'') !='',substring(f_access_time,1,10)>=?1,1=1) "
            + " and if(IFNULL(?2,'') !='',substring(f_access_time,1,10)<=?2,1=1)",nativeQuery = true)
    List<Map<String, Object>> getRecentAcessUV(String fStartTime, String fEndTime);



    /**
     * 获取访问记录PV
     *
     * @return
     */
    @Transactional
    @Query(value = "select count(1) as pv  "
            + " from t_recent_access_records "
            + " where if(IFNULL(?1,'') !='',substring(f_access_time,1,10)>=?1,1=1) "
            + " and if(IFNULL(?2,'') !='',substring(f_access_time,1,10)<=?2,1=1)", nativeQuery = true)
    List<Map<String, Object>> getRecentAcessPV(String fStartTime, String fEndTime);


    /**
     * 获取高频访问用户（top10）
     *
     * @return
     */
    @Transactional
    @Query(value = "select f_admin_user,count(1) as pv  "
            + " from t_recent_access_records "
            + " where if(IFNULL(?1,'') !='',substring(f_access_time,1,10)>=?1,1=1) "
            + " and if(IFNULL(?2,'') !='',substring(f_access_time,1,10)<=?2,1=1)"
            + " group by f_admin_user "
            + " order by pv desc limit 10", nativeQuery = true)
    List<Map<String, Object>> getRecentAcessFrequency(String fStartTime, String fEndTime);


}
