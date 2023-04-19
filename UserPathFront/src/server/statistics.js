/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import request from "@/utils/request";

// 统计图
// 获取session总览查询
export const getSessionLogCnt = params => request({
  url: "/session/getSessionLogCnt",
  params,
});

// session分布 25 50 75 最大数 平均数
export const getLogSessionDistribute = params => request({
  url: "/session/getLogSessionDistribute",
  params,
});

// session-pv分布 25 50 75 最大数 平均数
export const getLogPVDistribute = params => request({
  url: "/session/getLogPVDistribute",
  params,
});

// session 总览查询(总用户数 趋势)分年龄段
export const getSessionLogAgeUserCntTrend = params => request({
  url: "/age/getSessionLogAgeUserCntTrend",
  params,
});

// session 总览查询(总session数 趋势)分年龄段
export const getSessionLogAgeSessionCntTrend = params => request({
  url: "/age/getSessionLogAgeSessionCntTrend",
  params,
});

// session 总览查询(总pv数 趋势)分年龄段
export const getSessionLogAgePvCntTrend = params => request({
  url: "/age/getSessionLogAgePvCntTrend",
  params,
});

// 年龄分布 session分布详情 表格
export const getLogSessionAgeDistribute = params => request({
  url: "/age/getLogSessionAgeDistribute",
  params,
});

// 年龄分布 pv分布详情 表格
export const getLogPVAgeDistribute = params => request({
  url: "/age/getLogPVAgeDistribute",
  params,
});


// 性别分布 session分布详情 表格
export const getLogSessionSexDistribute = params => request({
  url: "/sex/getLogSessionSexDistribute",
  params,
});

// 性别分布 pv分布详情 表格
export const getLogPVSexDistribute = params => request({
  url: "/sex/getLogPVSexDistribute",
  params,
});


// 省份分布 session分布详情 表格
export const getLogSessionProvinceDistribute = params => request({
  url: "/province/getLogSessionProvinceDistribute",
  params,
});

// 省份分布 pv分布详情 表格
export const getLogPVProvinceDistribute = params => request({
  url: "/province/getLogPVProvinceDistribute",
  params,
});

// 渠道分布 session分布详情 表格
export const getLogSessionChannelDistribute = params => request({
  url: "/channel/getLogSessionChannelDistribute",
  params,
});

// 渠道分布 pv分布详情 表格
export const getLogPVChannelDistribute = params => request({
  url: "/channel/getLogPVChannelDistribute",
  params,
});

// 统计分析页面三个饼图
export const getSessionLogCntByAge = params => request({
  url: "/age/getSessionLogCntByAge",
  params,
});

// 统计图省份
export const getSessionLogCntByProvince = params => request({
  url: "/province/getSessionLogCntByProvince",
  params,
});

// 统计图渠道
export const getSessionLogCntByChannel = params => request({
  url: "/channel/getSessionLogCntByChannel",
  params,
});

// 统计图渠道
export const getSessionLogCntBySex = params => request({
  url: "/sex/getSessionLogCntBySex",
  params,
});


