/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.dto;

import lombok.Data;

/**
 * @ClassName ReqFunnelDimEventDto
 * @Description 漏斗图事件分维度Dto
 * @Author author
 * @Date 2023/3/25 22:10
 * @Version 1.0
 **/
@Data
public class ReqFunnelDimEventDto {

    private String fUploadName;
    private String fUploadUser;
    private String fEventFrom1;
    private String fEventFrom2;
    private String fEventFrom3;
    private String fEventFrom4;
    private String fDim;
    private String fEventFromA;
}
