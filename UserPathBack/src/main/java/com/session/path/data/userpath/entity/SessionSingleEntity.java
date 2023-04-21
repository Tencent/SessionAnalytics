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
 * @ClassName SessionSingleEntity
 * @Description 数据挖掘single （暂未使用）
 * @Author author
 * @Date 2023/03/18 18:00
 * @Version 1.0
 **/
@Entity
@Table(name = "t_session_single")
@Data
@DynamicUpdate
public class SessionSingleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;    // 自增ID
    private String fEventFrom; //'f_event_from'
    private String fEventTo; //'f_event_to'
    private int fWeightSession; //'f_weight_session'
    private int fMakeVersionId; //'治理版本id'
    private String fUserSceneVersionId; //'f_user_scene_version_id'
    private Date fUpdateTime; //'f_update_time'
}
