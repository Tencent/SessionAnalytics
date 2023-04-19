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
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


/**
 * @ClassName UserLogMakeEntity
 * @Description '用户日志数据治理（原始值与新值）'
 * @Author author
 * @Date 2023/01/14 18:00
 * @Version 1.0
 **/
@Entity
@Table(name = "t_user_log_make")
@Data
@DynamicUpdate
@DynamicInsert
public class UserLogMakeEntity {

    @Id
    private int id;    // 自增ID
    private int fPv;//pv数
    private String fType; // event/category/subcategory
    private String fOld; //old值
    private String fNew;//new值
    private int fMakeStatus;//是否make完成
    private int fMakeVersionId;//治理版本id
    private String fUserSceneVersionId;//用户场景版本id
    private Date fCreateTime;
    private Date fUpdateTime;

}
