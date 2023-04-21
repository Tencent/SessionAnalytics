/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.util;


import com.session.path.data.userpath.dto.BaseRsp;
import com.session.path.data.userpath.repository.UserLogUploadStatusRepository;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName CommonUtil
 * @Description common集成方法
 * @Author author
 * @Date 2023/02/18 14:50
 * @Version 1.0
 **/


public class CommonUtil {

    @Autowired
    UserLogUploadStatusRepository userLogUploadStatusRepository;

    /**
     * 获取token
     *
     * @return
     */
    public static BaseRsp getToken(HttpServletRequest httpServletRequest
    ) {
        BaseRsp baseRsp = new BaseRsp();
        if (!JwtUtil.checkToken(httpServletRequest)) {
            baseRsp.setCode(10011);
            baseRsp.setMessage("token无效");
        }
        if (!JwtUtil.getUserIdEqualByJwtToken(httpServletRequest)) {
            baseRsp.setCode(401);
            baseRsp.setMessage("未授权");
        }
        return baseRsp;
    }


    /**
     * 获取用户场景版本号
     *
     * @return
     */
    public Map<String, Object> getUserScene(
            @RequestParam("f_upload_user") String fUploadUser,
            @RequestParam(value = "f_upload_name", required = false) String fUploadName
    ) {

        int fMakeVersionId = userLogUploadStatusRepository
                .getMakeVersionId(fUploadName,fUploadUser).get("f_make_version_id");

        String fUserSceneVersionId = userLogUploadStatusRepository
                .getUserSceneVersionId(fUploadName,fUploadUser).get("f_user_scene_version_id");


        Map<String, Object> map =  new HashMap<>();

        map.put("fMakeVersionId", fMakeVersionId);

        map.put("fUserSceneVersionId", fUserSceneVersionId);

        return map;

    }


}
