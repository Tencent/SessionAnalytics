/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.repository;


import com.session.path.data.userpath.entity.ExcelUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * describe: 上传用户原始日志
 *
 * @author author
 * @date 2021/10/27
 */
public interface ExcelUploadRepository extends JpaRepository<ExcelUploadEntity, Integer> {


    /**
     * 插入excel数据
     *
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "insert ignore into t_user_log_orig (f_user_id,f_age,f_sex,f_province,"
            + "f_city,f_channel,f_event,f_event_detail,f_client_time,f_category,f_subcategory) "
            + "values(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11)", nativeQuery = true)
    int addExcel(String fUserId, String fAge, String fSex, String fProvince, String fCity, String fChannel,
            String fEvent, String fEventDetail, String fClientTime, String fCategory, String fSubcategory);


}
