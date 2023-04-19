/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.service.impl;

import com.session.path.data.userpath.repository.UserLogUploadStatusRepository;
import com.session.path.data.userpath.service.LogEventSessionService;
import com.session.path.data.userpath.task.ExcuteSessionSplitTask;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName LogEventSessionServiceImpl
 * @Description 原始日志切分session
 * @Author author
 * @Date 2023/02/18 11:56
 * @Version 1.0
 **/
@Service
public class LogEventSessionServiceImpl implements LogEventSessionService {

    public static final Logger LOGGER = LoggerFactory.getLogger(LogEventSessionServiceImpl.class);

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;


    @Override
    public void executeSessionSplit(String fUserSceneVersionId, int fMakeVersionId) {
        try {
            //异步提交上传任务
            ExcuteSessionSplitTask splitTask = new ExcuteSessionSplitTask();
            splitTask.setUserSceneVersionId(fUserSceneVersionId);
            splitTask.setMakeVersionId(fMakeVersionId);
            LOGGER.info("异步提交session切分任务:{}", fUserSceneVersionId);
            ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
            cachedThreadPool.execute(splitTask);
            LOGGER.info("异步提交session切分任务成功:{}", fUserSceneVersionId);
        } catch (Exception e) {
            userLogUploadStatusRepository.updateSplitStatus(fUserSceneVersionId, fMakeVersionId,3);
            LOGGER.error("执行切分调度任务失败", e);
        }
    }



}
