/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.action;

import com.session.path.data.userpath.dto.*;
import com.session.path.data.userpath.repository.*;
import com.session.path.data.userpath.util.BizCode;
import com.session.path.data.userpath.util.CommonUtil;
import com.session.path.data.userpath.util.CsvExportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SessionSankeyAction
 * @Description 桑基图用户路径分析
 * @Author author
 * @Date 2023/03/04 16:34
 * @Version 1.0
 **/
@Controller
@CrossOrigin
@RestController
@RequestMapping("/sankey")
public class SessionSankeyAction extends BaseAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(SessionSankeyAction.class);

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;

    @Autowired
    SessionEventRepository sessionEventRepository;

    @Autowired
    SessionDistinctEventRepository sessionDistinctEventRepository;


    @Autowired
    SessionNodeRepository sessionNodeRepository;

    @Autowired
    SessionSingleRepository sessionSingleRepository;


    /**
     * 获取桑基图页面事件分析结果（不去重）
     *
     * @return
     */

    @GetMapping("/getSessionEventEntity")
    public BaseRsp getSessionEventEntity(HttpServletRequest httpServletRequest,
                                        @RequestParam("from_layer") int fromLayer,
                                        @RequestParam("to_layer") int toLayer,
                                        @RequestParam("f_session_num_start") int fSessionNumStart,
                                        @RequestParam("f_session_num_end") int fSessionNumEnd,
                                        @RequestParam("f_pv_num_start") int fPVNumStart,
                                        @RequestParam("f_pv_num_end") int fPVNumEnd,
                                        @RequestParam("f_user_num_start") int fUserNumStart,
                                        @RequestParam("f_user_num_end") int fUserNumEnd,
                                        @RequestParam("f_category_from") String fCategoryFrom,
                                        @RequestParam("f_category") String fCategory,
                                        @RequestParam("f_subcategory_from") String fSubcategoryFrom,
                                        @RequestParam("f_subcategory") String fSubcategory,
                                        @RequestParam("f_event_from") String fEventFrom,
                                        @RequestParam("f_event") String fEvent,
                                        @RequestParam("f_upload_name") String fUploadName,
                                        @RequestParam("f_upload_user") String fUploadUser
    ) {
        LOGGER.info("获取session event");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser)
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser)
                    .get("f_user_scene_version_id");

            String [] categoryArr = fCategoryFrom.split(",");

            String [] subcategoryArr = fSubcategoryFrom.split(",");

            String [] eventArr = fEventFrom.split(",");

            resList = sessionEventRepository
                    .getSessionEventEntity(
                            fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],eventArr[0],
                            fromLayer,
                            toLayer,
                            fSessionNumStart,
                            fCategory,
                            fSubcategory,
                            fEvent,
                            fSessionNumEnd,
                            fUserNumStart,
                            fUserNumEnd,
                            fPVNumStart,
                            fPVNumEnd,
                            categoryArr[1],subcategoryArr[1],eventArr[1],
                            categoryArr[2],subcategoryArr[2],eventArr[2],
                            categoryArr[3],subcategoryArr[3],eventArr[3],
                            categoryArr[4],subcategoryArr[4],eventArr[4]);

            rspPage.list = resList;
            baseRsp.setMessage("获取session event成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event失败");
        }
        return baseRsp;
    }

    /**
     * 获取桑基图页面事件分析结果（不去重）
     *
     * @return
     */

    @PostMapping("/getSessionDimEventDetailEntity")
    public BaseRsp getSessionDimEventDetailEntity(HttpServletRequest httpServletRequest,
                                                  @RequestBody ReqSankeyDimEventDto reqSankeyEventDim
    ) {
        LOGGER.info("获取session event");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionIdDim = userLogUploadStatusRepository
                    .getMakeVersionId(reqSankeyEventDim.getFUploadName(),reqSankeyEventDim.getFUploadUser())
                    .get("f_make_version_id");
            String fUserSceneVersionIdDim = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqSankeyEventDim.getFUploadName(),reqSankeyEventDim.getFUploadUser())
                    .get("f_user_scene_version_id");

            String [] categoryArr = reqSankeyEventDim.getFCategoryFrom().split(",");
            String [] categoryArrTmp = reqSankeyEventDim.getFCategoryFrom().split(",");
            String [] subcategoryArr = reqSankeyEventDim.getFSubcategoryFrom().split(",");
            String [] subcategoryTmp = reqSankeyEventDim.getFSubcategoryFrom().split(",");
            String [] eventArr = reqSankeyEventDim.getFEventFrom().split(",");

            String [] categoryTo = reqSankeyEventDim.getFCategoryFrom().split(",");

            String [] subcategoryTo = reqSankeyEventDim.getFSubcategoryFrom().split(",");

            String [] eventTo = reqSankeyEventDim.getFEventFrom().split(",");

            if (reqSankeyEventDim.getFDim().equals("age") && reqSankeyEventDim.getFClick().equals("from")) {
                resList = sessionEventRepository
                        .getSessionEventSankeyCntByAgeFrom(fUserSceneVersionIdDim,
                                fMakeVersionIdDim,
                                categoryArr[0], subcategoryArr[0],eventArr[0], reqSankeyEventDim.getFCategory(),
                                reqSankeyEventDim.getFSubcategory(),
                                reqSankeyEventDim.getFEvent(),
                                categoryArr[1],subcategoryArr[1],eventArr[1],
                                categoryArr[2],subcategoryArr[2],eventArr[2],
                                categoryArr[3],subcategoryArr[3],eventArr[3],
                                categoryArr[4],subcategoryArr[4],eventArr[4],
                                reqSankeyEventDim.getFEventFromA());

            } else if (reqSankeyEventDim.getFDim().equals("sex") && reqSankeyEventDim.getFClick().equals("from")) {
                resList = sessionEventRepository
                        .getSessionEventSankeyCntBySexFrom(fUserSceneVersionIdDim,
                                fMakeVersionIdDim, categoryArrTmp[0],
                                subcategoryArr[0],eventArr[0],
                                reqSankeyEventDim.getFCategory(), reqSankeyEventDim.getFSubcategory(),
                                reqSankeyEventDim.getFEvent(),
                                categoryArrTmp[1],subcategoryArr[1],eventArr[1],
                                categoryArrTmp[2],subcategoryArr[2],eventArr[2],
                                categoryArrTmp[3],subcategoryArr[3],eventArr[3],
                                categoryArrTmp[4],subcategoryArr[4],eventArr[4],
                                reqSankeyEventDim.getFEventFromA());

            } else if (reqSankeyEventDim.getFDim().equals("province") && reqSankeyEventDim.getFClick().equals("from")) {
                resList = sessionEventRepository
                        .getEventSankeyCntByProvFrom(fUserSceneVersionIdDim,
                                fMakeVersionIdDim, categoryArr[0], subcategoryArr[0],eventArr[0],
                                reqSankeyEventDim.getFCategory(), reqSankeyEventDim.getFSubcategory(),
                                reqSankeyEventDim.getFEvent(),
                                categoryArr[1],subcategoryArr[1],eventArr[1],
                                categoryArr[2],subcategoryArr[2],eventArr[2],
                                categoryArr[3],subcategoryArr[3],eventArr[3],
                                categoryArr[4],subcategoryArr[4],eventArr[4],
                                reqSankeyEventDim.getFEventFromA());

            } else if (reqSankeyEventDim.getFDim().equals("channel") && reqSankeyEventDim.getFClick().equals("from")) {
                resList = sessionEventRepository
                        .getEventSankeyCntByChannelFrom(fUserSceneVersionIdDim,
                                fMakeVersionIdDim, categoryArr[0], subcategoryTmp[0],eventArr[0],
                                reqSankeyEventDim.getFCategory(), reqSankeyEventDim.getFSubcategory(),
                                reqSankeyEventDim.getFEvent(),
                                categoryArr[1],subcategoryTmp[1],eventArr[1],
                                categoryArr[2],subcategoryTmp[2],eventArr[2],
                                categoryArr[3],subcategoryTmp[3],eventArr[3],
                                categoryArr[4],subcategoryTmp[4],eventArr[4],
                                reqSankeyEventDim.getFEventFromA());


            } else if (reqSankeyEventDim.getFDim().equals("age") && reqSankeyEventDim.getFClick().equals("to")) {
                resList = sessionEventRepository
                        .getSessionEventSankeyCntByAgeTo(fUserSceneVersionIdDim,
                                fMakeVersionIdDim, categoryTo[0], subcategoryTo[0], eventTo[0],
                                reqSankeyEventDim.getFCategory(), reqSankeyEventDim.getFSubcategory(),
                                reqSankeyEventDim.getFEvent(),
                                categoryTo[1],subcategoryTo[1],eventTo[1],
                                categoryTo[2],subcategoryTo[2],eventTo[2],
                                categoryTo[3],subcategoryTo[3],eventTo[3],
                                categoryTo[4],subcategoryTo[4],eventTo[4],
                                reqSankeyEventDim.getFEventFromA(),reqSankeyEventDim.getFEventToA());


            } else if (reqSankeyEventDim.getFDim().equals("sex") && reqSankeyEventDim.getFClick().equals("to")) {
                resList = sessionEventRepository
                        .getSessionEventSankeyCntBySexTo(fUserSceneVersionIdDim,
                                fMakeVersionIdDim, categoryTo[0], subcategoryTmp[0], eventTo[0],
                                reqSankeyEventDim.getFCategory(), reqSankeyEventDim.getFSubcategory(),
                                reqSankeyEventDim.getFEvent(),
                                categoryTo[1],subcategoryTmp[1],eventTo[1],
                                categoryTo[2],subcategoryTmp[2],eventTo[2],
                                categoryTo[3],subcategoryTmp[3],eventTo[3],
                                categoryTo[4],subcategoryTmp[4],eventTo[4],
                                reqSankeyEventDim.getFEventFromA(),reqSankeyEventDim.getFEventToA());

            } else if (reqSankeyEventDim.getFDim().equals("province") && reqSankeyEventDim.getFClick().equals("to")) {
                resList = sessionEventRepository
                        .getEventSankeyCntByProvTo(fUserSceneVersionIdDim,
                                fMakeVersionIdDim,
                                categoryTo[0], subcategoryTo[0], eventTo[0],
                                reqSankeyEventDim.getFCategory(), reqSankeyEventDim.getFSubcategory(),
                                reqSankeyEventDim.getFEvent(),
                                categoryTo[1],subcategoryTo[1],eventTo[1],
                                categoryTo[2],subcategoryTo[2],eventTo[2],
                                categoryTo[3],subcategoryTo[3],eventTo[3],
                                categoryTo[4],subcategoryTo[4],eventTo[4],
                                reqSankeyEventDim.getFEventFromA(),reqSankeyEventDim.getFEventToA());

            } else if (reqSankeyEventDim.getFDim().equals("channel") && reqSankeyEventDim.getFClick().equals("to")) {

                resList = sessionEventRepository
                        .getSessionEventSankeyCntByChannelTo(fUserSceneVersionIdDim,
                                fMakeVersionIdDim, categoryArrTmp[0], subcategoryTo[0], eventTo[0],
                                reqSankeyEventDim.getFCategory(),
                                reqSankeyEventDim.getFSubcategory(),
                                reqSankeyEventDim.getFEvent(),
                                categoryArrTmp[1],subcategoryTo[1],eventTo[1],
                                categoryArrTmp[2],subcategoryTo[2],eventTo[2],
                                categoryArrTmp[3],subcategoryTo[3],eventTo[3],
                                categoryArrTmp[4],subcategoryTo[4],eventTo[4],
                                reqSankeyEventDim.getFEventFromA(),reqSankeyEventDim.getFEventToA());

            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session event成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event失败");
        }
        return baseRsp;
    }







    /**
     * 获取桑基图页面事件分析结果（分维度去重）
     *
     * @return
     */

    @PostMapping("/getSessionDimEventDistinctEntity")
    public BaseRsp getSessionDimEventDistinctEntity(HttpServletRequest httpServletRequest,
                                                    @RequestBody ReqSankeyDimEventDto reqSankeyEventDimD

    ) {
        LOGGER.info("获取session event dim distinct 分析");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqSankeyEventDimD.getFUploadName(),reqSankeyEventDimD.getFUploadUser())
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqSankeyEventDimD.getFUploadName(),reqSankeyEventDimD.getFUploadUser())
                    .get("f_user_scene_version_id");

            String [] categoryArr = reqSankeyEventDimD.getFCategoryFrom().split(",");

            String [] subcategoryArr = reqSankeyEventDimD.getFSubcategoryFrom().split(",");

            String [] eventArr = reqSankeyEventDimD.getFEventFrom().split(",");


            String [] categoryDis = reqSankeyEventDimD.getFCategoryFrom().split(",");

            String [] subcategoryDis = reqSankeyEventDimD.getFSubcategoryFrom().split(",");

            String [] eventDis = reqSankeyEventDimD.getFEventFrom().split(",");


            if (reqSankeyEventDimD.getFDim().equals("age")  && reqSankeyEventDimD.getFClick().equals("from")) {
                resList = sessionDistinctEventRepository
                        .getDEventSankeyCntByAgeFrom(fUserSceneVersionId,
                                fMakeVersionId, categoryArr[0], subcategoryArr[0],eventArr[0],
                                reqSankeyEventDimD.getFCategory(), reqSankeyEventDimD.getFSubcategory(),
                                reqSankeyEventDimD.getFEvent(),
                                categoryArr[1],subcategoryArr[1],eventArr[1],
                                categoryArr[2],subcategoryArr[2],eventArr[2],
                                categoryArr[3],subcategoryArr[3],eventArr[3],
                                categoryArr[4],subcategoryArr[4],eventArr[4],
                                reqSankeyEventDimD.getFEventFromA());

            } else if (reqSankeyEventDimD.getFDim().equals("sex")  && reqSankeyEventDimD.getFClick().equals("from")) {
                resList = sessionDistinctEventRepository
                        .getDEventSankeyCntBySexFrom(fUserSceneVersionId,fMakeVersionId,
                                categoryArr[0], subcategoryArr[0],eventArr[0],
                                reqSankeyEventDimD.getFCategory(), reqSankeyEventDimD.getFSubcategory(),
                                reqSankeyEventDimD.getFEvent(),
                                categoryArr[1],subcategoryArr[1],eventArr[1],
                                categoryArr[2],subcategoryArr[2],eventArr[2],
                                categoryArr[3],subcategoryArr[3],eventArr[3],
                                categoryArr[4],subcategoryArr[4],eventArr[4],
                                reqSankeyEventDimD.getFEventFromA());

            } else if (reqSankeyEventDimD.getFDim().equals("province")
                    && reqSankeyEventDimD.getFClick().equals("from")) {

                resList = sessionDistinctEventRepository
                        .getDEventSankeyCntByProFrom(fUserSceneVersionId,fMakeVersionId,
                                categoryArr[0], subcategoryArr[0],eventArr[0],
                                reqSankeyEventDimD.getFCategory(), reqSankeyEventDimD.getFSubcategory(),
                                reqSankeyEventDimD.getFEvent(),
                                categoryArr[1],subcategoryArr[1],eventArr[1],
                                categoryArr[2],subcategoryArr[2],eventArr[2],
                                categoryArr[3],subcategoryArr[3],eventArr[3],
                                categoryArr[4],subcategoryArr[4],eventArr[4],
                                reqSankeyEventDimD.getFEventFromA());

            } else if (reqSankeyEventDimD.getFDim().equals("channel")
                    && reqSankeyEventDimD.getFClick().equals("from")) {
                resList = sessionDistinctEventRepository
                        .getDEventSankeyCntByChannelFrom(fUserSceneVersionId,fMakeVersionId,
                                categoryArr[0], subcategoryArr[0], eventArr[0],
                                reqSankeyEventDimD.getFCategory(), reqSankeyEventDimD.getFSubcategory(),
                                reqSankeyEventDimD.getFEvent(),
                                categoryArr[1],subcategoryArr[1],eventArr[1],
                                categoryArr[2],subcategoryArr[2],eventArr[2],
                                categoryArr[3],subcategoryArr[3],eventArr[3],
                                categoryArr[4],subcategoryArr[4],eventArr[4],
                                reqSankeyEventDimD.getFEventFromA());


            } else if (reqSankeyEventDimD.getFDim().equals("age") && reqSankeyEventDimD.getFClick().equals("to")) {
                resList = sessionDistinctEventRepository
                        .getDEventSankeyCntByAgeTo(fUserSceneVersionId,fMakeVersionId,
                                categoryDis[0], subcategoryDis[0],eventDis[0],
                                reqSankeyEventDimD.getFCategory(),
                                reqSankeyEventDimD.getFSubcategory(),
                                reqSankeyEventDimD.getFEvent(),
                                categoryDis[1],subcategoryDis[1],eventDis[1],
                                categoryDis[2],subcategoryDis[2],eventDis[2],
                                categoryDis[3],subcategoryDis[3],eventDis[3],
                                categoryDis[4],subcategoryDis[4],eventDis[4],
                                reqSankeyEventDimD.getFEventFromA(),reqSankeyEventDimD.getFEventToA());


            } else if (reqSankeyEventDimD.getFDim().equals("sex") && reqSankeyEventDimD.getFClick().equals("to")) {

                resList = sessionDistinctEventRepository
                        .getDEventSankeyCntBySexTo(fUserSceneVersionId,fMakeVersionId,
                                categoryDis[0], subcategoryDis[0],eventDis[0],
                                reqSankeyEventDimD.getFCategory(), reqSankeyEventDimD.getFSubcategory(),
                                reqSankeyEventDimD.getFEvent(),
                                categoryDis[1],subcategoryDis[1],eventDis[1],
                                categoryDis[2],subcategoryDis[2],eventDis[2],
                                categoryDis[3],subcategoryDis[3],eventDis[3],
                                categoryDis[4],subcategoryDis[4],eventDis[4],
                                reqSankeyEventDimD.getFEventFromA(),reqSankeyEventDimD.getFEventToA());

            } else if (reqSankeyEventDimD.getFDim().equals("province") && reqSankeyEventDimD.getFClick().equals("to")) {

                resList = sessionDistinctEventRepository
                        .getDEventSankeyCntByProTo(fUserSceneVersionId, fMakeVersionId,
                                categoryDis[0], subcategoryDis[0],eventDis[0],
                                reqSankeyEventDimD.getFCategory(),
                                reqSankeyEventDimD.getFSubcategory(),
                                reqSankeyEventDimD.getFEvent(),
                                categoryDis[1],subcategoryDis[1],eventDis[1],
                                categoryDis[2],subcategoryDis[2],eventDis[2],
                                categoryDis[3],subcategoryDis[3],eventDis[3],
                                categoryDis[4],subcategoryDis[4],eventDis[4],
                                reqSankeyEventDimD.getFEventFromA(),reqSankeyEventDimD.getFEventToA());

            } else if (reqSankeyEventDimD.getFDim().equals("channel") && reqSankeyEventDimD.getFClick().equals("to")) {

                resList = sessionDistinctEventRepository
                        .getDEventSankeyCntByChannelTo(fUserSceneVersionId, fMakeVersionId,
                                categoryDis[0], subcategoryDis[0],eventDis[0],
                                reqSankeyEventDimD.getFCategory(), reqSankeyEventDimD.getFSubcategory(),
                                reqSankeyEventDimD.getFEvent(),
                                categoryDis[1],subcategoryDis[1],eventDis[1],
                                categoryDis[2],subcategoryDis[2],eventDis[2],
                                categoryDis[3],subcategoryDis[3],eventDis[3],
                                categoryDis[4],subcategoryDis[4],eventDis[4],
                                reqSankeyEventDimD.getFEventFromA(),reqSankeyEventDimD.getFEventToA());

            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session event dim distinct 分析成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event dim distinct 分析失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event dim distinct 分析失败");
        }
        return baseRsp;
    }

    /**
     * 获取桑基图大类筛选列表及示例图结果-无层级
     *
     * @return
     */
    @GetMapping("/getSessionEventCategorySample")
    public BaseRsp getSessionEventCategorySample(HttpServletRequest httpServletRequest,
                                                 @RequestParam("f_upload_name") String fUploadName,
                                                 @RequestParam("f_upload_user") String fUploadUser
    ) {
        LOGGER.info("获取session category(示例图)");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);

            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser)
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser)
                    .get("f_user_scene_version_id");

            LOGGER.info("fUserSceneVersionId:" + fUserSceneVersionId);


            if (fMakeVersionId == 0) {
                resList = sessionEventRepository.getSessionEventCategorySample(fUserSceneVersionId);

            } else {
                resList = sessionEventRepository
                        .getSessionEventCategoryMakeSample(fUserSceneVersionId,fMakeVersionId);

            }

            LOGGER.info("resList:" + resList);
            rspPage.list = resList;
            baseRsp.setMessage("获取session category(示例图)成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session category(示例图)失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session category(示例图)失败");
        }
        return baseRsp;
    }


    /**
     * 获取小类筛选列表-无层级
     *
     * @return
     */

    @GetMapping("/getSessionEventSubCategorySample")
    public BaseRsp getSessionEventSubCategorySample(HttpServletRequest httpServletRequest,
                                                    @RequestParam("f_upload_name") String fUploadName,
                                                    @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("获取subcategory 无层级");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);

            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser)
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser)
                    .get("f_user_scene_version_id");

            if (fMakeVersionId == 0) {
                resList = sessionEventRepository.getSessionEventSubCategorySample(fUserSceneVersionId);

            } else {
                resList = sessionEventRepository
                        .getEventSubMakeSample(fUserSceneVersionId,fMakeVersionId);

            }

            LOGGER.info("resList:" + resList);
            rspPage.list = resList;
            baseRsp.setMessage("获取subcategory 无层级成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取subcategory 无层级失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取subcategory 无层级失败");
        }
        return baseRsp;
    }


    /**
     * 获取页面筛选列表-无层级
     *
     * @return
     */

    @GetMapping("/getSessionEventSample")
    public BaseRsp getSessionEventSample(HttpServletRequest httpServletRequest,
                                         @RequestParam("f_upload_name") String fUploadName,
                                         @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("获取event 无层级");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser)
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser)
                    .get("f_user_scene_version_id");

            if (fMakeVersionId == 0) {
                resList = sessionEventRepository.getSessionEventSample(fUserSceneVersionId);

            } else {
                resList = sessionEventRepository
                        .getSessionEventMakeSample(fUserSceneVersionId,fMakeVersionId);
            }

            rspPage.list = resList;
            baseRsp.setMessage("获取event 无层级成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取event 无层级失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取event 无层级失败");
        }
        return baseRsp;
    }


    /**
     * 获取大类筛选列表-去重
     *
     * @return
     */

    @GetMapping("/getSessionEventCategoryList")
    public BaseRsp getSessionEventCategoryList(HttpServletRequest httpServletRequest,
                                               @RequestParam("f_upload_name") String fUploadName,
                                               @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("获取session category");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);

            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser)
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser)
                    .get("f_user_scene_version_id");

            resList = sessionEventRepository.getSessionEventCategoryList(fUserSceneVersionId,fMakeVersionId);
            rspPage.list = resList;
            baseRsp.setMessage("获取session category列表成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session category列表失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session category列表失败");
        }
        return baseRsp;
    }


    /**
     * 获取小类筛选列表-去重
     *
     * @return
     */

    @GetMapping("/getSessionEventSubCategoryList")
    public BaseRsp getSessionEventSubCategoryList(HttpServletRequest httpServletRequest,
                                                  @RequestParam("f_upload_name") String fUploadName,
                                                  @RequestParam("f_upload_user") String fUploadUser,
                                                  @RequestParam(value = "f_category_from", required = false) String fCategoryFrom
    ) {
        LOGGER.info("获取session subcategory列表");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);

            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser)
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser)
                    .get("f_user_scene_version_id");

            resList = sessionEventRepository
                    .getSessionEventSubCategoryList(fUserSceneVersionId,fMakeVersionId, fCategoryFrom);
            rspPage.list = resList;
            baseRsp.setMessage("获取session subcategory列表成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session subcategory列表失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session subcategory列表失败");
        }
        return baseRsp;
    }


    /**
     * 获取页面event筛选列表-去重
     *
     * @return
     */

    @GetMapping("/getSessionEventList")
    public BaseRsp getSessionEventList(HttpServletRequest httpServletRequest,
                                       @RequestParam("f_upload_name") String fUploadName,
                                       @RequestParam("f_upload_user") String fUploadUser,
                                       @RequestParam(value = "f_category_from", required = false) String fCategoryFrom,
                                       @RequestParam(value = "f_subcategory_from", required = false) String fSubcategoryFrom) {
        LOGGER.info("获取session event list");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser)
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser)
                    .get("f_user_scene_version_id");

            resList = sessionEventRepository
                    .getSessionEventList(fUserSceneVersionId,fMakeVersionId, fCategoryFrom, fSubcategoryFrom);
            rspPage.list = resList;
            baseRsp.setMessage("获取session event list成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event list失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event list失败");
        }
        return baseRsp;
    }


    /**
     * 获取桑基图小类分析结果-不去重
     *
     * @return
     */

    @GetMapping("/getSessionSubcategoryEntity")
    public BaseRsp getSessionSubcategoryEntity(HttpServletRequest httpServletRequest,
                                               @RequestParam("from_layer") int fromLayer,
                                               @RequestParam("to_layer") int toLayer,
                                               @RequestParam("f_session_num_start") int fSessionNumStart,
                                               @RequestParam("f_session_num_end") int fSessionNumEnd,
                                               @RequestParam("f_pv_num_start") int fPVNumStart,
                                               @RequestParam("f_pv_num_end") int fPVNumEnd,
                                               @RequestParam("f_user_num_start") int fUserNumStart,
                                               @RequestParam("f_user_num_end") int fUserNumEnd,
                                               @RequestParam("f_category_from") String fCategoryFrom,
                                               @RequestParam("f_category") String fCategory,
                                               @RequestParam("f_subcategory_from") String fSubcategoryFrom,
                                               @RequestParam("f_subcategory") String fSubcategory,
                                               @RequestParam("f_upload_name") String fUploadName,
                                               @RequestParam("f_upload_user") String fUploadUser
    ) {
        LOGGER.info("获取session Subcategory detail 分析");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser)
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser)
                    .get("f_user_scene_version_id");

            String [] categoryArr = fCategoryFrom.split(",");

            String [] subcategoryArr = fSubcategoryFrom.split(",");

            resList = sessionEventRepository
                    .getSessionSubcategoryEntity(
                            fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],
                            fromLayer, toLayer,
                            fSessionNumStart,
                            fCategory, fSubcategory,
                            fSessionNumEnd,
                            fUserNumStart,fUserNumEnd,
                            fPVNumStart,fPVNumEnd,
                            categoryArr[1],subcategoryArr[1],categoryArr[2],subcategoryArr[2],
                            categoryArr[3],subcategoryArr[3],categoryArr[4],subcategoryArr[4]);

            rspPage.list = resList;
            baseRsp.setMessage("获取session Subcategory detail 成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session Subcategory detail 失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session Subcategory detail 失败");
        }
        return baseRsp;
    }



    /**
     * 获取桑基图页面事件分析结果（分维度不去重）
     *
     * @return
     */

    @PostMapping("/getDimSubcategoryDetailEntity")
    public BaseRsp getDimSubcategoryDetailEntity(HttpServletRequest httpServletRequest,
                                                 @RequestBody ReqSankeyDimSubDto reqSubSankeyDim
    ) {
        LOGGER.info("获取session subcategory dim 分析");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqSubSankeyDim.getFUploadName(),reqSubSankeyDim.getFUploadUser())
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqSubSankeyDim.getFUploadName(),reqSubSankeyDim.getFUploadUser())
                    .get("f_user_scene_version_id");

            String [] categoryArr = reqSubSankeyDim.getFCategoryFrom().split(",");

            String [] subcategoryArr = reqSubSankeyDim.getFSubcategoryFrom().split(",");


            String [] categoryDim = reqSubSankeyDim.getFCategoryFrom().split(",");

            String [] subcategoryDim = reqSubSankeyDim.getFSubcategoryFrom().split(",");

            if (reqSubSankeyDim.getFDim().equals("age") && reqSubSankeyDim.getFClick().equals("from")) {
                resList = sessionEventRepository
                        .getSubSankeyCntByAgeFrom(
                                fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],
                                reqSubSankeyDim.getFCategory(), reqSubSankeyDim.getFSubcategory(),
                                categoryArr[1],subcategoryArr[1],
                                categoryArr[2],subcategoryArr[2],
                                categoryArr[3],subcategoryArr[3],
                                categoryArr[4],subcategoryArr[4],
                                reqSubSankeyDim.getFSubcategoryFromA());

            } else if (reqSubSankeyDim.getFDim().equals("sex") && reqSubSankeyDim.getFClick().equals("from")) {
                resList = sessionEventRepository
                        .getSubSankeyCntBySexFrom(
                                fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],
                                reqSubSankeyDim.getFCategory(), reqSubSankeyDim.getFSubcategory(),
                                categoryArr[1],subcategoryArr[1],
                                categoryArr[2],subcategoryArr[2],
                                categoryArr[3],subcategoryArr[3],
                                categoryArr[4],subcategoryArr[4],
                                reqSubSankeyDim.getFSubcategoryFromA());

            } else if (reqSubSankeyDim.getFDim().equals("province")
                    && reqSubSankeyDim.getFClick().equals("from")) {

                resList = sessionEventRepository
                        .getSubSankeyCntByProvFrom(
                                fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],
                                reqSubSankeyDim.getFCategory(), reqSubSankeyDim.getFSubcategory(),
                                categoryArr[1],subcategoryArr[1],
                                categoryArr[2],subcategoryArr[2],
                                categoryArr[3],subcategoryArr[3],
                                categoryArr[4],subcategoryArr[4],
                                reqSubSankeyDim.getFSubcategoryFromA());

            } else if (reqSubSankeyDim.getFDim().equals("channel")
                    && reqSubSankeyDim.getFClick().equals("from")) {
                resList = sessionEventRepository
                        .getSubSankeyCntByChannelFrom(
                                fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],
                                reqSubSankeyDim.getFCategory(), reqSubSankeyDim.getFSubcategory(),
                                categoryArr[1],subcategoryArr[1],
                                categoryArr[2],subcategoryArr[2],
                                categoryArr[3],subcategoryArr[3],
                                categoryArr[4],subcategoryArr[4],
                                reqSubSankeyDim.getFSubcategoryFromA());


            } else if (reqSubSankeyDim.getFDim().equals("age") && reqSubSankeyDim.getFClick().equals("to")) {
                resList = sessionEventRepository
                        .getSubSankeyCntByAgeTo(
                                fUserSceneVersionId,fMakeVersionId, categoryDim[0], subcategoryDim[0],
                                reqSubSankeyDim.getFCategory(), reqSubSankeyDim.getFSubcategory(),
                                categoryDim[1],subcategoryDim[1],
                                categoryDim[2],subcategoryDim[2],
                                categoryDim[3],subcategoryDim[3],
                                categoryDim[4],subcategoryDim[4],
                                reqSubSankeyDim.getFSubcategoryFromA(),reqSubSankeyDim.getFSubcategoryToA());


            } else if (reqSubSankeyDim.getFDim().equals("sex") && reqSubSankeyDim.getFClick().equals("to")) {

                resList = sessionEventRepository
                        .getSubSankeyCntBySexTo(
                                fUserSceneVersionId,fMakeVersionId, categoryDim[0], subcategoryDim[0],
                                reqSubSankeyDim.getFCategory(), reqSubSankeyDim.getFSubcategory(),
                                categoryDim[1],subcategoryDim[1],
                                categoryDim[2],subcategoryDim[2],
                                categoryDim[3],subcategoryDim[3],
                                categoryDim[4],subcategoryDim[4],
                                reqSubSankeyDim.getFSubcategoryFromA(),reqSubSankeyDim.getFSubcategoryToA());

            } else if (reqSubSankeyDim.getFDim().equals("province")
                    && reqSubSankeyDim.getFClick().equals("to")) {

                resList = sessionEventRepository
                        .getSubSankeyCntByProvTo(
                                fUserSceneVersionId,fMakeVersionId, categoryDim[0], subcategoryDim[0],
                                reqSubSankeyDim.getFCategory(), reqSubSankeyDim.getFSubcategory(),
                                categoryDim[1],subcategoryDim[1],
                                categoryDim[2],subcategoryDim[2],
                                categoryDim[3],subcategoryDim[3],
                                categoryDim[4],subcategoryDim[4],
                                reqSubSankeyDim.getFSubcategoryFromA(),reqSubSankeyDim.getFSubcategoryToA());

            } else if (reqSubSankeyDim.getFDim().equals("channel")
                    && reqSubSankeyDim.getFClick().equals("to")) {

                resList = sessionEventRepository
                        .getSubSankeyCntByChannelTo(
                                fUserSceneVersionId,fMakeVersionId, categoryDim[0], subcategoryDim[0],
                                reqSubSankeyDim.getFCategory(), reqSubSankeyDim.getFSubcategory(),
                                categoryDim[1],subcategoryDim[1],
                                categoryDim[2],subcategoryDim[2],
                                categoryDim[3],subcategoryDim[3],
                                categoryDim[4],subcategoryDim[4],
                                reqSubSankeyDim.getFSubcategoryFromA(),reqSubSankeyDim.getFSubcategoryToA());

            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session subcategory dim 成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session subcategory dim 失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session subcategory dim 失败");
        }
        return baseRsp;
    }


    /**
     * 获取桑基图大类分析结果-不去重
     *
     * @return
     */

    @GetMapping("/getSessionCategoryEntity")
    public BaseRsp getSessionCategoryEntity(HttpServletRequest httpServletRequest,
                                            @RequestParam("from_layer") int fromLayer,
                                            @RequestParam("to_layer") int toLayer,
                                            @RequestParam("f_session_num_start") int fSessionNumStart,
                                            @RequestParam("f_session_num_end") int fSessionNumEnd,
                                            @RequestParam("f_pv_num_start") int fPVNumStart,
                                            @RequestParam("f_pv_num_end") int fPVNumEnd,
                                            @RequestParam("f_user_num_start") int fUserNumStart,
                                            @RequestParam("f_user_num_end") int fUserNumEnd,
                                            @RequestParam("f_category_from") String fCategoryFrom,
                                            @RequestParam("f_category") String fCategory,
                                            @RequestParam("f_upload_name") String fUploadName,
                                            @RequestParam("f_upload_user") String fUploadUser
    ) {
        LOGGER.info("获取session Category");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser)
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser)
                    .get("f_user_scene_version_id");


            String [] categoryArr = fCategoryFrom.split(",");

            resList = sessionEventRepository
                    .getSessionCategoryEntity(fUserSceneVersionId,fMakeVersionId, categoryArr[0],
                            fromLayer, toLayer,
                            fSessionNumStart,
                            fCategory, fSessionNumEnd,
                            fUserNumStart,fUserNumEnd,
                            fPVNumStart,fPVNumEnd,
                            categoryArr[1],categoryArr[2],categoryArr[3],categoryArr[4]);

            rspPage.list = resList;
            baseRsp.setMessage("获取session Category成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session Category失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session Category失败");
        }
        return baseRsp;
    }


    /**
     * 获取桑基图页面event分析结果-去重
     *
     * @return
     */

    @GetMapping("/getSessionEventDistinctEntity")
    public BaseRsp getSessionEventDistinctEntity(HttpServletRequest httpServletRequest,
                                                 @RequestParam("from_layer") int fromLayer,
                                                 @RequestParam("to_layer") int toLayer,
                                                 @RequestParam("f_session_num_start") int fSessionNumStart,
                                                 @RequestParam("f_session_num_end") int fSessionNumEnd,
                                                 @RequestParam("f_pv_num_start") int fPVNumStart,
                                                 @RequestParam("f_pv_num_end") int fPVNumEnd,
                                                 @RequestParam("f_user_num_start") int fUserNumStart,
                                                 @RequestParam("f_user_num_end") int fUserNumEnd,
                                                 @RequestParam("f_category_from") String fCategoryFrom,
                                                 @RequestParam("f_category") String fCategory,
                                                 @RequestParam("f_subcategory_from") String fSubcategoryFrom,
                                                 @RequestParam("f_subcategory") String fSubcategory,
                                                 @RequestParam("f_event_from") String fEventFrom,
                                                 @RequestParam("f_event") String fEvent,
                                                 @RequestParam("f_upload_name") String fUploadName,
                                                 @RequestParam("f_upload_user") String fUploadUser
    ) {
        LOGGER.info("获取session event");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser)
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser)
                    .get("f_user_scene_version_id");


            String [] categoryArr = fCategoryFrom.split(",");

            String [] subcategoryArr = fSubcategoryFrom.split(",");

            String [] eventArr = fEventFrom.split(",");

            resList = sessionDistinctEventRepository
                    .getSessionEventDistinctEntity(
                            fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],eventArr[0],
                            fromLayer,toLayer,
                            fSessionNumStart,
                            fCategory,
                            fSubcategory,fEvent,
                            fSessionNumEnd,
                            fUserNumStart,fUserNumEnd,
                            fPVNumStart,
                            fPVNumEnd,
                            categoryArr[1],subcategoryArr[1],eventArr[1],
                            categoryArr[2],subcategoryArr[2],eventArr[2],
                            categoryArr[3],subcategoryArr[3],eventArr[3],
                            categoryArr[4],subcategoryArr[4],eventArr[4]);


            LOGGER.info("resList event" + resList);
            rspPage.list = resList;
            baseRsp.setMessage("获取session event成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event失败");
        }
        return baseRsp;
    }


    /**
     * 获取桑基图小类分析结果-去重
     *
     * @return
     */

    @GetMapping("/getSessionSubcategoryDistinctEntity")
    public BaseRsp getSessionSubcategoryDistinctEntity(HttpServletRequest httpServletRequest,
                                                       @RequestParam("from_layer") int fromLayer,
                                                       @RequestParam("to_layer") int toLayer,
                                                       @RequestParam("f_session_num_start") int fSessionNumStart,
                                                       @RequestParam("f_session_num_end") int fSessionNumEnd,
                                                       @RequestParam("f_pv_num_start") int fPVNumStart,
                                                       @RequestParam("f_pv_num_end") int fPVNumEnd,
                                                       @RequestParam("f_user_num_start") int fUserNumStart,
                                                       @RequestParam("f_user_num_end") int fUserNumEnd,
                                                       @RequestParam("f_category_from") String fCategoryFrom,
                                                       @RequestParam("f_category") String fCategory,
                                                       @RequestParam("f_subcategory") String fSubcategory,
                                                       @RequestParam("f_subcategory_from") String fSubcategoryFrom,
                                                       @RequestParam("f_upload_name") String fUploadName,
                                                       @RequestParam("f_upload_user") String fUploadUser
    ) {
        LOGGER.info("获取session Subcategory distinct");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser)
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser)
                    .get("f_user_scene_version_id");


            String [] categoryArr = fCategoryFrom.split(",");

            String [] subcategoryArr = fSubcategoryFrom.split(",");

            resList = sessionDistinctEventRepository
                    .getSessionSubcategoryDistinctEntity(
                            fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],
                            fromLayer, toLayer,
                            fSessionNumStart,
                            fCategory, fSubcategory,
                            fSessionNumEnd,
                            fUserNumStart,fUserNumEnd,
                            fPVNumStart,fPVNumEnd,
                            categoryArr[1],subcategoryArr[1],categoryArr[2],subcategoryArr[2],
                            categoryArr[3],subcategoryArr[3],categoryArr[4],subcategoryArr[4]);
            rspPage.list = resList;
            baseRsp.setMessage("获取session Subcategory distinct 成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session Subcategory distinct 失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session Subcategory distinct 失败");
        }
        return baseRsp;
    }

    /**
     * 获取桑基图大类分析结果-去重
     *
     * @return
     */

    @GetMapping("/getSessionCategoryDistinctEntity")
    public BaseRsp getSessionCategoryDistinctEntity(HttpServletRequest httpServletRequest,
                                                    @RequestParam("from_layer") int fromLayer,
                                                    @RequestParam("to_layer") int toLayer,
                                                    @RequestParam("f_session_num_start") int fSessionNumStart,
                                                    @RequestParam("f_session_num_end") int fSessionNumEnd,
                                                    @RequestParam("f_pv_num_start") int fPVNumStart,
                                                    @RequestParam("f_pv_num_end") int fPVNumEnd,
                                                    @RequestParam("f_user_num_start") int fUserNumStart,
                                                    @RequestParam("f_user_num_end") int fUserNumEnd,
                                                    @RequestParam("f_category_from") String fCategoryFrom,
                                                    @RequestParam("f_category") String fCategory,
                                                    @RequestParam("f_upload_name") String fUploadName,
                                                    @RequestParam("f_upload_user") String fUploadUser
    ) {
        LOGGER.info("获取session Category");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser)
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser)
                    .get("f_user_scene_version_id");


            String [] categoryArr = fCategoryFrom.split(",");

            resList = sessionDistinctEventRepository
                    .getSessionCategoryDistinctEntity(fUserSceneVersionId,fMakeVersionId,
                            categoryArr[0], fromLayer, toLayer,
                            fSessionNumStart,
                            fCategory, fSessionNumEnd,
                            fUserNumStart,fUserNumEnd,
                            fPVNumStart,fPVNumEnd,
                            categoryArr[1],categoryArr[2],categoryArr[3],categoryArr[4]);

            rspPage.list = resList;
            baseRsp.setMessage("获取session Category成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session Category失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session Category失败");
        }
        return baseRsp;
    }



    /**
     * 获取桑基图页面事件分析结果（分维度不去重）
     *
     * @return
     */

    @PostMapping("/getSessionDimCategoryDetailEntity")
    public BaseRsp getSessionDimCategoryDetailEntity(HttpServletRequest httpServletRequest,
                                                     @RequestBody ReqSankeyDimCategoryDto reqCateDimSankeyD
    ) {
        LOGGER.info("获取session event");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqCateDimSankeyD.getFUploadName(),reqCateDimSankeyD.getFUploadUser())
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqCateDimSankeyD.getFUploadName(),reqCateDimSankeyD.getFUploadUser())
                    .get("f_user_scene_version_id");

            String [] categoryArr = reqCateDimSankeyD.getFCategoryFrom().split(",");

            String [] categoryArrDim = reqCateDimSankeyD.getFCategoryFrom().split(",");

            if (reqCateDimSankeyD.getFDim().equals("age") && reqCateDimSankeyD.getFClick().equals("from")) {
                resList = sessionEventRepository
                        .getCateSankeyCntByAgeFrom(
                                fUserSceneVersionId,fMakeVersionId, categoryArr[0],
                                reqCateDimSankeyD.getFCategory(),
                                categoryArr[1], categoryArr[2], categoryArr[3], categoryArr[4],
                                reqCateDimSankeyD.getFCategoryFromA());

            } else if (reqCateDimSankeyD.getFDim().equals("sex") && reqCateDimSankeyD.getFClick().equals("from")) {
                resList = sessionEventRepository
                        .getCateSankeyCntBySexFrom(
                                fUserSceneVersionId,fMakeVersionId, categoryArr[0],
                                reqCateDimSankeyD.getFCategory(),
                                categoryArr[1], categoryArr[2], categoryArr[3], categoryArr[4],
                                reqCateDimSankeyD.getFCategoryFromA());

            } else if (reqCateDimSankeyD.getFDim().equals("province") && reqCateDimSankeyD.getFClick().equals("from")) {

                resList = sessionEventRepository
                        .getCateSankeyCntByProvFrom(
                                fUserSceneVersionId,fMakeVersionId, categoryArr[0],
                                reqCateDimSankeyD.getFCategory(),
                                categoryArr[1], categoryArr[2], categoryArr[3], categoryArr[4],
                                reqCateDimSankeyD.getFCategoryFromA());

            } else if (reqCateDimSankeyD.getFDim().equals("channel") && reqCateDimSankeyD.getFClick().equals("from")) {
                resList = sessionEventRepository
                        .getCateSankeyCntByChannelFrom(
                                fUserSceneVersionId,fMakeVersionId, categoryArr[0],
                                reqCateDimSankeyD.getFCategory(),
                                categoryArr[1], categoryArr[2], categoryArr[3], categoryArr[4],
                                reqCateDimSankeyD.getFCategoryFromA());


            } else if (reqCateDimSankeyD.getFDim().equals("age") && reqCateDimSankeyD.getFClick().equals("to")) {
                resList = sessionEventRepository
                        .getSessionCategorySankeyCntByAgeTo(
                                fUserSceneVersionId,fMakeVersionId, categoryArrDim[0],
                                reqCateDimSankeyD.getFCategory(),
                                categoryArrDim[1], categoryArrDim[2], categoryArrDim[3], categoryArrDim[4],
                                reqCateDimSankeyD.getFCategoryFromA(),reqCateDimSankeyD.getFCategoryToA());


            } else if (reqCateDimSankeyD.getFDim().equals("sex") && reqCateDimSankeyD.getFClick().equals("to")) {

                resList = sessionEventRepository
                        .getSessionCategorySankeyCntBySexTo(
                                fUserSceneVersionId,fMakeVersionId, categoryArrDim[0],
                                reqCateDimSankeyD.getFCategory(),
                                categoryArrDim[1], categoryArrDim[2], categoryArrDim[3], categoryArrDim[4],
                                reqCateDimSankeyD.getFCategoryFromA(),reqCateDimSankeyD.getFCategoryToA());

            } else if (reqCateDimSankeyD.getFDim().equals("province") && reqCateDimSankeyD.getFClick().equals("to")) {

                resList = sessionEventRepository
                        .getCateSankeyCntByProvTo(
                                fUserSceneVersionId,fMakeVersionId, categoryArrDim[0],
                                reqCateDimSankeyD.getFCategory(),
                                categoryArrDim[1], categoryArrDim[2], categoryArrDim[3], categoryArrDim[4],
                                reqCateDimSankeyD.getFCategoryFromA(),reqCateDimSankeyD.getFCategoryToA());

            } else if (reqCateDimSankeyD.getFDim().equals("channel") && reqCateDimSankeyD.getFClick().equals("to")) {

                resList = sessionEventRepository
                        .getCateSankeyCntByChannelTo(
                                fUserSceneVersionId,fMakeVersionId, categoryArrDim[0],
                                reqCateDimSankeyD.getFCategory(),
                                categoryArrDim[1], categoryArrDim[2], categoryArrDim[3], categoryArrDim[4],
                                reqCateDimSankeyD.getFCategoryFromA(),reqCateDimSankeyD.getFCategoryToA());

            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session event成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event失败");
        }
        return baseRsp;
    }







    /**
     * 获取桑基图大类分析结果（分维度去重）
     *
     * @return
     */

    @PostMapping("/getSessionDimCategoryDistinctEntity")
    public BaseRsp getSessionDimCategoryDistinctEntity(HttpServletRequest httpServletRequest,
                                                       @RequestBody ReqSankeyDimCategoryDto reqCateDimDistinct

    ) {
        LOGGER.info("获取session event");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqCateDimDistinct.getFUploadName(),reqCateDimDistinct.getFUploadUser())
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqCateDimDistinct.getFUploadName(),reqCateDimDistinct.getFUploadUser())
                    .get("f_user_scene_version_id");

            String [] categoryArr = reqCateDimDistinct.getFCategoryFrom().split(",");

            String [] categoryArrDimD = reqCateDimDistinct.getFCategoryFrom().split(",");

            if (reqCateDimDistinct.getFDim().equals("age")  && reqCateDimDistinct.getFClick().equals("from")) {
                resList = sessionDistinctEventRepository
                        .getDCateSankeyCntByAgeFrom(
                                fUserSceneVersionId,fMakeVersionId, categoryArr[0],
                                reqCateDimDistinct.getFCategory(),
                                categoryArr[1], categoryArr[2], categoryArr[3], categoryArr[4],
                                reqCateDimDistinct.getFCategoryFromA());

            } else if (reqCateDimDistinct.getFDim().equals("sex")  && reqCateDimDistinct.getFClick().equals("from")) {
                resList = sessionDistinctEventRepository
                        .getDCateSankeyCntBySexFrom(
                                fUserSceneVersionId,fMakeVersionId, categoryArr[0],
                                reqCateDimDistinct.getFCategory(),
                                categoryArr[1], categoryArr[2], categoryArr[3], categoryArr[4],
                                reqCateDimDistinct.getFCategoryFromA());

            } else if (reqCateDimDistinct.getFDim().equals("province")
                    && reqCateDimDistinct.getFClick().equals("from")) {

                resList = sessionDistinctEventRepository
                        .getDCateSankeyCntByProFrom(
                                fUserSceneVersionId,fMakeVersionId, categoryArr[0],
                                reqCateDimDistinct.getFCategory(),
                                categoryArr[1], categoryArr[2], categoryArr[3], categoryArr[4],
                                reqCateDimDistinct.getFCategoryFromA());

            } else if (reqCateDimDistinct.getFDim().equals("channel")
                    && reqCateDimDistinct.getFClick().equals("from")) {
                resList = sessionDistinctEventRepository
                        .getDCategorySankeyCntByChannelFrom(
                                fUserSceneVersionId,fMakeVersionId, categoryArr[0],
                                reqCateDimDistinct.getFCategory(),
                                categoryArr[1], categoryArr[2], categoryArr[3], categoryArr[4],
                                reqCateDimDistinct.getFCategoryFromA());


            } else if (reqCateDimDistinct.getFDim().equals("age") && reqCateDimDistinct.getFClick().equals("to")) {
                resList = sessionDistinctEventRepository
                        .getDCateSankeyCntByAgeTo(
                                fUserSceneVersionId,fMakeVersionId, categoryArrDimD[0],
                                reqCateDimDistinct.getFCategory(),
                                categoryArrDimD[1], categoryArrDimD[2], categoryArrDimD[3], categoryArrDimD[4],
                                reqCateDimDistinct.getFCategoryFromA(),reqCateDimDistinct.getFCategoryToA());


            } else if (reqCateDimDistinct.getFDim().equals("sex") && reqCateDimDistinct.getFClick().equals("to")) {

                resList = sessionDistinctEventRepository
                        .getDCateSankeyCntBySexTo(
                                fUserSceneVersionId,fMakeVersionId, categoryArrDimD[0],
                                reqCateDimDistinct.getFCategory(),
                                categoryArrDimD[1], categoryArrDimD[2], categoryArrDimD[3], categoryArrDimD[4],
                                reqCateDimDistinct.getFCategoryFromA(),reqCateDimDistinct.getFCategoryToA());

            } else if (reqCateDimDistinct.getFDim().equals("province") && reqCateDimDistinct.getFClick().equals("to")) {

                resList = sessionDistinctEventRepository
                        .getDCateSankeyCntByProTo(
                                fUserSceneVersionId,fMakeVersionId, categoryArrDimD[0],
                                reqCateDimDistinct.getFCategory(),
                                categoryArrDimD[1], categoryArrDimD[2], categoryArrDimD[3], categoryArrDimD[4],
                                reqCateDimDistinct.getFCategoryFromA(),reqCateDimDistinct.getFCategoryToA());

            } else if (reqCateDimDistinct.getFDim().equals("channel") && reqCateDimDistinct.getFClick().equals("to")) {

                resList = sessionDistinctEventRepository
                        .getDCategorySankeyCntByChannelTo(
                                fUserSceneVersionId,fMakeVersionId, categoryArrDimD[0],
                                reqCateDimDistinct.getFCategory(),
                                categoryArrDimD[1], categoryArrDimD[2], categoryArrDimD[3], categoryArrDimD[4],
                                reqCateDimDistinct.getFCategoryFromA(),reqCateDimDistinct.getFCategoryToA());

            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session event成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event失败");
        }
        return baseRsp;
    }






    /**
     * 获取桑基图小类分析结果（分维度去重）
     *
     * @return
     */

    @PostMapping("/getDimSubCategoryDistinctEntity")
    public BaseRsp getDimSubCategoryDistinctEntity(HttpServletRequest httpServletRequest,
                                                   @RequestBody ReqSankeyDimSubDto reqSankeyDimSubDto

    ) {
        LOGGER.info("获取session event");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqSankeyDimSubDto.getFUploadName(),reqSankeyDimSubDto.getFUploadUser())
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqSankeyDimSubDto.getFUploadName(),reqSankeyDimSubDto.getFUploadUser())
                    .get("f_user_scene_version_id");

            String [] categoryArr = reqSankeyDimSubDto.getFCategoryFrom().split(",");

            String [] subcategoryArr = reqSankeyDimSubDto.getFSubcategoryFrom().split(",");

            String [] categoryArrSub = reqSankeyDimSubDto.getFCategoryFrom().split(",");

            String [] subcategoryArrSub = reqSankeyDimSubDto.getFSubcategoryFrom().split(",");

            if (reqSankeyDimSubDto.getFDim().equals("age")  && reqSankeyDimSubDto.getFClick().equals("from")) {
                resList = sessionDistinctEventRepository
                        .getDSubSankeyCntByAgeFrom(
                                fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],
                                reqSankeyDimSubDto.getFCategory(), reqSankeyDimSubDto.getFSubcategory(),
                                categoryArr[1],subcategoryArr[1],
                                categoryArr[2],subcategoryArr[2],
                                categoryArr[3],subcategoryArr[3],
                                categoryArr[4],subcategoryArr[4],
                                reqSankeyDimSubDto.getFSubcategoryFromA());

            } else if (reqSankeyDimSubDto.getFDim().equals("sex")  && reqSankeyDimSubDto.getFClick().equals("from")) {
                resList = sessionDistinctEventRepository
                        .getDSubSankeyCntBySexFrom(

                                fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],
                                reqSankeyDimSubDto.getFCategory(), reqSankeyDimSubDto.getFSubcategory(),
                                categoryArr[1],subcategoryArr[1],
                                categoryArr[2],subcategoryArr[2],
                                categoryArr[3],subcategoryArr[3],
                                categoryArr[4],subcategoryArr[4],
                                reqSankeyDimSubDto.getFSubcategoryFromA());

            } else if (reqSankeyDimSubDto.getFDim().equals("province")
                    && reqSankeyDimSubDto.getFClick().equals("from")) {

                resList = sessionDistinctEventRepository
                        .getDSubSankeyCntByProFrom(

                                fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],
                                reqSankeyDimSubDto.getFCategory(), reqSankeyDimSubDto.getFSubcategory(),
                                categoryArr[1],subcategoryArr[1],
                                categoryArr[2],subcategoryArr[2],
                                categoryArr[3],subcategoryArr[3],
                                categoryArr[4],subcategoryArr[4],
                                reqSankeyDimSubDto.getFSubcategoryFromA());

            } else if (reqSankeyDimSubDto.getFDim().equals("channel")
                    && reqSankeyDimSubDto.getFClick().equals("from")) {
                resList = sessionDistinctEventRepository
                        .getDSubSankeyCntByChannelFrom(

                                fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],
                                reqSankeyDimSubDto.getFCategory(), reqSankeyDimSubDto.getFSubcategory(),
                                categoryArr[1],subcategoryArr[1],
                                categoryArr[2],subcategoryArr[2],
                                categoryArr[3],subcategoryArr[3],
                                categoryArr[4],subcategoryArr[4],
                                reqSankeyDimSubDto.getFSubcategoryFromA());


            } else if (reqSankeyDimSubDto.getFDim().equals("age") && reqSankeyDimSubDto.getFClick().equals("to")) {
                resList = sessionDistinctEventRepository
                        .getDSubSankeyCntByAgeTo(

                                fUserSceneVersionId,fMakeVersionId, categoryArrSub[0], subcategoryArrSub[0],
                                reqSankeyDimSubDto.getFCategory(), reqSankeyDimSubDto.getFSubcategory(),
                                categoryArrSub[1],subcategoryArrSub[1],
                                categoryArrSub[2],subcategoryArrSub[2],
                                categoryArrSub[3],subcategoryArrSub[3],
                                categoryArrSub[4],subcategoryArrSub[4],
                                reqSankeyDimSubDto.getFSubcategoryFromA(), reqSankeyDimSubDto.getFSubcategoryToA());


            } else if (reqSankeyDimSubDto.getFDim().equals("sex") && reqSankeyDimSubDto.getFClick().equals("to")) {
                resList = sessionDistinctEventRepository
                        .getDSubSankeyCntBySexTo(

                                fUserSceneVersionId,fMakeVersionId, categoryArrSub[0], subcategoryArrSub[0],
                                reqSankeyDimSubDto.getFCategory(), reqSankeyDimSubDto.getFSubcategory(),
                                categoryArrSub[1],subcategoryArrSub[1],
                                categoryArrSub[2],subcategoryArrSub[2],
                                categoryArrSub[3],subcategoryArrSub[3],
                                categoryArrSub[4],subcategoryArrSub[4],
                                reqSankeyDimSubDto.getFSubcategoryFromA(),reqSankeyDimSubDto.getFSubcategoryToA());

            } else if (reqSankeyDimSubDto.getFDim().equals("province") && reqSankeyDimSubDto.getFClick().equals("to")) {
                resList = sessionDistinctEventRepository
                        .getDSubSankeyCntByProTo(

                                fUserSceneVersionId,fMakeVersionId, categoryArrSub[0], subcategoryArrSub[0],
                                reqSankeyDimSubDto.getFCategory(), reqSankeyDimSubDto.getFSubcategory(),
                                categoryArrSub[1],subcategoryArrSub[1],
                                categoryArrSub[2],subcategoryArrSub[2],
                                categoryArrSub[3],subcategoryArrSub[3],
                                categoryArrSub[4],subcategoryArrSub[4],
                                reqSankeyDimSubDto.getFSubcategoryFromA(),reqSankeyDimSubDto.getFSubcategoryToA());

            } else if (reqSankeyDimSubDto.getFDim().equals("channel") && reqSankeyDimSubDto.getFClick().equals("to")) {

                resList = sessionDistinctEventRepository
                        .getDSubCategorySankeyCntByChannelTo(

                                fUserSceneVersionId,fMakeVersionId, categoryArrSub[0], subcategoryArrSub[0],
                                reqSankeyDimSubDto.getFCategory(), reqSankeyDimSubDto.getFSubcategory(),
                                categoryArrSub[1],subcategoryArrSub[1],
                                categoryArrSub[2],subcategoryArrSub[2],
                                categoryArrSub[3],subcategoryArrSub[3],
                                categoryArrSub[4],subcategoryArrSub[4],
                                reqSankeyDimSubDto.getFSubcategoryFromA(),reqSankeyDimSubDto.getFSubcategoryToA());

            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session event成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event失败");
        }
        return baseRsp;
    }




    /**
     * 导出csv文件（点） 暂未使用
     *
     * @return
     */

    @GetMapping("/downloadNodeCsv")
    public BaseRsp downloadNodeCsv(HttpServletRequest httpServletRequest,
                                   @RequestParam("f_upload_name") String fUploadName,
                                   @RequestParam("f_upload_user") String fUploadUser,
                                   HttpServletResponse response) {
        LOGGER.info("导出csv文件");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");


            List<Map<String, Object>> exportData = sessionNodeRepository
                    .getSessionNode(fUserSceneVersionId,fMakeVersionId);

            /**
             * 构造导出数据结构
             */
            String titles = "~id,~label,name:string,pv:int"; // 设置表头
            // 设置每列字段
            String keys = "~id,~label,name:string,pv:int";
            // 设置导出文件前缀
            String fName = "df_node_";


            OutputStream os = response.getOutputStream();
            CsvExportUtil.responseSetProperties(fName, response);
            CsvExportUtil.doExport(exportData, titles, keys, os);
            os.close();
            LOGGER.info("下载csv成功");
            baseRsp.setMessage("下载csv成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("下载csv失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("下载csv失败");
        }
        return baseRsp;
    }




    /**
     * 导出csv文件（边） 暂未使用
     *
     * @return
     */

    @GetMapping("/downloadSingleCsv")
    public BaseRsp downloadSingleCsv(HttpServletRequest httpServletRequest,
                                     @RequestParam("f_upload_name") String fUploadName,
                                     @RequestParam("f_upload_user") String fUploadUser,
                                     HttpServletResponse response) {
        LOGGER.info("导出csv文件");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);
//            Map<String, Object> map = LogEventSessionService.getUserScene(fUploadUser,fUploadName);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");


            List<Map<String, Object>> exportData = sessionSingleRepository
                    .getSessionSingle(fUserSceneVersionId,fMakeVersionId);


            /**
             * 构造导出数据结构
             */
            String titles = "~id,~from,~to,~label,weight:double"; // 设置表头
            // 设置每列字段
            String keys = "~id,~from,~to,~label,weight:double";
            // 设置导出文件前缀
            String fName = "df_single_";


            OutputStream os = response.getOutputStream();
            CsvExportUtil.responseSetProperties(fName, response);
            CsvExportUtil.doExport(exportData, titles, keys, os);
            os.close();
            LOGGER.info("下载csv成功");
            baseRsp.setMessage("下载csv成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("下载csv失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("下载csv失败");
        }
        return baseRsp;
    }


//    // 上传文件
//    @PostMapping("/uploadmuban")
//    public BaseRsp uploadmuban(@RequestParam("file") MultipartFile multipartFile) {
//        BaseRsp baseRsp = new BaseRsp();
//        try {
//            File file = null;
//            String originalFilename = "";
//            try {
//                originalFilename = multipartFile.getOriginalFilename();
//                String[] filename = originalFilename.split("\\.");
//                file = File.createTempFile(filename[0], filename[1]);
//                multipartFile.transferTo(file);
//                file.deleteOnExit();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            // 初始化用户身份信息（secretId, secretKey）。
//            String secretId = "";
//            String secretKey = "";
//            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
//
//            // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
//            ClientConfig clientConfig = new ClientConfig(new Region(""));
//            String region = "";
//
//            // 使用内网域名
//            String endpoint = String.format("", region);
//            clientConfig.setEndpointBuilder(new CosEndpointBuilder(endpoint));
//
//            // 这里建议设置使用 https 协议
//            // 从 5.6.54 版本开始，默认使用了 https
//            clientConfig.setHttpProtocol(HttpProtocol.https);
//
//            // 生成 cos 客户端。
//            COSClient cosClient = new COSClient(cred, clientConfig);
//
//            // 生成uuid
////            String uuid = UUID.randomUUID().toString();
//            String bucketName = "";
//            String key = "/excel/" + originalFilename;
//            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
//            cosClient.putObject(putObjectRequest);
//            URL url = cosClient.getObjectUrl("", key);
//
//            // 替换内网域名
//            String urlStr = url.toString().replace("", "");
//            Map<String, Object> data = new HashMap<>();
//            data.put("url", urlStr);
//            baseRsp.data = data;
//
//        } catch (Exception e) {
//            logger.error("上传文件失败", e);
//            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
//            baseRsp.setMessage("上传文件失败");
//        }
//        return baseRsp;
//    }
//
//
//    //创建公报
//    @PostMapping("/create_content")
//    public BaseRsp createContent(@RequestBody ReqNewsInfo req) {
//
//        BaseRsp baseRsp = new BaseRsp();
//
//        int type = req.getF_type();
//        String f_sorce = req.getF_sorce();
//        String f_region = req.getF_region() == null ? "" : req.getF_region();
//        String f_title = req.getF_title();
//        String f_url = req.getF_url() == null ? "" : req.getF_url();
//        String f_image = req.getF_image() == null ? "" : req.getF_image();
//        String f_content = req.getF_content();
//        Object f_documents_json = req.getF_documents_json();
//        int f_status = req.getF_status();
//        String f_editor = req.getF_editor();
//
//        if (type == 0) {
//            if (f_url.isEmpty()) {
//                baseRsp.setCode(BizCode.INTERRUNPT.getCode());
//                baseRsp.setMessage("f_url不能为空");
//            }
//        } else {
//            if (f_content.isEmpty()) {
//                baseRsp.setCode(BizCode.INTERRUNPT.getCode());
//                baseRsp.setMessage("f_content不能为空");
//            }
//        }
//
//        if (f_sorce.equals("") && f_region.isEmpty()) {
//            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
//            baseRsp.setMessage("f_region不能为空");
//        }
//
//        if (f_sorce.equals("") || f_sorce.equals("")) {
//            if (f_image.isEmpty()) {
//                baseRsp.setCode(BizCode.INTERRUNPT.getCode());
//                baseRsp.setMessage("f_image不能为空");
//            }
//        }
//
//        logger.info("开始创建公报");
//
//        try {
//            NewsContentEntity entity = new NewsContentEntity();
//            entity.setF_type(type);
//            entity.setF_sorce(f_sorce);
//            entity.setF_region(f_region);
//            entity.setF_title(f_title);
//            entity.setF_url(f_url);
//            entity.setF_image(f_image);
//            entity.setF_content(f_content);
//            entity.setF_status(f_status);
//            entity.setF_editor(f_editor);
//            entity.setF_create_time(new Date());
//            if (f_documents_json != null) {
//                entity.setF_documents_json(JSON.toJSONString(f_documents_json));
//            } else {
//                entity.setF_documents_json("");
//            }
//            newsContentRepositoty.save(entity);
//
//            baseRsp.setMessage("创建成功");
//        } catch (Exception e) {
//            logger.error("创建公报失败", e);
//            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
//            baseRsp.setMessage("创建公报失败");
//        }
//        return baseRsp;
//    }

}
