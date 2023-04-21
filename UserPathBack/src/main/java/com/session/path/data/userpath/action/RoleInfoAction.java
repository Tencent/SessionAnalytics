/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.action;


import static com.session.path.data.userpath.util.RsaUtil.decrypt1;

import com.session.path.data.userpath.dto.BaseRsp;
import com.session.path.data.userpath.dto.RspPage;
import com.session.path.data.userpath.entity.CategoryColorEntity;
import com.session.path.data.userpath.entity.RecentAcessRecordEntity;
import com.session.path.data.userpath.entity.RoleInfoEntity;
import com.session.path.data.userpath.entity.User;
import com.session.path.data.userpath.repository.CategoryColorRepository;
import com.session.path.data.userpath.repository.RecentAcessRecordRepository;
import com.session.path.data.userpath.repository.RoleInfoRepository;
import com.session.path.data.userpath.repository.UserLogUploadStatusRepository;
import com.session.path.data.userpath.repository.UserRepository;
import com.session.path.data.userpath.util.BizCode;
import com.session.path.data.userpath.util.CommonUtil;
import com.session.path.data.userpath.util.JwtUtil;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName RoleInfoAction
 * @Description 权限管理
 * @Author author
 * @Date 2023/03/04 16:34
 * @Version 1.0
 **/
@Controller
@CrossOrigin
@RestController
@RequestMapping("/role")
public class RoleInfoAction extends BaseAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(RoleInfoAction.class);

    @Autowired
    RoleInfoRepository roleInfoRepository;

    @Autowired
    RecentAcessRecordRepository recentAcessRecordRepository;

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;

    @Autowired
    CategoryColorRepository categoryColorRepository;

    @Autowired
    UserRepository userRepository;


    /**
     * 获取权限
     *
     * @return
     */

    @GetMapping("/getRoleInfo")
    public BaseRsp getRoleInfo(HttpServletRequest httpServletRequest,
            @RequestParam(value = "f_upload_user", required = false) String fUploadUser) {
        LOGGER.info("获取权限");
        BaseRsp baseRsp = new BaseRsp();
        try {
            CommonUtil.getToken(httpServletRequest);
            RoleInfoEntity relist = roleInfoRepository.getRoleInfo(fUploadUser);
            baseRsp.setCode(200);
            baseRsp.setMessage("获取权限成功");
            baseRsp.setData(relist);
        } catch (Exception e) {
            LOGGER.error("获取权限失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取权限失败");
        }
        return baseRsp;
    }


    /**
     * 判断注册用户是否存在
     *
     * @return
     */

    @PostMapping("/getUserNameExist")
    public BaseRsp getUserNameExist(@RequestParam(value = "username", required = false) String username) {
        LOGGER.info("判断注册用户是否存在");
        BaseRsp baseRsp = new BaseRsp();
        try {
            String id = userRepository.getUserNameExist(username);
            if (id != null) {
                baseRsp.setCode(1);
                baseRsp.setMessage("该名称已存在");
                return baseRsp;
            } else {
                baseRsp.setCode(0);
                baseRsp.setMessage("该名称可以注册");
            }

        } catch (Exception e) {
            LOGGER.error("注册失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("注册失败");
        }
        return baseRsp;
    }


    /**
     * 注册
     *
     * @return
     */

    @PostMapping("/register")
    public BaseRsp register(@RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password) {
        LOGGER.info("注册");
        BaseRsp baseRsp = new BaseRsp();
        try {
            String id = userRepository.getUserNameExist(username);
            if (id != null) {
                baseRsp.setCode(1);
                baseRsp.setMessage("该名称已存在,不能注册");
                return baseRsp;
            }

            //测试私钥key
            String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAL"
                    + "A2JmZDXI1WlP2Ev4P+jBzWJJeXsL95SQfi59Rx94tytnvkpygOCsvJH3v1gq"
                    + "Ynio/Y2RgTbI94qjwgQNK7gUdnhCabi611hs2wdV8dvHpm6UUDVuo2nbAB"
                    + "9D5Etq1U+F8CCmR/zArpJFw1IQv72/oXFJN4mmU9kQRrGOx8Ua4PAgMBAAEC"
                    + "gYB6u4ytZ7e8HhUyK1b1icNvhlwAVkBebQzH+Gw+"
                    + "1Y50y1Z9HrqqtHUikZhjT5JLRnlFTeWP1l+j4oi3zkM4RC97M2eh5VF+K2mFHd"
                    + "9TTCPKVBjNXM+1v4VbqQv986uUH4eBLkW1UoYGy"
                    + "RPGnhDcaPQSVevkvyRr9zDrb1rAObzNKQJBAN1poFX3BU7JZk27VpUYqvXbCwMq"
                    + "quWHdnbVw0znNlpPfaN2XPYxEUo+5AkvHAvEIyW"
                    + "Qcz6t+WgFmIFqJklF/FsCQQDLvOpZ9At/GYLHn1CNZprUysMlqHNA5SsZk4e9ag6"
                    + "1uaDTRoJBjG5fbLKGxKAguacpP+ofjAU0jJFVzZ8"
                    + "5ktNdAkEAv51i+iKegFHB7LGdO8kuYzke7a2mWqACQNO8gjhzDHNQyv9rclCCoxuM"
                    + "Y/pWLkSfGipJ4kvT1VdKZW2FFtKvkwJBAMm"
                    + "m6EW0WRSpyVmPhAkQfzsSnWhOfBTFPo2IChgeOwT/AAFdjlsWhcAJNAJT5zG8z4jy"
                    + "a+Zhjl1erBLC/E/d1TkCQQCXGHIB4+FKNgNvW9TN2o4/Q/p+MvHQXGliBKMEdYc+dg"
                    + "XmX2D0PABit6RjPpX/uTp3ItWyNvqEK8NP5FTXkrLE";
            LOGGER.info("私钥:[{}]，长度:[{}]", privateKey, privateKey.length());

            // 解密
            String plainText = decrypt1(password, privateKey);
//            String password1 = passwordEncoder.encode(plainText);

            userRepository.insertUsers(username, plainText);
            baseRsp.setMessage("添加用户成功");
            baseRsp.setCode(BizCode.SUCCESS.getCode());
        } catch (Exception e) {
            LOGGER.error("添加用户失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("添加用户失败");
        }
        return baseRsp;
    }


    /**
     * 获取公钥
     *
     * @return
     */

    @PostMapping("/getPublicKey")
    public BaseRsp getPublicKey() {
        LOGGER.info("获取公钥");
        BaseRsp baseRsp = new BaseRsp();
        try {

            ////测试公钥key
            String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCwNiZmQ1yNVpT9h"
                    + "L+D/owc1iSXl7C/eUkH4ufUcfeLcrZ75KcoDgrLyR979YKmJ4qP2NkYE2yPeKo8"
                    + "IEDSu4FHZ4Qmm4utdYbNsHVfHbx6ZulFA1bqNp2wAfQ+RLatVPhfAgpkf8wK"
                    + "6SRcNSEL+9v6FxSTeJplPZEEaxjsfFGuDwIDAQAB";
            LOGGER.info("公钥:[{}]，长度:[{}]", publicKey, publicKey.length());

            baseRsp.setMessage("获取公钥成功");
            baseRsp.setData(publicKey);
            baseRsp.setCode(BizCode.SUCCESS.getCode());
        } catch (Exception e) {
            LOGGER.error("获取公钥失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取公钥失败");
        }
        return baseRsp;
    }


    //登录
    @PostMapping(value = "/login")
    public BaseRsp login(@RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password) {
        BaseRsp baseRsp = new BaseRsp();
        String token = null;
        try {
            //测试私钥key
            String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALA2JmZDXI1Wl"
                    + "P2Ev4P+jBzWJJeXsL95SQfi59Rx94tytnvkpygOCsvJH3v1gqYnio/Y2RgTbI94qjwgQNK7"
                    + "gUdnhCabi611hs2wdV8dvHpm6UUDVuo2nbAB9D5Etq1U+F8CCmR/zArpJFw1IQv72/oXFJN"
                    + "4mmU9kQRrGOx8Ua4PAgMBAAECgYB6u4ytZ7e8HhUyK1b1icNvhlwAVkBebQzH+Gw+1Y50y1Z"
                    + "9HrqqtHUikZhjT5JLRnlFTeWP1l+j4oi3zkM4RC97M2eh5VF+K2mFHd9TTCPKVBjNXM+1v4Vb"
                    + "qQv986uUH4eBLkW1UoYGyRPGnhDcaPQSVevkvyRr9zDrb1rAObzNKQJBAN1poFX3BU7JZk27Vp"
                    + "UYqvXbCwMqquWHdnbVw0znNlpPfaN2XPYxEUo+5AkvHAvEIyWQcz6t+WgFmIFqJklF/FsCQQDL"
                    + "vOpZ9At/GYLHn1CNZprUysMlqHNA5SsZk4e9ag61uaDTRoJBjG5fbLKGxKAguacpP+ofjAU0jJF"
                    + "VzZ85ktNdAkEAv51i+iKegFHB7LGdO8kuYzke7a2mWqACQNO8gjhzDHNQyv9rclCCoxuMY/pWLk"
                    + "SfGipJ4kvT1VdKZW2FFtKvkwJBAMmm6EW0WRSpyVmPhAkQfzsSnWhOfBTFPo2IChgeOwT/AAFdjl"
                    + "sWhcAJNAJT5zG8z4jya+Zhjl1erBLC/E/d1TkCQQCXGHIB4+FKNgNvW9TN2o4/Q/p+MvHQXGliBKM"
                    + "EdYc+dgXmX2D0PABit6RjPpX/uTp3ItWyNvqEK8NP5FTXkrLE";
            LOGGER.info("私钥:[{}]，长度:[{}]", privateKey, privateKey.length());
            Map<String, String> keyMap = new HashMap<>(2);
            // 解密
            String plainText = decrypt1(password, privateKey);
            User user = userRepository.getUserInfo(username);
            if (user != null) {
//                boolean matches = passwordEncoder.matches(plainText, user.getPassword());
                if (plainText.equals(user.getPassword()) && username.equals("adminuser")) {
                    token = JwtUtil.getJwtToken(userRepository.getUserInfo(username).getId(), username);
                    keyMap.put("token", token);
                    keyMap.put("username", username);
                    keyMap.put("isadmin", "1");
                    baseRsp.setData(keyMap);
                    baseRsp.setMessage("登录成功");
                    baseRsp.setCode(BizCode.SUCCESS.getCode());
                } else if (plainText.equals(user.getPassword()) && !username.equals("adminuser")) {
                    token = JwtUtil.getJwtToken(userRepository.getUserInfo(username).getId(), username);
                    keyMap.put("token", token);
                    keyMap.put("username", username);
                    keyMap.put("isadmin", "0");
                    baseRsp.setData(keyMap);
                    baseRsp.setMessage("登录成功");
                    baseRsp.setCode(BizCode.SUCCESS.getCode());
                } else {
                    baseRsp.setData(token);
                    baseRsp.setMessage("用户名密码错误");
                    baseRsp.setCode(BizCode.FAIL.getCode());
                }
            } else {
                baseRsp.setMessage("用户名密码错误");
                baseRsp.setCode(BizCode.FAIL.getCode());
            }

        } catch (Exception e) {
            LOGGER.error("登录失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("登录失败");
        }
        return baseRsp;

    }


    /**
     * 判断上传人是否存在
     *
     * @return
     */

    @PostMapping("/getUploadUserExist")
    public BaseRsp getUploadUserExist(HttpServletRequest httpServletRequest,
            @RequestParam(value = "f_upload_user", required = false) String fUploadUser) {
        LOGGER.info("判断上传人是否存在");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);
            String id = userLogUploadStatusRepository.getUploadUserExist(fUploadUser);
            if (id != null) {
                baseRsp.setCode(1);
                baseRsp.setMessage("该上传人已存在");
                return baseRsp;
            } else {
                baseRsp.setCode(0);
                baseRsp.setMessage("该上传人不存在");
            }
        } catch (Exception e) {
            LOGGER.error("判断上传人失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("判断上传人失败");
        }
        return baseRsp;
    }


    /**
     * 获取上传用户列表
     *
     * @return
     */
    @PostMapping(value = "/getUploadUserList")
    public BaseRsp getUploadUserList(HttpServletRequest httpServletRequest,
            @RequestParam(value = "username", required = false) String username) {
        BaseRsp baseRsp = new BaseRsp();
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            CommonUtil.getToken(httpServletRequest);
            resList = userLogUploadStatusRepository.getUploadUserList();
            if (username.equals("adminuser")) {
                baseRsp.setCode(1);
                baseRsp.setData(resList);
                baseRsp.setMessage("管理员");
                return baseRsp;
            } else {
                baseRsp.setCode(0);
                baseRsp.setData(username);
                baseRsp.setMessage("不是管理员");
            }

        } catch (Exception e) {
            LOGGER.error("失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("失败");
        }
        return baseRsp;

    }


    /**
     * 获取最近访问记录
     *
     * @return
     */

    @PostMapping("/getRecentAcessRecord")
    public BaseRsp getRecentAcessRecord(HttpServletRequest httpServletRequest,
            @RequestParam(value = "f_admin_user", required = false) String fAdminUser) {
        LOGGER.info("获取最近访问记录");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);
            List<Map<String, Object>> relist = recentAcessRecordRepository.getRecentAcessRecord(fAdminUser);
            rspPage.list = relist;
            baseRsp.setMessage("获取最近访问记录成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取最近访问记录失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取最近访问记录失败");
        }
        return baseRsp;
    }


    /**
     * 最近访问记录插入
     *
     * @return
     */

    @PostMapping("/saveRecentAcessRecord")
    public BaseRsp saveRecentAcessRecord(HttpServletRequest httpServletRequest,
            @RequestParam("f_analysis_name_first") String fAnalysisNameFirst,
            @RequestParam("f_analysis_name_second") String fAnalysisNameSecond,
            @RequestParam("f_upload_name") String fUploadName,
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam("f_admin_user") String fAdminUser,
            @RequestParam("f_access_time") String fAccessTime
    ) {
        LOGGER.info("最近访问记录插入");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);
            int fMakeVersionId = userLogUploadStatusRepository
                    .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");
            String fSceneId = userLogUploadStatusRepository
                    .getSceneId(fUploadName,fUploadUser).get("f_scene_id");
            String fUserSceneVersionId = userLogUploadStatusRepository
                    .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");

            RecentAcessRecordEntity recentAcessRecordEntity = new RecentAcessRecordEntity();
            recentAcessRecordEntity.setFSceneId(fSceneId);
            recentAcessRecordEntity.setFUploadName(fUploadName);
            recentAcessRecordEntity.setFAdminUser(fAdminUser);
            recentAcessRecordEntity.setFAnalysisNameFirst(fAnalysisNameFirst);
            recentAcessRecordEntity.setFAnalysisNameSecond(fAnalysisNameSecond);
            recentAcessRecordEntity.setFVersionId(1);
            recentAcessRecordEntity.setFMakeVersionId(fMakeVersionId);
            recentAcessRecordEntity.setFUserSceneVersionId(fUserSceneVersionId);
            recentAcessRecordEntity.setFAccessTime(fAccessTime);
            recentAcessRecordRepository.saveAndFlush(recentAcessRecordEntity);
            baseRsp.setMessage("最近访问记录插入成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("最近访问记录插入失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("最近访问记录插入失败");
        }
        return baseRsp;
    }


    /**
     * 获取大类颜色
     *
     * @return
     */

    @PostMapping("/getCategoryColor")
    public BaseRsp getCategoryColor(HttpServletRequest httpServletRequest) {
        LOGGER.info("获取大类颜色");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);
            List<Map<String, Object>> relist = categoryColorRepository.getCategoryColor();
            rspPage.list = relist;
            baseRsp.setMessage("获取大类颜色成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取大类颜色失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取大类颜色失败");
        }
        return baseRsp;
    }


    /**
     * 大类颜色插入
     *
     * @return
     */

    @PostMapping("/saveCategoryColor")
    public BaseRsp saveCategoryColor(HttpServletRequest httpServletRequest,
            @RequestParam("f_category") String fCategory,
            @RequestParam("f_color") String fColor
    ) {
        LOGGER.info("大类颜色插入");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);
            CategoryColorEntity categoryColorEntity = new CategoryColorEntity();
            categoryColorEntity.setFCategory(fCategory);
            categoryColorEntity.setFColor(fColor);
            categoryColorRepository.saveAndFlush(categoryColorEntity);
            baseRsp.setMessage("大类颜色插入成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("大类颜色插入失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("大类颜色插入失败");
        }
        return baseRsp;
    }


    /**
     * 获取访问记录
     *
     * @return
     */

    @GetMapping("/getRecentAcessRecordList")
    public BaseRsp getRecentAcessRecordList(HttpServletRequest httpServletRequest,
            @RequestParam(value = "f_start_time", required = false) String fStartTime,
            @RequestParam(value = "f_end_time", required = false) String fEndTime,
            @RequestParam(value = "f_admin_user", required = false) String fAdminUser,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "20") Integer pageSize) {
        LOGGER.info("获取全部访问记录");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        List<RecentAcessRecordEntity> resList = new ArrayList<>();
        Long total = 0L;
        try {
            CommonUtil.getToken(httpServletRequest);
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<RecentAcessRecordEntity> recentAcessRecordEntityPage = null;
            recentAcessRecordEntityPage = recentAcessRecordRepository
                    .getRecentAcessRecordList(fStartTime,fEndTime,fAdminUser,pageable);
            total = recentAcessRecordEntityPage.getTotalElements();
            for (RecentAcessRecordEntity recentAcessRecordEntity : recentAcessRecordEntityPage) {
                resList.add(recentAcessRecordEntity);
            }
            rspPage.list = resList;
            rspPage.total = total;
            baseRsp.setMessage("获取访问记录成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取访问记录失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取访问记录失败");
        }
        return baseRsp;
    }


    /**
     * 获取访问记录UV
     *
     * @return
     */

    @GetMapping("/getRecentAcessUV")
    public BaseRsp getRecentAcessUV(HttpServletRequest httpServletRequest,
            @RequestParam(value = "f_start_time", required = false) String fStartTime,
            @RequestParam(value = "f_end_time", required = false) String fEndTime) {
        LOGGER.info("获取访问记录UV");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);
            List<Map<String, Object>> relist = recentAcessRecordRepository.getRecentAcessUV(fStartTime,fEndTime);
            rspPage.list = relist;
            baseRsp.setMessage("获取访问记录UV成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取访问记录UV失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取访问记录UV失败");
        }
        return baseRsp;
    }


    /**
     * 获取注册用户数
     *
     * @return
     */

    @GetMapping("/getRegisterUserNum")
    public BaseRsp getRegisterUserNum(HttpServletRequest httpServletRequest,
            @RequestParam(value = "f_start_time", required = false) String fStartTime,
            @RequestParam(value = "f_end_time", required = false) String fEndTime) {
        LOGGER.info("获取注册用户数");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);
            List<Map<String, Object>> relist = recentAcessRecordRepository.getRegisterUserNum(fStartTime,fEndTime);
            rspPage.list = relist;
            baseRsp.setMessage("获取注册用户数成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取注册用户数失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取注册用户数失败");
        }
        return baseRsp;
    }


    /**
     * 获取访问记录PV
     *
     * @return
     */

    @GetMapping("/getRecentAcessPV")
    public BaseRsp getRecentAcessPV(HttpServletRequest httpServletRequest,
            @RequestParam(value = "f_start_time", required = false) String fStartTime,
            @RequestParam(value = "f_end_time", required = false) String fEndTime) {
        LOGGER.info("获取访问记录PV");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);
            List<Map<String, Object>> relist = recentAcessRecordRepository.getRecentAcessPV(fStartTime,fEndTime);
            rspPage.list = relist;
            baseRsp.setMessage("获取访问记录PV成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取访问记录PV失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取访问记录PV失败");
        }
        return baseRsp;
    }


    /**
     * 获取高频访问用户top10
     *
     * @return
     */

    @GetMapping("/getRecentAcessFrequency")
    public BaseRsp getRecentAcessFrequency(HttpServletRequest httpServletRequest,
            @RequestParam(value = "f_start_time", required = false) String fStartTime,
            @RequestParam(value = "f_end_time", required = false) String fEndTime) {
        LOGGER.info("获取高频访问用户");
        BaseRsp baseRsp = new BaseRsp();
        RspPage rspPage = new RspPage();
        try {
            CommonUtil.getToken(httpServletRequest);

            List<Map<String, Object>> relist = recentAcessRecordRepository
                    .getRecentAcessFrequency(fStartTime, fEndTime);
            rspPage.list = relist;
            baseRsp.setMessage("获取高频访问用户成功");
            baseRsp.data = rspPage;
        } catch (Exception e) {
            LOGGER.error("获取高频访问用户失败", e);
            baseRsp.setCode(BizCode.INTERRUNPT.getCode());
            baseRsp.setMessage("获取高频访问用户失败");
        }
        return baseRsp;
    }


}
