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
import com.session.path.data.userpath.repository.LogEventProvinceRepository;
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
 * @ClassName SessionProvinceAction
 * @Description session数据查询(省份)
 * @Author author
 * @Date 2023/03/04 16:34
 * @Version 1.0
 **/
@Controller
@CrossOrigin
@RestController
@RequestMapping("/province")
public class SessionProvinceAction extends BaseAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(SessionProvinceAction.class);

    @Autowired
    LogEventProvinceRepository logEventProvinceRepository;

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;

    @Autowired
    LogEventPVDistributeRepository logEventPVDistributeRepository;

    @Autowired
    LogEventSessionDistributeRepository logEventSessionDistributeRepository;


    /**
     * 获取session省份列表
     *
     * @return
     */

    @GetMapping("/getSessionLogProvinceList")
    public BaseRsp getSessionLogProvinceList(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取session省份查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName, fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName, fUploadUser).get("f_user_scene_version_id");

            resList = logEventProvinceRepository.getSessionLogProvinceList(fUserSceneVersionId, fMakeVersionId);

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取session省份查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session省份查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session省份查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取session省份分布（pv、session、user）
     *
     * @return
     */

    @GetMapping("/getSessionLogCntByProvince")
    public BaseRsp getSessionLogCntByProvince(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取session省份分布查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resListPVNum = new ArrayList<>();
        List<Map<String, Object>> resListSessionNum = new ArrayList<>();
        List<Map<String, Object>> resListAvgPVNum = new ArrayList<>();
        List<Map<String, Object>> resListAvgSessionNum = new ArrayList<>();
        List<Map<String, Object>> resListUserNum = new ArrayList<>();
        List<List> list = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName, fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName, fUploadUser).get("f_user_scene_version_id");

            resListPVNum = logEventProvinceRepository
                    .getSessionLogCntByProvincePVNum(fUserSceneVersionId, fMakeVersionId);
            resListSessionNum = logEventProvinceRepository
                    .getLogCntByProvinceSessionNum(fUserSceneVersionId, fMakeVersionId);
            resListUserNum = logEventProvinceRepository
                    .getSessionLogCntByProvinceUserNum(fUserSceneVersionId, fMakeVersionId);
            resListAvgPVNum = logEventProvinceRepository
                    .getSessionLogCntByProvinceAvgPVNum(fUserSceneVersionId, fMakeVersionId);
            resListAvgSessionNum = logEventProvinceRepository
                    .getLogCntByProvinceAvgSessionNum(fUserSceneVersionId, fMakeVersionId);

            list.add(resListPVNum);
            list.add(resListSessionNum);
            list.add(resListUserNum);
            list.add(resListAvgPVNum);
            list.add(resListAvgSessionNum);

            rspPage.list = list;
            rspPage.total = total;
            baseRsp.setMessage("获取session省份分布查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session省份分布查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session省份分布查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取session分省份用户趋势查询
     *
     * @return
     */

    @GetMapping("/getSessionLogProvinceUserCntTrend")
    public BaseRsp getSessionLogProvinceUserCntTrend(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_province", required = false) String fProvince) {
        LOGGER.info("获取session分省份用户趋势查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        List<String> provinceList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName, fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName, fUploadUser).get("f_user_scene_version_id");

            if (fProvince.equals("all")) {
                resList = logEventProvinceRepository
                        .getLogProvinceUserCntTrendALL(fUserSceneVersionId, fMakeVersionId);
            } else {
                String[] provinceArr = fProvince.split(",");
                for (int i = 0; i < provinceArr.length; i++) {
                    provinceList.add(provinceArr[i]);
                }
                LOGGER.info("provinceList:" + provinceList);

                resList = logEventProvinceRepository
                        .getSessionLogProvinceUserCntTrend(fUserSceneVersionId, fMakeVersionId, provinceList);

            }
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取session分省份用户趋势查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session分省份用户趋势查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session分省份用户趋势查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取分省份session趋势查询
     *
     * @return
     */

    @GetMapping("/getProvinceSessionCntTrend")
    public BaseRsp getProvinceSessionCntTrend(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_province", required = false) String fProvince) {
        LOGGER.info("获取分省份session趋势查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        List<Map<String, Object>> resListZHE = new ArrayList<>();
        List<List> list = new ArrayList<>();
        List<String> provinceList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName, fUploadUser).get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName, fUploadUser).get("f_user_scene_version_id");

            if (fProvince.equals("all")) {
                resList = logEventProvinceRepository
                        .getLogProvinceSessionCntTrendALL(fUserSceneVersionId, fMakeVersionId);
                resListZHE = logEventProvinceRepository
                        .getLogProvinceCntTrendALLZHE(fUserSceneVersionId, fMakeVersionId);
            } else {
                String[] provinceArr = fProvince.split(",");
                for (int i = 0; i < provinceArr.length; i++) {
                    provinceList.add(provinceArr[i]);
                }
                LOGGER.info("provinceList:" + provinceList);

                resList = logEventProvinceRepository
                        .getLogProvinceSessionCntTrend(fUserSceneVersionId, fMakeVersionId, provinceList);
                resListZHE = logEventProvinceRepository
                        .getLogProvinceSessionCntTrendZHE(fUserSceneVersionId, fMakeVersionId, provinceList);

            }
            list.add(resList);
            list.add(resListZHE);
            rspPage.list = list;
            rspPage.total = total;
            baseRsp.setMessage("获取分省份session趋势查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分省份session趋势查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分省份session趋势查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取分省份pv趋势查询
     *
     * @return
     */

    @GetMapping("/getSessionLogProvincePvCntTrend")
    public BaseRsp getSessionLogProvincePvCntTrend(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_province", required = false) String fProvince) {
        LOGGER.info("获取分省份pv趋势查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        List<String> provinceList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName, fUploadUser).get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName, fUploadUser).get("f_user_scene_version_id");

            if (fProvince.equals("all")) {
                resList = logEventProvinceRepository
                        .getSessionLogProvincePvCntTrendALL(fUserSceneVersionId, fMakeVersionId);
            } else {
                String[] provinceArr = fProvince.split(",");
                for (int i = 0; i < provinceArr.length; i++) {
                    provinceList.add(provinceArr[i]);
                }
                LOGGER.info("provinceList:" + provinceList);

                resList = logEventProvinceRepository
                        .getSessionLogProvincePvCntTrend(fUserSceneVersionId, fMakeVersionId, provinceList);

            }
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取分省份pv趋势查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分省份pv趋势查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分省份pv趋势查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取分省份session分布查询 (25  50  75  最大数  平均数)
     *
     * @return
     */

    @GetMapping("/getLogSessionProvinceDistribute")
    public BaseRsp getLogSessionProvinceDistribute(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取分省份session分布查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName, fUploadUser).get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName, fUploadUser).get("f_user_scene_version_id");

            resList = logEventSessionDistributeRepository
                    .getLogDimSessionDistribute(fUserSceneVersionId, fMakeVersionId, "province");
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取分省份session分布查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分省份session分布查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分省份session分布查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取分省份session-pv分布查询(25  50  75  最大数  平均数)
     *
     * @return
     */

    @GetMapping("/getLogPVProvinceDistribute")
    public BaseRsp getLogPVProvinceDistribute(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "20") Integer pageSize) {
        LOGGER.info("获取分省份session-pv分布查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        int total = 0;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName, fUploadUser).get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName, fUploadUser).get("f_user_scene_version_id");

            Integer limit = (page - 1) * pageSize;
            resList = logEventPVDistributeRepository
                    .getLogDimPVDistribute(fUserSceneVersionId, fMakeVersionId, "province", pageSize, limit);

            total = logEventPVDistributeRepository
                    .getLogDimPVDistributeCount(fUserSceneVersionId, fMakeVersionId, "province");

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取分省份session-pv分布查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分省份session-pv分布查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分省份session-pv分布查询失败");
        }
        return baseRsp;
    }


}
