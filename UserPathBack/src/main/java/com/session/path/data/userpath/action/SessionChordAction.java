/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.action;


import com.session.path.data.userpath.dto.BaseRsp;
import com.session.path.data.userpath.dto.ReqChordCategoryDto;
import com.session.path.data.userpath.dto.ReqChordDimCategoryDto;
import com.session.path.data.userpath.dto.ReqChordDimEventDto;
import com.session.path.data.userpath.dto.ReqChordDimSubDto;
import com.session.path.data.userpath.dto.ReqChordEventDto;
import com.session.path.data.userpath.dto.ReqChordSubDto;
import com.session.path.data.userpath.dto.RspPage;
import com.session.path.data.userpath.repository.ChordSessionDistinctEventRepository;
import com.session.path.data.userpath.repository.ChordSessionEventRepository;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SessionChordAction
 * @Description 和弦图用户路径分析
 * @Author author
 * @Date 2023/03/04 16:34
 * @Version 1.0
 **/
@Controller
@CrossOrigin
@RestController
@RequestMapping("/chord")
public class SessionChordAction extends BaseAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(SessionChordAction.class);

    @Autowired
    ChordSessionDistinctEventRepository chordSessionDistinctEventRepository;

    @Autowired
    ChordSessionEventRepository chordSessionEventRepository;

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;


    /**
     * 获取和弦图小类分析结果（去重）
     *
     * @return
     */
    @PostMapping("/getChordSubcategoryDistinctEntity")
    public BaseRsp getChordSubcategoryDistinctEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqChordSubDto reqSubChordD
    ) {
        LOGGER.info("获取session Subcategory");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqSubChordD.getFUploadName(),reqSubChordD.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqSubChordD.getFUploadName(),reqSubChordD.getFUploadUser())
                    .get("f_user_scene_version_id");

            resList = chordSessionDistinctEventRepository
                    .getChordSubDisEntity(
                            fUserSceneVersionId,fMakeVersionId,
                            reqSubChordD.getFSubcategoryFrom(),
                            reqSubChordD.getFSessionNumStart(),
                            reqSubChordD.getFSessionNumEnd(),
                            reqSubChordD.getFUserNumStart(),
                            reqSubChordD.getFUserNumEnd(),
                            reqSubChordD.getFPVNumStart(),
                            reqSubChordD.getFPVNumEnd());
            rspPage.list = resList;
            baseRsp.setMessage("获取session Subcategory成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session Subcategory失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session Subcategory失败");
        }
        return baseRsp;
    }


    /**
     * 获取和弦图小类分析结果（不去重）
     *
     * @return
     */

    @PostMapping("/getChordSessionSubcategoryEntity")
    public BaseRsp getChordSessionSubcategoryEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqChordSubDto reqSubChord

    ) {
        LOGGER.info("获取session Subcategory");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqSubChord.getFUploadName(),reqSubChord.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqSubChord.getFUploadName(),reqSubChord.getFUploadUser())
                    .get("f_user_scene_version_id");

            resList = chordSessionEventRepository
                    .getChordSessionSubcategoryEntity(
                            fUserSceneVersionId,fMakeVersionId,
                            reqSubChord.getFSubcategoryFrom(),
                            reqSubChord.getFSessionNumStart(),
                            reqSubChord.getFSessionNumEnd(),
                            reqSubChord.getFUserNumStart(),
                            reqSubChord.getFUserNumEnd(),
                            reqSubChord.getFPVNumStart(),
                            reqSubChord.getFPVNumEnd());
            rspPage.list = resList;
            baseRsp.setMessage("获取session Subcategory成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session Subcategory失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session Subcategory失败");
        }
        return baseRsp;
    }


    /**
     * 获取和弦图大类分析结果（不去重）
     *
     * @return
     */

    @PostMapping("/getChordSessionCategoryEntity")
    public BaseRsp getChordSessionCategoryEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqChordCategoryDto reqCateChord
    ) {
        LOGGER.info("获取session Category");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqCateChord.getFUploadName(),reqCateChord.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqCateChord.getFUploadName(),reqCateChord.getFUploadUser())
                    .get("f_user_scene_version_id");

            resList = chordSessionEventRepository
                    .getChordSessionCategoryEntity(
                            fUserSceneVersionId,fMakeVersionId,
                            reqCateChord.getFCategoryFrom(),
                            reqCateChord.getFSessionNumStart(),
                            reqCateChord.getFSessionNumEnd(),
                            reqCateChord.getFUserNumStart(),
                            reqCateChord.getFUserNumEnd(),
                            reqCateChord.getFPVNumStart(),
                            reqCateChord.getFPVNumEnd());
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
     * 获取和弦图大类分析结果（去重）
     *
     * @return
     */

    @PostMapping("/getChordCategoryDistinctEntity")
    public BaseRsp getChordCategoryDistinctEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqChordCategoryDto reqCateChordD
    ) {
        LOGGER.info("获取session Category");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqCateChordD.getFUploadName(),reqCateChordD.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqCateChordD.getFUploadName(),reqCateChordD.getFUploadUser())
                    .get("f_user_scene_version_id");

            resList = chordSessionDistinctEventRepository
                    .getChordCateDisEntity(
                            fUserSceneVersionId,fMakeVersionId,
                            reqCateChordD.getFCategoryFrom(),
                            reqCateChordD.getFSessionNumStart(),
                            reqCateChordD.getFSessionNumEnd(),
                            reqCateChordD.getFUserNumStart(),
                            reqCateChordD.getFUserNumEnd(),
                            reqCateChordD.getFPVNumStart(),
                            reqCateChordD.getFPVNumEnd());
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
     * 获取和弦图页面event分析结果（不去重）
     *
     * @return
     */

    @PostMapping("/getChordSessionEventEntity")
    public BaseRsp getChordSessionEventEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqChordEventDto reqEventChord
    ) {
        LOGGER.info("获取session event");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqEventChord.getFUploadName(),reqEventChord.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqEventChord.getFUploadName(),reqEventChord.getFUploadUser())
                    .get("f_user_scene_version_id");

            resList = chordSessionEventRepository
                    .getChordSessionEventEntity(
                            fUserSceneVersionId,fMakeVersionId,
                            reqEventChord.getFEventFrom(),
                            reqEventChord.getFSessionNumStart(),
                            reqEventChord.getFSessionNumEnd(),
                            reqEventChord.getFUserNumStart(),
                            reqEventChord.getFUserNumEnd(),
                            reqEventChord.getFPVNumStart(),
                            reqEventChord.getFPVNumEnd());
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
     * 获取和弦图页面分析结果（去重）
     *
     * @return
     */

    @PostMapping("/getChordSessionEventDistinctEntity")
    public BaseRsp getChordSessionEventDistinctEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqChordEventDto reqEventChordD
    ) {
        LOGGER.info("获取session 去重event");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqEventChordD.getFUploadName(),reqEventChordD.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqEventChordD.getFUploadName(),reqEventChordD.getFUploadUser())
                    .get("f_user_scene_version_id");

            resList = chordSessionDistinctEventRepository
                    .getChordSessionEventDistinctEntity(
                            fUserSceneVersionId,fMakeVersionId,
                            reqEventChordD.getFEventFrom(),
                            reqEventChordD.getFSessionNumStart(),
                            reqEventChordD.getFSessionNumEnd(),
                            reqEventChordD.getFUserNumStart(),
                            reqEventChordD.getFUserNumEnd(),
                            reqEventChordD.getFPVNumStart(),
                            reqEventChordD.getFPVNumEnd());
            rspPage.list = resList;
            baseRsp.setMessage("获取session 去重event成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session 去重event失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session 去重event失败");
        }
        return baseRsp;
    }




    /**
     * 和弦图查看分析(大类)
     *
     * @return
     */

    @PostMapping("/getChordDimCategoryDetailEntity")
    public BaseRsp getChordDimCategoryDetailEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqChordDimCategoryDto reqCateChord
    ) {
        LOGGER.info("获取session category detail 分析");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqCateChord.getFUploadName(),reqCateChord.getFUploadUser())
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqCateChord.getFUploadName(),reqCateChord.getFUploadUser())
                    .get("f_user_scene_version_id");


            if (reqCateChord.getFDim().equals("age") && reqCateChord.getFClick().equals("to")) {
                resList = chordSessionEventRepository
                        .getChordCateCntByAgeTo(
                                fUserSceneVersionId,fMakeVersionId,
                                reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA(),reqCateChord.getFCategoryToA());

            } else if (reqCateChord.getFDim().equals("sex") && reqCateChord.getFClick().equals("to")) {
                resList = chordSessionEventRepository
                        .getChordCateCntBySexTo(
                                fUserSceneVersionId,fMakeVersionId,
                                reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA(),reqCateChord.getFCategoryToA());

            } else if (reqCateChord.getFDim().equals("province") && reqCateChord.getFClick().equals("to")) {
                resList = chordSessionEventRepository
                        .getChordCateCntByProvinceTo(
                                fUserSceneVersionId,fMakeVersionId, reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA(),reqCateChord.getFCategoryToA());

            } else if (reqCateChord.getFDim().equals("channel") && reqCateChord.getFClick().equals("to")) {
                resList = chordSessionEventRepository
                        .getChordCateCntByChannelTo(
                                fUserSceneVersionId,fMakeVersionId, reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA(),reqCateChord.getFCategoryToA());
            } else if (reqCateChord.getFDim().equals("age") && reqCateChord.getFClick().equals("from")) {
                resList = chordSessionEventRepository
                        .getChordCateCntByAgeFrom(
                                fUserSceneVersionId,fMakeVersionId, reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA());

            } else if (reqCateChord.getFDim().equals("sex") && reqCateChord.getFClick().equals("from")) {
                resList = chordSessionEventRepository
                        .getChordCateCntBySexFrom(
                                fUserSceneVersionId,fMakeVersionId, reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA());

            } else if (reqCateChord.getFDim().equals("province") && reqCateChord.getFClick().equals("from")) {
                resList = chordSessionEventRepository
                        .getChordCateCntByProvinceFrom(
                                fUserSceneVersionId,fMakeVersionId, reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA());

            } else if (reqCateChord.getFDim().equals("channel") && reqCateChord.getFClick().equals("from")) {
                resList = chordSessionEventRepository
                        .getChordCateCntByChannelFrom(
                                fUserSceneVersionId,fMakeVersionId, reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA());
            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session category detail 分析成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session category detail 分析失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session category detail 分析失败");
        }
        return baseRsp;
    }




    /**
     * 和弦图查看分析(大类)
     *
     * @return
     */

    @PostMapping("/getChordDimCategoryDistinctEntity")
    public BaseRsp getChordDimCategoryDistinctEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqChordDimCategoryDto reqCateChord
    ) {
        LOGGER.info("获取session category distinct 分析");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqCateChord.getFUploadName(),reqCateChord.getFUploadUser())
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqCateChord.getFUploadName(),reqCateChord.getFUploadUser())
                    .get("f_user_scene_version_id");


            if (reqCateChord.getFDim().equals("age") && reqCateChord.getFClick().equals("to")) {
                resList = chordSessionDistinctEventRepository
                        .getChordCateDisCntByAgeTo(
                                fUserSceneVersionId,fMakeVersionId, reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA(),reqCateChord.getFCategoryToA());

            } else if (reqCateChord.getFDim().equals("sex") && reqCateChord.getFClick().equals("to")) {
                resList = chordSessionDistinctEventRepository
                        .getChordCateDisCntBySexTo(
                                fUserSceneVersionId,fMakeVersionId, reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA(),reqCateChord.getFCategoryToA());

            } else if (reqCateChord.getFDim().equals("province") && reqCateChord.getFClick().equals("to")) {
                resList = chordSessionDistinctEventRepository
                        .getChordCateDisCntByProvinceTo(
                                fUserSceneVersionId,fMakeVersionId, reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA(),reqCateChord.getFCategoryToA());

            } else if (reqCateChord.getFDim().equals("channel") && reqCateChord.getFClick().equals("to")) {
                resList = chordSessionDistinctEventRepository
                        .getChordCateDisCntByChannelTo(
                                fUserSceneVersionId,fMakeVersionId, reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA(),reqCateChord.getFCategoryToA());

            } else if (reqCateChord.getFDim().equals("age") && reqCateChord.getFClick().equals("from")) {
                resList = chordSessionDistinctEventRepository
                        .getChordCateDisCntByAgeFrom(
                                fUserSceneVersionId,fMakeVersionId, reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA());

            } else if (reqCateChord.getFDim().equals("sex") && reqCateChord.getFClick().equals("from")) {
                resList = chordSessionDistinctEventRepository
                        .getChordCateDisCntBySexFrom(
                                fUserSceneVersionId,fMakeVersionId, reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA());

            } else if (reqCateChord.getFDim().equals("province") && reqCateChord.getFClick().equals("from")) {
                resList = chordSessionDistinctEventRepository
                        .getChordCateDisCntByProvinceFrom(
                                fUserSceneVersionId,fMakeVersionId, reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA());

            } else if (reqCateChord.getFDim().equals("channel") && reqCateChord.getFClick().equals("from")) {
                resList = chordSessionDistinctEventRepository
                        .getChordCateDisCntByChannelFrom(
                                fUserSceneVersionId,fMakeVersionId, reqCateChord.getFCategoryFrom(),
                                reqCateChord.getFCategoryFromA());
            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session category distinct 分析成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session category distinct 分析失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session category distinct 分析失败");
        }
        return baseRsp;
    }




    /**
     * 和弦图查看分析(小类)
     *
     * @return
     */

    @PostMapping("/getChordDimSubcategoryDetailEntity")
    public BaseRsp getChordDimSubcategoryDetailEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqChordDimSubDto reqSubChord
    ) {
        LOGGER.info("获取session subcategory detail 分析");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqSubChord.getFUploadName(),reqSubChord.getFUploadUser())
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqSubChord.getFUploadName(),reqSubChord.getFUploadUser())
                    .get("f_user_scene_version_id");


            if (reqSubChord.getFDim().equals("age") && reqSubChord.getFClick().equals("to")) {
                resList = chordSessionEventRepository
                        .getChordSubCntByAgeTo(
                                fUserSceneVersionId,fMakeVersionId, reqSubChord.getFSubcategoryFrom(),
                                reqSubChord.getFSubcategoryFromA(),reqSubChord.getFSubcategoryToA());

            } else if (reqSubChord.getFDim().equals("sex") && reqSubChord.getFClick().equals("to")) {
                resList = chordSessionEventRepository
                        .getChordSubCntBySexTo(
                                fUserSceneVersionId,fMakeVersionId, reqSubChord.getFSubcategoryFrom(),
                                reqSubChord.getFSubcategoryFromA(),reqSubChord.getFSubcategoryToA());

            } else if (reqSubChord.getFDim().equals("province") && reqSubChord.getFClick().equals("to")) {
                resList = chordSessionEventRepository
                        .getChordSubCntByProvinceTo(
                                fUserSceneVersionId,fMakeVersionId, reqSubChord.getFSubcategoryFrom(),
                                reqSubChord.getFSubcategoryFromA(),reqSubChord.getFSubcategoryToA());

            } else if (reqSubChord.getFDim().equals("channel") && reqSubChord.getFClick().equals("to")) {
                resList = chordSessionEventRepository
                        .getChordSubCntByChannelTo(
                                fUserSceneVersionId,fMakeVersionId, reqSubChord.getFSubcategoryFrom(),
                                reqSubChord.getFSubcategoryFromA(),reqSubChord.getFSubcategoryToA());

            } else if (reqSubChord.getFDim().equals("age") && reqSubChord.getFClick().equals("from")) {
                resList = chordSessionEventRepository
                        .getChordSubCntByAgeFrom(
                                fUserSceneVersionId,fMakeVersionId, reqSubChord.getFSubcategoryFrom(),
                                reqSubChord.getFSubcategoryFromA());

            } else if (reqSubChord.getFDim().equals("sex") && reqSubChord.getFClick().equals("from")) {
                resList = chordSessionEventRepository
                        .getChordSubCntBySexFrom(
                                fUserSceneVersionId,fMakeVersionId, reqSubChord.getFSubcategoryFrom(),
                                reqSubChord.getFSubcategoryFromA());

            } else if (reqSubChord.getFDim().equals("province") && reqSubChord.getFClick().equals("from")) {
                resList = chordSessionEventRepository
                        .getChordSubCntByProvinceFrom(
                                fUserSceneVersionId,fMakeVersionId, reqSubChord.getFSubcategoryFrom(),
                                reqSubChord.getFSubcategoryFromA());

            } else if (reqSubChord.getFDim().equals("channel") && reqSubChord.getFClick().equals("from")) {
                resList = chordSessionEventRepository
                        .getChordSubCntByChannelFrom(
                                fUserSceneVersionId,fMakeVersionId, reqSubChord.getFSubcategoryFrom(),
                                reqSubChord.getFSubcategoryFromA());
            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session subcategory detail 分析成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session subcategory detail 分析失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session subcategory detail 分析失败");
        }
        return baseRsp;
    }




    /**
     * 和弦图查看分析(小类)
     *
     * @return
     */

    @PostMapping("/getChordDimSubDistinctEntity")
    public BaseRsp getChordDimSubDistinctEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqChordDimSubDto reqSubChordDimD
    ) {
        LOGGER.info("获取session subcategory dim distinct 分析");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqSubChordDimD.getFUploadName(),reqSubChordDimD.getFUploadUser())
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqSubChordDimD.getFUploadName(),reqSubChordDimD.getFUploadUser())
                    .get("f_user_scene_version_id");


            if (reqSubChordDimD.getFDim().equals("age") && reqSubChordDimD.getFClick().equals("to")) {
                resList = chordSessionDistinctEventRepository
                        .getChordSubDisCntByAgeTo(
                                fUserSceneVersionId,fMakeVersionId, reqSubChordDimD.getFSubcategoryFrom(),
                                reqSubChordDimD.getFSubcategoryFromA(),reqSubChordDimD.getFSubcategoryToA());

            } else if (reqSubChordDimD.getFDim().equals("sex") && reqSubChordDimD.getFClick().equals("to")) {
                resList = chordSessionDistinctEventRepository
                        .getChordSubDisCntBySexTo(
                                fUserSceneVersionId,fMakeVersionId, reqSubChordDimD.getFSubcategoryFrom(),
                                reqSubChordDimD.getFSubcategoryFromA(),reqSubChordDimD.getFSubcategoryToA());

            } else if (reqSubChordDimD.getFDim().equals("province") && reqSubChordDimD.getFClick().equals("to")) {
                resList = chordSessionDistinctEventRepository
                        .getChordSubDisCntByProvinceTo(
                                fUserSceneVersionId,fMakeVersionId, reqSubChordDimD.getFSubcategoryFrom(),
                                reqSubChordDimD.getFSubcategoryFromA(),reqSubChordDimD.getFSubcategoryToA());

            } else if (reqSubChordDimD.getFDim().equals("channel") && reqSubChordDimD.getFClick().equals("to")) {
                resList = chordSessionDistinctEventRepository
                        .getChordSubDisCntByChannelTo(
                                fUserSceneVersionId,fMakeVersionId, reqSubChordDimD.getFSubcategoryFrom(),
                                reqSubChordDimD.getFSubcategoryFromA(),reqSubChordDimD.getFSubcategoryToA());
            } else if (reqSubChordDimD.getFDim().equals("age") && reqSubChordDimD.getFClick().equals("from")) {
                resList = chordSessionDistinctEventRepository
                        .getChordSubDisCntByAgeFrom(
                                fUserSceneVersionId,fMakeVersionId, reqSubChordDimD.getFSubcategoryFrom(),
                                reqSubChordDimD.getFSubcategoryFromA());

            } else if (reqSubChordDimD.getFDim().equals("sex") && reqSubChordDimD.getFClick().equals("from")) {
                resList = chordSessionDistinctEventRepository
                        .getChordSubDisCntBySexFrom(
                                fUserSceneVersionId,fMakeVersionId, reqSubChordDimD.getFSubcategoryFrom(),
                                reqSubChordDimD.getFSubcategoryFromA());

            } else if (reqSubChordDimD.getFDim().equals("province") && reqSubChordDimD.getFClick().equals("from")) {
                resList = chordSessionDistinctEventRepository
                        .getChordSubDisCntByProvinceFrom(
                                fUserSceneVersionId,fMakeVersionId, reqSubChordDimD.getFSubcategoryFrom(),
                                reqSubChordDimD.getFSubcategoryFromA());

            } else if (reqSubChordDimD.getFDim().equals("channel") && reqSubChordDimD.getFClick().equals("from")) {
                resList = chordSessionDistinctEventRepository
                        .getChordSubDisCntByChannelFrom(
                                fUserSceneVersionId,fMakeVersionId, reqSubChordDimD.getFSubcategoryFrom(),
                                reqSubChordDimD.getFSubcategoryFromA());
            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session subcategory dim distinct 分析成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session subcategory dim distinct 分析失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session subcategory dim distinct 分析失败");
        }
        return baseRsp;
    }




    /**
     * 和弦图查看分析(event)
     *
     * @return
     */

    @PostMapping("/getSessionChordDimEventDetailEntity")
    public BaseRsp getSessionChordDimEventDetailEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqChordDimEventDto reqEventChord
    ) {
        LOGGER.info("获取session event chord dim detail 分析");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqEventChord.getFUploadName(),reqEventChord.getFUploadUser())
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqEventChord.getFUploadName(),reqEventChord.getFUploadUser())
                    .get("f_user_scene_version_id");


            if (reqEventChord.getFDim().equals("age") && reqEventChord.getFClick().equals("to")) {
                resList = chordSessionEventRepository
                        .getChordEventCntByAgeTo(
                                fUserSceneVersionId,fMakeVersionId, reqEventChord.getFEventFrom(),
                                reqEventChord.getFEventFromA(),reqEventChord.getFEventToA());

            } else if (reqEventChord.getFDim().equals("sex") && reqEventChord.getFClick().equals("to")) {
                resList = chordSessionEventRepository
                        .getChordEventCntBySexTo(
                                fUserSceneVersionId,fMakeVersionId, reqEventChord.getFEventFrom(),
                                reqEventChord.getFEventFromA(),reqEventChord.getFEventToA());

            } else if (reqEventChord.getFDim().equals("province") && reqEventChord.getFClick().equals("to")) {
                resList = chordSessionEventRepository
                        .getChordEventCntByProvinceTo(
                                fUserSceneVersionId,fMakeVersionId, reqEventChord.getFEventFrom(),
                                reqEventChord.getFEventFromA(),reqEventChord.getFEventToA());

            } else if (reqEventChord.getFDim().equals("channel") && reqEventChord.getFClick().equals("to")) {
                resList = chordSessionEventRepository
                        .getChordEventCntByChannelTo(
                                fUserSceneVersionId,fMakeVersionId, reqEventChord.getFEventFrom(),
                                reqEventChord.getFEventFromA(),reqEventChord.getFEventToA());
            } else if (reqEventChord.getFDim().equals("age") && reqEventChord.getFClick().equals("from")) {
                resList = chordSessionEventRepository
                        .getChordEventCntByAgeFrom(
                                fUserSceneVersionId,fMakeVersionId, reqEventChord.getFEventFrom(),
                                reqEventChord.getFEventFromA());

            } else if (reqEventChord.getFDim().equals("sex") && reqEventChord.getFClick().equals("from")) {
                resList = chordSessionEventRepository
                        .getChordEventCntBySexFrom(
                                fUserSceneVersionId,fMakeVersionId, reqEventChord.getFEventFrom(),
                                reqEventChord.getFEventFromA());

            } else if (reqEventChord.getFDim().equals("province") && reqEventChord.getFClick().equals("from")) {
                resList = chordSessionEventRepository
                        .getChordEventCntByProvinceFrom(
                                fUserSceneVersionId,fMakeVersionId, reqEventChord.getFEventFrom(),
                                reqEventChord.getFEventFromA());

            } else if (reqEventChord.getFDim().equals("channel") && reqEventChord.getFClick().equals("from")) {
                resList = chordSessionEventRepository
                        .getChordEventCntByChannelFrom(
                                fUserSceneVersionId,fMakeVersionId, reqEventChord.getFEventFrom(),
                                reqEventChord.getFEventFromA());
            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session event chord dim detail 分析成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event  chord dim detail 分析失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event  chord dim detail 分析失败");
        }
        return baseRsp;
    }




    /**
     * 和弦图查看分析(event)
     *
     * @return
     */

    @PostMapping("/getChordDimEventDistinctEntity")
    public BaseRsp getChordDimEventDistinctEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqChordDimEventDto reqEventChordDis
    ) {
        LOGGER.info("获取session event  chord dim distinct 分析");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqEventChordDis.getFUploadName(),reqEventChordDis.getFUploadUser())
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqEventChordDis.getFUploadName(),reqEventChordDis.getFUploadUser())
                    .get("f_user_scene_version_id");


            if (reqEventChordDis.getFDim().equals("age") && reqEventChordDis.getFClick().equals("to")) {
                resList = chordSessionDistinctEventRepository
                        .getChordEventDisCntByAgeTo(
                                fUserSceneVersionId,fMakeVersionId, reqEventChordDis.getFEventFrom(),
                                reqEventChordDis.getFEventFromA(),reqEventChordDis.getFEventToA());

            } else if (reqEventChordDis.getFDim().equals("sex") && reqEventChordDis.getFClick().equals("to")) {
                resList = chordSessionDistinctEventRepository
                        .getChordEventDisCntBySexTo(
                                fUserSceneVersionId,fMakeVersionId, reqEventChordDis.getFEventFrom(),
                                reqEventChordDis.getFEventFromA(),reqEventChordDis.getFEventToA());

            } else if (reqEventChordDis.getFDim().equals("province") && reqEventChordDis.getFClick().equals("to")) {
                resList = chordSessionDistinctEventRepository
                        .getChordEventDisCntByProvinceTo(
                                fUserSceneVersionId,fMakeVersionId, reqEventChordDis.getFEventFrom(),
                                reqEventChordDis.getFEventFromA(),reqEventChordDis.getFEventToA());

            } else if (reqEventChordDis.getFDim().equals("channel") && reqEventChordDis.getFClick().equals("to")) {
                resList = chordSessionDistinctEventRepository
                        .getChordEventDisCntByChannelTo(
                                fUserSceneVersionId,fMakeVersionId, reqEventChordDis.getFEventFrom(),
                                reqEventChordDis.getFEventFromA(),reqEventChordDis.getFEventToA());
            } else if (reqEventChordDis.getFDim().equals("age") && reqEventChordDis.getFClick().equals("from")) {
                resList = chordSessionDistinctEventRepository
                        .getChordEventDisCntByAgeFrom(
                                fUserSceneVersionId,fMakeVersionId, reqEventChordDis.getFEventFrom(),
                                reqEventChordDis.getFEventFromA());

            } else if (reqEventChordDis.getFDim().equals("sex") && reqEventChordDis.getFClick().equals("from")) {
                resList = chordSessionDistinctEventRepository
                        .getChordEventDisCntBySexFrom(
                                fUserSceneVersionId,fMakeVersionId, reqEventChordDis.getFEventFrom(),
                                reqEventChordDis.getFEventFromA());

            } else if (reqEventChordDis.getFDim().equals("province") && reqEventChordDis.getFClick().equals("from")) {
                resList = chordSessionDistinctEventRepository
                        .getChordEventDisCntByProvinceFrom(
                                fUserSceneVersionId,fMakeVersionId, reqEventChordDis.getFEventFrom(),
                                reqEventChordDis.getFEventFromA());

            } else if (reqEventChordDis.getFDim().equals("channel") && reqEventChordDis.getFClick().equals("from")) {
                resList = chordSessionDistinctEventRepository
                        .getChordEventDisCntByChannelFrom(
                                fUserSceneVersionId,fMakeVersionId, reqEventChordDis.getFEventFrom(),
                                reqEventChordDis.getFEventFromA());
            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session event chord dim distinct分析成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event chord dim distinct分析失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event chord dim distinct 分析失败");
        }
        return baseRsp;
    }




}
