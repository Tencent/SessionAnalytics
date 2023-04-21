/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName UserLogDetailMakeService
 * @Description 用户上传的日志治理结果存储
 * @Author author
 * @Date 2023/02/18 14:56
 * @Version 1.0
 **/
public interface UserLogDetailMakeService {

    /**
     * 保存数据治理结果
     */
    public void saveBatchLogMakeEntity(List<Map<String, Object>> list, int fMakeVersionId);

}
