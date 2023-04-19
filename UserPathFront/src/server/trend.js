/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import request from "@/utils/request";

// session 总览查询(总用户数 趋势)
export const getSessionLogUserCntTrend = params => request({
  url: "/session/getSessionLogUserCntTrend",
  params,
});

// session 总览查询(总session数 趋势)
export const getSessionLogSessionCntTrend = params => request({
  url: "/session/getSessionLogSessionCntTrend",
  params,
});

// session 总览查询(总PV数 趋势)
export const getSessionLogPvCntTrend = params => request({
  url: "/session/getSessionLogPvCntTrend",
  params,
});

// 获取session年龄段
export const getSessionLogAgeList = params => request({
  url: "/age/getSessionLogAgeList",
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

// 获取session性别
export const getSessionLogSexList = params => request({
  url: "/sex/getSessionLogSexList",
  params,
});

// session 总览查询(总用户数 趋势)分性别
export const getSessionLogSexUserCntTrend = params => request({
  url: "/sex/getSessionLogSexUserCntTrend",
  params,
});

// session 总览查询(总session数 趋势)分性别
export const getSessionLogSexSessionCntTrend = params => request({
  url: "/sex/getSessionLogSexSessionCntTrend",
  params,
});

// session 总览查询(总pv数 趋势)分性别
export const getSessionLogSexPvCntTrend = params => request({
  url: "/sex/getSessionLogSexPvCntTrend",
  params,
});

// 获取session省份
export const getSessionLogProvinceList = params => request({
  url: "/province/getSessionLogProvinceList",
  params,
});

// session 总览查询(总用户数 趋势)分省份
export const getSessionLogProvinceUserCntTrend = params => request({
  url: "/province/getSessionLogProvinceUserCntTrend",
  params,
});

// session 总览查询(总session数 趋势)分省份
export const getSessionLogProvinceSessionCntTrend = params => request({
  url: "/province/getSessionLogProvinceSessionCntTrend",
  params,
});

// session 总览查询(总pv数 趋势)分省份
export const getSessionLogProvincePvCntTrend = params => request({
  url: "/province/getSessionLogProvincePvCntTrend",
  params,
});

// 获取session渠道
export const getSessionLogChannelList = params => request({
  url: "/channel/getSessionLogChannelList",
  params,
});

// session 总览查询(总用户数 趋势)分渠道
export const getSessionLogChannelUserCntTrend = params => request({
  url: "/channel/getSessionLogChannelUserCntTrend",
  params,
});

// session 总览查询(总session数 趋势)分渠道
export const getSessionLogChannelSessionCntTrend = params => request({
  url: "/channel/getSessionLogChannelSessionCntTrend",
  params,
});

// session 总览查询(总pv数 趋势)分渠道
export const getSessionLogChannelPvCntTrend = params => request({
  url: "/channel/getSessionLogChannelPvCntTrend",
  params,
});

// 聚类分析
// 获取用户聚类分析日志
export const getUserLogClusterAnalysis = params => request({
  url: "/cluster/getUserLogClusterAnalysis",
  params,
});

// 获取用户聚类占比
export const getUserLogClusterDistributeClusterAnalysis = params => request({
  url: "/cluster/getUserLogClusterDistribute",
  params,
});

// kmeans获取用户聚类详情统计
export const getUserLogClusterDetail = params => request({
  url: "/cluster/getUserLogClusterDetail",
  params,
});

// 获取用户dtw聚类示例成功
export const getUserLogClusterDetailSample = params => request({
  url: "/cluster/getUserLogClusterDetailSample",
  params,
});

// Profile_Factor
export const getUserLogClusterK = params => request({
  url: "/cluster/getUserLogClusterK",
  params,
});

// 平台管理

// uv
export const getRecentAcessUV = params => request({
  url: "/role/getRecentAcessUV",
  params,
});

// pv
export const getRecentAcessPV = params => request({
  url: "/role/getRecentAcessPV",
  params,
});

// 高频访问用户
export const getRecentAcessFrequency = params => request({
  url: "/role/getRecentAcessFrequency",
  params,
});

// 全部访问明细
export const getRecentAcessRecordList = params => request({
  url: "/role/getRecentAcessRecordList",
  params,
});

// 内部的管理员
export const getRoleInfo = params => request({
  url: "role/getRoleInfo",
  params,
});

// 数据治理
// 获取头部状态
export const getUserLogMakeStatusCnt = params => request({
  url: "/maker/getUserLogMakeStatusCnt",
  params,
});

// 获取治理状态信息结果
export const getUserLogMakeStatusEntity = params => request({
  url: "/maker/getUserLogMakeStatusEntity",
  params,
});

// 获取统计上传日志维度数据（top10）
export const getUserLogTop = params => request({
  url: "/maker/getUserLogTop",
  params,
});

// 插入要替换的维度数据
export const saveUserLogMake = params => request({
  url: "/maker/saveUserLogMake",
  params,
});

// 更新上传日志维度数据（top10）
export const updateUserLogTop = params => request({
  url: "/maker/updateUserLogTop",
  params,
});

// 更新上传日志维度数据（top10）
export const getUserLogMake = params => request({
  url: "/maker/getUserLogMake",
  params,
});

// 修改治理数据集列表
export const updateMakeLogName = params => request({
  method: "POST",
  url: "/maker/updateMakeLogName",
  params,
});

// 切分原始数据集列表
export const getUserLogSplitStatusOldfEntity = params => request({
  url: "/session/getUserLogSplitStatusOldfEntity",
  params,
});

// 切分治理数据集列表
export const getUserLogSplitStatusNewfEntity = params => request({
  url: "/session/getUserLogSplitStatusNewfEntity",
  params,
});

// 切分页切分
export const postSessionSplit = params => request({
  method: "POST",
  url: "/session/sessionSplit",
  params,
});

// 治理数据预览
export const getUserLogMakeLimit = params => request({
  url: "/upload/getUserLogMakeLimit",
  params,
});

// 获取用户kms数据解读
export const getUserLogClusterDetailCategory = params => request({
  url: "/cluster/getUserLogClusterDetailCategory",
  params,
});

// 获取graph
export const getUserLogSingleNetwork = params => request({
  url: "/cluster/getUserLogSingleNetwork",
  params,
});


