/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.dto;


import com.session.path.data.userpath.util.BizCode;
import lombok.Data;

/**
 * @ClassName BaseRsp
 * @Description 通用返回消息报文
 * @Author author
 * @Date 2020/10/21 17:10
 * @Version 1.0
 **/
@Data
public class BaseRsp<T> {

    public int code;
    public String message;
    //    @JsonIgnore   //这个参数慎用:感觉会导致如果为空值就会不返回整个data
//    @JsonInclude(Include.NON_NULL)
    public T data;

    public BaseRsp() {
        this.code = BizCode.SUCCESS.getCode();
        this.message = BizCode.SUCCESS.getMessage();
    }

    public BaseRsp(String code,String message) {
        this.code = BizCode.SUCCESS.getCode();
        this.message = BizCode.SUCCESS.getMessage();
    }

    public BaseRsp(T data) {
        this.data = data;
        this.code = BizCode.SUCCESS.getCode();
        this.message = BizCode.SUCCESS.getMessage();
    }

    public BaseRsp(T data,String message) {
        this.code = BizCode.SUCCESS.getCode();
        this.message = message;
        this.data = data;
    }



}


