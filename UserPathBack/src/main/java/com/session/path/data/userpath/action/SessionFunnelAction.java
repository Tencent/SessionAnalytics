/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.action;

import com.session.path.data.userpath.dto.BaseRsp;
import com.session.path.data.userpath.dto.ReqFunnelCategoryDto;
import com.session.path.data.userpath.dto.ReqFunnelDimCategoryDto;
import com.session.path.data.userpath.dto.ReqFunnelDimEventDto;
import com.session.path.data.userpath.dto.ReqFunnelDimSubDto;
import com.session.path.data.userpath.dto.RspPage;
import com.session.path.data.userpath.repository.FunnelSessionDistinctEventRepository;
import com.session.path.data.userpath.repository.FunnelSessionEventRepository;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName FunnelUserPathAction
 * @Description 漏斗图用户路径分析
 * @Author author
 * @Date 2023/03/04 16:34
 * @Version 1.0
 **/
@Controller
@CrossOrigin
@RestController
@RequestMapping("/funnel")
public class SessionFunnelAction extends BaseAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(SessionFunnelAction.class);

    @Autowired
    FunnelSessionDistinctEventRepository funnelSessionDistinctEventRepository;

    @Autowired
    FunnelSessionEventRepository funnelSessionEventRepository;

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;


    /**
     * 获取漏斗页面event分析结果（不去重）
     *
     * @return
     */
    @GetMapping("/getFunnelSessionEventEntity")
    public BaseRsp getFunnelSessionEventEntity(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_event_from1", required = false) String fEventFrom1,
            @RequestParam(value = "f_event_from2", required = false) String fEventFrom2,
            @RequestParam(value = "f_event_from3", required = false) String fEventFrom3,
            @RequestParam(value = "f_event_from4", required = false) String fEventFrom4
    ) {
        LOGGER.info("获取session event");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionEventRepository
                    .getFunnelSessionEventEntity(fEventFrom1, fEventFrom2, fEventFrom3, fEventFrom4,
                            fUserSceneVersionId,fMakeVersionId);
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
     * 获取漏斗页面event分析结果（去重）
     *
     * @return
     */
    @GetMapping("/getFunnelSessionEventDistinctEntity")
    public BaseRsp getFunnelSessionEventDistinctEntity(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_event_from1", required = false) String fEventFrom1,
            @RequestParam(value = "f_event_from2", required = false) String fEventFrom2,
            @RequestParam(value = "f_event_from3", required = false) String fEventFrom3,
            @RequestParam(value = "f_event_from4", required = false) String fEventFrom4
    ) {
        LOGGER.info("获取session 去重event");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionDistinctEventRepository
                    .getFunnelSessionEventDistinctEntity(fEventFrom1, fEventFrom2, fEventFrom3, fEventFrom4,
                            fUserSceneVersionId,fMakeVersionId);
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
     * 获取漏斗层级1不去重大类结果
     *
     * @return
     */
    @GetMapping("/getFunnelSessionCategoryList1")
    public BaseRsp getFunnelSessionCategoryList1(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("获取session 筛选category");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionEventRepository
                    .getFunnelSessionCategoryList1(fUserSceneVersionId,fMakeVersionId);
            rspPage.list = resList;
            baseRsp.setMessage("获取session 筛选category成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event 筛选category失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event 筛选category失败");
        }
        return baseRsp;
    }


    /**
     * 获取漏斗层级2不去重大类结果
     *
     * @return
     */
    @GetMapping("/getFunnelSessionCategoryList2")
    public BaseRsp getFunnelSessionCategoryList2(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_category_from1", required = false) String fCategoryFrom1) {
        LOGGER.info("获取session 筛选category");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionEventRepository
                    .getFunnelSessionCategoryList2(fUserSceneVersionId,fMakeVersionId, fCategoryFrom1);
            rspPage.list = resList;
            baseRsp.setMessage("获取session 筛选category成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event 筛选category失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event 筛选category失败");
        }
        return baseRsp;
    }


    /**
     * 获取漏斗层级3不去重大类结果
     *
     * @return
     */
    @GetMapping("/getFunnelSessionCategoryList3")
    public BaseRsp getFunnelSessionCategoryList3(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_category_from1", required = false) String fCategoryFrom1,
            @RequestParam(value = "f_category_from2", required = false) String fCategoryFrom2) {
        LOGGER.info("获取session 筛选category");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionEventRepository
                    .getFunnelSessionCategoryList3(fUserSceneVersionId,fMakeVersionId, fCategoryFrom1, fCategoryFrom2);
            rspPage.list = resList;
            baseRsp.setMessage("获取session 筛选category成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event 筛选category失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event 筛选category失败");
        }
        return baseRsp;
    }


    /**
     * 获取漏斗层级4不去重大类结果
     *
     * @return
     */
    @GetMapping("/getFunnelSessionCategoryList4")
    public BaseRsp getFunnelSessionCategoryList4(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_category_from1", required = false) String fCategoryFrom1,
            @RequestParam(value = "f_category_from2", required = false) String fCategoryFrom2,
            @RequestParam(value = "f_category_from3", required = false) String fCategoryFrom3) {
        LOGGER.info("获取session 筛选category");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionEventRepository
                    .getFunnelSessionCategoryList4(fUserSceneVersionId,fMakeVersionId, fCategoryFrom1, fCategoryFrom2,
                            fCategoryFrom3);
            rspPage.list = resList;
            baseRsp.setMessage("获取session 筛选category成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event 筛选category失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event 筛选category失败");
        }
        return baseRsp;
    }


    /**
     * 获取漏斗层级1不去重小类结果
     *
     * @return
     */
    @GetMapping("/getFunnelSessionSubcategoryList1")
    public BaseRsp getFunnelSessionSubcategoryList1(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("获取session event 筛选subcategory");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionEventRepository
                    .getFunnelSessionSubcategoryList1(fUserSceneVersionId,fMakeVersionId);
            rspPage.list = resList;
            baseRsp.setMessage("获取session event 筛选subcategory成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event 筛选subcategory失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event 筛选subcategory失败");
        }
        return baseRsp;
    }


    /**
     * 获取漏斗层级2不去重小类结果
     *
     * @return
     */
    @GetMapping("/getFunnelSessionSubcategoryList2")
    public BaseRsp getFunnelSessionSubcategoryList2(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_subcategory_from1", required = false) String fSubcategoryFrom1) {
        LOGGER.info("获取session event 筛选subcategory");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionEventRepository
                    .getFunnelSessionSubcategoryList2(fUserSceneVersionId,fMakeVersionId, fSubcategoryFrom1);
            rspPage.list = resList;
            baseRsp.setMessage("获取session event 筛选subcategory成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event 筛选subcategory失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event 筛选subcategory失败");
        }
        return baseRsp;
    }


    /**
     * 获取漏斗层级3不去重小类结果
     *
     * @return
     */
    @GetMapping("/getFunnelSessionSubcategoryList3")
    public BaseRsp getFunnelSessionSubcategoryList3(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_subcategory_from1", required = false) String fSubcategoryFrom1,
            @RequestParam(value = "f_subcategory_from2", required = false) String fSubcategoryFrom2) {
        LOGGER.info("获取session event 筛选subcategory");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionEventRepository
                    .getFunnelSessionSubcategoryList3(fUserSceneVersionId,fMakeVersionId, fSubcategoryFrom1,
                            fSubcategoryFrom2);
            rspPage.list = resList;
            baseRsp.setMessage("获取session event 筛选subcategory成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event 筛选subcategory失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event 筛选subcategory失败");
        }
        return baseRsp;
    }


    /**
     * 获取漏斗层级4不去重小类结果
     *
     * @return
     */
    @GetMapping("/getFunnelSessionSubcategoryList4")
    public BaseRsp getFunnelSessionSubcategoryList4(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_subcategory_from1", required = false) String fSubcategoryFrom1,
            @RequestParam(value = "f_subcategory_from2", required = false) String fSubcategoryFrom2,
            @RequestParam(value = "f_subcategory_from3", required = false) String fSubcategoryFrom3) {
        LOGGER.info("获取session event 筛选subcategory");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionEventRepository
                    .getFunnelSessionSubcategoryList4(
                            fUserSceneVersionId,fMakeVersionId, fSubcategoryFrom1,
                            fSubcategoryFrom2, fSubcategoryFrom3);
            rspPage.list = resList;
            baseRsp.setMessage("获取session event 筛选subcategory成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event 筛选subcategory失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event 筛选subcategory失败");
        }
        return baseRsp;
    }


    /**
     * 获取漏斗层级1不去重event结果
     *
     * @return
     */
    @GetMapping("/getFunnelSessionEventList1")
    public BaseRsp getFunnelSessionEventList1(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser) {
        LOGGER.info("获取session event 筛选event list");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionEventRepository
                    .getFunnelSessionEventList1(fUserSceneVersionId,fMakeVersionId);
            rspPage.list = resList;
            baseRsp.setMessage("获取session event 筛选event list成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event 筛选event list失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event 筛选event list失败");
        }
        return baseRsp;
    }


    /**
     * 获取漏斗层级2不去重event结果
     *
     * @return
     */
    @GetMapping("/getFunnelSessionEventList2")
    public BaseRsp getFunnelSessionEventList2(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_event_from1", required = false) String fEventFrom1) {
        LOGGER.info("获取session event 筛选event list");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionEventRepository
                    .getFunnelSessionEventList2(fUserSceneVersionId,fMakeVersionId, fEventFrom1);
            rspPage.list = resList;
            baseRsp.setMessage("获取session event 筛选event list成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event 筛选event list失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event 筛选event list失败");
        }
        return baseRsp;
    }


    /**
     * 获取漏斗层级3不去重event结果
     *
     * @return
     */
    @GetMapping("/getFunnelSessionEventList3")
    public BaseRsp getFunnelSessionEventList3(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_event_from1", required = false) String fEventFrom1,
            @RequestParam(value = "f_event_from2", required = false) String fEventFrom2) {
        LOGGER.info("获取session event 筛选event list");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionEventRepository
                    .getFunnelSessionEventList3(fUserSceneVersionId,fMakeVersionId, fEventFrom1, fEventFrom2);
            rspPage.list = resList;
            baseRsp.setMessage("获取session event 筛选event list成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event 筛选event list失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event 筛选event list失败");
        }
        return baseRsp;
    }


    /**
     * 获取漏斗层级4不去重event结果
     *
     * @return
     */
    @GetMapping("/getFunnelSessionEventList4")
    public BaseRsp getFunnelSessionEventList4(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_event_from1", required = false) String fEventFrom1,
            @RequestParam(value = "f_event_from2", required = false) String fEventFrom2,
            @RequestParam(value = "f_event_from3", required = false) String fEventFrom3) {
        LOGGER.info("获取session event 筛选event list");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionEventRepository
                    .getFunnelSessionEventList4(
                            fUserSceneVersionId, fMakeVersionId, fEventFrom1, fEventFrom2, fEventFrom3);
            rspPage.list = resList;
            baseRsp.setMessage("获取session event 筛选event list成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event 筛选event list失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event 筛选event list失败");
        }
        return baseRsp;
    }


    /**
     * 获取漏斗小类分析结果（去重）
     *
     * @return
     */
    @GetMapping("/getFunnelSubcategoryDistinctEntity")
    public BaseRsp getFunnelSubcategoryDistinctEntity(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_subcategory_from1", required = false) String fSubcategoryFrom1,
            @RequestParam(value = "f_subcategory_from2", required = false) String fSubcategoryFrom2,
            @RequestParam(value = "f_subcategory_from3", required = false) String fSubcategoryFrom3,
            @RequestParam(value = "f_subcategory_from4", required = false) String fSubcategoryFrom4

    ) {
        LOGGER.info("获取session Subcategory");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionDistinctEventRepository
                    .getFunnelSubDisEntity(fSubcategoryFrom1, fSubcategoryFrom2,
                            fSubcategoryFrom3, fSubcategoryFrom4, fUserSceneVersionId,fMakeVersionId);
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
     * 获取漏斗小类分析结果（不去重）
     *
     * @return
     */
    @GetMapping("/getFunnelSessionSubcategoryEntity")
    public BaseRsp getFunnelSessionSubcategoryEntity(HttpServletRequest httpServletRequest,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_subcategory_from1", required = false) String fSubcategoryFrom1,
            @RequestParam(value = "f_subcategory_from2", required = false) String fSubcategoryFrom2,
            @RequestParam(value = "f_subcategory_from3", required = false) String fSubcategoryFrom3,
            @RequestParam(value = "f_subcategory_from4", required = false) String fSubcategoryFrom4

    ) {
        LOGGER.info("获取session Subcategory");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            resList = funnelSessionEventRepository
                    .getFunnelSessionSubcategoryEntity(fSubcategoryFrom1, fSubcategoryFrom2, fSubcategoryFrom3,
                            fSubcategoryFrom4, fUserSceneVersionId,fMakeVersionId);
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
     * 获取漏斗大类分析结果（不去重）
     *
     * @return
     */
    @PostMapping("/getFunnelSessionCategoryEntity")
    public BaseRsp getFunnelSessionCategoryEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqFunnelCategoryDto reqCateFunnel
    ) {
        LOGGER.info("获取session Category");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqCateFunnel.getFUploadName(),reqCateFunnel.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqCateFunnel.getFUploadName(),reqCateFunnel.getFUploadUser())
                    .get("f_user_scene_version_id");


            resList = funnelSessionEventRepository
                    .getFunnelSessionCategoryEntity(
                            reqCateFunnel.getFCategoryFrom1(),
                            reqCateFunnel.getFCategoryFrom2(),
                            reqCateFunnel.getFCategoryFrom3(),
                            reqCateFunnel.getFCategoryFrom4(),
                            fUserSceneVersionId,fMakeVersionId);
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
     * 获取漏斗大类分析结果（去重）
     *
     * @return
     */
    @PostMapping("/getFunnelCategoryDistinctEntity")
    public BaseRsp getFunnelCategoryDistinctEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqFunnelCategoryDto reqCateFunnelD
    ) {
        LOGGER.info("获取session Category");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqCateFunnelD.getFUploadName(),reqCateFunnelD.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqCateFunnelD.getFUploadName(),reqCateFunnelD.getFUploadUser())
                    .get("f_user_scene_version_id");

            resList = funnelSessionDistinctEventRepository
                    .getFunnelCateDisEntity(
                            reqCateFunnelD.getFCategoryFrom1(),
                            reqCateFunnelD.getFCategoryFrom2(),
                            reqCateFunnelD.getFCategoryFrom3(),
                            reqCateFunnelD.getFCategoryFrom4(),
                            fUserSceneVersionId,fMakeVersionId);
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
     * 漏斗图查看分析(大类)分维度不去重
     *
     * @return
     */

    @PostMapping("/getFunnelDimCategoryDetailEntity")
    public BaseRsp getFunnelDimCategoryDetailEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqFunnelDimCategoryDto reqCateDimFunnel
    ) {
        LOGGER.info("获取session category分析");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqCateDimFunnel.getFUploadName(),reqCateDimFunnel.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqCateDimFunnel.getFUploadName(),reqCateDimFunnel.getFUploadUser())
                    .get("f_user_scene_version_id");


            if (reqCateDimFunnel.getFDim().equals("age")) {
                resList = funnelSessionEventRepository
                        .getFunnelCateCntByAgeTo(
                                reqCateDimFunnel.getFCategoryFrom1(),
                                reqCateDimFunnel.getFCategoryFrom2(),
                                reqCateDimFunnel.getFCategoryFrom3(),
                                reqCateDimFunnel.getFCategoryFrom4(),
                                fUserSceneVersionId,fMakeVersionId,
                                reqCateDimFunnel.getFCategoryFromA());

            } else if (reqCateDimFunnel.getFDim().equals("sex")) {
                resList = funnelSessionEventRepository
                        .getFunnelCateCntBySexTo(
                                reqCateDimFunnel.getFCategoryFrom1(),
                                reqCateDimFunnel.getFCategoryFrom2(),
                                reqCateDimFunnel.getFCategoryFrom3(),
                                reqCateDimFunnel.getFCategoryFrom4(),
                                fUserSceneVersionId,fMakeVersionId, reqCateDimFunnel.getFCategoryFromA());

            } else if (reqCateDimFunnel.getFDim().equals("province")) {
                resList = funnelSessionEventRepository
                        .getFunnelCateCntByProvinceTo(
                                reqCateDimFunnel.getFCategoryFrom1(),
                                reqCateDimFunnel.getFCategoryFrom2(),
                                reqCateDimFunnel.getFCategoryFrom3(),
                                reqCateDimFunnel.getFCategoryFrom4(),
                                fUserSceneVersionId,fMakeVersionId, reqCateDimFunnel.getFCategoryFromA());

            } else if (reqCateDimFunnel.getFDim().equals("channel")) {
                resList = funnelSessionEventRepository
                        .getFunnelCateCntByChannelTo(
                                reqCateDimFunnel.getFCategoryFrom1(),
                                reqCateDimFunnel.getFCategoryFrom2(),
                                reqCateDimFunnel.getFCategoryFrom3(),
                                reqCateDimFunnel.getFCategoryFrom4(),
                                fUserSceneVersionId,fMakeVersionId, reqCateDimFunnel.getFCategoryFromA());
            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session category分析成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session category分析失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session category分析失败");
        }
        return baseRsp;
    }



    /**
     * 漏斗图查看分析(大类)分维度去重
     *
     * @return
     */

    @PostMapping("/getFunnelDimCategoryDistinctEntity")
    public BaseRsp getFunnelDimCategoryDistinctEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqFunnelDimCategoryDto reqCateDimFunnelD
    ) {
        LOGGER.info("获取session category分析");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqCateDimFunnelD.getFUploadName(),reqCateDimFunnelD.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqCateDimFunnelD.getFUploadName(),reqCateDimFunnelD.getFUploadUser())
                    .get("f_user_scene_version_id");


            if (reqCateDimFunnelD.getFDim().equals("age")) {
                resList = funnelSessionDistinctEventRepository
                        .getFunnelCateDisCntByAgeTo(
                                reqCateDimFunnelD.getFCategoryFrom1(),
                                reqCateDimFunnelD.getFCategoryFrom2(),
                                reqCateDimFunnelD.getFCategoryFrom3(),
                                reqCateDimFunnelD.getFCategoryFrom4(),
                                fUserSceneVersionId,fMakeVersionId, reqCateDimFunnelD.getFCategoryFromA());

            } else if (reqCateDimFunnelD.getFDim().equals("sex")) {
                resList = funnelSessionDistinctEventRepository
                        .getFunnelCateDisCntBySexTo(
                                reqCateDimFunnelD.getFCategoryFrom1(),
                                reqCateDimFunnelD.getFCategoryFrom2(),
                                reqCateDimFunnelD.getFCategoryFrom3(),
                                reqCateDimFunnelD.getFCategoryFrom4(),
                                fUserSceneVersionId,fMakeVersionId, reqCateDimFunnelD.getFCategoryFromA());

            } else if (reqCateDimFunnelD.getFDim().equals("province")) {
                resList = funnelSessionDistinctEventRepository
                        .getFunnelCateDisCntByProvinceTo(
                                reqCateDimFunnelD.getFCategoryFrom1(),
                                reqCateDimFunnelD.getFCategoryFrom2(),
                                reqCateDimFunnelD.getFCategoryFrom3(),
                                reqCateDimFunnelD.getFCategoryFrom4(),
                                fUserSceneVersionId,fMakeVersionId, reqCateDimFunnelD.getFCategoryFromA());

            } else if (reqCateDimFunnelD.getFDim().equals("channel")) {
                resList = funnelSessionDistinctEventRepository
                        .getFunnelCateDisCntByChannelTo(
                                reqCateDimFunnelD.getFCategoryFrom1(),
                                reqCateDimFunnelD.getFCategoryFrom2(),
                                reqCateDimFunnelD.getFCategoryFrom3(),
                                reqCateDimFunnelD.getFCategoryFrom4(),
                                fUserSceneVersionId,fMakeVersionId, reqCateDimFunnelD.getFCategoryFromA());
            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session category分析成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session category分析失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session category分析失败");
        }
        return baseRsp;
    }





    /**
     * 漏斗图查看分析(小类)分维度不去重
     *
     * @return
     */
    @PostMapping("/getFunnelDimSubcategoryDetailEntity")
    public BaseRsp getFunnelDimSubcategoryDetailEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqFunnelDimSubDto reqSubDimFunnel

    ) {
        LOGGER.info("获取session Subcategory分析");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqSubDimFunnel.getFUploadName(),reqSubDimFunnel.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqSubDimFunnel.getFUploadName(),reqSubDimFunnel.getFUploadUser())
                    .get("f_user_scene_version_id");

            if (reqSubDimFunnel.getFDim().equals("age")) {
                resList = funnelSessionEventRepository
                        .getFunnelSubCntByAgeTo(
                                reqSubDimFunnel.getFSubcategoryFrom1(),
                                reqSubDimFunnel.getFSubcategoryFrom2(),
                                reqSubDimFunnel.getFSubcategoryFrom3(),
                                reqSubDimFunnel.getFSubcategoryFrom4(),
                                fUserSceneVersionId, fMakeVersionId, reqSubDimFunnel.getFSubcategoryFromA());

            } else if (reqSubDimFunnel.getFDim().equals("sex")) {
                resList = funnelSessionEventRepository
                        .getFunnelSubCntBySexTo(reqSubDimFunnel.getFSubcategoryFrom1(),
                                reqSubDimFunnel.getFSubcategoryFrom2(),
                                reqSubDimFunnel.getFSubcategoryFrom3(),
                                reqSubDimFunnel.getFSubcategoryFrom4(),
                                fUserSceneVersionId, fMakeVersionId, reqSubDimFunnel.getFSubcategoryFromA());

            } else if (reqSubDimFunnel.getFDim().equals("province")) {
                resList = funnelSessionEventRepository
                        .getFunnelSubCntByProvinceTo(reqSubDimFunnel.getFSubcategoryFrom1(),
                                reqSubDimFunnel.getFSubcategoryFrom2(),
                                reqSubDimFunnel.getFSubcategoryFrom3(),
                                reqSubDimFunnel.getFSubcategoryFrom4(),
                                fUserSceneVersionId, fMakeVersionId, reqSubDimFunnel.getFSubcategoryFromA());

            } else if (reqSubDimFunnel.getFDim().equals("channel")) {
                resList = funnelSessionEventRepository
                        .getFunnelSubCntByChannelTo(reqSubDimFunnel.getFSubcategoryFrom1(),
                                reqSubDimFunnel.getFSubcategoryFrom2(),
                                reqSubDimFunnel.getFSubcategoryFrom3(),
                                reqSubDimFunnel.getFSubcategoryFrom4(),
                                fUserSceneVersionId, fMakeVersionId, reqSubDimFunnel.getFSubcategoryFromA());
            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session Subcategory分析成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session Subcategory分析失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session Subcategory分析失败");
        }
        return baseRsp;
    }



    /**
     * 漏斗图查看分析(小类)分维度去重
     *
     * @return
     */
    @PostMapping("/getFunnelDimSubDistinctEntity")
    public BaseRsp getFunnelDimSubDistinctEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqFunnelDimSubDto reqSubDimFunnelD

    ) {
        LOGGER.info("获取session Subcategory分析");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqSubDimFunnelD.getFUploadName(),reqSubDimFunnelD.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqSubDimFunnelD.getFUploadName(),reqSubDimFunnelD.getFUploadUser())
                    .get("f_user_scene_version_id");

            if (reqSubDimFunnelD.getFDim().equals("age")) {
                resList = funnelSessionDistinctEventRepository
                        .getFunnelSubDisCntByAgeTo(
                                reqSubDimFunnelD.getFSubcategoryFrom1(),
                                reqSubDimFunnelD.getFSubcategoryFrom2(),
                                reqSubDimFunnelD.getFSubcategoryFrom3(),
                                reqSubDimFunnelD.getFSubcategoryFrom4(),
                                fUserSceneVersionId, fMakeVersionId, reqSubDimFunnelD.getFSubcategoryFromA());

            } else if (reqSubDimFunnelD.getFDim().equals("sex")) {
                resList = funnelSessionDistinctEventRepository
                        .getFunnelSubDisCntBySexTo(
                                reqSubDimFunnelD.getFSubcategoryFrom1(),
                                reqSubDimFunnelD.getFSubcategoryFrom2(),
                                reqSubDimFunnelD.getFSubcategoryFrom3(),
                                reqSubDimFunnelD.getFSubcategoryFrom4(),
                                fUserSceneVersionId, fMakeVersionId, reqSubDimFunnelD.getFSubcategoryFromA());

            } else if (reqSubDimFunnelD.getFDim().equals("province")) {
                resList = funnelSessionDistinctEventRepository
                        .getFunnelSubDisCntByProvinceTo(
                                reqSubDimFunnelD.getFSubcategoryFrom1(),
                                reqSubDimFunnelD.getFSubcategoryFrom2(),
                                reqSubDimFunnelD.getFSubcategoryFrom3(),
                                reqSubDimFunnelD.getFSubcategoryFrom4(),
                                fUserSceneVersionId, fMakeVersionId, reqSubDimFunnelD.getFSubcategoryFromA());

            } else if (reqSubDimFunnelD.getFDim().equals("channel")) {
                resList = funnelSessionDistinctEventRepository
                        .getFunnelSubDisCntByChannelTo(
                                reqSubDimFunnelD.getFSubcategoryFrom1(),
                                reqSubDimFunnelD.getFSubcategoryFrom2(),
                                reqSubDimFunnelD.getFSubcategoryFrom3(),
                                reqSubDimFunnelD.getFSubcategoryFrom4(),
                                fUserSceneVersionId, fMakeVersionId, reqSubDimFunnelD.getFSubcategoryFromA());
            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session Subcategory分析成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session Subcategory分析失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session Subcategory分析失败");
        }
        return baseRsp;
    }



    /**
     * 漏斗图查看分析(event)分维度不去重
     *
     * @return
     */

    @PostMapping("/getFunnelDimEventDetailEntity")
    public BaseRsp getFunnelDimEventDetailEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqFunnelDimEventDto reqEventDimFunnelD
    ) {
        LOGGER.info("获取session event分析");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqEventDimFunnelD.getFUploadName(),reqEventDimFunnelD.getFUploadUser())
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqEventDimFunnelD.getFUploadName(),reqEventDimFunnelD.getFUploadUser())
                    .get("f_user_scene_version_id");


            if (reqEventDimFunnelD.getFDim().equals("age")) {
                resList = funnelSessionEventRepository
                        .getFunnelEventCntByAgeTo(
                                reqEventDimFunnelD.getFEventFrom1(),
                                reqEventDimFunnelD.getFEventFrom2(),
                                reqEventDimFunnelD.getFEventFrom3(),
                                reqEventDimFunnelD.getFEventFrom4(),
                                fUserSceneVersionId,fMakeVersionId,
                                reqEventDimFunnelD.getFEventFromA());

            } else if (reqEventDimFunnelD.getFDim().equals("sex")) {
                resList = funnelSessionEventRepository
                        .getFunnelEventCntBySexTo(
                                reqEventDimFunnelD.getFEventFrom1(),
                                reqEventDimFunnelD.getFEventFrom2(),
                                reqEventDimFunnelD.getFEventFrom3(),
                                reqEventDimFunnelD.getFEventFrom4(),
                                fUserSceneVersionId,fMakeVersionId, reqEventDimFunnelD.getFEventFromA());

            } else if (reqEventDimFunnelD.getFDim().equals("province")) {
                resList = funnelSessionEventRepository
                        .getFunnelEventCntByProvinceTo(
                                reqEventDimFunnelD.getFEventFrom1(),
                                reqEventDimFunnelD.getFEventFrom2(),
                                reqEventDimFunnelD.getFEventFrom3(),
                                reqEventDimFunnelD.getFEventFrom4(),
                                fUserSceneVersionId,fMakeVersionId, reqEventDimFunnelD.getFEventFromA());

            } else if (reqEventDimFunnelD.getFDim().equals("channel")) {
                resList = funnelSessionEventRepository
                        .getFunnelEventCntByChannelTo(
                                reqEventDimFunnelD.getFEventFrom1(),
                                reqEventDimFunnelD.getFEventFrom2(),
                                reqEventDimFunnelD.getFEventFrom3(),
                                reqEventDimFunnelD.getFEventFrom4(),
                                fUserSceneVersionId,fMakeVersionId, reqEventDimFunnelD.getFEventFromA());
            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session event分析成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event分析失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event分析失败");
        }
        return baseRsp;
    }



    /**
     * 漏斗图查看分析(event)分维度去重
     *
     * @return
     */

    @PostMapping("/getFunnelDimEventDistinctEntity")
    public BaseRsp getFunnelDimEventDistinctEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqFunnelDimEventDto reqEventDimFunnelDis
    ) {
        LOGGER.info("获取session event分析");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqEventDimFunnelDis.getFUploadName(),reqEventDimFunnelDis.getFUploadUser())
                    .get("f_make_version_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqEventDimFunnelDis.getFUploadName(),reqEventDimFunnelDis.getFUploadUser())
                    .get("f_user_scene_version_id");


            if (reqEventDimFunnelDis.getFDim().equals("age")) {
                resList = funnelSessionDistinctEventRepository
                        .getFunnelEventDisCntByAgeTo(
                                reqEventDimFunnelDis.getFEventFrom1(),
                                reqEventDimFunnelDis.getFEventFrom2(),
                                reqEventDimFunnelDis.getFEventFrom3(),
                                reqEventDimFunnelDis.getFEventFrom4(),
                                fUserSceneVersionId,fMakeVersionId, reqEventDimFunnelDis.getFEventFromA());

            } else if (reqEventDimFunnelDis.getFDim().equals("sex")) {
                resList = funnelSessionDistinctEventRepository
                        .getFunnelEventDisCntBySexTo(
                                reqEventDimFunnelDis.getFEventFrom1(),
                                reqEventDimFunnelDis.getFEventFrom2(),
                                reqEventDimFunnelDis.getFEventFrom3(),
                                reqEventDimFunnelDis.getFEventFrom4(),
                                fUserSceneVersionId,fMakeVersionId, reqEventDimFunnelDis.getFEventFromA());

            } else if (reqEventDimFunnelDis.getFDim().equals("province")) {
                resList = funnelSessionDistinctEventRepository
                        .getFunnelEventDisCntByProvinceTo(
                                reqEventDimFunnelDis.getFEventFrom1(),
                                reqEventDimFunnelDis.getFEventFrom2(),
                                reqEventDimFunnelDis.getFEventFrom3(),
                                reqEventDimFunnelDis.getFEventFrom4(),
                                fUserSceneVersionId,fMakeVersionId, reqEventDimFunnelDis.getFEventFromA());

            } else if (reqEventDimFunnelDis.getFDim().equals("channel")) {
                resList = funnelSessionDistinctEventRepository
                        .getFunnelEventDisCntByChannelTo(
                                reqEventDimFunnelDis.getFEventFrom1(),
                                reqEventDimFunnelDis.getFEventFrom2(),
                                reqEventDimFunnelDis.getFEventFrom3(),
                                reqEventDimFunnelDis.getFEventFrom4(),
                                fUserSceneVersionId,fMakeVersionId, reqEventDimFunnelDis.getFEventFromA());
            }

            rspPage.list = resList;
            baseRsp.setMessage("获取session event分析成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event分析失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event分析失败");
        }
        return baseRsp;
    }


}
