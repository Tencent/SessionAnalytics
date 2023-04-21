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
 * @ClassName LogEventDimPVDistributeEntity
 * @Description pv分布计算(分维度)
 * @Author author
 * @Date 2023/02/11 18:00
 * @Version 1.0
 **/
@Entity
@Table(name = "t_user_log_event_dim_pv_distribute")
@Data
@DynamicUpdate
public class LogEventDimPVDistributeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;    // 自增ID
    private String fType; //'类型'
    private int fIsDistinct; //'f_is_distinct'
    private String fDim; //'维度'
    private int fSessionPvNum; //'f_session_pv_num'
    private int fUserPvNum; //'f_user_pv_num'
    private int fSessionPvAvg; //'f_session_pv_avg'
    private int fSessionPvMax; //'f_session_pv_max'
    private int fSessionPvMin; //'f_session_pv_min'
    private int fSessionPv50p; //'f_session_pv_50p'
    private int fPvStatus; //'f_pv_status'
    private int fMakeVersionId; //'治理版本id'
    private String fUserSceneVersionId; //'f_user_scene_version_id'
    private Date fCreateTime; //'f_create_time'
    private Date fUpdateTime; //'f_update_time'
}
