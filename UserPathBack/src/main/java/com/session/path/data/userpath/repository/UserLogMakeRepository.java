/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.repository;

import com.session.path.data.userpath.entity.UserLogMakeEntity;
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
 * @date 2023/01/21
 */
public interface UserLogMakeRepository extends JpaRepository<UserLogMakeEntity, Integer> {

    /**
     * 获取治理后的数据结果
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "select * from t_user_log_make where f_user_scene_version_id=?1 "
            + " and if(IFNULL(?2,'') !='',f_make_version_id=?2,1=1)", nativeQuery = true)
    List<Map<String, Object>> getUserLogMake(String fUserSceneVersionId,int fMakeVersionId);



    /**
     * 获取数据治理的版本id
     *
     * @return
     */
    @Transactional
    @Query(value = "select ifnull(max(f_make_version_id),0) from t_user_log_upload_split_status "
            + " where f_user_scene_version_id=?1", nativeQuery = true)
    int getMaxUserLogMakeVersion(String fUserSceneVersionId);


    /**
     * 插入治理后的数据
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "insert ignore into t_user_log_make (f_pv,f_type,f_old,f_new,f_make_status,"
            + "f_make_version_id,f_user_scene_version_id) "
            + "values(?1,?2,?3,?4,?5,?6,?7)", nativeQuery = true)
    int insertUserLogMake(int fPv, String fType, String fOld, String fNew,
            int fMakeStatus, int fMakeVersionId, String fUserSceneVersionId);



}
