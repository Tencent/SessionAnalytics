/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.session.path.data.userpath.dto.BaseRsp;
import com.session.path.data.userpath.dto.RspPage;
import com.session.path.data.userpath.entity.UserLogUploadStatusEntity;
import com.session.path.data.userpath.repository.UserLogDetailMakeRepository;
import com.session.path.data.userpath.repository.UserLogDetailRepository;
import com.session.path.data.userpath.repository.UserLogMakeRepository;
import com.session.path.data.userpath.repository.UserLogUploadStatusRepository;
import com.session.path.data.userpath.service.UserLogDetailMakeService;
import com.session.path.data.userpath.util.BizCode;
import com.session.path.data.userpath.util.CommonUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
 * @ClassName SessionMakerAction
 * @Description 数据治理模块
 * @Author author
 * @Date 2023/03/04 16:34
 * @Version 1.0
 **/
@Controller
@CrossOrigin
@RestController
@RequestMapping("/maker")
public class SessionMakerAction extends BaseAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(SessionMakerAction.class);

    @Autowired
    UserLogDetailRepository userLogDetailRepository;

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;

    @Autowired
    UserLogMakeRepository userLogMakeRepository;

    @Autowired
    UserLogDetailMakeRepository userLogDetailMakeRepository;

    @Autowired
    UserLogDetailMakeService userLogDetailMakeService;


    /**
     * 获取数据治理任务执行状态汇总
     *
     * @return
     */

    @GetMapping("/getUserLogMakeStatusCnt")
    public BaseRsp getUserLogMakeStatusCnt(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("获取数据治理任务执行状态汇总");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            resList = userLogUploadStatusRepository.getUserLogMakeStatusCnt(fUploadUser);
            rspPage.list = resList;
            baseRsp.setMessage("获取数据治理任务执行状态汇总成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取数据治理任务执行状态汇总失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取数据治理任务执行状态汇总失败");
        }
        return baseRsp;
    }


    /**
     * 获取治理状态信息结果（0:等待中 1：成功  2：进行中  3：失败）
     *
     * @return
     */

    @GetMapping("/getUserLogMakeStatusEntity")
    public BaseRsp getUserLogMakeStatusEntity(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_make_status", required = false) Integer fMakeStatus,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "20") Integer pageSize) {
        LOGGER.info("getUserLogMakeStatusEntity");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<UserLogUploadStatusEntity> resList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<UserLogUploadStatusEntity> userLogMakeStatusEntityPage = null;

            userLogMakeStatusEntityPage = userLogUploadStatusRepository
                    .getUserLogMakeStatusEntity(fUploadUser, fUploadName, fMakeStatus, pageable);
            total = userLogMakeStatusEntityPage.getTotalElements();

            for (UserLogUploadStatusEntity userLogMakeStatusEntity : userLogMakeStatusEntityPage) {
                resList.add(userLogMakeStatusEntity);
            }

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取UserLogMakeStatusEntity成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取UserLogMakeStatusEntity失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取UserLogMakeStatusEntity失败");
        }
        return baseRsp;
    }


    /**
     * 获取替换的维度数据
     *
     * @return
     */

    @GetMapping("/getUserLogMake")
    public BaseRsp getUserLogMake(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser
    ) {
        LOGGER.info("获取替换的维度数据");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName, fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName, fUploadUser).get("f_user_scene_version_id");

            resList = userLogMakeRepository
                    .getUserLogMake(fUserSceneVersionId, fMakeVersionId);
            rspPage.list = resList;
            baseRsp.setMessage("获取替换的维度数据成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取替换的维度数据失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取替换的维度数据失败");
        }
        return baseRsp;
    }


    /**
     * 获取统计上传日志维度数据（top）
     *
     * @return
     */

    @GetMapping("/getUserLogTop")
    public BaseRsp getUserLogEventTop(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser
    ) {
        LOGGER.info("统计上传日志维度数据");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resListEvent = new ArrayList<>();
        List<Map<String, Object>> resListCategory = new ArrayList<>();
        List<Map<String, Object>> resListSubCategory = new ArrayList<>();
        List<List> list = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName, fUploadUser).get("f_user_scene_version_id");

            resListEvent = userLogDetailRepository
                    .getUserLogEventTop(fUserSceneVersionId);
            resListSubCategory = userLogDetailRepository
                    .getUserLogSubcategoryTop(fUserSceneVersionId);
            resListCategory = userLogDetailRepository
                    .getUserLogCategoryTop(fUserSceneVersionId);
            list.add(resListEvent);
            list.add(resListSubCategory);
            list.add(resListCategory);

            rspPage.list = list;
            baseRsp.setMessage("统计上传日志维度数据成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("统计上传日志维度数据失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("统计上传日志维度数据失败");
        }
        return baseRsp;
    }


    /**
     * 更新上传日志维度数据
     *
     * @return
     */

    @GetMapping("/updateUserLogTop")
    public BaseRsp updateUserLogEventTop(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam("f_json_array") String fJsonArray
    ) {
        LOGGER.info("更新上传日志维度数据");
        BaseRsp baseRsp = new BaseRsp();
        String fUserSceneVersionId = userLogUploadStatusRepository
                .getUserSceneVersionId(fUploadName, fUploadUser).get("f_user_scene_version_id");

        List<Map<String, Object>> list = new ArrayList<>();
        int fMakeVersionId = userLogMakeRepository.getMaxUserLogMakeVersion(fUserSceneVersionId) + 1;
        try {
            CommonUtil.getToken(httpServletRequest);
            UserLogUploadStatusEntity userLogUploadStatusEntity = new UserLogUploadStatusEntity();
            UserLogUploadStatusEntity userLogUploadStatusEntityOld = userLogUploadStatusRepository.
                    getUserLogStatusEntity(fUserSceneVersionId, 0);
            userLogUploadStatusEntity.setFUploadUser(userLogUploadStatusEntityOld.getFUploadUser());
            userLogUploadStatusEntity.setFUploadName(
                    userLogUploadStatusEntityOld.getFUploadName() + "(治理版本V" + fMakeVersionId + ")");
            userLogUploadStatusEntity.setFSceneDesc(
                    userLogUploadStatusEntityOld.getFSceneDesc() + "(治理版本V" + fMakeVersionId + ")");
            userLogUploadStatusEntity.setFSceneId(userLogUploadStatusEntityOld.getFSceneId());
            userLogUploadStatusEntity.setFSessionSplitTime(userLogUploadStatusEntityOld.getFSessionSplitTime());
            userLogUploadStatusEntity.setFVersionId(1);
            userLogUploadStatusEntity.setFUserSceneVersionId(userLogUploadStatusEntityOld.getFUserSceneVersionId());
            userLogUploadStatusEntity.setFSessionEvent(userLogUploadStatusEntityOld.getFSessionEvent());
            userLogUploadStatusEntity.setFMakeVersionId(fMakeVersionId);
            userLogUploadStatusEntity.setFUploadStatus(1);
            userLogUploadStatusEntity.setFMakeCreateTime(new Date());
            userLogUploadStatusEntity.setFMakeUpdateTime(new Date());
            userLogUploadStatusEntity.setFMakeStatus(2);
            userLogUploadStatusEntity.setFSessionSplitStatus(0);
            userLogUploadStatusEntity.setFCreateTime(new Date());
            userLogUploadStatusEntity.setFUpdateTime(new Date());
            userLogUploadStatusRepository.saveAndFlush(userLogUploadStatusEntity);

            list = userLogDetailRepository.getUserLogDetail(fUserSceneVersionId);

            userLogDetailMakeService.saveBatchLogMakeEntity(list, fMakeVersionId);

            JSONArray jsonArray = JSON.parseArray(fJsonArray);

            int size = jsonArray.size();

            for (int i = 0; i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int fPv = jsonObject.getInteger("f_pv");
                String fType = jsonObject.getString("f_type");
                String fOld = jsonObject.getString("f_old");
                String fNew = jsonObject.getString("f_new");

                userLogMakeRepository.insertUserLogMake(fPv,
                        fType, fOld, fNew, 1, fMakeVersionId, fUserSceneVersionId);

                if (fType.equals("event")) {
                    userLogDetailMakeRepository
                            .updateUserLogEventTop(fUserSceneVersionId, fMakeVersionId, fOld, fNew);

                } else if (fType.equals("category")) {
                    userLogDetailMakeRepository
                            .updateUserLogCategoryTop(fUserSceneVersionId, fMakeVersionId, fOld, fNew);
                } else {
                    userLogDetailMakeRepository
                            .updateUserLogSubcategoryTop(fUserSceneVersionId, fMakeVersionId, fOld, fNew);
                }

            }

            userLogUploadStatusRepository.updateMakeStatus(fUserSceneVersionId, fMakeVersionId, 1);
            userLogUploadStatusRepository.updateMakeEndTime(fUploadName, fUploadUser, fMakeVersionId);
            baseRsp.setCode(1);
            baseRsp.setMessage("更新上传日志维度数据成功");
        } catch (Exception e) {
            LOGGER.error("更新上传日志维度数据失败", e);
            userLogUploadStatusRepository.updateMakeStatus(fUserSceneVersionId, fMakeVersionId, 3);
            userLogUploadStatusRepository.updateMakeEndTime(fUploadName, fUploadUser, fMakeVersionId);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("更新上传日志维度数据失败");
        }
        return baseRsp;
    }


    /**
     * 修改治理数据集列表（名称和描述）
     *
     * @return
     */

    @PostMapping("/updateMakeLogName")
    public BaseRsp updateMakeLogName(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_name_new") String fUploadNameNew,
            @RequestParam("f_scene_desc_new") String fSceneDescNew
    ) {
        LOGGER.info("修改治理数据集列表（名称和描述）");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName, fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName, fUploadUser).get("f_user_scene_version_id");

            userLogUploadStatusRepository
                    .updateMakeLogName(fUserSceneVersionId, fMakeVersionId, fUploadNameNew, fSceneDescNew);

            baseRsp.setMessage("修改治理数据集列表（名称和描述）成功");
            baseRsp.setCode(200);
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("修改治理数据集列表（名称和描述）失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("修改治理数据集列表（名称和描述）失败");
        }
        return baseRsp;
    }


}
