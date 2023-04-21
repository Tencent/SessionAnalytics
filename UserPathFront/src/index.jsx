/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import React from "react";
import { createRoot } from "react-dom/client";
import App from "./app";
import "@ant-design/flowchart/dist/index.css";
import "antd/dist/antd.css";
import "./style/index.less";
import "react-simple-verify/dist/react-simple-verify.css";
import "@/utils/rem";
import "@/locales/i18n";
import * as echarts from "echarts/core";
import { MapChart } from "echarts/charts";
import json from "../public/china.json";

const app = document.getElementById("app");
if (app) {
  createRoot(app).render(<App />);
  echarts.use([MapChart]);
  echarts.registerMap("china", { geoJSON: json });
}
