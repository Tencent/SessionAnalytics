/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.action;

import com.session.path.data.userpath.dto.BaseRsp;
import com.session.path.data.userpath.dto.RspPage;
import com.session.path.data.userpath.entity.UserLogUploadStatusEntity;
import com.session.path.data.userpath.repository.UserLogUploadStatusRepository;
import com.session.path.data.userpath.service.LogEventSessionService;
import com.session.path.data.userpath.util.BizCode;
import com.session.path.data.userpath.util.CommonUtil;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SessionSessionAction
 * @Description session切分
 * @Author author
 * @Date 2023/03/04 16:34
 * @Version 1.0
 **/
@Controller
@CrossOrigin
@RestController
@RequestMapping("/session")
public class SessionSplitAction extends BaseAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(SessionSplitAction.class);

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;

    @Autowired
    LogEventSessionService logEventSessionService;

    /**
     * 执行session切分任务
     *
     * @return
     */

    @PostMapping("/sessionSplit")
    public BaseRsp sessionSplit(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("执行session切分任务");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            logEventSessionService.executeSessionSplit(fUserSceneVersionId,fMakeVersionId);
            baseRsp.setMessage("发起执行session切分任务成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("发起执行session切分任务失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("发起执行session切分任务失败");
        }
        return baseRsp;
    }



    /**
     * 获取原始数据集切分列表（0:等待中 1：成功  2：进行中  3：失败）
     *
     * @return
     */

    @GetMapping("/getUserLogSplitStatusOldfEntity")
    public BaseRsp getUserLogSplitStatusOldfEntity(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_session_split_status", required = false) Integer fSessionSplitStatus,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "20") Integer pageSize) {
        LOGGER.info("获取原始数据集切分列表");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<UserLogUploadStatusEntity> resList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<UserLogUploadStatusEntity> userLogMakeStatusEntityPage = null;

            userLogMakeStatusEntityPage = userLogUploadStatusRepository
                    .getUserLogSplitStatusOldfEntity(fUploadUser, fUploadName, fSessionSplitStatus, pageable);
            total = userLogMakeStatusEntityPage.getTotalElements();

            for (UserLogUploadStatusEntity userLogMakeStatusEntity : userLogMakeStatusEntityPage) {
                resList.add(userLogMakeStatusEntity);
            }

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取原始数据集切分列表成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取原始数据集切分列表失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取原始数据集切分列表失败");
        }
        return baseRsp;
    }



    /**
     * 获取治理数据集切分列表（0:等待中 1：成功  2：进行中  3：失败）
     *
     * @return
     */

    @GetMapping("/getUserLogSplitStatusNewfEntity")
    public BaseRsp getUserLogSplitStatusNewfEntity(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_session_split_status", required = false) Integer fSessionSplitStatus,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "20") Integer pageSize) {
        LOGGER.info("获取治理数据集切分列表");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<UserLogUploadStatusEntity> resList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<UserLogUploadStatusEntity> userLogMakeStatusEntityPage = null;

            userLogMakeStatusEntityPage = userLogUploadStatusRepository
                    .getUserLogSplitStatusNewfEntity(fUploadUser, fUploadName, fSessionSplitStatus, pageable);
            total = userLogMakeStatusEntityPage.getTotalElements();

            for (UserLogUploadStatusEntity userLogMakeStatusEntity : userLogMakeStatusEntityPage) {
                resList.add(userLogMakeStatusEntity);
            }

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取治理数据集切分列表成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取治理数据集切分列表失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取治理数据集切分列表失败");
        }
        return baseRsp;
    }

}
