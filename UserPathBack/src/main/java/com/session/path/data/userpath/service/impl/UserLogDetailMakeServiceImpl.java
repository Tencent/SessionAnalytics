/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.service.impl;

import com.session.path.data.userpath.repository.CommonInfoRepository;
import com.session.path.data.userpath.repository.UserLogUploadStatusRepository;
import com.session.path.data.userpath.service.UserLogDetailMakeService;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserLogDetailMakeServiceImpl
 * @Description 原始日志治理结果存储
 * @Author author
 * @Date 2023/02/04 11:56
 * @Version 1.0
 **/
@Service
public class UserLogDetailMakeServiceImpl implements UserLogDetailMakeService {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserLogDetailMakeServiceImpl.class);

    @Autowired
    CommonInfoRepository commonInfoRepository;

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;

    @Override
    public void saveBatchLogMakeEntity(List<Map<String, Object>> logMake, int fMakeVersionId) {
        try {
            LOGGER.info("执行上传用户日志");
            String initSql = "insert into t_user_log_orig_make ("
                    + "f_user_id,f_age,f_sex,f_province,f_city,f_channel,"
                    + "f_event,f_event_detail,f_client_time,f_category,f_subcategory,f_make_version_id,"
                    + "f_user_scene_version_id) values ";
            for (int i = 1; i <= logMake.size(); i++) {
                //改为批量1000,上传速度2W条/s;大约是批量100速度的1.6倍,是批量10的16倍,是逐条上传速度的180倍
                if (i % 1000 == 0) {
                    String batchSql = initSql.substring(0, initSql.length() - 1);
                    LOGGER.info("执行上传用户日志第{}行", i);
                    LOGGER.info("执行batchSql:{}", batchSql);
                    LOGGER.info("执行batchSql结果:{}", commonInfoRepository.updateInfo(batchSql));
                    initSql = "insert into t_user_log_orig_make ("
                            + "f_user_id,f_age,f_sex,f_province,f_city,f_channel,"
                            + "f_event,f_event_detail,f_client_time,f_category,f_subcategory,f_make_version_id,"
                            + "f_user_scene_version_id) values ";
                } else {
                    initSql +=
                            "('" + logMake.get(i - 1).get("f_user_id") + "','"
                                    + logMake.get(i - 1).get("f_age") + "','"
                                    + logMake.get(i - 1).get("f_sex") + "','"
                                    + logMake.get(i - 1).get("f_province") + "','"
                                    + logMake.get(i - 1).get("f_city") + "','"
                                    + logMake.get(i - 1).get("f_channel") + "','"
                                    + logMake.get(i - 1).get("f_event") + "','"
                                    + logMake.get(i - 1).get("f_event_detail") + "','"
                                    + logMake.get(i - 1).get("f_client_time") + "','"
                                    + logMake.get(i - 1).get("f_category") + "','"
                                    + logMake.get(i - 1).get("f_subcategory") + "','"
                                    + fMakeVersionId + "','"
                                    + logMake.get(i - 1).get("f_user_scene_version_id") + "'),";
                }
            }
            String batchSql = initSql.substring(0, initSql.length() - 1);
            LOGGER.info("批量执行上传人群的最后部分:{}", batchSql);
            LOGGER.info("执行batchSql结果:{}", commonInfoRepository.updateInfo(batchSql));
            LOGGER.info("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            userLogUploadStatusRepository.updateMakeStatus(
                    logMake.get(0).get("f_user_scene_version_id").toString(), fMakeVersionId,3);
            LOGGER.info("上传操作异常");
        }
    }

}
