/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.action;

import com.session.path.data.userpath.dto.BaseRsp;
import com.session.path.data.userpath.dto.RspPage;
import com.session.path.data.userpath.entity.LogEventEntity;
import com.session.path.data.userpath.repository.LogEventPVDistributeRepository;
import com.session.path.data.userpath.repository.LogEventRepository;
import com.session.path.data.userpath.repository.LogEventSessionDistributeRepository;
import com.session.path.data.userpath.repository.UserLogUploadStatusRepository;
import com.session.path.data.userpath.util.BizCode;
import com.session.path.data.userpath.util.CommonUtil;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SessionQueryAction
 * @Description session数据查询
 * @Author author
 * @Date 2023/03/04 16:34
 * @Version 1.0
 **/
@Controller
@CrossOrigin
@RestController
@RequestMapping("/session")
public class SessionQueryAction extends BaseAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(SessionQueryAction.class);

    @Autowired
    LogEventRepository logEventRepository;

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;

    @Autowired
    LogEventPVDistributeRepository logEventPVDistributeRepository;

    @Autowired
    LogEventSessionDistributeRepository logEventSessionDistributeRepository;


    /**
     * 获取session log数据查询
     *
     * @return
     */

    @GetMapping("/getSessionLogQuery")
    public BaseRsp getSessionLogQuery(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_user_id", required = false) String fUserId,
            @RequestParam(value = "f_channel", required = false) String fChannel,
            @RequestParam(value = "f_province", required = false) String fProvince,
            @RequestParam(value = "f_city", required = false) String fCity,
            @RequestParam(value = "f_category", required = false) String fCategory,
            @RequestParam(value = "f_subcategory", required = false) String fSubcategory,
            @RequestParam(value = "f_event", required = false) String fEvent,
            @RequestParam(value = "f_start_time", required = false) String fStartTime,
            @RequestParam(value = "f_end_time", required = false) String fEndTime,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "20") Integer pageSize) {
        LOGGER.info("获取session数据查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<LogEventEntity> resList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<LogEventEntity> userLogDetailPage = null;
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            userLogDetailPage = logEventRepository
                    .getSessionLogQuery(
                            fUserSceneVersionId, fMakeVersionId, fUserId, fChannel, fProvince, fCity, fCategory,
                            fSubcategory, fStartTime, fEndTime, fEvent, pageable);
            total = userLogDetailPage.getTotalElements();
            for (LogEventEntity logEventEntity : userLogDetailPage) {
                resList.add(logEventEntity);
            }

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取session数据查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session数据查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session数据查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取session数目总览查询
     *
     * @return
     */

    @GetMapping("/getSessionLogCnt")
    public BaseRsp getSessionLogCnt(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取session总览查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");


            resList = logEventRepository.getSessionLogCnt(fUserSceneVersionId,fMakeVersionId);
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取session总览查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session总览查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session总览查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取session总用户数趋势查询
     *
     * @return
     */

    @GetMapping("/getSessionLogUserCntTrend")
    public BaseRsp getSessionLogUserCntTrend(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取session总用户数趋势查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = logEventRepository
                    .getSessionLogUserCntTrend(fUserSceneVersionId,fMakeVersionId);
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取session总用户数趋势查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session总用户数趋势查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session总用户数趋势查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取总session数趋势查询
     *
     * @return
     */

    @GetMapping("/getSessionLogSessionCntTrend")
    public BaseRsp getSessionLogSessionCntTrend(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取session总session数趋势查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = logEventRepository
                    .getSessionLogSessionCntTrend(fUserSceneVersionId,fMakeVersionId);
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取session总session数趋势查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session总session数趋势查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session总session数趋势查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取session总PV数趋势查询
     *
     * @return
     */

    @GetMapping("/getSessionLogPvCntTrend")
    public BaseRsp getSessionLogPvCntTrend(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取session总PV数趋势查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = logEventRepository
                    .getSessionLogPvCntTrend(fUserSceneVersionId,fMakeVersionId);
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取session总PV数趋势查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session总PV数趋势查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session总PV数趋势查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取session分布查询( 25  50  75  最大数  平均数)
     *
     * @return
     */

    @GetMapping("/getLogSessionDistribute")
    public BaseRsp getLogSessionDistribute(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取session分布查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = logEventSessionDistributeRepository
                    .getLogSessionDistribute(fUserSceneVersionId,fMakeVersionId,"all");
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取session分布查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session分布查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session分布查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取session-pv分布查询(25 50  75  最大数  平均数)
     *
     * @return
     */

    @GetMapping("/getLogPVDistribute")
    public BaseRsp getLogPVDistribute(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取session-pv分布查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = logEventPVDistributeRepository
                    .getLogPVDistribute(fUserSceneVersionId,fMakeVersionId,"all");
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取session-pv分布查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session-pv分布查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session-pv分布查询失败");
        }
        return baseRsp;
    }


}
