/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.util;

/**
 * @ClassName BizCode
 * @Description 返回状态码工具类
 * @Author author
 * @Date 2023/02/18 14:50
 * @Version 1.0
 **/

public enum BizCode implements BizCodeInterface {

    SUCCESS(0,"成功"),

    REQUEST_PRAPERTER_ERROR(400,"请求参数错误"),
    UNAUTHORIZED(401,"鉴权失败"),
    UNAUTHORIZED_CLIENTID(4011,"clientId不存在"),
    UNAUTHORIZED_TIMEOUT(4012,"密钥超时"),
    UNAUTHORIZED_REQUEST_MISSING(4013,"鉴权参数缺失"),

    INTERRUNPT(500,"服务器异常"),
    NOT_FOUND_USER_ID(5004,"找不到用户ID"),
    NOT_FOUND_USER_PROFILE(5004,"找不到用户画像结果"),
    DATEBASE_ERROR(600,"数据库异常"),

    FAIL(1,"请求失败");

//    public static final int REQ_APPLY_ERROR = "0003"; //申请失败
//    public static final int REQ_EDIT_BUSI_ERROR = "0004"; //编辑业务失败
//    public static final int PERMISSION_ERROR = "0005"; //权限异常
//    public static final int QUERY_CK_ERROR = "0006"; //查询CK异常
//    public static final int UNKOWN = "9999"; //未知错误

    private Integer code;
    private String msg;

    BizCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return msg;
    }


}
