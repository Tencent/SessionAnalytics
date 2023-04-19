/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
// 状态管理 总文件
import {
  Store,
} from "@royjs/core";
import devtools from "@royjs/core/devtools";
import user from "./user";

// 生成store 开启devtools
const store = new Store({}, {
  plugins: [devtools],
});
store.create("user", user);

// 导出状态管理
export default store;
