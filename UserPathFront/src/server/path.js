/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import request from "@/utils/request";
import { encodeParams } from "@/utils";

// 获取历史场景标题列表(分析页面)
export const getUploadNameList = params => request({
  url: "/upload/getUploadNameList",
  params,
});

// 获取历史场景标题列表(分析页面)
export const getUploadNameDoneList = params => request({
  url: "/upload/getUploadNameDoneList",
  params,
});

// 获取 大类筛选
export const getSessionEventCategoryList = params => request({
  url: "/sankey/getSessionEventCategoryList",
  params,
});

// 获取 小类筛选
export const getSessionEventSubCategoryList = params => request({
  url: "/sankey/getSessionEventSubCategoryList",
  params,
});

// 页面筛选
export const getSessionEventList = params => request({
  url: "/sankey/getSessionEventList",
  params,
});

// 获取session Category（大类分析去重）
export const getSessionCategoryDistinctEntity = params => request({
  url: `/sankey/getSessionCategoryDistinctEntity?${encodeParams(params)}`,
});

// 获取session Category（大类分析不去重）
export const getSessionCategoryEntity = params => request({
  url: `/sankey/getSessionCategoryEntity?${encodeParams(params)}`,
  // params,
});
// 获取session Subcategory（小类分析去重）
export const getSessionSubcategoryDistinctEntity = params => request({
  url: `/sankey/getSessionSubcategoryDistinctEntity?${encodeParams(params)}`,
});

// 获取session Subcategory（小类分析不去重）
export const getSessionSubcategoryEntity = params => request({
  url: `/sankey/getSessionSubcategoryEntity?${encodeParams(params)}`,
});

// 获取session event(页面分析去重)
export const getSessionEventDistinctEntity = params => request({
  url: `/sankey/getSessionEventDistinctEntity?${encodeParams(params)}`,
});

// 获取session event(页面分析不去重)
export const getSessionEventEntity = params => request({
  url: `/sankey/getSessionEventEntity?${encodeParams(params)}`,
});

// 树图
// 获取session Category（大类分析去重）
export const getTreeSessionCategoryDistinctEntity = params => request({
  url: `/tree/getTreeSessionCategoryDistinctEntity?${encodeParams(params)}`,
});

// 获取session Category（大类分析不去重）
export const getTreeSessionCategoryEntity = params => request({
  url: `/tree/getTreeSessionCategoryEntity?${encodeParams(params)}`,
});
// 获取session Subcategory（小类分析去重）
export const getTreeSessionSubcategoryDistinctEntity = params => request({
  url: `/tree/getTreeSessionSubcategoryDistinctEntity?${encodeParams(params)}`,
});

// 获取session Subcategory（小类分析不去重）
export const getTreeSessionSubcategoryEntity = params => request({
  url: `/tree/getTreeSessionSubcategoryEntity?${encodeParams(params)}`,
});

// 获取session event(页面分析去重)
export const getTreeSessionEventDistinctEntity = params => request({
  url: `/tree/getTreeSessionEventDistinctEntity?${encodeParams(params)}`,
});

// 获取session event(页面分析不去重)
export const getTreeSessionEventEntity = params => request({
  url: `/tree/getTreeSessionEventEntity?${encodeParams(params)}`,
});

// 获取上传的版本号列表
export const getVersionIdList = params => request({
  url: "/sankey/getVersionIdList",
  params,
});

// 不含层级 示例图 大类
export const getSessionEventCategorySample = data => request({
  url: "/sankey/getSessionEventCategorySample",
  params: data,
});

// 不含层级 小类
export const getSessionEventSubCategorySample = params => request({
  url: "/sankey/getSessionEventSubCategorySample",
  params,
});

// 获取session event 不含层级  页面
export const getSessionEventSample = params => request({
  url: "/sankey/getSessionEventSample",
  params,
});

// 数据上传页面
// 数据上传任务完成情况
export const getUserLogUploadStatusCnt = params => request({
  url: "/upload/getUserLogUploadStatusCnt",
  params,
});

// 获取UserLogUploadStatusEntity（0:等待中 1：成功 2：进行中 3：失败）
export const getUserLogUploadStatusEntity = params => request({
  url: "/upload/getUserLogUploadStatusEntity",
  params,
});

// 获取上传的版本号
export const getMaxVersionId = params => request({
  url: "/sankey/getMaxVersionId",
  params,
});

// 上传excel及参数
export const uploadExcel = data => request({
  url: "/upload/uploadExcel",
  method: "POST",
  data,
  headers: {
    "Content-Type": "multipart/form-data",
  },
  timeout: 3600 * 1000,
});

// 获取预览数据limit 10
export const getUserLogLimit = params => request({
  url: "/upload/getUserLogLimit",
  params,
});

// 获取最近访问记录
export const getRecentAcessRecord = params => request({
  url: "/role/getRecentAcessRecord",
  params,
  method: "POST",
});

// 最近访问记录插入
export const saveRecentAcessRecord = params => request({
  method: "POST",
  url: "/role/saveRecentAcessRecord",
  params,
});

// 大类颜色插入
export const saveCategoryColor = params => request({
  method: "POST",
  url: "/role/saveCategoryColor",
  params,
});

// 取大类颜色
export const getCategoryColor = () => request({
  url: "/role/getCategoryColor",
  method: "POST",
});

// 漏斗图
// 获取漏斗图大类筛选层级1
export const getFunnelSessionCategoryList1 = params => request({
  url: "/funnel/getFunnelSessionCategoryList1",
  params,
});

// 获取漏斗图大类筛选层级2
export const getFunnelSessionCategoryList2 = params => request({
  url: "/funnel/getFunnelSessionCategoryList2",
  params,
});

// 获取漏斗图大类筛选层级3
export const getFunnelSessionCategoryList3 = params => request({
  url: "/funnel/getFunnelSessionCategoryList3",
  params,
});

// 获取漏斗图大类筛选层级4
export const getFunnelSessionCategoryList4 = params => request({
  url: "/funnel/getFunnelSessionCategoryList4",
  params,
});

// 获取漏斗图小类筛选层级1
export const getFunnelSessionSubcategoryList1 = params => request({
  url: "/funnel/getFunnelSessionSubcategoryList1",
  params,
});

// 获取漏斗图小类筛选层级2
export const getFunnelSessionSubcategoryList2 = params => request({
  url: "/funnel/getFunnelSessionSubcategoryList2",
  params,
});

// 获取漏斗图小类筛选层级3
export const getFunnelSessionSubcategoryList3 = params => request({
  url: "/funnel/getFunnelSessionSubcategoryList3",
  params,
});

// 获取漏斗图小类筛选层级4
export const getFunnelSessionSubcategoryList4 = params => request({
  url: "/funnel/getFunnelSessionSubcategoryList4",
  params,
});

// 获取漏斗图页面筛选层级1
export const getFunnelSessionEventList1 = params => request({
  url: "/funnel/getFunnelSessionEventList1",
  params,
});

// 获取漏斗图页面筛选层级2
export const getFunnelSessionEventList2 = params => request({
  url: "/funnel/getFunnelSessionEventList2",
  params,
});

// 获取漏斗图页面筛选层级3
export const getFunnelSessionEventList3 = params => request({
  url: "/funnel/getFunnelSessionEventList3",
  params,
});

// 获取漏斗图页面筛选层级4
export const getFunnelSessionEventList4 = params => request({
  url: "/funnel/getFunnelSessionEventList4",
  params,
});

// 获取漏斗图大类分析
export const getFunnelSessionCategoryEntity = params => request({
  url: "/funnel/getFunnelSessionCategoryEntity",
  params,
});

// 获取漏斗图大类分析_(去重)
export const getFunnelSessionCategoryDistinctEntity = params => request({
  url: "/funnel/getFunnelSessionCategoryDistinctEntity",
  params,
});

// 获取漏斗图小类分析
export const getFunnelSessionSubcategoryEntity = params => request({
  url: "/funnel/getFunnelSessionSubcategoryEntity",
  params,
});

// 获取漏斗图小类分析_(去重)
export const getFunnelSessionSubcategoryDistinctEntity = params => request({
  url: "/funnel/getFunnelSessionSubcategoryDistinctEntity",
  params,
});

// 获取漏斗图event页面分析
export const getFunnelSessionEventEntity = params => request({
  url: "/funnel/getFunnelSessionEventEntity",
  params,
});

// 获取漏斗图event页面分析_(去重)
export const getFunnelSessionEventDistinctEntity = params => request({
  url: "/funnel/getFunnelSessionEventDistinctEntity",
  params,
});

// 和弦图接口
// 获取session事件和弦图大类分析结果（去重）
export const getChordSessionCategoryDistinctEntity = params => request({
  url: "/chord/getChordSessionCategoryDistinctEntity",
  params,
});

// 获取session事件和弦图大类分析结果（不去重）
export const getChordSessionCategoryEntity = params => request({
  url: "/chord/getChordSessionCategoryEntity",
  params,
});

// 获取session事件和弦图小类分析结果（去重）
export const getChordSessionSubcategoryDistinctEntity = params => request({
  url: "/chord/getChordSessionSubcategoryDistinctEntity",
  params,
});

// 获取session事件和弦图小类分析结果（不去重）
export const getChordSessionSubcategoryEntity = params => request({
  url: "/chord/getChordSessionSubcategoryEntity",
  params,
});

// 获取session事件和弦图页面分析结果（去重）
export const getChordSessionEventDistinctEntity = params => request({
  url: "/chord/getChordSessionEventDistinctEntity",
  params,
});

// 获取session事件和弦图页面event分析结果（不去重）
export const getChordSessionEventEntity = params => request({
  url: "/chord/getChordSessionEventEntity",
  params,
});

// 数据查询
export const getSessionLogQuery = params => request({
  url: "/session/getSessionLogQuery",
  params,
});

// 查看分析
// 桑基
// 场景不去重
export const getSessionDimCategoryDetailEntity = params => request({
  url: `/sankey/getSessionDimCategoryDetailEntity?${encodeParams(params)}`,
});

// 场景去重
export const getSessionDimCategoryDistinctEntity = params => request({
  url: `/sankey/getSessionDimCategoryDistinctEntity?${encodeParams(params)}`,
});

// 类型不去重
export const getSessionDimSubCategoryDetailEntity = params => request({
  url: `/sankey/getSessionDimSubcategoryDetailEntity?${encodeParams(params)}`,
});

// 类型去重
export const getSessionDimSubCategoryDistinctEntity = params => request({
  url: `/sankey/getSessionDimSubCategoryDistinctEntity?${encodeParams(params)}`,
});

// 事件不去重
export const getSessionDimEventDetailEntity = params => request({
  url: `/sankey/getSessionDimEventDetailEntity?${encodeParams(params)}`,
});

// 事件去重
export const getSessionDimEventDistinctEntity = params => request({
  url: `/sankey/getSessionDimEventDistinctEntity?${encodeParams(params)}`,
});

// 漏斗
// 场景不去重
export const getSessionFunnelDimCategoryDetailEntity = params => request({
  url: "/funnel/getSessionFunnelDimCategoryDetailEntity",
  params,
});

// 场景去重
export const getSessionFunnelDimCategoryDistinctEntity = params => request({
  url: "/funnel/getSessionFunnelDimCategoryDistinctEntity",
  params,
});

// 类型不去重
export const getSessionFunnelDimSubcategoryDetailEntity = params => request({
  url: "/funnel/getSessionFunnelDimSubcategoryDetailEntity",
  params,
});

// 类型去重
export const getSessionFunnelDimSubcategoryDistinctEntity = params => request({
  url: "/funnel/getSessionFunnelDimSubcategoryDistinctEntity",
  params,
});

// 事件不去重
export const getSessionFunnelDimEventDetailEntity = params => request({
  url: "/funnel/getSessionFunnelDimEventDetailEntity",
  params,
});

// 事件去重
export const getSessionFunnelDimEventDistinctEntity = params => request({
  url: "/funnel/getSessionFunnelDimEventDistinctEntity",
  params,
});


// 和弦图
// 场景不去重
export const getSessionChordDimCategoryDetailEntity = params => request({
  url: "/chord/getSessionChordDimCategoryDetailEntity",
  params,
});

// 场景去重
export const getSessionChordDimCategoryDistinctEntity = params => request({
  url: "/chord/getSessionChordDimCategoryDistinctEntity",
  params,
});

// 类型不去重
export const getSessionChordDimSubcategoryDetailEntity = params => request({
  url: "/chord/getSessionChordDimSubcategoryDetailEntity",
  params,
});

// 类型去重
export const getSessionChordDimSubcategoryDistinctEntity = params => request({
  url: "/chord/getSessionChordDimSubcategoryDistinctEntity",
  params,
});

// 事件不去重
export const getSessionChordDimEventDetailEntity = params => request({
  url: "/chord/getSessionChordDimEventDetailEntity",
  params,
});

// 事件去重
export const getSessionChordDimEventDistinctEntity = params => request({
  url: "/chord/getSessionChordDimEventDistinctEntity",
  params,
});
