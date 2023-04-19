/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.action;

import com.session.path.data.userpath.dto.RspPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName BaseAction
 * @Description action基类
 * @Author author
 * @Date 2023/03/04 17:40
 * @Version 1.0
 **/
public class BaseAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(BaseAction.class);

    public RspPage rspPage = new RspPage();

}
