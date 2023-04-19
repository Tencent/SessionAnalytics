/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.dto;

/**
 * @ClassName ReqPage
 * @Description 分页请求基类
 * @Author author
 * @Date 2021/11/12 17:02
 * @Version 1.0
 **/
public class ReqPage {

    public int pageNo = 1;
    public int pageSize = 10;

    public int getPageNo() {
        if (pageNo > 0) {
          pageNo = pageNo - 1;
        }
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
