/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName ReqSankeyDimCategoryDto
 * @Description 桑基图大类分维度 Dto
 * @Author author
 * @Date 2023/3/25 22:10
 * @Version 1.0
 **/
@Data
public class ReqSankeyDimCategoryDto {

    private String fUploadName;
    private String fUploadUser;
    private String fCategoryFrom;
    private String fCategory;
    private String fDim;
    private String fClick;
    private String fCategoryFromA;
    private String fCategoryToA;

}
