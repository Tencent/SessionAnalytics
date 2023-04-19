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
import com.session.path.data.userpath.repository.SessionSingleNetworkRepository;
import com.session.path.data.userpath.repository.SessionSingleRepository;
import com.session.path.data.userpath.repository.UserLogClusterAnalysisRepository;
import com.session.path.data.userpath.repository.UserLogClusterKRepository;
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
 * @ClassName SessionClusterAction
 * @Description session聚类分析
 * @Author author
 * @Date 2023/03/04 16:34
 * @Version 1.0
 **/
@Controller
@CrossOrigin
@RestController
@RequestMapping("/cluster")
public class SessionClusterAction extends BaseAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(SessionClusterAction.class);

    @Autowired
    UserLogClusterAnalysisRepository userLogClusterAnalysisRepository;

    @Autowired
    UserLogClusterKRepository userLogClusterKRepository;

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;

    @Autowired
    SessionSingleRepository sessionSingleRepository;

    @Autowired
    SessionSingleNetworkRepository sessionSingleNetworkRepository;



    /**
     * 获取用户聚类分析K值趋势
     *
     * @return
     */

    @GetMapping("/getUserLogClusterK")
    public BaseRsp getUserLogClusterK(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_cluster_type", required = false) String fClusterType
    ) {
        LOGGER.info("获取用户聚类分析K值趋势");
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

            resList = userLogClusterKRepository
                    .getUserLogClusterK(fUserSceneVersionId,fMakeVersionId, fClusterType);

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取用户聚类分析K值趋势成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取用户聚类分析K值趋势失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取用户聚类分析K值趋势失败");
        }
        return baseRsp;
    }


    /**
     * 获取用户聚类分析日志
     *
     * @return
     */

    @GetMapping("/getUserLogClusterAnalysis")
    public BaseRsp getUserLogClusterAnalysis(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_cluster_type", required = false) String fClusterType,
            @RequestParam(value = "f_label", required = false) String fLabel
    ) {
        LOGGER.info("获取用户聚类分析日志");
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

            resList = userLogClusterAnalysisRepository
                    .getUserLogClusterAnalysis(fUserSceneVersionId,fMakeVersionId, fClusterType, fLabel);

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取用户聚类分析日志成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取用户聚类分析日志失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取用户聚类分析日志失败");
        }
        return baseRsp;
    }


    /**
     * 获取用户聚类占比
     *
     * @return
     */

    @GetMapping("/getUserLogClusterDistribute")
    public BaseRsp getUserLogClusterAnalysis(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_cluster_type", required = false) String fClusterType
    ) {
        LOGGER.info("获取用户聚类占比");
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

            resList = userLogClusterAnalysisRepository
                    .getUserLogClusterDistribute(fUserSceneVersionId,fMakeVersionId, fClusterType);
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取用户聚类占比成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取用户聚类占比失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取用户聚类占比失败");
        }
        return baseRsp;
    }


    /**
     * 获取用户聚类详情统计
     *
     * @return
     */

    @GetMapping("/getUserLogClusterDetail")
    public BaseRsp getUserLogClusterDetail(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_label", required = false) String fLabel
    ) {
        LOGGER.info("获取用户聚类详情统计");
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

            resList = userLogClusterAnalysisRepository
                    .getUserLogClusterDetail(fUserSceneVersionId,fMakeVersionId,fLabel);
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取用户聚类详情统计成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取用户聚类详情统计失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取用户聚类详情统计失败");
        }
        return baseRsp;
    }


    /**
     * 获取用户dtw聚类示例
     *
     * @return
     */

    @GetMapping("/getUserLogClusterDetailSample")
    public BaseRsp getUserLogClusterDetailSample(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_cluster_type", required = false) String fClusterType,
            @RequestParam(value = "f_label", required = false) String fLabel
    ) {
        LOGGER.info("获取用户dtw聚类示例");
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

            resList = userLogClusterAnalysisRepository
                    .getUserLogClusterDetailSample(fUserSceneVersionId,fMakeVersionId, fClusterType, fLabel);
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取用户dtw聚类示例成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取用户dtw聚类示例失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取用户dtw聚类示例失败");
        }
        return baseRsp;
    }




    /**
     * 获取用户kmeans聚类示例(场景pv)
     *
     * @return
     */

    @GetMapping("/getUserLogClusterDetailCategory")
    public BaseRsp getUserLogClusterDetailCategory(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_cluster_type", required = false) String fClusterType,
            @RequestParam(value = "f_label", required = false) String fLabel
    ) {
        LOGGER.info("获取用户kmeans聚类示例(场景pv)");
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

            resList = userLogClusterAnalysisRepository
                    .getUserLogClusterDetailCategory(fUserSceneVersionId,fMakeVersionId, fClusterType, fLabel);
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取用户kmeans聚类示例(场景pv)成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取用户kmeans聚类示例(场景pv)失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取用户kmeans聚类示例(场景pv)失败");
        }
        return baseRsp;
    }




    /**
     * 获取中心性计算值
     *
     * @return
     */

    @GetMapping("/getUserLogSingleNetwork")
    public BaseRsp getUserLogSingleNetwork(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName
    ) {
        LOGGER.info("获取中心性计算值");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<List> resList = new ArrayList<>();
        List<Map<String, Object>> resListSingle = new ArrayList<>();
        List<Map<String, Object>> resListNetwork = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resListSingle = sessionSingleRepository
                    .getSessionSingle(fUserSceneVersionId,fMakeVersionId);

            resListNetwork = sessionSingleNetworkRepository
                    .getSessionSingleNetwork(fUserSceneVersionId,fMakeVersionId);

            resList.add(resListSingle);
            resList.add(resListNetwork);

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取中心性计算值成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取中心性计算值失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取中心性计算值失败");
        }
        return baseRsp;
    }


}
