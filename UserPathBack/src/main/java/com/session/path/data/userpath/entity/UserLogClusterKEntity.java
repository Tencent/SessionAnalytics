/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @ClassName UserLogClusterKEntity
 * @Description 用户日志聚类K值
 * @Author author
 * @Date 2023/02/25 18:00
 * @Version 1.0
 **/
@Entity
@Table(name = "t_user_log_cluster_k")
@Data
@DynamicUpdate
public class UserLogClusterKEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;    // 自增ID
    private String fClusterType; //'聚类类型'
    private String fX; //'x坐标'
    private String fY; //'y坐标'
    private int fIsTurnpoint; //'是否拐点，0否1是'
    private int fClusterStatus; //'f_cluster_status'
    private int fMakeVersionId; //'治理版本id'
    private String fUserSceneVersionId; //'f_user_scene_version_id'
    private Date fCreateTime; //'f_create_time'
    private Date fUpdateTime; //'f_update_time'
}
