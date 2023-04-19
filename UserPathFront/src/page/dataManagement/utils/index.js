/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

/**
 *
 * @param {*} t 国际化
 * title 标题
 * dataIndex 索引
 * width 宽度
 * @return 获取公共的colums
 */
export const getCommPreviewColums = t => [
  {
    title: t("f_user_id"),
    dataIndex: "f_user_id",
    width: 200,
  },
  {
    title: t("f_age"),
    dataIndex: "f_age",
    width: 100,
  },
  {
    title: t("f_sex"),
    dataIndex: "f_sex",
    width: 100,
  },
  {
    title: t("f_province"),
    dataIndex: "f_province",
    width: 150,
  },
  {
    title: t("f_city"),
    dataIndex: "f_city",
    width: 150,
  },
  {
    title: t("f_channel"),
    dataIndex: "f_channel",
    width: 150,
  },
  {
    title: "event",
    dataIndex: "f_event",
    width: 150,
  },
  {
    title: "event_path",
    dataIndex: "f_event_detail",
    width: 150,
  },
  {
    title: t("f_client_time"),
    dataIndex: "f_client_time",
    width: 150,
  },
  {
    title: t("f_category"),
    dataIndex: "f_category",
    width: 150,
  },
  {
    title: t("f_subcategory"),
    dataIndex: "f_subcategory",
    width: 150,
  },
];

/**
 *  downloadUlr 模板文件枚举
 *  cn:中文版本
 *  en:英文版本
 */
export const downloadUlr = {
  cn: "https://mdp-1251316161.cos.ap-guangzhou.myqcloud.com/excel/%E7%94%A8%E6%88%B7session%E5%88%86%E6%9E%90%E6%97%A5%E5%BF%97%E4%B8%8A%E4%BC%A0%E6%A8%A1%E6%9D%BF.csv",
  en: "https://mdp-1251316161.cos.ap-guangzhou.myqcloud.com/excel/User%20session%20analysis%20log%20upload%20template.csv",
};
