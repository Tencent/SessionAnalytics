/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.repository;

import com.session.path.data.userpath.entity.RoleInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * describe: 权限管理
 *
 * @author author
 * @date 2023/02/25
 */
public interface RoleInfoRepository extends JpaRepository<RoleInfoEntity, Integer> {

    /**
     * 获取管理员权限
     *
     * @return
     */
    @Transactional
    @Query(value = "select * from t_role_info where f_user=?1", nativeQuery = true)
    RoleInfoEntity getRoleInfo(String fUploadUser);


}
