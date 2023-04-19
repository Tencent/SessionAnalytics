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
import com.session.path.data.userpath.repository.LogEventAgeRepository;
import com.session.path.data.userpath.repository.LogEventPVDistributeRepository;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SessionAgeAction
 * @Description session数据查询(年龄段)
 * @Author author
 * @Date 2023/03/04 16:34
 * @Version 1.0
 **/
@Controller
@CrossOrigin
@RestController
@RequestMapping("/age")
public class SessionAgeAction extends BaseAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(SessionAgeAction.class);

    @Autowired
    LogEventAgeRepository logEventAgeRepository;

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;

    @Autowired
    LogEventPVDistributeRepository logEventPVDistributeRepository;

    @Autowired
    LogEventSessionDistributeRepository logEventSessionDistributeRepository;


    /**
     * 获取session年龄段列表
     *
     * @return
     */

    @GetMapping("/getSessionLogAgeList")
    public BaseRsp getSessionLogAgeList(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取session年龄段查询");
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

            resList = logEventAgeRepository.getSessionLogAgeList(fUserSceneVersionId,fMakeVersionId);

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取session年龄段查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session年龄段查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session年龄段查询失败");
        }
        return baseRsp;
    }


    /**
     * (用户数、 session数 、PV数)分年龄段
     *
     * @return
     */

    @GetMapping("/getSessionLogCntByAge")
    public BaseRsp getSessionLogCntByAge(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取session年龄分布查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resListPVNumAge = new ArrayList<>();
        List<Map<String, Object>> resListSessionNumAge = new ArrayList<>();
        List<Map<String, Object>> resListUserNumAge = new ArrayList<>();
        List<List> list = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resListPVNumAge = logEventAgeRepository
                    .getSessionLogCntByAgePVNum(fUserSceneVersionId,fMakeVersionId);
            resListSessionNumAge = logEventAgeRepository
                    .getSessionLogCntByAgeSessionNUM(fUserSceneVersionId,fMakeVersionId);
            resListUserNumAge = logEventAgeRepository
                    .getSessionLogCntByAgeUserNum(fUserSceneVersionId,fMakeVersionId);

            list.add(resListPVNumAge);
            list.add(resListSessionNumAge);
            list.add(resListUserNumAge);

            rspPage.list = list;
            rspPage.total = total;
            baseRsp.setMessage("获取session年龄分布查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session年龄分布查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session年龄分布查询失败");
        }
        return baseRsp;
    }


    /**
     * (总用户数 趋势)分年龄段
     *
     * @return
     */

    @GetMapping("/getSessionLogAgeUserCntTrend")
    public BaseRsp getSessionLogAgeUserCntTrend(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_age", required = false) String fAge) {
        LOGGER.info("获取session分年龄段用户趋势查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        List<String> ageList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionIdUser = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionIdUser = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");


            if (fAge.equals("all")) {
                resList = logEventAgeRepository
                        .getSessionLogAgeUserCntTrendALL(fUserSceneVersionIdUser,fMakeVersionIdUser);
            } else {
                String [] ageArr = fAge.split(",");
                for (int i = 0; i < ageArr.length; i++) {
                    ageList.add(ageArr[i]);
                }
                LOGGER.info("ageList:" + ageList);
                resList = logEventAgeRepository
                        .getSessionLogAgeUserCntTrend(fUserSceneVersionIdUser,fMakeVersionIdUser, ageList);
            }

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取session分年龄段用户趋势查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session分年龄段用户趋势查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session分年龄段用户趋势查询失败");
        }
        return baseRsp;
    }


    /**
     * (总session数 趋势)分年龄段
     *
     * @return
     */

    @GetMapping("/getSessionLogAgeSessionCntTrend")
    public BaseRsp getSessionLogAgeSessionCntTrend(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_age", required = false) String fAge) {
        LOGGER.info("获取分年龄段session趋势查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        List<Map<String, Object>> resListZHE = new ArrayList<>();
        List<List> list = new ArrayList<>();

        List<String> ageList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");


            if (fAge.equals("all")) {
                resList = logEventAgeRepository
                        .getSessionLogAgeSessionCntTrendALL(fUserSceneVersionId,fMakeVersionId);
                resListZHE = logEventAgeRepository
                        .getSessionLogAgeCntTrendALLZHE(fUserSceneVersionId,fMakeVersionId);

            } else {
                String [] ageArr = fAge.split(",");
                for (int i = 0; i < ageArr.length; i++) {
                    ageList.add(ageArr[i]);
                }
                LOGGER.info("ageList:" + ageList);
                resList = logEventAgeRepository
                        .getSessionLogAgeSessionCntTrend(fUserSceneVersionId,fMakeVersionId, ageList);
                resListZHE = logEventAgeRepository
                        .getSessionLogAgeSessionCntTrendZHE(fUserSceneVersionId,fMakeVersionId, ageList);
            }

            list.add(resList);
            list.add(resListZHE);
            rspPage.list = list;
            rspPage.total = total;
            baseRsp.setMessage("获取分年龄段session趋势查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分年龄段session趋势查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分年龄段session趋势查询失败");
        }
        return baseRsp;
    }


    /**
     * (总pv数 趋势)分年龄段
     *
     * @return
     */

    @GetMapping("/getSessionLogAgePvCntTrend")
    public BaseRsp getSessionLogAgePvCntTrend(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_age", required = false) String fAge) {
        LOGGER.info("获取分年龄段pv趋势查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        List<String> ageList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionIdPv = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionIdPv = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");


            if (fAge.equals("all")) {
                resList = logEventAgeRepository
                        .getSessionLogAgePvCntTrendALL(fUserSceneVersionIdPv,fMakeVersionIdPv);
            } else {
                String [] ageArr = fAge.split(",");
                for (int i = 0; i < ageArr.length; i++) {
                    ageList.add(ageArr[i]);
                }
                LOGGER.info("ageList:" + ageList);
                resList = logEventAgeRepository
                        .getSessionLogAgePvCntTrend(fUserSceneVersionIdPv,fMakeVersionIdPv, ageList);
            }

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取分年龄段pv趋势查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分年龄段pv趋势查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分年龄段pv趋势查询失败");
        }
        return baseRsp;
    }


    /**
     * session年龄段分布    25  50  75  最大数  平均数
     *
     * @return
     */

    @GetMapping("/getLogSessionAgeDistribute")
    public BaseRsp getLogSessionSexDistribute(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取分年龄段session分布查询");
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
                    .getLogDimSessionDistribute(fUserSceneVersionId,fMakeVersionId,"age");
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取分年龄段session分布查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分年龄段session分布查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分年龄段session分布查询失败");
        }
        return baseRsp;
    }


    /**
     * session-pv年龄段分布    25  50  75  最大数  平均数
     *
     * @return
     */

    @GetMapping("/getLogPVAgeDistribute")
    public BaseRsp getLogPVAgeDistribute(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "20") Integer pageSize) {
        LOGGER.info("获取分年龄段session-pv分布查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        int total = 0;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            Integer limit = (page - 1) * pageSize;
            resList = logEventPVDistributeRepository
                    .getLogDimPVDistribute(fUserSceneVersionId, fMakeVersionId, "age", pageSize, limit);

            total = logEventPVDistributeRepository
                    .getLogDimPVDistributeCount(fUserSceneVersionId, fMakeVersionId, "age");


            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取分年龄段session-pv分布查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分年龄段session-pv分布查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分年龄段session-pv分布查询失败");
        }
        return baseRsp;
    }


}
