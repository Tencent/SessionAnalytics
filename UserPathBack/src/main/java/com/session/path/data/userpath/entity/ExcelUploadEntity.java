/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

/**
 * @ClassName ExcelUploadEntity
 * @Description 用户原始上传日志
 * @Author author
 * @Date 2023/03/04 16:27
 * @Version 1.0
 **/

@Data
@Entity
public class ExcelUploadEntity extends BaseRowModel {

    @Id
    @ExcelProperty(value = "f_user_id", index = 0)
    private String fUserId;
    @ExcelProperty(value = "f_age", index = 1)
    private String fAge;
    @ExcelProperty(value = "f_sex", index = 2)
    private String fSex;
    @ExcelProperty(value = "f_province", index = 3)
    private String fProvince;
    @ExcelProperty(value = "f_city", index = 4)
    private String fCity;
    @ExcelProperty(value = "f_channel", index = 5)
    private String fChannel;
    @ExcelProperty(value = "f_event", index = 6)
    private String fEvent;
    @ExcelProperty(value = "f_event_detail", index = 7)
    private String fEventDetail;
    @ExcelProperty(value = "f_client_time", index = 8)
    private String fClientTime;
    @ExcelProperty(value = "f_category", index = 9)
    private String fCategory;
    @ExcelProperty(value = "f_subcategory", index = 10)
    private String fSubcategory;

}