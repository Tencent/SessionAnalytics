/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.service;

import java.io.File;

/**
 * @ClassName UserLogDetailService
 * @Description 解析用户上传的日志
 * @Author author
 * @Date 2023/02/18 11:56
 * @Version 1.0
 **/
public interface UserLogDetailService {

    /**
     * 解析用户上传的日志
     *
     * @param file
     * @param fUploadName
     * @param fUploadUser
     * @param file
     * @return
     * @throws Exception
     */
    public void excelImportAll(File file, String fUploadName, String fUploadUser)
            throws Exception;

}
