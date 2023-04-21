/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @ClassName RecentAcessRecordEntity
 * @Description 最近访问记录
 * @Author author
 * @Date 2023/03/04 18:00
 * @Version 1.0
 **/
@Entity
@Table(name = "t_recent_access_records")
@Data
@DynamicUpdate
public class RecentAcessRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 自增ID
    private String fAnalysisNameFirst; //'桑基图分析/漏斗图分析'
    private String fAnalysisNameSecond; //'大类分析/小类分析'
    private String fUploadUser; //'上传人'
    private String fAdminUser; //'管理员'
    private String fUploadName; //'上传场景标题'
    private String fSceneId; //'场景id'
    private int fVersionId; //'版本id'
    private int fMakeVersionId; //'治理版本id'
    private String fUserSceneVersionId; //'用户场景版本id'
    private String fAccessTime; //'访问时间'
}