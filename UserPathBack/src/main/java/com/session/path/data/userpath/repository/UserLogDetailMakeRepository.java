/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.repository;

import com.session.path.data.userpath.entity.UserLogDetailMakeEntity;
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
public interface UserLogDetailMakeRepository extends JpaRepository<UserLogDetailMakeEntity, Integer> {

//    /**
//     * 获取数据治理过滤结果
//     *
//     * @return
//     */
//    @Modifying
//    @Transactional
//    @Query(value = "select * from t_user_log_orig_make where "
//            + " f_user_scene_version_id=?1 and f_make_version_id=?2", nativeQuery = true)
//    List<Map<String, Object>> getUserLogDetailMake(String fUserSceneVersionId,int fMakeVersionId);


    /**
     * 更新上传日志页面事件维度数据
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "update t_user_log_orig_make set f_event=?4 "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " and f_event=?3", nativeQuery = true)
    void updateUserLogEventTop(String fUserSceneVersionId, int fMakeVersionId,
            String fEventOld,String fEventNew);


    /**
     * 更新上传日志小类维度数据
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "update t_user_log_orig_make set f_subcategory=?4 "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " and f_subcategory=?3", nativeQuery = true)
    void updateUserLogSubcategoryTop(String fUserSceneVersionId, int fMakeVersionId,
            String fSubcategoryOld,String fSubcategoryNew);


    /**
     * 更新上传日志大类维度数据
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "update t_user_log_orig_make set f_category=?4 "
            + " where f_user_scene_version_id=?1 and f_make_version_id=?2 "
            + " and f_category=?3", nativeQuery = true)
    void updateUserLogCategoryTop(String fUserSceneVersionId,int fMakeVersionId,
            String fCategoryOld,String fCategoryNew);


}
