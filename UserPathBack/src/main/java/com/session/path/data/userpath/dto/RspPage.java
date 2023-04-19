/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.dto;

import java.util.List;

/**
 * @ClassName BaseRsp
 * @Description 带分页的通用返回消息报文
 * @Author author
 * @Date 2021/11/08 18:57
 * @Version 1.0
 **/
public class RspPage {

    //返回的结果集数量
    public long total = 0;
    //当前第几页
    public int pageNo = 1;
    //当前每页大小
    public int pageSize = 10;
    //当前总页数
    public int pageCount = 0;
    //返回的结果集
    public List list;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPageNo() {
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

    public int getPageCount() {
        if (total % pageSize == 0) {
            return (int)total / pageSize;
        }
        return (int)total / pageSize + 1;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}