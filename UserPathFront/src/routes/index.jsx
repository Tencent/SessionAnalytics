/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

import { lazy } from "react";

const Home = lazy(() => import("@/page/home"));
const Sankey = lazy(() => import("@/page/sankey"));
const Funnel = lazy(() => import("@/page/funnel"));
const Tree = lazy(() => import("@/page/tree"));
const Chord = lazy(() => import("@/page/chord"));
const Query = lazy(() => import("@/page/dataManagement/query"));
const Upload = lazy(() => import("@/page/dataManagement/upload"));
const Uploadcsv = lazy(() => import("@/page/dataManagement/upload/From"));
const Cleansing = lazy(() => import("@/page/dataManagement/cleansing"));
const Slice = lazy(() => import("@/page/dataManagement/slice"));
const CommandLine = lazy(() => import("@/page/commandLine"));
const Statistics = lazy(() => import("@/page/statistics"));
const Trend = lazy(() => import("@/page/trend"));
const Clustering = lazy(() => import("@/page/clustering"));
const Management = lazy(() => import("@/page/management"));

export const routers = [
  {
    text: "首页",
    key: "home",
    path: "/",
    component: Home,
    line: true,
    include: false,
  },
  {
    text: "统计分析",
    path: "/statistics",
    key: "statistics",
    component: Statistics,
    line: true,
    include: true,
  },
  {
    text: "桑基图",
    path: "/sankey",
    key: "sankey",
    component: Sankey,
    include: true,
  },
  {
    text: "漏斗图",
    path: "/funnel",
    key: "funnel",
    component: Funnel,
    include: true,
  },
  {
    text: "和弦图",
    path: "/chord",
    key: "chord",
    component: Chord,
    include: true,
  },
  {
    text: "树状图",
    path: "/tree",
    key: "tree",
    component: Tree,
    include: true,
  },
  {
    text: "趋势图",
    path: "/trend",
    key: "trend",
    component: Trend,
    line: true,
    include: true,
  },
  {
    text: "数据挖掘",
    path: "/clustering",
    key: "clustering",
    component: Clustering,
    line: true,
    include: true,
  },
  {
    text: "数据上传",
    path: "/upload",
    key: "upload",
    component: Upload,
    children: [
      {
        text: "上传scv",
        path: "/upload/csv",
        key: "upload_csv",
        component: Uploadcsv,
      },
    ],
  },
  {
    text: "数据清洗",
    path: "/cleaning",
    key: "data_cleansing",
    component: Cleansing,
  },
  {
    text: "数据切分",
    path: "/slice",
    key: "data_slice",
    component: Slice,
  },
  {
    text: "数据查询",
    path: "/query",
    key: "data_query",
    component: Query,
    line: true,
    include: true,
  },
  {
    text: "平台管理",
    path: "/platform_management",
    key: "platform_management",
    component: Management,
    line: true,
  },
  {
    text: "演示与帮助",
    path: "/commandLine",
    key: "help",
    component: CommandLine,
  },
];
