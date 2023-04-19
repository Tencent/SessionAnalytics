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
import com.session.path.data.userpath.repository.LogEventPVDistributeRepository;
import com.session.path.data.userpath.repository.LogEventSessionDistributeRepository;
import com.session.path.data.userpath.repository.LogEventSexRepository;
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
 * @ClassName SessionSexAction
 * @Description session数据查询(性别)
 * @Author author
 * @Date 2023/03/04 16:34
 * @Version 1.0
 **/
@Controller
@CrossOrigin
@RestController
@RequestMapping("/sex")
public class SessionSexAction extends BaseAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(SessionSexAction.class);

    @Autowired
    LogEventSexRepository logEventSexRepository;

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;

    @Autowired
    LogEventPVDistributeRepository logEventPVDistributeRepository;

    @Autowired
    LogEventSessionDistributeRepository logEventSessionDistributeRepository;


    /**
     * 获取session性别查询列表
     *
     * @return
     */

    @GetMapping("/getSessionLogSexList")
    public BaseRsp getSessionLogSexList(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取session性别查询");
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


            resList = logEventSexRepository.getSessionLogSexList(fUserSceneVersionId,fMakeVersionId);

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取session性别查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session性别查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session性别查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取session性别分布（pv、session、user）
     *
     * @return
     */

    @GetMapping("/getSessionLogCntBySex")
    public BaseRsp getSessionLogCntBySex(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取session性别分布查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resListSexPVNum = new ArrayList<>();
        List<Map<String, Object>> resListSexSessionNum = new ArrayList<>();
        List<Map<String, Object>> resListSexUserNum = new ArrayList<>();
        List<List> list = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");


            resListSexPVNum = logEventSexRepository.
                    getSessionLogCntBySexPVNum(fUserSceneVersionId,fMakeVersionId);
            resListSexSessionNum = logEventSexRepository.
                    getSessionLogCntBySexSessionNum(fUserSceneVersionId,fMakeVersionId);
            resListSexUserNum = logEventSexRepository.
                    getSessionLogCntBySexUserNUm(fUserSceneVersionId,fMakeVersionId);

            list.add(resListSexPVNum);
            list.add(resListSexSessionNum);
            list.add(resListSexUserNum);

            rspPage.list = list;
            rspPage.total = total;
            baseRsp.setMessage("获取session性别分布查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session性别分布查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session性别分布查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取session 分性别用户趋势查询
     *
     * @return
     */

    @GetMapping("/getSessionLogSexUserCntTrend")
    public BaseRsp getSessionLogSexUserCntTrend(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_sex", required = false) String fSex) {
        LOGGER.info("获取session 分性别用户趋势查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        List<String> sexList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionIdSex = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionIdSex = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");


            if (fSex.equals("all")) {
                resList = logEventSexRepository
                        .getSessionLogSexUserCntTrendALL(fUserSceneVersionIdSex,fMakeVersionIdSex);
            } else {
                String [] sexArr = fSex.split(",");
                for (int i = 0; i < sexArr.length; i++) {
                    sexList.add(sexArr[i]);
                }
                LOGGER.info("sexList:" + sexList);
                resList = logEventSexRepository
                        .getSessionLogSexUserCntTrend(fUserSceneVersionIdSex,fMakeVersionIdSex, sexList);
            }

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取session分性别用户趋势查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session分性别用户趋势查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session分性别用户趋势查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取分性别session趋势查询
     *
     * @return
     */

    @GetMapping("/getSessionLogSexSessionCntTrend")
    public BaseRsp getSessionLogSexSessionCntTrend(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_sex", required = false) String fSex) {
        LOGGER.info("获取分性别session趋势查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        List<Map<String, Object>> resListZHE = new ArrayList<>();
        List<List> list = new ArrayList<>();
        List<String> sexList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");


            if (fSex.equals("all")) {
                resList = logEventSexRepository
                        .getSessionLogSexSessionCntTrendALL(fUserSceneVersionId,fMakeVersionId);
                resListZHE = logEventSexRepository
                        .getLogSexSessionCntTrendALLZHE(fUserSceneVersionId,fMakeVersionId);
            } else {
                String [] sexArr = fSex.split(",");
                for (int i = 0; i < sexArr.length; i++) {
                    sexList.add(sexArr[i]);
                }
                LOGGER.info("sexList:" + sexList);
                resList = logEventSexRepository
                        .getSessionLogSexSessionCntTrend(fUserSceneVersionId,fMakeVersionId, sexList);
                resListZHE = logEventSexRepository
                        .getSessionLogSexSessionCntTrendZHE(fUserSceneVersionId,fMakeVersionId, sexList);
            }
            list.add(resList);
            list.add(resListZHE);
            rspPage.list = list;
            rspPage.total = total;
            baseRsp.setMessage("获取分性别session趋势查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分性别session趋势查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分性别session趋势查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取分性别pv趋势查询
     *
     * @return
     */

    @GetMapping("/getSessionLogSexPvCntTrend")
    public BaseRsp getSessionLogSexPvCntTrend(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_sex", required = false) String fSex) {
        LOGGER.info("获取分性别pv趋势查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        List<String> sexList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            if (fSex.equals("all")) {
                resList = logEventSexRepository
                        .getSessionLogSexPvCntTrendALL(fUserSceneVersionId,fMakeVersionId);
            } else {
                String [] sexArr = fSex.split(",");
                for (int i = 0; i < sexArr.length; i++) {
                    sexList.add(sexArr[i]);
                }
                LOGGER.info("sexList:" + sexList);
                resList = logEventSexRepository
                        .getSessionLogSexPvCntTrend(fUserSceneVersionId,fMakeVersionId, sexList);
            }

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取分性别pv趋势查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分性别pv趋势查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分性别pv趋势查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取分性别session分布查询 （25  50  75  最大数  平均数）
     *
     * @return
     */

    @GetMapping("/getLogSessionSexDistribute")
    public BaseRsp getLogSessionSexDistribute(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取分性别session分布查询");
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
                    .getLogDimSessionDistribute(fUserSceneVersionId,fMakeVersionId,"sex");
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取分性别session分布查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分性别session分布查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分性别session分布查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取分性别session-pv分布查询（ 25  50  75  最大数  平均数）
     *
     * @return
     */

    @GetMapping("/getLogPVSexDistribute")
    public BaseRsp getLogPVSexDistribute(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "20") Integer pageSize) {
        LOGGER.info("获取分性别session-pv分布查询");
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
                    .getLogDimPVDistribute(fUserSceneVersionId, fMakeVersionId, "sex", pageSize, limit);

            total = logEventPVDistributeRepository
                    .getLogDimPVDistributeCount(fUserSceneVersionId, fMakeVersionId, "sex");


            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取分性别session-pv分布查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分性别session-pv分布查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分性别session-pv分布查询失败");
        }
        return baseRsp;
    }


}
