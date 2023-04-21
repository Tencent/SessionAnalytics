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
import com.session.path.data.userpath.dto.ReqTreeCategoryDto;
import com.session.path.data.userpath.dto.ReqTreeEventDto;
import com.session.path.data.userpath.dto.ReqTreeSubDto;
import com.session.path.data.userpath.dto.RspPage;
import com.session.path.data.userpath.repository.TreeSessionDistinctEventRepository;
import com.session.path.data.userpath.repository.TreeSessionEventRepository;
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
 * @ClassName SessionTreeAction
 * @Description 树图用户路径分析
 * @Author author
 * @Date 2023/03/04 16:34
 * @Version 1.0
 **/
@Controller
@CrossOrigin
@RestController
@RequestMapping("/tree")
public class SessionTreeAction extends BaseAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(SessionTreeAction.class);

    @Autowired
    TreeSessionEventRepository treeSessionEventRepository;

    @Autowired
    TreeSessionDistinctEventRepository treeSessionDistinctEventRepository;

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;


    /**
     * 获取树图页面事件分析结果（不去重）
     *
     * @return
     */

    @PostMapping("/getTreeSessionEventEntity")
    public BaseRsp getTreeSessionEventEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqTreeEventDto reqEvent
    ) {
        LOGGER.info("获取树图 session event detail");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqEvent.getFUploadName(),reqEvent.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqEvent.getFUploadName(),reqEvent.getFUploadUser())
                    .get("f_user_scene_version_id");


            String [] categoryArr = reqEvent.getFCategoryFrom().split(",");

            String [] subcategoryArr = reqEvent.getFSubcategoryFrom().split(",");

            String [] eventArr = reqEvent.getFEventFrom().split(",");


            resList = treeSessionEventRepository
                    .getTreeSessionEventEntity(
                            fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],eventArr[0],
                            reqEvent.getFromLayer(),reqEvent.getToLayer(),reqEvent.getFSessionNumStart(),
                            reqEvent.getFCategory(),
                            reqEvent.getFSubcategory(),reqEvent.getFEvent(),reqEvent.getFSessionNumEnd(),
                            reqEvent.getFUserNumStart(),reqEvent.getFUserNumEnd(),reqEvent.getFPVNumStart(),
                            reqEvent.getFPVNumEnd(),
                            categoryArr[1],subcategoryArr[1],eventArr[1], categoryArr[2],subcategoryArr[2],eventArr[2],
                            categoryArr[3],subcategoryArr[3],eventArr[3],
                            categoryArr[4],subcategoryArr[4],eventArr[4]);

            rspPage.list = resList;
            baseRsp.setMessage("获取树图 session event detail 成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取树图 session event detail 失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取树图 session event detail 失败");
        }
        return baseRsp;
    }


    /**
     * 获取树图小类分析结果-不去重
     *
     * @return
     */

    @PostMapping("/getTreeSessionSubcategoryEntity")
    public BaseRsp getTreeSessionSubcategoryEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqTreeSubDto reqSub

    ) {
        LOGGER.info("获取session Subcategory detail");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqSub.getFUploadName(),reqSub.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqSub.getFUploadName(),reqSub.getFUploadUser())
                    .get("f_user_scene_version_id");


            String [] categoryArr = reqSub.getFCategoryFrom().split(",");

            String [] subcategoryArr = reqSub.getFSubcategoryFrom().split(",");

            resList = treeSessionEventRepository
                    .getTreeSessionSubcategoryEntity(
                            fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],
                            reqSub.getFromLayer(), reqSub.getToLayer(), reqSub.getFSessionNumStart(),
                            reqSub.getFCategory(), reqSub.getFSubcategory(), reqSub.getFSessionNumEnd(),
                            reqSub.getFUserNumStart(),reqSub.getFUserNumEnd(),reqSub.getFPVNumStart(),
                            reqSub.getFPVNumEnd(),
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
     * 获取树图大类分析结果-不去重
     *
     * @return
     */

    @PostMapping("/getTreeSessionCategoryEntity")
    public BaseRsp getTreeSessionCategoryEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqTreeCategoryDto reqCate
    ) {
        LOGGER.info("获取session Category detail");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqCate.getFUploadName(),reqCate.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqCate.getFUploadName(),reqCate.getFUploadUser())
                    .get("f_user_scene_version_id");


            String [] categoryArr = reqCate.getFCategoryFrom().split(",");

            resList = treeSessionEventRepository
                    .getTreeSessionCategoryEntity(fUserSceneVersionId,fMakeVersionId,
                            categoryArr[0], reqCate.getFromLayer(), reqCate.getToLayer(),
                            reqCate.getFSessionNumStart(),
                            reqCate.getFCategory(), reqCate.getFSessionNumEnd(),
                            reqCate.getFUserNumStart(),reqCate.getFUserNumEnd(),
                            reqCate.getFPVNumStart(),reqCate.getFPVNumEnd(),
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
     * 获取树图页面event分析结果-去重
     *
     * @return
     */

    @PostMapping("/getTreeSessionEventDistinctEntity")
    public BaseRsp getTreeSessionEventDistinctEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqTreeEventDto reqDistinct
    ) {
        LOGGER.info("获取session event distinct ");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);

            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqDistinct.getFUploadName(),reqDistinct.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqDistinct.getFUploadName(),reqDistinct.getFUploadUser())
                    .get("f_user_scene_version_id");


            String [] categoryArr = reqDistinct.getFCategoryFrom().split(",");

            String [] subcategoryArr = reqDistinct.getFSubcategoryFrom().split(",");

            String [] eventArr = reqDistinct.getFEventFrom().split(",");

            resList = treeSessionDistinctEventRepository
                    .getTreeSessionEventDistinctEntity(
                            fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],eventArr[0],
                            reqDistinct.getFromLayer(),reqDistinct.getToLayer(),reqDistinct.getFSessionNumStart(),
                            reqDistinct.getFCategory(),
                            reqDistinct.getFSubcategory(),reqDistinct.getFEvent(),reqDistinct.getFSessionNumEnd(),
                            reqDistinct.getFUserNumStart(),reqDistinct.getFUserNumEnd(),reqDistinct.getFPVNumStart(),
                            reqDistinct.getFPVNumEnd(),
                            categoryArr[1],subcategoryArr[1],eventArr[1],
                            categoryArr[2],subcategoryArr[2],eventArr[2],
                            categoryArr[3],subcategoryArr[3],eventArr[3],
                            categoryArr[4],subcategoryArr[4],eventArr[4]);

            LOGGER.info("resList event distinct" + resList);
            rspPage.list = resList;
            baseRsp.setMessage("获取session event distinct 成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取session event distinct 失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取session event distinct 失败");
        }
        return baseRsp;
    }


    /**
     * 获取树图小类分析结果-去重
     *
     * @return
     */

    @PostMapping("/getTreeSubcategoryDistinctEntity")
    public BaseRsp getTreeSubcategoryDistinctEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqTreeSubDto reqSubDistinct

    ) {
        LOGGER.info("获取树图 session Subcategory distinct");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqSubDistinct.getFUploadName(),reqSubDistinct.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqSubDistinct.getFUploadName(),reqSubDistinct.getFUploadUser())
                    .get("f_user_scene_version_id");


            String [] categoryArr = reqSubDistinct.getFCategoryFrom().split(",");

            String [] subcategoryArr = reqSubDistinct.getFSubcategoryFrom().split(",");

            resList = treeSessionDistinctEventRepository
                    .getTreeSubDisEntity(
                            fUserSceneVersionId,fMakeVersionId, categoryArr[0], subcategoryArr[0],
                            reqSubDistinct.getFromLayer(), reqSubDistinct.getToLayer(),
                            reqSubDistinct.getFSessionNumStart(),
                            reqSubDistinct.getFCategory(), reqSubDistinct.getFSubcategory(),
                            reqSubDistinct.getFSessionNumEnd(),
                            reqSubDistinct.getFUserNumStart(),reqSubDistinct.getFUserNumEnd(),
                            reqSubDistinct.getFPVNumStart(),reqSubDistinct.getFPVNumEnd(),
                            categoryArr[1],subcategoryArr[1],categoryArr[2],subcategoryArr[2],
                            categoryArr[3],subcategoryArr[3],categoryArr[4],subcategoryArr[4]);

            rspPage.list = resList;
            baseRsp.setMessage("获取树图 session Subcategory distinct 成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取树图 session Subcategory distinct 失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取树图 session Subcategory distinct 失败");
        }
        return baseRsp;
    }

    /**
     * 获取树图大类分析结果-去重
     *
     * @return
     */

    @PostMapping("/getTreeCategoryDistinctEntity")
    public BaseRsp getTreeCategoryDistinctEntity(HttpServletRequest httpServletRequest,
            @RequestBody ReqTreeCategoryDto reqCateDistinct
    ) {
        LOGGER.info("获取session Category distinct ");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(reqCateDistinct.getFUploadName(),reqCateDistinct.getFUploadUser())
                    .get("f_make_version_id");

            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(reqCateDistinct.getFUploadName(),reqCateDistinct.getFUploadUser())
                    .get("f_user_scene_version_id");


            String [] categoryArr = reqCateDistinct.getFCategoryFrom().split(",");

            resList = treeSessionDistinctEventRepository
                    .getTreeCateDisEntity(fUserSceneVersionId,fMakeVersionId,
                            categoryArr[0], reqCateDistinct.getFromLayer(), reqCateDistinct.getToLayer(),
                            reqCateDistinct.getFSessionNumStart(),
                            reqCateDistinct.getFCategory(), reqCateDistinct.getFSessionNumEnd(),
                            reqCateDistinct.getFUserNumStart(),reqCateDistinct.getFUserNumEnd(),
                            reqCateDistinct.getFPVNumStart(),reqCateDistinct.getFPVNumEnd(),
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


}
