/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.repository;

import com.session.path.data.userpath.entity.UserLogClusterKEntity;
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
public interface UserLogClusterKRepository extends JpaRepository<UserLogClusterKEntity, Integer> {

    /**
     * 获取用户聚类分析K值趋势
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select * from t_user_log_cluster_k "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 and "
            + "f_cluster_status=1 and f_cluster_type=?3", nativeQuery = true)
    List<Map<String, Object>> getUserLogClusterK(String fUserSceneVersionId, int fMakeVersionId, String fClusterType);


}
