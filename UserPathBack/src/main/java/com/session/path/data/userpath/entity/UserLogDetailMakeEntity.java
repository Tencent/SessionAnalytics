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
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


/**
 * @ClassName UserLogDetailMakeEntity
 * @Description '用户原始上传日志治理结果'
 * @Author author
 * @Date 2023/02/05 18:00
 * @Version 1.0
 **/
@Entity
@Table(name = "t_user_log_orig_make")
@Data
@DynamicUpdate
@DynamicInsert
public class UserLogDetailMakeEntity {

    @Id
    private int id;    // 自增ID
    private String fUserId; // 用户ID
    private String fAge; //年龄
    private String fSex; //性别
    private String fProvince;
    private String fCity;
    private String fChannel;
    private String fEvent;
    private String fEventDetail;
    private String fClientTime;
    private String fCategory;
    private String fSubcategory;
    private int fMakeVersionId;
    private String fUserSceneVersionId;
    private Date fUpdateTime;

}
