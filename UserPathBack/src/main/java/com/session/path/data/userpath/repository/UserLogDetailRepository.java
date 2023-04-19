/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.repository;

import com.session.path.data.userpath.entity.UserLogDetailEntity;
import com.session.path.data.userpath.entity.UserLogUploadStatusEntity;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * describe: 用户上传日志
 *
 * @author author
 * @date 2023/02/18
 */
public interface UserLogDetailRepository extends JpaRepository<UserLogDetailEntity, Integer> {

    /**
     * 添加更新上传excel的f_user_scene_version_id字段
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "update t_user_log_orig set f_user_scene_version_id=?1 "
            + " where f_user_scene_version_id is null", nativeQuery = true)
    void updateUploadName(String fUserSceneVersionId);


    /**
     * 预览上传日志数据10条
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select * from t_user_log_orig "
            + " where f_user_scene_version_id=?1 limit 10", nativeQuery = true)
    List<Map<String, Object>> getUserLogLimit(String fUserSceneVersionId);


    /**
     * 预览治理后的数据10条
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select * from t_user_log_orig_make "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 limit 10", nativeQuery = true)
    List<Map<String, Object>> getUserLogMakeLimit(String fUserSceneVersionId, int fMakeVersionId);


    /**
     * 统计上传日志页面事件维度数据pv分布
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_event,count(*) as pv from t_user_log_orig "
            + " where f_user_scene_version_id=?1"
            + " group by f_event order by pv desc", nativeQuery = true)
    List<Map<String, Object>> getUserLogEventTop(String fUserSceneVersionId);


    /**
     * 统计上传日志小类维度数据pv分布
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_subcategory,count(*) as pv from t_user_log_orig "
            + " where f_user_scene_version_id=?1 "
            + " group by f_subcategory order by pv desc", nativeQuery = true)
    List<Map<String, Object>> getUserLogSubcategoryTop(String fUserSceneVersionId);


    /**
     * 统计上传日志大类维度数据pv分布
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select f_category,count(*) as pv from t_user_log_orig "
            + " where f_user_scene_version_id=?1 "
            + " group by f_category order by pv desc", nativeQuery = true)
    List<Map<String, Object>> getUserLogCategoryTop(String fUserSceneVersionId);


    /**
     * 查看上传的日志数据
     *
     * @return
     */
    @Transactional
    @Query(value = "select * from t_user_log_orig "
            + "where f_user_scene_version_id=?1", nativeQuery = true)
    List<Map<String, Object>> getUserLogDetail(String fUserSceneVersionId);


}
