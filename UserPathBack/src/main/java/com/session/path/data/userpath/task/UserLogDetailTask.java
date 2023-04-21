/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.task;

import com.session.path.data.userpath.dto.BaseRsp;
import com.session.path.data.userpath.repository.CommonInfoRepository;
import com.session.path.data.userpath.repository.UserLogDetailRepository;
import com.session.path.data.userpath.repository.UserLogUploadStatusRepository;
import com.session.path.data.userpath.util.BizCode;
import com.session.path.data.userpath.util.SpringContextUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName UserLogDetailTask
 * @Description 用户日志明细上传
 * @Author author
 * @Date 2023/02/18 17:58
 * @Version 1.0
 **/
@Component
public class UserLogDetailTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(UserLogDetailTask.class);

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;

    @Autowired
    UserLogDetailRepository userLogDetailRepository;

    @Autowired
    CommonInfoRepository commonInfoRepository;



    private File file;
    private String fUploadName;
    private String fUploadUser;

    public void setMultipartFile(File file) {
        this.file = file;
    }

    public void setUploadName(String fUploadName) {
        this.fUploadName = fUploadName;
    }

    public void setUploadUser(String fUploadUser) {
        this.fUploadUser = fUploadUser;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        BaseRsp baseRsp = new BaseRsp();
        try {

            if (userLogUploadStatusRepository == null) {
                userLogUploadStatusRepository = SpringContextUtil.getBean(UserLogUploadStatusRepository.class);
            }

            if (commonInfoRepository == null) {
                commonInfoRepository = SpringContextUtil.getBean(CommonInfoRepository.class);
            }

            if (userLogDetailRepository == null) {
                userLogDetailRepository = SpringContextUtil.getBean(UserLogDetailRepository.class);
            }

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            userLogUploadStatusRepository.deleteExcelUpload(fUserSceneVersionId);
            userLogUploadStatusRepository.updateUploadStatus(fUploadName, fUploadUser, 2);

            String temp = null;
            int line = 1;
            reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));//解决服务器上乱码
            String initSql = "insert into t_user_log_orig (f_user_id,f_age,f_sex,f_province,f_city,f_channel,"
                    + "f_event,f_event_detail,f_client_time,f_category,f_subcategory) values ";
            reader.readLine();
            while ((temp = reader.readLine()) != null) {
                logger.info("执行上传用户日志temp:{}", temp);
                line++;
                //改为批量1000,上传速度2W条/s;大约是批量100速度的1.6倍,是批量10的16倍,是逐条上传速度的180倍
                if (line % 1000 == 0) {
                    String batchSql = initSql.substring(0, initSql.length() - 1);
                    logger.info("执行上传用户日志第{}行", line);
                    logger.info("执行batchSql:{}", batchSql);
                    logger.info("执行batchSql结果:{}", commonInfoRepository.updateInfo(batchSql));
                    initSql = "insert into t_user_log_orig (f_user_id,f_age,f_sex,f_province,f_city,f_channel,"
                            + "f_event,f_event_detail,f_client_time,f_category,f_subcategory) values ";
                } else {
            initSql +=
        "('" + temp.split(",", 11)[0] + "','" + temp.split(",")[1] + "','" + temp.split(",")[2]
                + "','" + temp.split(",")[3] + "','" + temp.split(",")[4] + "','" + temp
                .split(",")[5]
                + "','" + temp.split(",")[6] + "','" + temp.split(",")[7] + "','" + temp
                .split(",", 11)[8] + "','" + temp.split(",", 11)[9] + "','" + temp
                .split(",", 11)[10] + "'),";
                }
            }
            String batchSql = initSql.substring(0, initSql.length() - 1);
            logger.info("批量执行上传人群的最后部分:{}", batchSql);
            logger.info("执行batchSql结果:{}", commonInfoRepository.updateInfo(batchSql));
            userLogDetailRepository
                    .updateUploadName(fUserSceneVersionId);
            logger.info("名称上传成功");
            userLogUploadStatusRepository.updateUploadStatus(fUploadName, fUploadUser, 1);
            userLogUploadStatusRepository.updateUploadEndTime(fUploadName, fUploadUser);
            logger.info("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            userLogUploadStatusRepository.updateUploadStatus(fUploadName, fUploadUser, 3);
            userLogUploadStatusRepository.updateUploadEndTime(fUploadName, fUploadUser);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("上传操作异常");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    logger.error("上传操作异常:", e);
                    userLogUploadStatusRepository.updateUploadStatus(fUploadName, fUploadUser, 3);
                    userLogUploadStatusRepository.updateUploadEndTime(fUploadName, fUploadUser);
                }
            }
        }
    }
}
