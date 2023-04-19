/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
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
 * @ClassName RoleInfoEntity
 * @Description 权限管理表
 * @Author author
 * @Date 2023/03/04 18:00
 * @Version 1.0
 **/
@Entity
@Table(name = "t_role_info")
@Data
@DynamicUpdate
public class RoleInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 自增ID
    private String fUser; //'用户'
    private int fAdmin; //'是否管理员'
    private String fBusiness; //'业务'
    private String fVersion; //'ALL/中英文'
    private String fMod; //'ALL/模块'


}
