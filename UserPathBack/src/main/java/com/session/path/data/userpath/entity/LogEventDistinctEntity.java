/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
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
 * @ClassName LogEventDistinctEntity
 * @Description 原始日志切分之后去重
 * @Author author
 * @Date 2023/03/04 18:00
 * @Version 1.0
 **/
@Entity
@Table(name = "t_session_log_event_distinct")
@Data
@DynamicUpdate
public class LogEventDistinctEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;    // 自增ID
    private String fUserId; //'用户id'
    private String fAge; //'用户年龄'
    private String fSex; //'用户性别'
    private String fProvince; //'用户省份'
    private String fCity; //'用户城市'
    private String fChannel; //'用户来源渠道'
    private int fSessionId; //'session_id'
    private int fSessionRank; //'f_session_rank'
    private String fEvent; //'event页面'
    private String fEventDetail; //'event path'
    private String fClientTime; //'客户端时间'
    private String fCategory; //'大类'
    private String fSubcategory; //'小类'
    private int fMakeVersionId; //'治理版本id'
    private String fUserSceneVersionId; //'用户场景版本id'
    private Date fUpdateTime; //'f_update_time'
}
