/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.task;

import static com.session.path.data.userpath.config.PythonConfig.readProcessOutput;

import com.session.path.data.userpath.entity.UserLogUploadStatusEntity;
import com.session.path.data.userpath.repository.CommonInfoRepository;
import com.session.path.data.userpath.repository.LogEventAgeRepository;
import com.session.path.data.userpath.repository.LogEventChannelRepository;
import com.session.path.data.userpath.repository.LogEventDistinctRepository;
import com.session.path.data.userpath.repository.LogEventPVDistributeRepository;
import com.session.path.data.userpath.repository.LogEventProvinceRepository;
import com.session.path.data.userpath.repository.LogEventRepository;
import com.session.path.data.userpath.repository.LogEventSessionDistributeRepository;
import com.session.path.data.userpath.repository.LogEventSexRepository;
import com.session.path.data.userpath.repository.SessionDistinctEventRepository;
import com.session.path.data.userpath.repository.SessionEventRepository;
import com.session.path.data.userpath.repository.SessionNodeRepository;
import com.session.path.data.userpath.repository.SessionSingleRepository;
import com.session.path.data.userpath.repository.UserLogUploadStatusRepository;
import com.session.path.data.userpath.util.SpringContextUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName ExcuteSessionSplitTask
 * @Description 执行用户日志切分任务
 * @Author author
 * @Date 2023/02/18 17:58
 * @Version 1.0
 **/
@Component
public class ExcuteSessionSplitTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ExcuteSessionSplitTask.class);

    @Autowired
    LogEventRepository logEventRepository;

    @Autowired
    LogEventDistinctRepository logEventDistinctRepository;

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;

    @Autowired
    SessionDistinctEventRepository sessionDistinctEventRepository;

    @Autowired
    SessionEventRepository sessionEventRepository;

    @Autowired
    CommonInfoRepository commonInfoRepository;

    @Autowired
    LogEventPVDistributeRepository logEventPVDistributeRepository;

    @Autowired
    LogEventSessionDistributeRepository logEventSessionDistributeRepository;

    @Autowired
    LogEventChannelRepository logEventChannelRepository;

    @Autowired
    LogEventSexRepository logEventSexRepository;

    @Autowired
    LogEventAgeRepository logEventAgeRepository;

    @Autowired
    LogEventProvinceRepository logEventProvinceRepository;

    @Autowired
    SessionSingleRepository sessionSingleRepository;


    private String fUserSceneVersionId;

    private int fMakeVersionId;

    public void setUserSceneVersionId(String fUserSceneVersionId) {
        this.fUserSceneVersionId = fUserSceneVersionId;
    }

    public void setMakeVersionId(int fMakeVersionId) {
        this.fMakeVersionId = fMakeVersionId;
    }


    @Override
    public void run() {
        List<Map<String, Object>> logEventEntity = new ArrayList<>();
        List<Map<String, Object>> logEventDistinctEntity = new ArrayList<>();

        List<Map<String, Object>> logEventPvDistribute = new ArrayList<>();
        List<Map<String, Object>> logEventAgePvDistribute = new ArrayList<>();
        List<Map<String, Object>> logEventSexPvDistribute = new ArrayList<>();
        List<Map<String, Object>> logEventChannelPvDistribute = new ArrayList<>();
        List<Map<String, Object>> logEventProvincePvDistribute = new ArrayList<>();

        List<Map<String, Object>> sessionEventEntity = new ArrayList<>();
        List<Map<String, Object>> sessionEventDistinctEntity = new ArrayList<>();

        if (userLogUploadStatusRepository == null) {
            userLogUploadStatusRepository = SpringContextUtil.getBean(UserLogUploadStatusRepository.class);
        }

        if (logEventRepository == null) {
            logEventRepository = SpringContextUtil.getBean(LogEventRepository.class);
        }

        if (sessionEventRepository == null) {
            sessionEventRepository = SpringContextUtil.getBean(SessionEventRepository.class);
        }

        if (logEventDistinctRepository == null) {
            logEventDistinctRepository = SpringContextUtil.getBean(LogEventDistinctRepository.class);
        }

        if (sessionDistinctEventRepository == null) {
            sessionDistinctEventRepository = SpringContextUtil.getBean(SessionDistinctEventRepository.class);
        }

        if (commonInfoRepository == null) {
            commonInfoRepository = SpringContextUtil.getBean(CommonInfoRepository.class);
        }

        if (logEventPVDistributeRepository == null) {
            logEventPVDistributeRepository = SpringContextUtil.getBean(LogEventPVDistributeRepository.class);
        }

        if (logEventSessionDistributeRepository == null) {
            logEventSessionDistributeRepository = SpringContextUtil.getBean(LogEventSessionDistributeRepository.class);
        }

        if (logEventChannelRepository == null) {
            logEventChannelRepository = SpringContextUtil.getBean(LogEventChannelRepository.class);
        }


        if (logEventSexRepository == null) {
            logEventSexRepository = SpringContextUtil.getBean(LogEventSexRepository.class);
        }


        if (logEventAgeRepository == null) {
            logEventAgeRepository = SpringContextUtil.getBean(LogEventAgeRepository.class);
        }

        if (logEventProvinceRepository == null) {
            logEventProvinceRepository = SpringContextUtil.getBean(LogEventProvinceRepository.class);
        }

        if (sessionSingleRepository == null) {
            sessionSingleRepository = SpringContextUtil.getBean(SessionSingleRepository.class);
        }

        UserLogUploadStatusEntity taskEntity = userLogUploadStatusRepository.
                getSessionSplit(fUserSceneVersionId,fMakeVersionId);
        try {
            taskEntity.setFSessionSplitStatus(2);
            taskEntity.setFCreateTime(new Date());
            taskEntity.setFUpdateTime(new Date());
            userLogUploadStatusRepository.saveAndFlush(taskEntity);
            String fSessionEvent = taskEntity.getFSessionEvent();
            int fSessionSplitTime = taskEntity.getFSessionSplitTime();

            if (fMakeVersionId == 0) {
                logEventEntity = logEventRepository
                        .getSessionOrig(fUserSceneVersionId, fSessionSplitTime, fSessionEvent);
            } else {
                logEventEntity = logEventRepository
                        .getSessionMake(fUserSceneVersionId,fMakeVersionId,fSessionSplitTime, fSessionEvent);
            }

            logger.info("插入logEventEntity：" + logEventEntity.size());

            saveBatchLogEventEntity(logEventEntity,"t_session_log_event");

            logger.info("插入logEventEntity完成");


            logEventPvDistribute = logEventRepository.getLogPVDistribute(fUserSceneVersionId,fMakeVersionId);

            for (int i = 0; i < logEventPvDistribute.size(); i++) {
                logEventPVDistributeRepository.
                        insertUserLogEventPVDistribute("all",0,
                                Integer.parseInt(String.valueOf(
                                        logEventPvDistribute.get(i).get("session_pv_avg"))),
                                Integer.parseInt(String.valueOf(
                                        logEventPvDistribute.get(i).get("session_pv_max"))),
                                Integer.parseInt(String.valueOf(
                                        logEventPvDistribute.get(i).get("session_pv_25p"))),
                                Integer.parseInt(String.valueOf(
                                        logEventPvDistribute.get(i).get("session_pv_50p"))),
                                Integer.parseInt(String.valueOf(
                                        logEventPvDistribute.get(i).get("session_pv_75p"))),
                                1, fMakeVersionId,fUserSceneVersionId);
            }

            logger.info("pv计算完成");


            List<Map<String, Object>> logEventSessionDistribute =
                    logEventRepository.getLogSessionDistribute(fUserSceneVersionId,fMakeVersionId);

            for (int i = 0; i < logEventSessionDistribute.size(); i++) {
                logEventSessionDistributeRepository.
                        insertUserLogEventSessionDistribute("all",0,
                                Integer.parseInt(String.valueOf(
                                        logEventSessionDistribute.get(i).get("session_avg"))),
                                Integer.parseInt(String.valueOf(
                                        logEventSessionDistribute.get(i).get("session_max"))),
                                Integer.parseInt(String.valueOf(
                                        logEventSessionDistribute.get(i).get("session_25p"))),
                                Integer.parseInt(String.valueOf(
                                        logEventSessionDistribute.get(i).get("session_50p"))),
                                Integer.parseInt(String.valueOf(
                                        logEventSessionDistribute.get(i).get("session_75p"))),
                                1, fMakeVersionId,fUserSceneVersionId);
            }

            logger.info("session计算完成");

            logEventAgePvDistribute = logEventAgeRepository.getLogPVAgeDistribute(fUserSceneVersionId,fMakeVersionId);

            for (int i = 0; i < logEventAgePvDistribute.size(); i++) {
                logEventPVDistributeRepository.
                        insertUserLogEventDimPVDistribute("age",0,
                                String.valueOf(logEventAgePvDistribute.get(i).get("age")),
                                Integer.parseInt(String.valueOf(
                                        logEventAgePvDistribute.get(i).get("session_pv_num"))),
                                Integer.parseInt(String.valueOf(
                                        logEventAgePvDistribute.get(i).get("user_pv_num"))),
                                Integer.parseInt(String.valueOf(
                                        logEventAgePvDistribute.get(i).get("session_pv_avg"))),
                                Integer.parseInt(String.valueOf(
                                        logEventAgePvDistribute.get(i).get("session_pv_max"))),
                                Integer.parseInt(String.valueOf(
                                        logEventAgePvDistribute.get(i).get("session_pv_min"))),
                                Integer.parseInt(String.valueOf(
                                        logEventAgePvDistribute.get(i).get("session_pv_50p"))),
                                1, fMakeVersionId,fUserSceneVersionId);
            }

            logger.info("age pv计算完成");


            List<Map<String, Object>> logEventAgeSessionDistribute = logEventAgeRepository
                    .getLogSessionAgeDistribute(fUserSceneVersionId,fMakeVersionId);

            for (int i = 0; i < logEventAgeSessionDistribute.size(); i++) {
                logEventSessionDistributeRepository.
                        insertLogEventDimDistribute("age",0,
                                String.valueOf(logEventAgeSessionDistribute.get(i).get("age")),
                                Integer.parseInt(String.valueOf(
                                        logEventAgeSessionDistribute.get(i).get("session_num"))),
                                Integer.parseInt(String.valueOf(
                                        logEventAgeSessionDistribute.get(i).get("user_num"))),
                                Integer.parseInt(String.valueOf(
                                        logEventAgeSessionDistribute.get(i).get("session_avg"))),
                                Integer.parseInt(String.valueOf(
                                        logEventAgeSessionDistribute.get(i).get("session_max"))),
                                Integer.parseInt(String.valueOf(
                                        logEventAgeSessionDistribute.get(i).get("session_min"))),
                                Integer.parseInt(String.valueOf(
                                        logEventAgeSessionDistribute.get(i).get("session_50p"))),
                                1, fMakeVersionId,fUserSceneVersionId);
            }

            logger.info("age session计算完成");


            logEventSexPvDistribute = logEventSexRepository.getLogPVSexDistribute(fUserSceneVersionId,fMakeVersionId);

            for (int i = 0; i < logEventSexPvDistribute.size(); i++) {
                logEventPVDistributeRepository.
                        insertUserLogEventDimPVDistribute("sex",0,
                                String.valueOf(logEventSexPvDistribute.get(i).get("f_sex")),
                                Integer.parseInt(String.valueOf(logEventSexPvDistribute.get(i).get("session_pv_num"))),
                                Integer.parseInt(String.valueOf(logEventSexPvDistribute.get(i).get("user_pv_num"))),
                                Integer.parseInt(String.valueOf(logEventSexPvDistribute.get(i).get("session_pv_avg"))),
                                Integer.parseInt(String.valueOf(logEventSexPvDistribute.get(i).get("session_pv_max"))),
                                Integer.parseInt(String.valueOf(logEventSexPvDistribute.get(i).get("session_pv_min"))),
                                Integer.parseInt(String.valueOf(logEventSexPvDistribute.get(i).get("session_pv_50p"))),
                                1, fMakeVersionId,fUserSceneVersionId);
            }

            logger.info("sex pv计算完成");


            List<Map<String, Object>> logEventSexSessionDistribute = logEventSexRepository
                    .getLogSessionSexDistribute(fUserSceneVersionId,fMakeVersionId);

            for (int i = 0; i < logEventSexSessionDistribute.size(); i++) {
                logEventSessionDistributeRepository.
                        insertLogEventDimDistribute("sex",0,
                                String.valueOf(logEventSexSessionDistribute.get(i).get("f_sex")),
                                Integer.parseInt(String.valueOf(
                                        logEventSexSessionDistribute.get(i).get("session_num"))),
                                Integer.parseInt(String.valueOf(
                                        logEventSexSessionDistribute.get(i).get("user_num"))),
                                Integer.parseInt(String.valueOf(
                                        logEventSexSessionDistribute.get(i).get("session_avg"))),
                                Integer.parseInt(String.valueOf(
                                        logEventSexSessionDistribute.get(i).get("session_max"))),
                                Integer.parseInt(String.valueOf(
                                        logEventSexSessionDistribute.get(i).get("session_min"))),
                                Integer.parseInt(String.valueOf(
                                        logEventSexSessionDistribute.get(i).get("session_50p"))),
                                1, fMakeVersionId,fUserSceneVersionId);
            }

            logger.info("sex session计算完成");


            logEventChannelPvDistribute = logEventChannelRepository
                    .getLogPVChannelDistribute(fUserSceneVersionId,fMakeVersionId);

            for (int i = 0; i < logEventChannelPvDistribute.size(); i++) {
                logEventPVDistributeRepository.
                        insertUserLogEventDimPVDistribute("channel",0,
                                String.valueOf(logEventChannelPvDistribute.get(i).get("f_channel")),
                                Integer.parseInt(String.valueOf(
                                        logEventChannelPvDistribute.get(i).get("session_pv_num"))),
                                Integer.parseInt(String.valueOf(
                                        logEventChannelPvDistribute.get(i).get("user_pv_num"))),
                                Integer.parseInt(String.valueOf(
                                        logEventChannelPvDistribute.get(i).get("session_pv_avg"))),
                                Integer.parseInt(String.valueOf(
                                        logEventChannelPvDistribute.get(i).get("session_pv_max"))),
                                Integer.parseInt(String.valueOf(
                                        logEventChannelPvDistribute.get(i).get("session_pv_min"))),
                                Integer.parseInt(String.valueOf(
                                        logEventChannelPvDistribute.get(i).get("session_pv_50p"))),
                                1, fMakeVersionId,fUserSceneVersionId);
            }


            logger.info("channel pv计算完成");


            List<Map<String, Object>> logEventChannelSessionDistribute = logEventChannelRepository
                    .getLogSessionChannelDistribute(fUserSceneVersionId,fMakeVersionId);

            for (int i = 0; i < logEventChannelSessionDistribute.size(); i++) {
                logEventSessionDistributeRepository.
                        insertLogEventDimDistribute("channel",0,
                                String.valueOf(logEventChannelSessionDistribute.get(i).get("f_channel")),
                                Integer.parseInt(String.valueOf(
                                        logEventChannelSessionDistribute.get(i).get("session_num"))),
                                Integer.parseInt(String.valueOf(
                                        logEventChannelSessionDistribute.get(i).get("user_num"))),
                                Integer.parseInt(String.valueOf(
                                        logEventChannelSessionDistribute.get(i).get("session_avg"))),
                                Integer.parseInt(String.valueOf(
                                        logEventChannelSessionDistribute.get(i).get("session_max"))),
                                Integer.parseInt(String.valueOf(
                                        logEventChannelSessionDistribute.get(i).get("session_min"))),
                                Integer.parseInt(String.valueOf(
                                        logEventChannelSessionDistribute.get(i).get("session_50p"))),
                                1, fMakeVersionId,fUserSceneVersionId);
            }


            logger.info("channel session计算完成");


            logEventProvincePvDistribute = logEventProvinceRepository
                    .getLogPVProvinceDistribute(fUserSceneVersionId,fMakeVersionId);

            for (int i = 0; i < logEventProvincePvDistribute.size(); i++) {
                logEventPVDistributeRepository.
                        insertUserLogEventDimPVDistribute("province",0,
                                String.valueOf(logEventProvincePvDistribute.get(i).get("f_province")),
                                Integer.parseInt(String.valueOf(
                                        logEventProvincePvDistribute.get(i).get("session_pv_num"))),
                                Integer.parseInt(String.valueOf(
                                        logEventProvincePvDistribute.get(i).get("user_pv_num"))),
                                Integer.parseInt(String.valueOf(
                                        logEventProvincePvDistribute.get(i).get("session_pv_avg"))),
                                Integer.parseInt(String.valueOf(
                                        logEventProvincePvDistribute.get(i).get("session_pv_max"))),
                                Integer.parseInt(String.valueOf(
                                        logEventProvincePvDistribute.get(i).get("session_pv_min"))),
                                Integer.parseInt(String.valueOf(
                                        logEventProvincePvDistribute.get(i).get("session_pv_50p"))),
                                1, fMakeVersionId,fUserSceneVersionId);
            }


            logger.info("province pv计算完成");


            List<Map<String, Object>> logEventProvinceSessionDistribute = logEventProvinceRepository
                    .getLogSessionProvinceDistribute(fUserSceneVersionId,fMakeVersionId);

            for (int i = 0; i < logEventProvinceSessionDistribute.size(); i++) {
                logEventSessionDistributeRepository.
                        insertLogEventDimDistribute("province",0,
                                String.valueOf(logEventProvinceSessionDistribute.get(i).get("f_province")),
                                Integer.parseInt(String.valueOf(
                                        logEventProvinceSessionDistribute.get(i).get("session_num"))),
                                Integer.parseInt(String.valueOf(
                                        logEventProvinceSessionDistribute.get(i).get("user_num"))),
                                Integer.parseInt(String.valueOf(
                                        logEventProvinceSessionDistribute.get(i).get("session_avg"))),
                                Integer.parseInt(String.valueOf(
                                        logEventProvinceSessionDistribute.get(i).get("session_max"))),
                                Integer.parseInt(String.valueOf(
                                        logEventProvinceSessionDistribute.get(i).get("session_min"))),
                                Integer.parseInt(String.valueOf(
                                        logEventProvinceSessionDistribute.get(i).get("session_50p"))),
                                1, fMakeVersionId,fUserSceneVersionId);
            }


            logger.info("province session计算完成");


            sessionEventEntity = sessionEventRepository.getSankey(fUserSceneVersionId,fMakeVersionId);

            saveBatchSessionEventEntity(sessionEventEntity,"t_session_event_distinct_sankey");

            logger.info("插入sessionEventEntity完成");


            List<Map<String, Object>> logEventSingle = logEventRepository
                    .getSessionSingleDf(fUserSceneVersionId,fMakeVersionId);

            for (int i = 0; i < logEventSingle.size(); i++) {
                sessionSingleRepository.insertSessionSingle(
                        String.valueOf(
                                logEventSingle.get(i).get("event_from")),
                        String.valueOf(
                                logEventSingle.get(i).get("event_to")),
                        Integer.parseInt(String.valueOf(
                                logEventSingle.get(i).get("f_weight_session"))), fMakeVersionId,fUserSceneVersionId);
            }

            logger.info("single插入完成");



            if (fMakeVersionId == 0) {
                logEventDistinctEntity = logEventDistinctRepository
                        .getDistinctSessionOrig(fUserSceneVersionId, fSessionSplitTime, fSessionEvent);

            } else {
                logEventDistinctEntity = logEventDistinctRepository
                        .getDistinctSessionMake(fUserSceneVersionId,fMakeVersionId, fSessionSplitTime, fSessionEvent);

            }
            saveBatchLogEventEntity(logEventDistinctEntity,"t_session_log_event_distinct");

            logger.info("插入logEventDistinctEntity完成");


            sessionEventDistinctEntity = sessionDistinctEventRepository.
                    getSankeyDistinct(fUserSceneVersionId,fMakeVersionId);

            saveBatchSessionEventEntity(sessionEventDistinctEntity,"t_session_event_distinct_sankey");



            logger.info("插入sessionEventDistinctEntity完成");

            ProcessBuilder processBuilderKms = new ProcessBuilder("python3",
                    "/usr/local/application/bin/classes/kms.py", fUserSceneVersionId,Integer.toString(fMakeVersionId));
            processBuilderKms.redirectErrorStream(true);

            Process processKms = processBuilderKms.start();
            List<String> resultKms = readProcessOutput(processKms.getInputStream());

            logger.info("resultKms =" + resultKms);

            ProcessBuilder processBuilderDtw = new ProcessBuilder("python3",
                    "/usr/local/application/bin/classes/dtw.py", fUserSceneVersionId,Integer.toString(fMakeVersionId));
            processBuilderDtw.redirectErrorStream(true);

            Process processDtw = processBuilderDtw.start();
            List<String> resultDtw = readProcessOutput(processDtw.getInputStream());

            logger.info("resultDtw =" + resultDtw);

            ProcessBuilder processBuilderNetwork = new ProcessBuilder("python3",
                    "/usr/local/application/bin/classes/networkxdemo.py",
                    fUserSceneVersionId,Integer.toString(fMakeVersionId));
            processBuilderNetwork.redirectErrorStream(true);

            Process processNetwork = processBuilderNetwork.start();
            List<String> resultNetwork = readProcessOutput(processNetwork.getInputStream());

            logger.info("resultNetwork =" + resultNetwork);

            taskEntity.setFSessionSplitStatus(1);
            taskEntity.setFUpdateTime(new Date());
            userLogUploadStatusRepository.saveAndFlush(taskEntity);

            logger.info("执行切分任务完成");

        } catch (Exception e) {
            taskEntity.setFSessionSplitStatus(3);
            taskEntity.setFUpdateTime(new Date());
            userLogUploadStatusRepository.save(taskEntity);
            logger.error("执行切分调度任务失败", e);
        }
    }


    public void saveBatchLogEventEntity(List<Map<String, Object>> list, String tablename) {
        try {
            logger.info("执行上传用户日志");
            String initSql = "insert into " + tablename + " (f_user_id,f_age,f_sex,f_province,f_city,f_channel,"
                    + "f_session_id,f_session_rank,f_event,f_event_detail,f_client_time,f_category,f_subcategory,"
                    + "f_make_version_id,f_user_scene_version_id) values ";
            for (int i = 1; i <= list.size(); i++) {
                //改为批量1000,上传速度2W条/s;大约是批量100速度的1.6倍,是批量10的16倍,是逐条上传速度的180倍
                if (i % 1000 == 0) {
                    String batchSql = initSql.substring(0, initSql.length() - 1);
                    logger.info("执行上传用户日志第{}行", i);
                    logger.info("执行batchSql:{}", batchSql);
                    logger.info("执行batchSql结果:{}", commonInfoRepository.updateInfo(batchSql));
                    initSql = "insert into " + tablename + " (f_user_id,f_age,f_sex,f_province,f_city,f_channel,"
                            + "f_session_id,f_session_rank,f_event,f_event_detail,f_client_time,f_category,"
                            + "f_subcategory,f_make_version_id,f_user_scene_version_id) values ";
                } else {
                    initSql +=
                            "('" + list.get(i - 1).get("f_user_id") + "','"
                                    + list.get(i - 1).get("f_age") + "','"
                                    + list.get(i - 1).get("f_sex") + "','"
                                    + list.get(i - 1).get("f_province") + "','"
                                    + list.get(i - 1).get("f_city") + "','"
                                    + list.get(i - 1).get("f_channel") + "','"
                                    + list.get(i - 1).get("f_session_id") + "','"
                                    + list.get(i - 1).get("f_session_rank") + "','"
                                    + list.get(i - 1).get("f_event") + "','"
                                    + list.get(i - 1).get("f_event_detail") + "','"
                                    + list.get(i - 1).get("f_client_time") + "','"
                                    + list.get(i - 1).get("f_category") + "','"
                                    + list.get(i - 1).get("f_subcategory") + "','"
                                    + fMakeVersionId + "','"
                                    + list.get(i - 1).get("f_user_scene_version_id") + "'),";
                }
            }
            String batchSql = initSql.substring(0, initSql.length() - 1);
            logger.info("批量执行上传人群的最后部分:{}", batchSql);
            logger.info("执行batchSql结果:{}", commonInfoRepository.updateInfo(batchSql));
            logger.info("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            userLogUploadStatusRepository.updateSplitStatus(
                    list.get(0).get("f_user_scene_version_id").toString(),fMakeVersionId, 3);
            logger.info("上传操作异常");
        }
    }



    public void saveBatchSessionEventEntity(List<Map<String, Object>> list, String tablename) {
        try {
            logger.info("执行上传用户日志");
            String initSql = "insert into " + tablename + " (f_age,f_sex,f_province,f_city,f_channel,"
                    + "f_event_from,f_event_to,f_weight_session,f_weight_user,f_weight_pv,"
                    + "f_event_path,f_category_from,f_category_to,"
                    + "f_category_path,f_subcategory_from,f_subcategory_to,f_subcategory_path,"
                    + "f_make_version_id,f_user_scene_version_id) values ";
            for (int i = 1; i <= list.size(); i++) {
                //改为批量1000,上传速度2W条/s;大约是批量100速度的1.6倍,是批量10的16倍,是逐条上传速度的180倍
                if (i % 1000 == 0) {
                    String batchSql = initSql.substring(0, initSql.length() - 1);
                    logger.info("执行上传用户日志第{}行", i);
                    logger.info("执行batchSql:{}", batchSql);
                    logger.info("执行batchSql结果:{}", commonInfoRepository.updateInfo(batchSql));
                    initSql = "insert into " + tablename + " (f_age,f_sex,f_province,f_city,f_channel,"
                            + "f_event_from,f_event_to,f_weight_session,f_weight_user,f_weight_pv,"
                            + "f_event_path,f_category_from,f_category_to,"
                            + "f_category_path,f_subcategory_from,f_subcategory_to,f_subcategory_path,"
                            + "f_make_version_id,f_user_scene_version_id) values ";
                } else {
                    initSql +=
                            "('" + list.get(i - 1).get("f_age") + "','"
                                    + list.get(i - 1).get("f_sex") + "','"
                                    + list.get(i - 1).get("f_province") + "','"
                                    + list.get(i - 1).get("f_city") + "','"
                                    + list.get(i - 1).get("f_channel") + "','"
                                    + list.get(i - 1).get("f_event_from") + "','"
                                    + list.get(i - 1).get("f_event_to") + "','"
                                    + list.get(i - 1).get("f_weight_session") + "','"
                                    + list.get(i - 1).get("f_weight_user") + "','"
                                    + list.get(i - 1).get("f_weight_pv") + "','"
                                    + list.get(i - 1).get("f_event_path") + "','"
                                    + list.get(i - 1).get("f_category_from") + "','"
                                    + list.get(i - 1).get("f_category_to") + "','"
                                    + list.get(i - 1).get("f_category_path") + "','"
                                    + list.get(i - 1).get("f_subcategory_from") + "','"
                                    + list.get(i - 1).get("f_subcategory_to") + "','"
                                    + list.get(i - 1).get("f_subcategory_path") + "','"
                                    + fMakeVersionId + "','"
                                    + list.get(i - 1).get("f_user_scene_version_id") + "'),";
                }
            }
            String batchSql = initSql.substring(0, initSql.length() - 1);
            logger.info("批量执行上传人群的最后部分:{}", batchSql);
            logger.info("执行batchSql结果:{}", commonInfoRepository.updateInfo(batchSql));
            logger.info("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            userLogUploadStatusRepository.updateSplitStatus(
                    list.get(0).get("f_user_scene_version_id").toString(),fMakeVersionId, 3);
            logger.info("上传操作异常");
        }
    }

    
}
