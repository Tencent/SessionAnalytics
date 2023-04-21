/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import en from "./en.json";
import cn from "./cn.json";

// 中英文对应的数据集
const resources = {
  cn: {
    translation: cn,
  },
  en: {
    translation: en,
  },
};

// 注册国际化
i18n.use(initReactI18next).init({
  resources,
  // 默认中文
  fallbackLng: "cn",
  detection: {
    caches: ["localStorage", "sessionStorage", "cookie"],
  },
});
