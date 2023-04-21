/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.service.impl;

import com.session.path.data.userpath.repository.UserLogUploadStatusRepository;
import com.session.path.data.userpath.service.UserLogDetailService;
import com.session.path.data.userpath.task.UserLogDetailTask;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName LogEventSessionServiceImpl
 * @Description 原始日志切分session
 * @Author author
 * @Date 2023/02/18 11:56
 * @Version 1.0
 **/
@Service
public class UserLogDetailServiceImpl implements UserLogDetailService {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserLogDetailServiceImpl.class);


    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;


    @Override
    public void excelImportAll(File file, String fUploadName, String fUploadUser)
            throws Exception {
        try {
            //异步提交上传任务
            UserLogDetailTask uploadTask = new UserLogDetailTask();
            uploadTask.setMultipartFile(file);
            uploadTask.setUploadName(fUploadName);
            uploadTask.setUploadUser(fUploadUser);
            ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
            LOGGER.info("异步提交日志上传任务:{}", fUploadName);
            cachedThreadPool.execute(uploadTask);
            LOGGER.info("异步提交日志上传任务成功:{}", fUploadName);
        } catch (Exception e) {
            userLogUploadStatusRepository.updateUploadStatus(fUploadName, fUploadUser, 3);
            userLogUploadStatusRepository.updateUploadEndTime(fUploadName, fUploadUser);
            throw new Exception("解析用户上传日志文件失败", e);
        }
    }

}
