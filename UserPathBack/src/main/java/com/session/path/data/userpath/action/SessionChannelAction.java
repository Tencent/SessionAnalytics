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
import com.session.path.data.userpath.repository.LogEventChannelRepository;
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
 * @ClassName SessionChannelAction
 * @Description session数据查询(渠道)
 * @Author author
 * @Date 2023/03/04 16:34
 * @Version 1.0
 **/
@Controller
@CrossOrigin
@RestController
@RequestMapping("/channel")
public class SessionChannelAction extends BaseAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(SessionChannelAction.class);

    @Autowired
    LogEventChannelRepository logEventChannelRepository;

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;

    @Autowired
    LogEventPVDistributeRepository logEventPVDistributeRepository;

    @Autowired
    LogEventSessionDistributeRepository logEventSessionDistributeRepository;


    /**
     * 获取session渠道列表
     *
     * @return
     */

    @GetMapping("/getSessionLogChannelList")
    public BaseRsp getSessionLogChannelList(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取session渠道查询");
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


            resList = logEventChannelRepository.getSessionLogChannelList(fUserSceneVersionId,fMakeVersionId);

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取session渠道查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session渠道查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session渠道查询失败");
        }
        return baseRsp;
    }


    /**
     * 获取session渠道分布
     *
     * @return
     */

    @GetMapping("/getSessionLogCntByChannel")
    public BaseRsp getSessionLogCntByChannel(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取session渠道分布查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resListPVNumChannel = new ArrayList<>();
        List<Map<String, Object>> resListSessionNumChannel = new ArrayList<>();
        List<Map<String, Object>> resListUserNumChannel = new ArrayList<>();
        List<List> list = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");


            resListPVNumChannel = logEventChannelRepository
                    .getSessionLogCntByChannelPVNum(fUserSceneVersionId,fMakeVersionId);
            resListSessionNumChannel = logEventChannelRepository
                    .getSessionLogCntByChannelSessionNum(fUserSceneVersionId,fMakeVersionId);
            resListUserNumChannel = logEventChannelRepository
                    .getSessionLogCntByChannelUserNum(fUserSceneVersionId,fMakeVersionId);

            list.add(resListPVNumChannel);
            list.add(resListSessionNumChannel);
            list.add(resListUserNumChannel);

            rspPage.list = list;
            rspPage.total = total;
            baseRsp.setMessage("获取session渠道分布查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session渠道分布查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session渠道分布查询失败");
        }
        return baseRsp;
    }


    /**
     * (总用户数 趋势)分渠道
     *
     * @return
     */

    @GetMapping("/getSessionLogChannelUserCntTrend")
    public BaseRsp getSessionLogChannelUserCntTrend(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_channel", required = false) String fChannel) {
        LOGGER.info("获取session分渠道用户趋势查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        List<String> channelList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionIdChannel = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionIdChannel = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");


            if (fChannel.equals("all")) {
                resList = logEventChannelRepository
                        .getSessionLogChannelUserCntTrendALL(fUserSceneVersionIdChannel,fMakeVersionIdChannel);
            } else {
                String [] channelArr = fChannel.split(",");
                for (int i = 0; i < channelArr.length; i++) {
                    channelList.add(channelArr[i]);
                }
                LOGGER.info("channelList:" + channelList);
                resList = logEventChannelRepository
                        .getSessionLogChannelUserCntTrend(
                                fUserSceneVersionIdChannel,fMakeVersionIdChannel, channelList);
            }

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取session分渠道用户趋势查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session分渠道用户趋势查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session分渠道用户趋势查询失败");
        }
        return baseRsp;
    }


    /**
     * (总session数 趋势)分渠道
     *
     * @return
     */

    @GetMapping("/getSessionLogChannelSessionCntTrend")
    public BaseRsp getSessionLogChannelSessionCntTrend(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_channel", required = false) String fChannel) {
        LOGGER.info("获取分渠道session趋势查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        List<Map<String, Object>> resListZHE = new ArrayList<>();
        List<List> list = new ArrayList<>();
        List<String> channelList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");


            if (fChannel.equals("all")) {
                resList = logEventChannelRepository
                        .getSessionLogChannelCntTrendALL(fUserSceneVersionId,fMakeVersionId);
                resListZHE = logEventChannelRepository
                        .getSessionLogChannelCntTrendALLZHE(fUserSceneVersionId,fMakeVersionId);
            } else {
                String [] channelArr = fChannel.split(",");
                for (int i = 0; i < channelArr.length; i++) {
                    channelList.add(channelArr[i]);
                }
                LOGGER.info("channelList:" + channelList);
                resList = logEventChannelRepository
                        .getSessionLogChannelSessionCntTrend(fUserSceneVersionId,fMakeVersionId, channelList);
                resListZHE = logEventChannelRepository
                        .getSessionLogChannelCntTrendZHE(fUserSceneVersionId,fMakeVersionId, channelList);
            }

            list.add(resList);
            list.add(resListZHE);
            rspPage.list = list;
            rspPage.total = total;
            baseRsp.setMessage("获取分渠道session趋势查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分渠道session趋势查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分渠道session趋势查询失败");
        }
        return baseRsp;
    }


    /**
     * (总pv数 趋势)分渠道
     *
     * @return
     */

    @GetMapping("/getSessionLogChannelPvCntTrend")
    public BaseRsp getSessionLogChannelPvCntTrend(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "f_channel", required = false) String fChannel) {
        LOGGER.info("获取分渠道pv趋势查询");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        List<String> channelList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            if (fChannel.equals("all")) {
                resList = logEventChannelRepository
                        .getSessionLogChannelPvCntTrendALL(fUserSceneVersionId,fMakeVersionId);
            } else {
                String [] channelArr = fChannel.split(",");
                for (int i = 0; i < channelArr.length; i++) {
                    channelList.add(channelArr[i]);
                }
                LOGGER.info("channelList:" + channelList);
                resList = logEventChannelRepository
                        .getSessionLogChannelPvCntTrend(fUserSceneVersionId,fMakeVersionId, channelList);
            }

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取分渠道pv趋势查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分渠道pv趋势查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分渠道pv趋势查询失败");
        }
        return baseRsp;
    }


    /**
     * session渠道分布    25  50  75  最大数  平均数
     *
     * @return
     */

    @GetMapping("/getLogSessionChannelDistribute")
    public BaseRsp getLogSessionChannelDistribute(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName) {
        LOGGER.info("获取分渠道session分布查询");
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
                    .getLogDimSessionDistribute(fUserSceneVersionId,fMakeVersionId,"channel");
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取分渠道session分布查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分渠道session分布查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分渠道session分布查询失败");
        }
        return baseRsp;
    }


    /**
     * session-pv渠道分布    25  50  75  最大数  平均数
     *
     * @return
     */

    @GetMapping("/getLogPVChannelDistribute")
    public BaseRsp getLogPVChannelDistribute(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "20") Integer pageSize) {
        LOGGER.info("获取分渠道session-pv分布查询");
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
                    .getLogDimPVDistribute(fUserSceneVersionId, fMakeVersionId, "channel", pageSize, limit);

            total = logEventPVDistributeRepository
                    .getLogDimPVDistributeCount(fUserSceneVersionId, fMakeVersionId, "channel");

            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取分渠道session-pv分布查询成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取分渠道session-pv分布查询失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取分渠道session-pv分布查询失败");
        }
        return baseRsp;
    }


}
