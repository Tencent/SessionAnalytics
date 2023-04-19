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
import com.session.path.data.userpath.repository.UserLogDetailRepository;
import com.session.path.data.userpath.repository.UserLogUploadStatusRepository;
import com.session.path.data.userpath.service.UserLogDetailService;
import com.session.path.data.userpath.util.BizCode;
import com.session.path.data.userpath.util.CommonUtil;
import com.session.path.data.userpath.util.MD5Util;
import java.io.File;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName SessionUploadAction
 * @Description 数据管理-数据上传
 * @Author author
 * @Date 2023/02/03 16:34
 * @Version 1.0
 **/
@Controller
@CrossOrigin
@RestController
@RequestMapping("/upload")
public class SessionUploadAction extends BaseAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(SessionUploadAction.class);

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;

    @Autowired
    UserLogDetailRepository userLogDetailRepository;

    @Autowired
    UserLogDetailService userLogDetailService;

    /**
     * 上传信息及excel
     *
     * @return
     */

    @PostMapping("/uploadExcel")
    public BaseRsp uploadExcel(HttpServletRequest httpServletRequest,
            @RequestParam("f_file") MultipartFile fFile,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam("f_scene_desc") String fSceneDesc,
            @RequestParam("f_session_event") String fSessionEvent,
            @RequestParam("f_session_split_time") int fSessionSplitTime) {
        LOGGER.info("上传信息及excel");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);

            File file = File.createTempFile("tmp", null);
            fFile.transferTo(file); //MultipartFile转File
            file.deleteOnExit();

            LOGGER.info("file.getAbsolutePath():{}", file.getAbsolutePath());
            LOGGER.info("file name:{}", file.getName());
            LOGGER.info("file path:{}", file.getPath());

            String fSceneId = MD5Util.encode(fUploadName);
            UserLogUploadStatusEntity userLogUploadStatusEntity = new UserLogUploadStatusEntity();
            userLogUploadStatusEntity.setFSceneId(fSceneId);
            userLogUploadStatusEntity.setFUploadName(fUploadName);
            userLogUploadStatusEntity.setFUploadUser(fUploadUser);
            userLogUploadStatusEntity.setFUploadStatus(2);
            userLogUploadStatusEntity.setFSceneDesc(fSceneDesc);
            userLogUploadStatusEntity.setFSessionEvent(fSessionEvent);
            userLogUploadStatusEntity.setFSessionSplitTime(fSessionSplitTime);
            userLogUploadStatusEntity.setFVersionId(1);
            String fUserSceneVersionId = fUploadUser + fSceneId + 1;
            userLogUploadStatusEntity.setFUserSceneVersionId(fUserSceneVersionId);
            userLogUploadStatusRepository.saveAndFlush(userLogUploadStatusEntity);
            userLogDetailService.excelImportAll(file, fUploadName, fUploadUser);
            baseRsp.setMessage("发起上传信息及excel成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("发起上传信息及excel失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("发起上传信息及excel失败");
        }
        return baseRsp;
    }


    /**
     * 获取上传的最大版本号 （暂未使用）
     *
     * @return
     */

    @GetMapping("/getMaxVersionId")
    public BaseRsp getMaxVersionId(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("获取上传的版本号");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fVersionId = userLogUploadStatusRepository.getMaxVersionId(fUploadName, fUploadUser) + 1;
            rspPage.total = fVersionId;
            baseRsp.setMessage("获取上传的版本号成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取上传的版本号失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取上传的版本号失败");
        }
        return baseRsp;
    }


    /**
     * 获取上传的版本号列表
     *
     * @return
     */

    @GetMapping("/getVersionIdList")
    public BaseRsp getVersionIdList(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("获取上传的版本号");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);
            List<Map<String, Object>> relist = userLogUploadStatusRepository
                    .getVersionIdList(fUploadName, fUploadUser);
            rspPage.list = relist;
            baseRsp.setMessage("获取上传的版本号列表成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取上传的版本号列表失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取上传的版本号列表失败");
        }
        return baseRsp;
    }



    /**
     * 获取数据治理版本号列表
     *
     * @return
     */

    @GetMapping("/getMakeVersionList")
    public BaseRsp getMakeVersionList(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("获取数据治理版本号");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            List<Map<String, Object>> relist = userLogUploadStatusRepository
                    .getUserLogMakeVersionList(fUserSceneVersionId);
            rspPage.list = relist;
            baseRsp.setMessage("获取数据治理版本号列表成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取数据治理版本号列表失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取数据治理版本号列表失败");
        }
        return baseRsp;
    }


    /**
     * 获取上传预览数据limit 10
     *
     * @return
     */

    @GetMapping("/getUserLogLimit")
    public BaseRsp getUserLogLimit(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("获取上传预览数据limit 10");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = userLogDetailRepository.getUserLogLimit(fUserSceneVersionId);
            rspPage.list = resList;
            baseRsp.setMessage("获取上传预览数据limit 10成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取上传预览数据limit 10失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取上传预览数据limit 10失败");
        }
        return baseRsp;
    }



    /**
     * 获取治理预览数据limit 10
     *
     * @return
     */

    @GetMapping("/getUserLogMakeLimit")
    public BaseRsp getUserLogMakeLimit(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("获取治理预览数据limit 10");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = userLogDetailRepository.getUserLogMakeLimit(fUserSceneVersionId,fMakeVersionId);
            rspPage.list = resList;
            baseRsp.setMessage("获取治理预览数据limit 10成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取治理预览数据limit 10失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取治理预览数据limit 10失败");
        }
        return baseRsp;
    }



    /**
     * 获取上传状态信息结果（0:等待中 1：成功  2：进行中  3：失败）
     *
     * @return
     */

    @GetMapping("/getUserLogUploadStatusEntity")
    public BaseRsp getUserLogUploadStatusEntity(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_status", required = false) Integer fStatus,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "20") Integer pageSize) {
        LOGGER.info("获取UserLogUploadStatusEntity");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<UserLogUploadStatusEntity> resList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<UserLogUploadStatusEntity> userLogUploadStatusEntityPage = null;

            userLogUploadStatusEntityPage = userLogUploadStatusRepository
                    .getUserLogUploadStatusEntity(fUploadUser, fUploadName, fStatus, pageable);

            total = userLogUploadStatusEntityPage.getTotalElements();
            for (UserLogUploadStatusEntity userLogUploadStatusEntity : userLogUploadStatusEntityPage) {
                resList.add(userLogUploadStatusEntity);
            }

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取UserLogUploadStatusEntity成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取UserLogUploadStatusEntity失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取UserLogUploadStatusEntity失败");
        }
        return baseRsp;
    }


    /**
     * 获取历史场景标题列表(首页上传)
     *
     * @return
     */

    @GetMapping("/getUploadNameList")
    public BaseRsp getUploadNameList(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("获取历史场景标题列表");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            resList = userLogUploadStatusRepository.getUploadNameList(fUploadUser);
            rspPage.list = resList;
            baseRsp.setMessage("获取历史场景标题列表成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取历史场景标题列表失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取历史场景标题列表失败");
        }
        return baseRsp;
    }


    /**
     * 获取历史场景标题列表(分析页面)
     *
     * @return
     */

    @GetMapping("/getUploadNameDoneList")
    public BaseRsp getUploadNameDoneList(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("获取历史场景标题完成切割列表");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            resList = userLogUploadStatusRepository.getUploadNameDoneList(fUploadUser);
            rspPage.list = resList;
            baseRsp.setMessage("获取历史场景标题完成切割列表成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取历史场景标题完成切割列表失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取历史场景标题完成切割列表失败");
        }
        return baseRsp;
    }


    /**
     * 获取上传任务执行情况（已完成，正在进行中任务多少个）
     *
     * @return
     */

    @GetMapping("/getUserLogUploadStatusCnt")
    public BaseRsp getUserLogUploadStatusCnt(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("获取上传任务执行状态汇总");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            resList = userLogUploadStatusRepository.getUserLogUploadStatusCnt(fUploadUser);
            rspPage.list = resList;
            baseRsp.setMessage("获取上传任务执行状态汇总成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取上传任务执行状态汇总失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取上传任务执行状态汇总失败");
        }
        return baseRsp;
    }







}
