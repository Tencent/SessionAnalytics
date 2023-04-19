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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @ClassName UserLogUploadStatusEntity
 * @Description excel上传切分状态
 * @Author author
 * @Date 2023/02/25 18:00
 * @Version 1.0
 **/
@Entity
@Data
@DynamicUpdate
@DynamicInsert
@Table(name = "t_user_log_upload_split_status")
public class UserLogUploadStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;    // 自增ID
    private String fUploadUser; //上传人
    private String fUploadName; //'上传场景标题'
    private String fSceneDesc; //'场景描述'
    private String fSceneId; //'场景id'
    private int fVersionId; //'版本id'
    private int fMakeVersionId; //'治理版本id'
    private String fSessionEvent; //'切分session的事件'
    private int fSessionSplitTime; //'切分session间隔时间分钟'
    private int fUploadStatus; //'是否上传完成'
    private Date fUploadCreateTime; //上传创建时间
    private Date fUploadUpdateTime; //上传结束时间
    private int fMakeStatus; //'大类是否数据治理完成' 0 未处理 1 完成  2 进行中  3 失败
    private Date fMakeCreateTime; //数据治理创建时间
    private Date fMakeUpdateTime; //数据治理结束时间
    private int fSessionSplitStatus; //'是否seesion切分完成'
    private String fUserSceneVersionId; //'用户场景sessionid'
    private Date fCreateTime; //切分创建时间
    private Date fUpdateTime; //切分更新时间


}
